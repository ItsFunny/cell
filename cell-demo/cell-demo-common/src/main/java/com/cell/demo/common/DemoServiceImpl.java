package com.cell.demo.common;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-12 10:04
 */
@Data
public class DemoServiceImpl
{
    private String rpcName;


    public DemoServiceImpl(String rpcName)
    {
        this.rpcName = rpcName;
    }


}
