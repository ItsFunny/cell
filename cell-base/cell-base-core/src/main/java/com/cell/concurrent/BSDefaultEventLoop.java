package com.cell.concurrent;


import com.cell.concurrent.base.DefaultThreadFactory;
import com.cell.concurrent.base.EventLoopGroup;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

public class BSDefaultEventLoop extends BSSingleThreadEventLoop {
	 public BSDefaultEventLoop() {
	        this((EventLoopGroup) null);
	    }

	    public BSDefaultEventLoop(ThreadFactory threadFactory) {
	        this(null, threadFactory);
	    }

	    public BSDefaultEventLoop(Executor executor) {
	        this(null, executor);
	    }

	    public BSDefaultEventLoop(EventLoopGroup parent) {
	        this(parent, new DefaultThreadFactory(BSDefaultEventLoop.class));
	    }

	    public BSDefaultEventLoop(EventLoopGroup parent, ThreadFactory threadFactory) {
	        super(parent, threadFactory, true);
	    }

	    public BSDefaultEventLoop(EventLoopGroup parent, Executor executor) {
	        super(parent, executor, true);
	    }

	    @Override
	    protected void run() {
	        for (;;) {
	            Runnable task = takeTask();
	            if (task != null) {
	                task.run();
	                updateLastExecutionTime();
	            }

	            if (confirmShutdown()) {
	                break;
	            }
	        }
	    }
}
