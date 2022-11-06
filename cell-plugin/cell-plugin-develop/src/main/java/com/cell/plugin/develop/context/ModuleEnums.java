package com.cell.plugin.develop.context;

import com.cell.base.common.models.ModuleInterface;

public enum ModuleEnums implements ModuleInterface
{
    PUFF((short) 0, "PUFF"),
    WORKER((short) 1,"WORKER"),
    ASSETS((short) 2,"ASSETS"),
    TASK((short) 3,"TASK"),
    ;
    private short moduleId;
    private String desc;

    ModuleEnums(short moduleId, String desc)
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
