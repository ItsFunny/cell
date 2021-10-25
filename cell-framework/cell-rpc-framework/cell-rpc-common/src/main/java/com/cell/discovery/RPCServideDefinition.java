package com.cell.discovery;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 13:00
 */
@Data
public class RPCServideDefinition
{
    private String beanName;
    private Class<?> beanClazz;

    public RPCServideDefinition(final String beanName, final Class<?> beanClazz)
    {
        this.beanName = beanName;
        this.beanClazz = beanClazz;
    }
}
