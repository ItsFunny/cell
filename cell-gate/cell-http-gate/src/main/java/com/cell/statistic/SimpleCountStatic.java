package com.cell.statistic;

import com.cell.base.IScheduleCounter;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-13 18:58
 */
public class SimpleCountStatic implements IScheduleCounter
{

    @Override
    public void start()
    {
    }

    @Override
    public void stop()
    {

    }

    @Override
    public int incAndGet()
    {
        return 0;
    }

    @Override
    public int incTotal()
    {
        return 0;
    }

    @Override
    public int incAndGet(int count)
    {
        return 0;
    }

    @Override
    public void reset()
    {

    }
}
