package com.cell.component.bot.telegram.service2.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.component.bot.telegram.handler.BotChainHandler;
import com.cell.component.bot.telegram.handler.SendMsgBot;
import com.cell.component.bot.telegram.module.TelegramModule;
import com.cell.component.bot.telegram.service.UserTelegramService;
import com.cell.component.bot.telegram.service2.ITelegramBotService;
import com.cell.node.core.context.CellContext;
import com.cell.sdk.log.LOG;
import com.cell.component.bot.common.AbstractBotService;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.bot.common.bo.MemberExistRespBO;
import com.cell.component.bot.common.constants.BotConstants;
import com.cell.component.bot.telegram.entity.UserTelegram;
import com.cell.component.bot.telegram.config.TelegramConfig;
import com.cell.component.bot.telegram.utils.TelegramUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class TelegramBotServiceImpl extends AbstractBotService implements ITelegramBotService
{
    private TelegramBotsApi api;
    private BotChainHandler chainHandler;

    @Autowired
    private UserTelegramService userTelegramService;

    public TelegramBotServiceImpl(EventLoopGroup eventExecutors)
    {
        super(TelegramModule.TELEGRAM, eventExecutors);
        this.chainHandler = new BotChainHandler();
    }


    @Override
    protected MemberExistRespBO onExist(CellContext cellContext, MemberExistReqBO req)
    {
        MemberExistRespBO ret = new MemberExistRespBO();
        // 判断是否存在
        QueryWrapper<UserTelegram> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_key", req.getUserKey());
        UserTelegram one = userTelegramService.getOne(queryWrapper);
        if (one != null && one.getStatus().equals(BotConstants.BINDED))
        {
            ret.setExist(true);
            return ret;
        }
        // 生成一个随机字符串
        String str = TelegramUtils.generateRandomStr(6);
        this.cacheService.set(str, req.getUserKey(), this.getRandomStrExpireTime());
        ret.setAttribute(TelegramConfig.getInstance().getMeUrl((v) -> v + "?code=" + str));
        return ret;
    }

    protected int getRandomStrExpireTime()
    {
        return 60 * 5;
    }

    @Override
    protected void onStart()
    {
        try
        {
            this.api = new TelegramBotsApi(DefaultBotSession.class);
        } catch (TelegramApiException e)
        {
            LOG.error(TelegramModule.TELEGRAM, e, "telegram 启动失败");
            throw new RuntimeException(e);
        }

        final CountDownLatch latch = new CountDownLatch(1);
        this.eventExecutors.next().execute(() ->
        {
            Collection<Object> bots = new ArrayList<>();
            TelegramConfig instance = TelegramConfig.getInstance();
            List<TelegramConfig.TelegramNode> nodes = instance.getNodes();
            for (TelegramConfig.TelegramNode node : nodes)
            {
                String name = node.getName();
                String token = node.getToken();
                try
                {
                    SendMsgBot bot = new SendMsgBot(name, token);
                    bots.add(bot);
                    this.api.registerBot(bot);
                } catch (TelegramApiException e)
                {
                    LOG.error(this.module, e, "注册bot失败,name:{},token:{}", name, token);
                }
            }
            this.chainHandler.invokeInterestNodes(bots);
            latch.countDown();
        });

        try
        {
            latch.await();
        } catch (InterruptedException e)
        {
            LOG.error(this.module, e, "编码错误");
            throw new RuntimeException(e);
        }

    }

    @Override
    public int getType()
    {
        return BotConstants.TELEGRAM_BOT;
    }
}
