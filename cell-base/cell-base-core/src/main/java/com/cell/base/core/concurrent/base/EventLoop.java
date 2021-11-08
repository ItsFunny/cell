package com.cell.base.core.concurrent.base;

public interface EventLoop extends EventExecutor, EventLoopGroup
{
    @Override
    EventLoopGroup parent();
}
