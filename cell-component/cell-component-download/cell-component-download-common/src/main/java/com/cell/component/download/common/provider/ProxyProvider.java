package com.cell.component.download.common.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.common.models.Module;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.base.core.protocol.IContext;
import com.cell.base.core.protocol.IOrderEvent;
import com.cell.component.download.common.entity.DownloadChainInfo;
import com.cell.component.download.common.enums.DownloadChainInfoEnums;
import com.cell.component.download.common.event.ScheduleChainInfoEvent;
import com.cell.component.download.common.pipeline.ProxyPipeline;
import com.cell.component.download.common.provider.event.ProxyWrapperEvent;
import com.cell.component.download.common.service.DownloadChainInfoService;
import com.cell.sdk.log.LOG;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProxyProvider extends AbstractProvider implements IBlockChainProvider
{
    @Autowired
    private DownloadChainInfoService chainInfoService;

    private List<IBlockChainProvider> providers;
    private Map<String, IBlockChainProvider> aliasProviders;

    public ProxyProvider(EventLoopGroup eventExecutors)
    {
        super(eventExecutors);
    }


    // 功能
    // 1. 定时查询
    private void schedule()
    {
        // TODO ,config
        this.eventExecutors.next().scheduleAtFixedRate(() ->
        {
            this.dispatch(new ScheduleChainInfoEvent());
        }, 10, 20, TimeUnit.SECONDS);
    }


    @Override
    protected void onStart()
    {
        Map<String, IBlockChainProvider> providerMap = providers.stream().collect(Collectors.toMap(p -> p.chainAlias(), p -> p));


        QueryWrapper<DownloadChainInfo> queryWrapper = new QueryWrapper<>();

        queryWrapper.in("chain_alias", providerMap.keySet());
        List<DownloadChainInfo> chains = this.chainInfoService.list(queryWrapper);
        if (chains.size() != this.providers.size())
        {
            LOG.erroring(Module.DOWNLOADER, "mismatch,providers:{},chains:{}", providerMap, chains);
            throw new RuntimeException("component error");
        }

        for (DownloadChainInfo chain : chains)
        {
            if (!chain.getStatus().equals(DownloadChainInfoEnums.AVAILABLE.getStatus()))
            {
                String chainAlias = chain.getChainAlias();
                IBlockChainProvider provider = providerMap.remove(chainAlias);
                LOG.warn(Module.DOWNLOADER, "该chain 不启用自动下链功能:{},删除对应的provider:{}", chain, provider);
            }
        }
        Collection<IBlockChainProvider> providers = providerMap.values();
        if (providers.size() == 0)
        {
            throw new RuntimeException("non alive providers");
        }
        // TODO,后续可能会有自动唤醒功能
        this.providers = new ArrayList<>(providers);
        this.aliasProviders = providerMap;
        for (IBlockChainProvider provider : this.providers)
        {
            provider.start();
        }

        this.schedule();
    }

    @Override
    protected IContext wrap(IOrderEvent event)
    {
        return new ProxyWrapperEvent(event,this);
    }


    @Override
    public String chainAlias()
    {
        return "MANAGER";
    }

    List<IBlockChainProvider> getProviders()
    {
        return providers;
    }
}
