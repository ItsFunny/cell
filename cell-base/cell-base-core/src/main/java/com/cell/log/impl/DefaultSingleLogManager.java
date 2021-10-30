package com.cell.log.impl;

import com.cell.enums.GroupEnums;
import com.cell.log.ILogManager;

import java.io.Serializable;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-02-01 22:39
 */
public class DefaultSingleLogManager implements ILogManager
{
    @Override
    public GroupEnums getGroup()
    {
        return GroupEnums.AS_PART;
    }

    @Override
    public Serializable getType()
    {
        return null;
    }
}
