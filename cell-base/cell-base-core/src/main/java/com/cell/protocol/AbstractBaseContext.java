package com.cell.protocol;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:33
 */
@Data
public abstract class AbstractBaseContext implements IContext
{
    protected String sequenceId;
    protected long requestTimestamp;

    public AbstractBaseContext()
    {
        this.requestTimestamp = System.currentTimeMillis();
    }

}
