package com.cell.base.common.context;


import com.cell.base.common.enums.ConfigurableEnums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 22:13
 */
public abstract class AbsSectionInitOnce extends AbstractInitOnce implements ISectionConfigurable
{
    @Override
    public ConfigurableEnums configurable()
    {
        return ConfigurableEnums.SECTION;
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        this.config();
    }
}
