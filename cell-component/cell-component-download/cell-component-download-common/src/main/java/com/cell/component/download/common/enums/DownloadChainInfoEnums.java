package com.cell.component.download.common.enums;

public enum DownloadChainInfoEnums
{
    AVAILABLE(1, "available"),

    ;
    private int status;
    private String desc;

    DownloadChainInfoEnums(int status, String desc)
    {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus()
    {
        return status;
    }

    public String getDesc()
    {
        return desc;
    }
}
