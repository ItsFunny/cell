package com.cell.component.download.common.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.protocol.IOrderEvent;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.component.download.common.entity.DownloadChainInfo;
import com.cell.component.download.common.entity.DownloadContractInfo;
import com.cell.component.download.common.fetcher.IBlockFetcher;
import com.cell.component.download.common.request.CatchUpRequest;
import com.cell.component.download.common.request.FetchBlockRequest;
import com.cell.component.download.common.response.CatchUpResponse;
import com.cell.component.download.common.response.FetchBlockResponse;
import com.cell.component.download.common.service.DownloadChainInfoService;
import com.cell.component.download.common.service.DownloadContractInfoService;
import com.cell.component.download.common.strategy.CatchupStrategy;
import com.cell.component.download.common.subscriber.ContractSubscriber;
import com.cell.component.download.common.writer.IBlockWriter;
import com.cell.sdk.log.LOG;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.PriorityBlockingQueue;

public abstract class AbstractProvider implements IBlockChainProvider
{
    protected Module module;
    protected EventLoopGroup eventExecutors;
    protected AbstractEventCenter eventCenter;

    private Map<Integer, CatchupStrategy> strategyMap;
    private IBlockFetcher blockFetcher;
    private IBlockWriter blockWriter;
    private Map<String, ContractSubscriber> subscribers;

    public AbstractProvider(EventLoopGroup eventExecutors)
    {
        this.eventExecutors = eventExecutors;
    }

    public void seal(AbstractEventCenter eventCenter, List<CatchupStrategy> strategies, List<ContractSubscriber> subscribers)
    {
        this.eventCenter = eventCenter;
        this.strategyMap = new HashMap<>();
        for (CatchupStrategy strategy : strategies)
        {
            int type = strategy.strategyType();
            if (this.strategyMap.containsKey(type))
            {
                throw new RuntimeException("duplicate strategy");
            }
            this.strategyMap.put(strategy.strategyType(), strategy);
        }
        this.subscribers = new HashMap<>();
        for (ContractSubscriber contractSubscriber : subscribers)
        {
            Set<String> contracts = contractSubscriber.contracts();
            for (String contract : contracts)
            {
                if (this.subscribers.containsKey(contract))
                {
                    throw new RuntimeException("duplicate contract subscriber:" + contract);
                }
                this.subscribers.put(contract, contractSubscriber);
            }
        }
    }


    private PriorityBlockingQueue<IOrderEvent> events = new PriorityBlockingQueue<>(32, (o1, o2) -> o1.getOrder() - o2.getOrder());
    protected boolean quit;

    private void startQueue()
    {
        this.eventExecutors.next().execute(() ->
        {
            while (!Thread.currentThread().isInterrupted() && !quit)
            {
                try
                {
                    IOrderEvent event = events.take();
                    this.handleEvent(event);
                } catch (Exception e)
                {
                    LOG.error(Module.DOWNLOADER, e, "处理event失败");
                }
            }
        });
    }

    @Override
    public void start()
    {
        this.onStart();
        this.startQueue();
    }

    protected abstract void onStart();

    @Override
    public void dispatch(IOrderEvent event)
    {
        this.events.add(event);
    }

    protected void handleEvent(IOrderEvent event)
    {
        IContext ctx = this.wrap(event);
        if (ctx != null)
        {
            this.eventCenter.execute(ctx);
        } else
        {
            this.eventCenter.execute(event);
        }
    }

    protected abstract IContext wrap(IOrderEvent event);

    @Autowired
    private DownloadChainInfoService chainInfoService;
    @Autowired
    private DownloadContractInfoService contractInfoService;

    protected DownloadChainInfo getCurrentState()
    {
        String alias = this.chainAlias();
        QueryWrapper<DownloadChainInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("chain_alias", alias);
        return this.chainInfoService.getOne(queryWrapper);
    }

    protected List<DownloadContractInfo> getCatchUpContracts(Integer chainId, Long height)
    {
        QueryWrapper<DownloadContractInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.le("block_number", height);
        queryWrapper.eq("chain_id", chainId);
        return this.contractInfoService.list(queryWrapper);
    }

    protected abstract Long getLatestHeight();

    protected void scheduleCatchup()
    {
        DownloadChainInfo state = this.getCurrentState();
        Long blockNumber = state.getBlockNumber();
        Long latestHeight = this.getLatestHeight();
        LOG.info(this.module, "定时追快,最新高度:{},currentInfo:{}", latestHeight, state);
        if (latestHeight > blockNumber)
        {
            FetchBlockRequest request = new FetchBlockRequest(blockNumber + 1, latestHeight);
            FetchBlockResponse fetchResponse = this.blockFetcher.fetchBlocks(request);
            // block信息存储到本地
            this.blockWriter.storeBlocks(fetchResponse.getCatchupBlocks());
            // 更新高度信息
            state.setBlockNumber(latestHeight);
            this.chainInfoService.updateById(state);
        }

        List<DownloadContractInfo> contracts = this.getCatchUpContracts(state.getId(), latestHeight);
        LOG.info(this.module, "开始追快,合约信息:{},currentState:{}", contracts, state);
        if (CollectionUtils.isEmpty(contracts))
        {
            return;
        }
        CountDownLatch latch = new CountDownLatch(contracts.size());
        for (DownloadContractInfo contractInfo : contracts)
        {
            this.eventExecutors.next().execute(() ->
            {
                try
                {
                    Integer strategy = contractInfo.getStrategy();
                    CatchupStrategy catchupStrategy = this.strategyMap.get(strategy);
                    if (catchupStrategy == null)
                    {
                        LOG.erroring(module, "无法找到对应的策略:{}", contractInfo);
                        return;
                    }
                    CatchUpRequest request = new CatchUpRequest(contractInfo.getBlockNumber() + 1, latestHeight);
                    try
                    {
                        CatchUpResponse response = catchupStrategy.catchUp(request);
                        String contract = contractInfo.getContract();
                        ContractSubscriber subscriber = this.subscribers.get(contract);
                        subscriber.catchUp(response);
                    } catch (Exception e)
                    {
                        LOG.error(module, e, "catchup失败,contract:{},chain:{}", contractInfo, state);
                    }
                } finally
                {
                    latch.countDown();
                }
            });
        }
        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
