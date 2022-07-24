package com.cell.component.trace;

public interface IElapsedTimeInfo
{
    void addInfo(String key, String info);

    void setElapsedTime(long elapsedTime);

    long getElapsedTime();
}
