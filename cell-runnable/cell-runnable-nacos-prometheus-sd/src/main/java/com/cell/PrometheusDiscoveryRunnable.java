package com.cell;

import com.cell.annotation.CellSpringHttpApplication;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class PrometheusDiscoveryRunnable
{
    public static void main(String[] args)
    {
        CellApplication.run(PrometheusDiscoveryRunnable.class, args);
    }
}
