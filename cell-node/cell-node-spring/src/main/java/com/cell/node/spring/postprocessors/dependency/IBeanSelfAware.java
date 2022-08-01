package com.cell.node.spring.postprocessors.dependency;

public interface IBeanSelfAware
{
    void setSelf(Object proxy);
}