package com.cell;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cell.utils.FileUtils;

import java.util.*;
import java.util.Map.Entry;

class RootConfig
{
    private JSONObject types;
    private String defaultType;
    private JSONArray configs;
    private JSONObject plugins;
    private HashMap<String, String> configTypes = null;
    private Map<String, ConfigModule> configModules = null;
    private Set<String> configPlugins = null;

    public Map<String, ConfigModule> getModules()
    {

        if (configModules != null)
        {
            return configModules;
        }

        configModules = new HashMap<>();
        for (int i = 0; i < configs.size(); ++i)
        {
            JSONObject object = (JSONObject) configs.get(i);
            JSONObject module = object.getJSONObject("modules");
            String schema = object.getString("schema");

            Iterator<Entry<String, Object>> iterator = module.entrySet().iterator();
            while (iterator.hasNext())
            {
                Entry<String, Object> entry = iterator.next();
                configModules.put(entry.getKey(), new ConfigModule((String) entry.getValue(), null, schema == null ? "json" : schema));
            }
        }

        return configModules;
    }

    public Set<String> getPluginPaths(String filePath)
    {

        if (configPlugins != null)
        {
            return configPlugins;
        }

        configPlugins = new HashSet<>();

        Iterator<Entry<String, Object>> iterator = plugins.entrySet().iterator();
        while (iterator.hasNext())
        {
            String pluginAbsPath = FileUtils.getAbsolutePathRelated(filePath, (String) iterator.next().getValue());
            if (pluginAbsPath == null)
            {
                // throw new InvalidRepoException();
                continue;
            }
            configPlugins.add(pluginAbsPath);
        }

        return configPlugins;
    }

    public HashMap<String, String> getConfigTypes()
    {

        if (configTypes != null)
        {
            return configTypes;
        }

        configTypes = new HashMap<>();

        Iterator<Entry<String, Object>> iterator = types.entrySet().iterator();

        while (iterator.hasNext())
        {
            Entry<String, Object> thisEntry = iterator.next();
            JSONObject typeObject = (JSONObject) thisEntry.getValue();
            String parent = typeObject.getString("parent");
            configTypes.put(thisEntry.getKey(), parent);
        }

        return configTypes;
    }

    public JSONArray getConfigs()
    {
        return configs;
    }

    public void setConfigs(JSONArray configs)
    {
        this.configs = configs;
    }

    public JSONObject getPlugins()
    {
        return plugins;
    }

    public void setPlugins(JSONObject plugins)
    {
        this.plugins = plugins;
    }

    public JSONObject getTypes()
    {
        return types;
    }

    public void setTypes(JSONObject types)
    {
        this.types = types;
    }

    public String getDefaultType()
    {
        return defaultType;
    }

    public void setDefaultType(String defaultType)
    {
        this.defaultType = defaultType;
    }
}
