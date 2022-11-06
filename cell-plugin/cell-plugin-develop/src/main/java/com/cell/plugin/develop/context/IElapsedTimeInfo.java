package com.cell.plugin.develop.context;

public interface IElapsedTimeInfo
{
    void traceInfo(String key, String info);
    void trace(String key);
    void traceEnd();
    void traceEndWithInfo(String info);
    String dump();
}
