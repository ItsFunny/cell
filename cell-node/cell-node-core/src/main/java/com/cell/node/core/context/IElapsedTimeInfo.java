package com.cell.node.core.context;

public interface IElapsedTimeInfo
{
    void addInfo(String key, String info);

    void setElapsedTime(long elapsedTime);

    long getElapsedTime();
}
