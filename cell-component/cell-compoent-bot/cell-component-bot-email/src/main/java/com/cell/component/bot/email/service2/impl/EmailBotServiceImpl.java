package com.cell.component.bot.email.service2.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.component.bot.email.service.UserEmailService;
import com.cell.component.bot.email.service2.IEmailBotService;
import com.cell.node.core.context.CellContext;
import com.cell.component.bot.common.AbstractBotService;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.bot.common.bo.MemberExistRespBO;
import com.cell.component.bot.common.constants.BotConstants;
import com.cell.component.bot.common.enums.BotEnums;
import com.cell.component.bot.email.entity.UserEmail;
import org.springframework.beans.factory.annotation.Autowired;

public class EmailBotServiceImpl extends AbstractBotService implements IEmailBotService
{
    @Autowired
    private UserEmailService userEmailService;

    public EmailBotServiceImpl(EventLoopGroup eventExecutors)
    {
        super(BotEnums.EMAIL_BOT, eventExecutors);
    }

    @Override
    protected MemberExistRespBO onExist(CellContext cellContext, MemberExistReqBO req)
    {
        MemberExistRespBO ret = new MemberExistRespBO();
        if (exist(req.getUserKey()))
        {
            ret.setExist(true);
            return ret;
        }


        // 发送邮件等


        return ret;
    }

    private boolean exist(String key)
    {
        QueryWrapper<UserEmail> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_key", key);
        UserEmail user = userEmailService.getOne(queryWrapper);
        return user != null && user.getStatus().equals(BotConstants.BINDED);
    }


    @Override
    protected void onStart()
    {

    }

    @Override
    public int getType()
    {
        return BotConstants.EMAIL_BOT;
    }
}
