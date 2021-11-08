package com.cell.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-12 09:48
 */
public enum GroupEnums
{
    GLOBAL(1, "全局"),
    AS_PART(2, "局部"),
    ;

    GroupEnums(int id, String desc)
    {
        this.id = id;
        this.desc = desc;
    }

    private int id;
    private String desc;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getDesc()
    {
        return desc;
    }

    public void setDesc(String desc)
    {
        this.desc = desc;
    }}
