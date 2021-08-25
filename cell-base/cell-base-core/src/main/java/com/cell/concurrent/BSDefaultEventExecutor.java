package com.cell.concurrent;


import com.cell.concurrent.base.DefaultThreadFactory;
import com.cell.concurrent.base.EventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;


public class BSDefaultEventExecutor extends BSSingleThreadEventLoop {
	
	static long TASK_LOOP_TIMES = 10L * 1000L * 1000L * 1000L;
	
	public BSDefaultEventExecutor() {
		this((EventLoopGroup) null);
	}

	public BSDefaultEventExecutor(ThreadFactory threadFactory) {
		this(null, threadFactory);
	}

	public BSDefaultEventExecutor(Executor executor) {
		this(null, executor);
	}

	public BSDefaultEventExecutor(EventLoopGroup parent) {
		this(parent, new DefaultThreadFactory(BSDefaultEventLoop.class));
	}

	public BSDefaultEventExecutor(EventLoopGroup parent, ThreadFactory threadFactory) {
		super(parent, threadFactory, true);
	}

	public BSDefaultEventExecutor(EventLoopGroup parent, Executor executor) {
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
//		LOG.warning(BSModule.COMMON, e, "exception caught in task: %s", task);
	}
}
