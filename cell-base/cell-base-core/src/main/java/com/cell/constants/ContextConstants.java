package com.cell.constants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 15:18
 */
public interface ContextConstants
{
    long SUCCESS = 1 << 0;
    long FAIL = 1 << 1;
    long TIMEOUT = 1 << 2 | FAIL;
    long PROGRAMA_ERROR = 1 << 3 | FAIL;



    String HTTP_STATUS="HTTP_STATUS";
}
