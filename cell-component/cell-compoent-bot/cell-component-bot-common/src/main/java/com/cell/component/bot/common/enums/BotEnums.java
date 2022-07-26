package com.cell.component.bot.common.enums;

import com.cell.base.common.models.ModuleInterface;

public enum BotEnums implements ModuleInterface
{
    TELEGRAM_BOT((short) 1, "TELEGRAM_BOT"),
    DISCORD_BOT((short) 2, "DISCORD_BOT"),
    TWITTER_BOT((short) 3,"TWITTER_BOT"),
    EMAIL_BOT((short) 4,"EMAIL_BOT"),
    ;
    private short moduleId;
    private String desc;

    BotEnums(short moduleId, String desc)
    {
        this.moduleId = moduleId;
        this.desc = desc;
    }

    @Override
    public short getModuleId()
    {
        return 0;
    }
}
