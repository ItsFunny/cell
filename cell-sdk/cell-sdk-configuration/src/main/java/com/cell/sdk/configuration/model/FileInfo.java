package com.cell.sdk.configuration.model;

public class FileInfo
{
    private ModuleConfigMetadata meta;
    private String data;

    public FileInfo(ModuleConfigMetadata meta, String data)
    {
        this.meta = meta;
        this.data = data;
    }

    public String getAbosultePath()
    {
        return meta.getAbosultePath();
    }

    public EnumConfigType getSchema()
    {
        return meta.getSchema();
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }

}
