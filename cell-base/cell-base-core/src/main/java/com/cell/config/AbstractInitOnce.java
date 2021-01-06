package com.cell.config;

import com.cell.exceptions.ConfigException;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-20 23:13
 */
@Data
public abstract class AbstractInitOnce implements IInitOnce
{
    private boolean init;

    @Override
    public void initOnce() throws ConfigException
    {
        if (init)
        {
            return;
        }
        this.init();
        init = true;
    }

    protected abstract void init() throws ConfigException;

}
