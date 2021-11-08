package com.cell.model;

public class ModuleConfigMetadata
{

    private String abosultePath;
    private EnumConfigType schema;

    public ModuleConfigMetadata(String abosultePath, EnumConfigType schema)
    {
        this.abosultePath = abosultePath;
        this.schema = schema;
    }

    public String getAbosultePath()
    {
        return abosultePath;
    }

    public EnumConfigType getSchema()
    {
        return schema;
    }
}
