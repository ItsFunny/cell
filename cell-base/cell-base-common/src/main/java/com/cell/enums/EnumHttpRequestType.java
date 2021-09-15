package com.cell.enums;


import com.cell.exceptions.ProgramaException;

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
    HTTP_URL_GET((byte) 1),
    HTTP_POST((byte) 2),
    UPLOAD_FILE((byte) 3),
    DOWNLOAD_FILE((byte) 4),
    WEBSOCKET_NOTIFY((byte) 5),
    NULL((byte) -1);

    private byte id;

    EnumHttpRequestType(byte id)
    {
        this.id = id;
    }

    public byte getId()
    {
        return id;
    }

    private final static Map<Byte, EnumHttpRequestType> METHOD_TYPE_MAP = new HashMap<>();

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

    public String toMethodName()
    {
        return this.name().toLowerCase();
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

    public static String getStrById(byte id)
    {
        switch (id)
        {
            case 1:
                return "get";
            case 2:
                return "post";
            default:
                throw new ProgramaException("asd");
        }
    }
}
