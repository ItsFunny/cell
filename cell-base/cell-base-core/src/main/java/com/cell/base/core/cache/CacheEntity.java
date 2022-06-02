package com.cell.base.core.cache;

import java.io.Serializable;

public class CacheEntity implements Serializable
{

    public static final String KEY_SPACE = "cache";

    /**
     *
     */
    private static final long serialVersionUID = 6937781571348381943L;
    private String key;
    private String value;
    private Long expireTime;

    public CacheEntity() {}

    public CacheEntity(String key, String value, Long expireTime)
    {
        this.key = key;
        this.value = value;
        this.expireTime = expireTime;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public Long getExpireTime()
    {
        return expireTime;
    }

    public void setExpireTime(Long expireTime)
    {
        this.expireTime = expireTime;
    }

    @Override
    public String toString()
    {
        return "CacheEntity [key=" + key + ", value=" + value + ", expireTime=" + expireTime + "]";
    }

}
