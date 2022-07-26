package com.cell.component.bot.discord.handler.executors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.component.bot.discord.entity.UserDiscord;
import com.cell.component.bot.discord.handler.DiscordBotMessageManager;
import com.cell.component.bot.discord.handler.DiscordWrapper;
import com.cell.component.bot.discord.service.UserDiscordService;
import com.cell.component.bot.discord.utils.DiscordUtils;
import com.cell.component.cache.service.ICacheService;
import com.cell.sdk.log.LOG;
import com.cell.component.bot.common.constants.BotConstants;
import com.cell.component.bot.common.entity.GamePadsUser;
import com.cell.component.bot.common.enums.BotEnums;
import com.cell.component.bot.common.service.GamePadsUserService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

@ManagerNode(group = DiscordBotMessageManager.discordBotMessage, name = "asddd")
public class DiscordUserExistExecutor extends AbstractDiscordExecutor
{
    @Autowired
    private ICacheService<String, String> cacheService;

    @Autowired
    private GamePadsUserService gamePadsUserService;

    @Autowired
    private UserDiscordService userDiscordService;

    @Override
    protected boolean doPredict(DiscordWrapper discordWrapper)
    {
        Message message = discordWrapper.getEvent().getMessage();
        if (message == null)
        {
            return false;
        }
        return DiscordUtils.isUserExistString(message.getContentRaw());
    }

    @Override
    protected void doExecute(DiscordWrapper eventWrapper)
    {
        MessageReceivedEvent event = eventWrapper.getEvent();
        Message message = event.getMessage();
        String contentRaw = message.getContentRaw();
        if (!cacheService.contains(contentRaw))
        {
            return;
        }
        String originV = cacheService.delete(contentRaw);
        if (StringUtils.isEmpty(originV))
        {
            return;
        }
        LOG.info(BotEnums.DISCORD_BOT, "收到缓存命中的消息,{},origin:{},event:{}", contentRaw, originV, message.toString());

        UserDiscord userDiscord = new UserDiscord();
        userDiscord.setDiscordUserId(message.getAuthor().getId());
        userDiscord.setCreateDate(new Date());
        userDiscord.setStatus(BotConstants.BINDED);
        userDiscord.setUserKey(originV);
        userDiscordService.save(userDiscord);

        QueryWrapper<GamePadsUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("address", originV);
        GamePadsUser one = gamePadsUserService.getOne(queryWrapper);
        if (one == null)
        {
            // TODO
            one=new GamePadsUser();
            one.setDiscord(message.getAuthor().getId());
            one.setAddress(originV);
            one.setCreateTime(new Date());
            gamePadsUserService.save(one);
        } else
        {
            one.setDiscord(message.getAuthor().getId());
            gamePadsUserService.updateById(one);
        }
    }
}
