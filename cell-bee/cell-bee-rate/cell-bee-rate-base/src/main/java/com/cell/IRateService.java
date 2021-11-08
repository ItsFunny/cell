package com.cell;

import com.cell.exception.RateBlockException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 07:31
 */
public interface IRateService
{
    IRateEntry acquire(IRateAcquireBody body) throws RateBlockException;

    void release(IRateEntry entry);
}
