package com.cell.component.bot.discord.config;

import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.io.IOException;
import java.util.List;


@Data
public class DiscordBotConfig
{
    private static final String module = "discordBot";
    private static DiscordBotConfig instance = new DiscordBotConfig();

    private List<DiscordBotNode> nodes;

    @Data
    public static class DiscordBotNode
    {
        private String token;
        private String serverId;
        private List<ChannelNode> nodes;
        private boolean active = true;
    }

    @Data
    public static class ChannelNode
    {
        private String name;
        private String channelId;
    }


    public static DiscordBotConfig getInstance()
    {
        return instance;
    }

    public void seal(INodeContext context)
    {
        try
        {
            instance = Configuration.getDefault().getConfigValue(module).asObject(DiscordBotConfig.class);
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

}
