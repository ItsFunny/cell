package com.cell.plugin.develop.context;

import java.io.IOException;

public interface ICellExceptionHandler
{
    void handleException(CellContext context, Throwable e)throws IOException;
}
