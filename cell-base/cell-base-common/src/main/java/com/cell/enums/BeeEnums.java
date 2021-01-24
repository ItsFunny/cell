package com.cell.enums;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-08 23:10
 */
public enum BeeEnums
{

    ;
    BeeEnums(Integer id, String desc)
    {
        this.id = id;
        this.desc = desc;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
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
    }

    private Integer id;
    private String desc;


}
