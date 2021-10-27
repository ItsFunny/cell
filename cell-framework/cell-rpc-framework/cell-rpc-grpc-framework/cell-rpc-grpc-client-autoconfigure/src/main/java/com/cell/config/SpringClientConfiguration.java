package com.cell.config;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.Plugin;
import com.cell.stub.impl.AsyncFutureStubFactory;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 09:56
 */
@ActivePlugin
public class SpringClientConfiguration
{

    @Plugin
    AsyncFutureStubFactory asyncStubFactory()
    {
        return new AsyncFutureStubFactory();
    }
}
