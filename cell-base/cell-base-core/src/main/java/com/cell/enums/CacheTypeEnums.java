package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description cache的类型分类
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 22:46
 */
public enum CacheTypeEnums
{

    ;
    private int type;

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    CacheTypeEnums(int type)
    {
        this.type = type;
    }}
