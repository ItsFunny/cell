package com.cell;


import com.cell.model.IConfigValue;

public class ConfigModule
{
    private String moduleFullPath;
    private String moduleDueFullPath;
    private IConfigValue configValue;
    private String schema;

    public String getModuleDueFullPath()
    {
        return moduleDueFullPath;
    }

    public void setModuleDueFullPath(String moduleDueFullPath)
    {
        this.moduleDueFullPath = moduleDueFullPath;
    }

    public String getModuleFullPath()
    {
        return moduleFullPath;
    }

    public void setModuleFullPath(String moduleFullPath)
    {
        this.moduleFullPath = moduleFullPath;
    }

    public IConfigValue getConfigValue()
    {
        return configValue;
    }

    public void setConfigValue(IConfigValue configValue)
    {
        this.configValue = configValue;
    }

    public String getSchema()
    {
        return schema;
    }

    public void setSchema(String schema)
    {
        this.schema = schema;
    }

    public ConfigModule(String moduleFullPath, IConfigValue configValue, String schema)
    {
        this.moduleFullPath = moduleFullPath;
        this.configValue = configValue;
        this.schema = schema;
    }
}
