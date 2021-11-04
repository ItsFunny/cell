package com.cell.configuration;

import com.cell.extension.AbstractNodeExtension;
import com.cell.extension.INodeExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 11:13
 */
public class RootConfiguration
{
    private static final RootConfiguration instance = new RootConfiguration();

    public static RootConfiguration getInstance()
    {
        return instance;
    }

    private Map<Class<? extends INodeExtension>, Object> extensionConfigurations = new HashMap<>();

    public Optional<Object> getExtensionConfiguration(Class<? extends AbstractNodeExtension> clz)
    {
        return Optional.ofNullable(this.extensionConfigurations.get(clz));
    }

    public <T> Optional<T> getConfigurationByType(Class<T> clz)
    {
        for (Class<? extends INodeExtension> aClass : extensionConfigurations.keySet())
        {
            Object o = extensionConfigurations.get(aClass);
            if (clz.isAssignableFrom(o.getClass()))
            {
                return (Optional<T>) Optional.of(o);
            }
        }
        return Optional.empty();
    }


    public void put(Class<? extends INodeExtension> clz, Object cfg)
    {
        this.extensionConfigurations.put(clz, cfg);
    }
}
