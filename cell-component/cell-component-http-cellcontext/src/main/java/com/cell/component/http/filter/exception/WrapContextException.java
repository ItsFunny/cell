package com.cell.component.http.filter.exception;

import com.cell.node.core.context.CellContext;
import lombok.Data;

@Data
public class WrapContextException extends RuntimeException
{
    private Exception exception;
    private CellContext context;

    public WrapContextException(CellContext context, Exception exception)
    {
        this.exception = exception;
        this.context = context;
    }
}
