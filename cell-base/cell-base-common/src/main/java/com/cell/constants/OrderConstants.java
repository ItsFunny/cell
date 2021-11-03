package com.cell.constants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-09 10:08
 */
public interface OrderConstants
{
    int MIN_ORDER = 1000;
    int DEFAULT_ORDER = Integer.MAX_VALUE - 100;
    int HTTP_EXTENSION = 10000;
    int ROOT_EXTENSION = Integer.MIN_VALUE + 100;
    int DISCOVERY_EXTENSION = Integer.MAX_VALUE - 100;
    int HTTP_NACOS_DISCOVERY_EXTENSION = DISCOVERY_EXTENSION;
    int RPC_NACOS_DISCOVERY_EXTENSION = DISCOVERY_EXTENSION;
    int MAX_ORDER = Integer.MAX_VALUE - 1;


}
