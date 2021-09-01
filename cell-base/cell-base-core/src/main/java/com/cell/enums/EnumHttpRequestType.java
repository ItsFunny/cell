package com.cell.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:27
 */
public enum EnumHttpRequestType
{
    HTTP_URL_GET(1),
    HTTP_POST(2),
    UPLOAD_FILE(3),
    DOWNLOAD_FILE(4),
    WEBSOCKET_NOTIFY(5),
    NULL(-1);

    private int id;

    EnumHttpRequestType(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    private final static Map<Integer, EnumHttpRequestType> METHOD_TYPE_MAP = new HashMap<>();

    static
    {
        registerMethod(EnumHttpRequestType.values());
    }

    public static EnumHttpRequestType getType(int id)
    {
        EnumHttpRequestType product = METHOD_TYPE_MAP.get(id);
        if (product == null)
        {
            return EnumHttpRequestType.NULL;
        }
        return product;
    }


    public static void registerMethod(EnumHttpRequestType[] type)
    {
        if (type != null)
        {
            for (EnumHttpRequestType method : type)
            {
                METHOD_TYPE_MAP.put(method.getId(), method);
            }
        }
    }
}