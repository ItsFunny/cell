package com.cell;

import com.cell.node.spring.annotation.CellSpringHttpApplication;
import com.cell.runnable.spring.framework.all.CellSpringFrameworkApplication;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 14:51
 */
@CellSpringHttpApplication
public class PrometheusDiscoveryApplication
{
    public static void main(String[] args)
    {
        CellSpringFrameworkApplication.run(PrometheusDiscoveryApplication.class, args);
    }

}
