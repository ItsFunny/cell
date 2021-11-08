package com.cell.base.common.context;

import com.cell.base.common.enums.ConfigurableEnums;
import com.cell.base.common.exceptions.ConfigException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 22:06
 */
public interface IEringConfigurable extends IInitOnce
{
    ConfigurableEnums configurable();

    void config() throws ConfigException;
}
