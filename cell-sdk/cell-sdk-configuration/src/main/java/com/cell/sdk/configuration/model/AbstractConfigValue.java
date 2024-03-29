package com.cell.sdk.configuration.model;


import com.cell.sdk.configuration.Configuration;

public abstract class AbstractConfigValue implements IConfigValue
{
    protected Configuration configurationManager = null;
    protected String moduleName = null;

    public AbstractConfigValue(Configuration configurationManager, String moduleName)
    {
        this.configurationManager = configurationManager;
        this.moduleName = moduleName;
    }

    @Override
    public String getModuleName()
    {
        return moduleName;
    }
}
