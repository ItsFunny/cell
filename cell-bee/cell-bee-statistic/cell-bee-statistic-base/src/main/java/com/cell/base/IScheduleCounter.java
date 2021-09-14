package com.cell.base;

public interface IScheduleCounter
{
    void start();

    void stop();

    int incAndGet();

    int incTotal();

    int incAndGet(int count);

    void reset();
}
