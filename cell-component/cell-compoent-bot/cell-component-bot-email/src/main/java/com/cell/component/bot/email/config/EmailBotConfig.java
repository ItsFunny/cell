package com.cell.component.bot.email.config;

import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

@Data
public class EmailBotConfig
{
    private static final String module = "module";
    private static EmailBotConfig instance = new EmailBotConfig();

    public static EmailBotConfig getInstance()
    {
        return instance;
    }

    public void seal(INodeContext context) throws Exception
    {
        instance = Configuration.getDefault().getConfigValue(module).asObject(EmailBotConfig.class);
    }

}
