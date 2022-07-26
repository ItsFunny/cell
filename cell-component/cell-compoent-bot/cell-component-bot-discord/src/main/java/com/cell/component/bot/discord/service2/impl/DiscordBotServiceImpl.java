package com.cell.component.bot.discord.service2.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.core.concurrent.base.EventLoopGroup;
import com.cell.component.bot.discord.config.DiscordBotConfig;
import com.cell.component.bot.discord.entity.UserDiscord;
import com.cell.component.bot.discord.listener.DiscordListener;
import com.cell.component.bot.discord.service.UserDiscordService;
import com.cell.component.bot.discord.service2.IDiscordBotService;
import com.cell.component.bot.discord.utils.DiscordUtils;
import com.cell.node.core.context.CellContext;
import com.cell.sdk.log.LOG;
import com.cell.component.bot.common.AbstractBotService;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.bot.common.bo.MemberExistRespBO;
import com.cell.component.bot.common.constants.BotConstants;
import com.cell.component.bot.common.enums.BotEnums;
import com.cell.component.bot.common.service.GamePadsUserService;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DiscordBotServiceImpl extends AbstractBotService implements IDiscordBotService
{
    @Autowired
    private UserDiscordService userDiscordService;
    @Autowired
    private GamePadsUserService gamePadsUserService;


    private List<TextChannel> textChannels = new ArrayList<>();

    public DiscordBotServiceImpl(EventLoopGroup eventExecutors)
    {
        super(BotEnums.DISCORD_BOT, eventExecutors);
    }

    @Override
    protected MemberExistRespBO onExist(CellContext cellContext, MemberExistReqBO req)
    {
        MemberExistRespBO ret = new MemberExistRespBO();
        QueryWrapper<UserDiscord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_key", req.getUserKey());
        UserDiscord user = userDiscordService.getOne(queryWrapper);
        if (user != null && user.getStatus().equals(BotConstants.BINDED))
        {
            ret.setExist(true);
            return ret;
        }
        String str = DiscordUtils.generateRandomStr(6);
        ret.setAttribute(str);
        this.cacheService.set(str, req.getUserKey(), this.getRandomStrExpireTime());
        return ret;
    }

    protected int getRandomStrExpireTime()
    {
        return 60 * 5;
    }

    @Override
    protected void onStart()
    {
        DiscordBotConfig instance = DiscordBotConfig.getInstance();
        List<DiscordBotConfig.DiscordBotNode> nodes = instance.getNodes();
        for (DiscordBotConfig.DiscordBotNode node : nodes)
        {
            try
            {
                JDA jda = JDABuilder.createLight(node.getToken(), GatewayIntent.GUILD_MESSAGES, GatewayIntent.DIRECT_MESSAGES)
                        .addEventListeners(new DiscordListener())
                        .build();
                jda.awaitReady();
                Guild guild = jda.getGuildById(node.getServerId());
                if (guild == null)
                {
                    continue;
                }
                List<DiscordBotConfig.ChannelNode> channelNodes = node.getNodes();
                if (CollectionUtils.isEmpty(channelNodes))
                {
                    continue;
                }
                List<TextChannel> channels = guild.getTextChannels();
                for (DiscordBotConfig.ChannelNode channelNode : channelNodes)
                {
                    for (TextChannel channel : channels)
                    {
                        if (channel.getId().equals(channelNode.getChannelId()))
                        {
                            this.textChannels.add(channel);
                            break;
                        }
                    }
                }
            } catch (Exception e)
            {
                LOG.error(BotEnums.DISCORD_BOT, e, "创建discord 机器人失败失败");
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public int getType()
    {
        return BotConstants.DISCORD_BOT;
    }
}
