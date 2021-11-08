package com.cell.parser;

import com.cell.Configuration;

import java.io.IOException;

public interface IConfigurationParser
{
    public <T> T parseFrom(Configuration configuration, Class<T> clazz, String moduleName, String filePath, Object userData) throws IOException;

    public void clearCache(String moduleName);
}
