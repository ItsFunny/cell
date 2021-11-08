package com.cell.sdk.configuration.refresh;

public interface ILoadConfigListener<T>
{

    void load(T obj) throws Exception;
}
