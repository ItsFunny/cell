package com.cell.config;

import com.cell.enums.ConfigurableEnums;
import com.cell.exceptions.ConfigException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 22:06
 */
public interface IEringConfigurable extends  IInitOnce
{
    ConfigurableEnums configurable();
    void config()throws ConfigException;
}
