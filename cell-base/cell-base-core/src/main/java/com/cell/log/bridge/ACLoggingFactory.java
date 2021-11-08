package com.cell.log.bridge;

import com.cell.models.Module;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogConfigurationException;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("rawtypes")
public class ACLoggingFactory extends LogFactory
{
    private static final ACLoggerWrapper COMMON_LOGGER_WRAPPER = new ACLoggerWrapper(Module.THIRD_PARTY);
    private static Map<String, ACLoggerWrapper> logWrapperMap = new HashMap<>();

    static
    {
        for (Map.Entry<String, Module> entry : ClassBridge.moduleMap.entrySet())
        {
            logWrapperMap.put(entry.getKey(), new ACLoggerWrapper(entry.getValue()));
        }
    }

    @Override
    public Object getAttribute(String name)
    {
        return null;
    }

    @Override
    public String[] getAttributeNames()
    {
        return new String[0];
    }

    @Override
    public Log getInstance(Class clazz) throws LogConfigurationException
    {
        return getInstance(clazz.getName());
    }

    @Override
    public Log getInstance(String name) throws LogConfigurationException
    {
        for (Map.Entry<String, ACLoggerWrapper> entry : logWrapperMap.entrySet())
        {
            if (name.startsWith(entry.getKey()))
            {
                return entry.getValue();
            }
        }
        return COMMON_LOGGER_WRAPPER;
    }

    @Override
    public void release()
    {
    }

    @Override
    public void removeAttribute(String name)
    {
    }

    @Override
    public void setAttribute(String name, Object value)
    {
    }

}
