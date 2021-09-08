package com.cell.app;

import lombok.Data;

import java.util.UUID;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-10 21:20
 */
@Data
public class DefaultApp implements IApp
{
    protected String applicationName;

    public DefaultApp()
    {
        this.applicationName = UUID.randomUUID().toString();
    }
}
