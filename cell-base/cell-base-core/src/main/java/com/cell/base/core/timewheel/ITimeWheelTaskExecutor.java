package com.cell.base.core.timewheel;

import com.cell.base.common.context.IInitOnce;

import java.util.concurrent.TimeUnit;

public interface ITimeWheelTaskExecutor extends IInitOnce
{
    void addTask(TaskFuncs funcs, TimeUnit unit, long delay);
}
