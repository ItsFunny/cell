package com.cell.parser;

import com.alibaba.fastjson.JSONObject;
import com.cell.Configuration;
import com.cell.base.common.utils.JSONUtil;
import com.cell.model.ConfigValueJson;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConfigurationParserJson implements IConfigurationParser
{

    private class JsonKey
    {

        public String moduleName;
        public Object jsonObject;

        public JsonKey(String moduleName, Object jsonObject)
        {
            this.moduleName = moduleName;
            this.jsonObject = jsonObject;
        }

        @Override
        public boolean equals(Object other)
        {
            if (other instanceof JsonKey)
            {
                JsonKey oth = (JsonKey) other;
                if (oth.moduleName.equals(moduleName) && oth.jsonObject.equals(jsonObject))
                {
                    return true;
                }

                return false;
            }

            return super.equals(other);
        }

        @Override
        public int hashCode()
        {
            return moduleName.hashCode() ^ jsonObject.hashCode();
        }
    }

    private ConcurrentHashMap<JsonKey, ConfigValueJson> jsonValues = new ConcurrentHashMap<>();

    @Override
    public <T> T parseFrom(Configuration configurationManager, Class<T> clazz, String moduleName, String filePath, Object userData) throws IOException
    {
        if (clazz != ConfigValueJson.class)
        {
            return null;
        }

        JSONObject root = JSONUtil.jsonFileToJsonObj(filePath);
        return (T) newJsonValue(configurationManager, moduleName, root);
    }

    @Override
    public void clearCache(String moduleName)
    {
        Iterator<Map.Entry<JsonKey, ConfigValueJson>> iterator = jsonValues.entrySet().iterator();

        while (iterator.hasNext())
        {
            if (iterator.next().getKey().moduleName.equals(moduleName))
            {
                iterator.remove();
            }
        }

    }

    private String getModuleName(JSONObject jsonObject)
    {
        return jsonObject.getString("module");
    }

    public ConfigValueJson newJsonValue(Configuration configurationManager, String moduleName, Object jsonObject) throws IOException
    {
        JsonKey key = new JsonKey(moduleName, jsonObject);

        ConfigValueJson jsonValue = jsonValues.get(key);
        if (jsonValue != null)
        {
            return jsonValue;
        }

        if (jsonObject instanceof JSONObject)
        {
            String subModule = getModuleName((JSONObject) jsonObject);

            if (subModule != null)
            {
                return (ConfigValueJson) configurationManager.getConfigValue(subModule);
            }
        }

        jsonValue = new ConfigValueJson(jsonObject, configurationManager, moduleName);
        jsonValues.put(key, jsonValue);
        return jsonValue;
    }

}
