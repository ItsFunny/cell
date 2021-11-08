package com.cell.node.discovery.nacos.discovery;

import com.cell.node.discovery.nacos.discovery.abs.Snap;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-05 10:23
 */
@FunctionalInterface
public interface IInstanceOnChange
{
    void onChange(Snap snap);
}
