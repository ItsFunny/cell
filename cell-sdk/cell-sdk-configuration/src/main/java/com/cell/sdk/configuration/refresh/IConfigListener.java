package com.cell.sdk.configuration.refresh;

public interface IConfigListener
{
    void configRefreshed(String configModule) throws Exception;
}
