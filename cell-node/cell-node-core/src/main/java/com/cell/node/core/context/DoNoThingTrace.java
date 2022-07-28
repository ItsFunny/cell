package com.cell.node.core.context;

public class DoNoThingTrace implements IElapsedTimeInfo
{

    @Override
    public void traceInfo(String key, String info)
    {
        
    }

    @Override
    public void trace(String key)
    {

    }

    @Override
    public void traceEnd()
    {

    }

    @Override
    public void traceEndWithInfo(String info)
    {

    }

    @Override
    public String dump()
    {
        return null;
    }
}
