package com.cell.model;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.util.TypeUtils;
import com.cell.Configuration;
import com.cell.parser.ConfigurationParserJson;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ConfigValueJson extends AbstractConfigValue
{

    static
    {
        TypeUtils.compatibleWithJavaBean = true;
    }

    ConfigurationParserJson parser = null;
    private Object object;

    public ConfigValueJson(Object jsonObject, Configuration configurationManager, String moduleName)
    {
        super(configurationManager, moduleName);
        object = jsonObject;
        parser = (ConfigurationParserJson) configurationManager.getParser("json");
    }

    @Override
    public IConfigValue getArrayObject(int index)
    {
        try
        {
            return parser.newJsonValue(configurationManager, moduleName, asJsonArray().get(index));
        } catch (IOException e)
        {
//			LOG.error(Module.CONFIGURATION, e, "Error getting array object.");
            return null;
        }
    }

    @Override
    public Set<String> getObjectKeys()
    {
        return asJsonObject().keySet();
    }

    @Override
    public IConfigValue getObject(String key)
    {
        try
        {
            Object subObj = asJsonObject().get(key);
            if (subObj == null)
            {
                return null;
            } else
            {
                return parser.newJsonValue(configurationManager, moduleName, subObj);
            }
        } catch (IOException e)
        {
//			LOG.error(Module.CONFIGURATION, e, "Error getting object.");
            return null;
        }
    }

    @Override
    public boolean asBoolean()
    {
        return TypeUtils.castToBoolean(object);
    }

    @Override
    public byte asByte()
    {
        return TypeUtils.castToByte(object);
    }

    @Override
    public short asShort()
    {
        return TypeUtils.castToShort(object);
    }

    @Override
    public int asInt()
    {
        return TypeUtils.castToInt(object);
    }

    @Override
    public long asLong()
    {
        return TypeUtils.castToLong(object);
    }

    @Override
    public float asFloat()
    {
        return TypeUtils.castToFloat(object);
    }

    @Override
    public double asDouble()
    {
        return TypeUtils.castToDouble(object);
    }

    @Override
    public BigDecimal asBigDecimal()
    {
        return TypeUtils.castToBigDecimal(object);
    }

    @Override
    public BigInteger asBigInteger()
    {
        return TypeUtils.castToBigInteger(object);
    }

    @Override
    public byte[] asBytes()
    {
        return TypeUtils.castToBytes(object);
    }

    @Override
    public char asChar()
    {
        return TypeUtils.castToChar(object);
    }

    @Override
    public String asString()
    {
        return TypeUtils.castToString(object);
    }

    private JSONObject asJsonObject()
    {
        return (JSONObject) object;
    }

    private JSONArray asJsonArray()
    {
        return (JSONArray) object;
    }

    @Override
    public <T> T asObject(Class<T> clazz)
    {
        return TypeUtils.castToJavaBean(object, clazz);
    }

    List<IConfigValue> valueList = null;

    @Override
    public List<IConfigValue> asValueList()
    {
        synchronized (object)
        {
            if (valueList == null)
            {
                valueList = new ArrayList<>();
            } else
            {
                return valueList;
            }

            for (Object obj : asJsonArray())
            {
                try
                {
                    valueList.add(parser.newJsonValue(configurationManager, moduleName, obj));
                } catch (IOException e)
                {
//					LOG.error(Module.CONFIGURATION, e, "Error getting array object.");
                }
            }
            return valueList;
        }
    }

    @Override
    public String toString()
    {
        if (object == null)
        {
            return "";
        }
        return object.toString();
    }
}
