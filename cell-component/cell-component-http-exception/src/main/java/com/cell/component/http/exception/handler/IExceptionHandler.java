package com.cell.component.http.exception.handler;

import com.cell.node.core.context.CellContext;

import java.io.IOException;

public interface IExceptionHandler
{
    void handleException(CellContext context, Throwable e)throws IOException;
}
