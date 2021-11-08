package com.cell.sdk.configuration.parser;

import com.cell.sdk.configuration.Configuration;

import java.io.IOException;

public interface IConfigurationParser
{
    public <T> T parseFrom(Configuration configuration, Class<T> clazz, String moduleName, String filePath, Object userData) throws IOException;

    public void clearCache(String moduleName);
}
