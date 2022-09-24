package com.cell.component.download.common.enums;

public enum ProviderEnums
{
    ;
    private String name;
    private String desc;

    ProviderEnums(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
    }

    public String getName()
    {
        return name;
    }

    public String getDesc()
    {
        return desc;
    }
}
