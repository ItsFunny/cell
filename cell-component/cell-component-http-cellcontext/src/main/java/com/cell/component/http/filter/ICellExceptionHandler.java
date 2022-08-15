package com.cell.component.http.filter;

import com.cell.node.core.context.CellContext;

import java.io.IOException;

public interface ICellExceptionHandler
{
    void handleException(CellContext context, Throwable e)throws IOException;
}
