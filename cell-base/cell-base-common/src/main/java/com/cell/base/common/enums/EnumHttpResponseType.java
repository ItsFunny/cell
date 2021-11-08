package com.cell.base.common.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:29
 */
public enum EnumHttpResponseType
{
    HTTP_HTML(1),
    HTTP_JSON(2),
    HTTP_XML(3),
    HTTP_STATIC_RESOURCE(4),

    NULL(-1);

    private int id;

    EnumHttpResponseType(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }


    private final static Map<Integer, EnumHttpResponseType> METHOD_TYPE_MAP = new HashMap<>();

    static
    {
        registerMethod(EnumHttpResponseType.values());
    }

    public static EnumHttpResponseType getType(int id)
    {
        EnumHttpResponseType product = METHOD_TYPE_MAP.get(id);
        if (product == null)
        {
            return EnumHttpResponseType.NULL;
        }
        return product;
    }


    public static void registerMethod(EnumHttpResponseType[] type)
    {
        if (type != null)
        {
            for (EnumHttpResponseType method : type)
            {
                METHOD_TYPE_MAP.put(method.getId(), method);
            }
        }
    }
}
