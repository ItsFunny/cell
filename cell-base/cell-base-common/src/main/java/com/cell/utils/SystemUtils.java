package com.cell.utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-20 23:15
 */
public class SystemUtils
{
    public static Properties getOSEnveiroments()
    {
        Map<String, String> envs = System.getenv();
        Properties properties=new Properties();
        for (String s : envs.keySet())
        {
            properties.put(s,envs.get(s));
        }
        return properties;
    }


    public static void setEnviroment(String key, String value)
    {
        System.setProperty(key, value);
    }

    public static Object getEnviroment(String key)
    {
        return getOSEnveiroments().get(key);
    }

    public static <T> T parseConfigFile(String env, Class<T> tClass)
    {
        String path = (String) getEnviroment(env);
        if (StringUtils.isEmpty(path))
        {
            throw new RuntimeException("环境变量:" + env + ",为空");
        }
        try
        {
            return JSONUtil.json2Object(FileUtils.readFileToString(new File(path), "UTF-8"), tClass);
        } catch (IOException e)
        {
            throw new RuntimeException("读取文件错误", e);
        }
    }

}
