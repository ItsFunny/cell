package com.cell.task;

import com.cell.base.core.timewheel.TaskFuncs;
import io.netty.util.Timeout;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 06:27
 */
public class DefaultTimerTask implements IBaseTimerTask
{
    private TaskFuncs funcs;

    public DefaultTimerTask(TaskFuncs funcs)
    {
        this.funcs = funcs;
    }

    @Override
    public void run(Timeout timeout) throws Exception
    {
        funcs.handler(timeout).subscribe();
    }
}
