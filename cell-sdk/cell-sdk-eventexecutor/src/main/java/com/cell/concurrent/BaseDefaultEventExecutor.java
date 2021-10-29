package com.cell.concurrent;


import com.cell.concurrent.base.DefaultThreadFactory;
import com.cell.concurrent.base.EventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;


public class BaseDefaultEventExecutor extends BaseSingleThreadEventLoop {
	
	static long TASK_LOOP_TIMES = 10L * 1000L * 1000L * 1000L;
	
	public BaseDefaultEventExecutor() {
		this((EventLoopGroup) null);
	}

	public BaseDefaultEventExecutor(ThreadFactory threadFactory) {
		this(null, threadFactory);
	}

	public BaseDefaultEventExecutor(Executor executor) {
		this(null, executor);
	}

	public BaseDefaultEventExecutor(EventLoopGroup parent) {
		this(parent, new DefaultThreadFactory(BaseDefaultEventLoop.class));
	}

	public BaseDefaultEventExecutor(EventLoopGroup parent, ThreadFactory threadFactory) {
		super(parent, threadFactory, true);
	}

	public BaseDefaultEventExecutor(EventLoopGroup parent, Executor executor) {
		super(parent, executor, true);
	}

	@Override
	protected void run() {
		for (;;) {
            Runnable task = takeTask();
            if (task != null) {
            	try {
            		task.run();
            	} catch(Throwable t) {
            		exceptionCaughtInTask(task, t);
            	}
            	updateLastExecutionTime();
            }

            if (confirmShutdown()) {
                break;
            }
		}
	}
	
	protected void exceptionCaughtInTask(Runnable task, Throwable e) {
//		LOG.warning(Module.COMMON, e, "exception caught in task: %s", task);
	}
}
