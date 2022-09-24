package com.cell.base.core.protocol;

import lombok.Data;

@Data
public class OnceContextWrapper implements IContext
{
    private IContext internal;
    private boolean executed;

    public OnceContextWrapper(IContext internal)
    {
        this.internal = internal;
        this.executed=false;
    }

    @Override
    public boolean done()
    {
        return this.executed;
    }
    public void setExecuted(boolean f){
        this.executed=f;
    }
}
