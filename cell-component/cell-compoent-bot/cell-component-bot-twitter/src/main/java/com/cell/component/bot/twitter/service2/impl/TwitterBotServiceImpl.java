package com.cell.component.bot.twitter.service2.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.component.bot.twitter.config.TwitterBotConfig;
import com.cell.component.bot.twitter.entity.UserTwitter;
import com.cell.component.bot.twitter.service.UserTwitterService;
import com.cell.component.bot.twitter.service2.ITwitterBotService;
import com.cell.component.http.exception.exception.BusinessException;
import com.cell.component.http.exception.exception.ErrorConstant;
import com.cell.component.http.exception.exception.WrapContextException;
import com.cell.node.core.context.CellContext;
import com.cell.sdk.log.LOG;
import com.fasterxml.jackson.databind.JsonNode;
import com.cell.component.bot.common.AbstractBotService;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.bot.common.bo.MemberExistRespBO;
import com.cell.component.bot.common.constants.BotConstants;
import com.cell.component.bot.common.enums.BotEnums;
import io.github.redouane59.twitter.TwitterClient;
import io.github.redouane59.twitter.dto.user.UserV2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class TwitterBotServiceImpl extends AbstractBotService implements ITwitterBotService
{
    @Autowired
    private UserTwitterService userTwitterService;

    public TwitterBotServiceImpl(EventLoopGroup eventExecutors)
    {
        super(BotEnums.TWITTER_BOT, eventExecutors);
    }

    @Override
    protected MemberExistRespBO onExist(CellContext cellContext, MemberExistReqBO req)
    {
        MemberExistRespBO ret = new MemberExistRespBO();
        QueryWrapper<UserTwitter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_key", req.getUserKey());
        UserTwitter user = userTwitterService.getOne(queryWrapper);
        if (user != null && user.getStatus().equals(BotConstants.BINDED))
        {
            ret.setExist(true);
            return ret;
        }
        TwitterBotConfig.TwitterBotNode node = TwitterBotConfig.getInstance().getActiveShuffleNode();
        if (node == null)
        {
            LOG.erroring(BotEnums.TWITTER_BOT, "twitter nodes is empty");
            throw new WrapContextException(cellContext, new BusinessException(ErrorConstant.SERVER_INIT_CONFIG));
        }
        TwitterClient client = node.getTwitterClient();
        if (client == null)
        {
            LOG.erroring(BotEnums.TWITTER_BOT, "client is nil");
            throw new WrapContextException(cellContext, new BusinessException(ErrorConstant.SERVER_INIT_CONFIG));
        }
        UserV2 twitterUser = client.getUserFromUserName(req.getUserKey());
        if (twitterUser != null)
        {
            JsonNode entities = twitterUser.getEntities();
            String text = entities.toString();
            String profileKey = TwitterBotConfig.getInstance().getProfileKey();
            if (StringUtils.isNotEmpty(text) && text.contains(profileKey))
            {
                UserTwitter userTwitter = new UserTwitter();
                userTwitter.setTwitterId(twitterUser.getId());
                userTwitter.setUserKey(req.getUserKey());
                userTwitter.setStatus(BotConstants.BINDED);
                userTwitter.setCreateDate(new Date());
                userTwitterService.save(userTwitter);
                ret.setExist(true);
            }
        }
        return ret;
    }

    @Override
    protected void onStart()
    {

    }

    @Override
    public int getType()
    {
        return BotConstants.TWITTER_BOT;
    }
}
