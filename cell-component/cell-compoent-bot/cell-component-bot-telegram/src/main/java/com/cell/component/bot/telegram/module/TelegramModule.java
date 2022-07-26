package com.cell.component.bot.telegram.module;

import com.cell.base.common.models.ModuleInterface;

public enum TelegramModule implements ModuleInterface
{
    TELEGRAM((short) 1, "TELEGRAM"),
    ;
    private short moduleId;
    private String desc;

    TelegramModule(short moduleId, String desc)
    {
        this.moduleId = moduleId;
        this.desc = desc;
    }

    @Override
    public short getModuleId()
    {
        return this.moduleId;
    }
}
