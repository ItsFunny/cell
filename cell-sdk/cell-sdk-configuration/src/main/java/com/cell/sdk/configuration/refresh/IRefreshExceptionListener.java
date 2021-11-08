package com.cell.sdk.configuration.refresh;

public interface IRefreshExceptionListener
{
    void exception(String moduleName, Throwable e);
}
