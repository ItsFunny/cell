package com.cell.component.bot.telegram.handler.executors;

import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.component.bot.telegram.entity.UserTelegram;
import com.cell.component.bot.telegram.handler.EventWrapper;
import com.cell.component.bot.telegram.handler.MsgExecutorManager;
import com.cell.component.bot.telegram.service.UserTelegramService;
import com.cell.component.bot.telegram.utils.TelegramUtils;
import com.cell.component.cache.service.ICacheService;
import com.cell.sdk.log.LOG;
import com.cell.component.bot.common.constants.BotConstants;
import com.cell.component.bot.common.enums.BotEnums;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Date;

@ManagerNode(group = MsgExecutorManager.receiveUpdate, name = "userExist", orderValue = 1)
public class UserExistExecutor extends AbstractTelegramExecutor
{
    @Autowired
    private ICacheService<String, String> cacheService;

    @Autowired
    private UserTelegramService userTelegramService;


    @Override
    protected boolean doPredict(EventWrapper wrapper)
    {
        Update update = wrapper.getUpdate();
        return update.hasMessage() && update.getMessage().hasText()
                && TelegramUtils.isUserExistString(update.getMessage().getText());
    }

    @Override
    protected void doExecute(EventWrapper eventWrapper)
    {
        Update update = eventWrapper.getUpdate();
        String str = update.getMessage().getText();
        boolean contains = cacheService.contains(str);
        if (!contains)
        {
            LOG.info(BotEnums.TELEGRAM_BOT, "info 缓存中不存在:{},update:{}", str, update);
            return;
        }
        String value = cacheService.delete(str);
        if (StringUtils.isEmpty(value))
        {
            LOG.erroring(BotEnums.TELEGRAM_BOT, "异常case, value 为空;{}", update);
            return;
        }
        Message message = update.getMessage();
        User fromUser = message.getFrom();
        LOG.info(BotEnums.TELEGRAM_BOT, "绑定用户,userKey:{},telegramUser:{}", value, fromUser);
        UserTelegram userTelegram = new UserTelegram();
        userTelegram.setUserKey(value);
        userTelegram.setCreateDate(new Date());
        userTelegram.setStatus(BotConstants.BINDED);
        userTelegram.setTelegramUserId(fromUser.getId() + "");
        userTelegramService.save(userTelegram);
    }
}
