package com.cell.component.cache.service.impl;

import lombok.Data;

import java.util.concurrent.Callable;

@Data
public class ValueWrapper<V>
{
    private V v;
    private Runnable callBack;
}
