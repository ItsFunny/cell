package com.cell.gate.common.config;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 04:42
 */
@Data
public class GatePropertyNode
{
    private int errorRespLimit = 100;
    private double errorRespPercentage = 0.01D;
    private long errorStatInterval = 1800 * 1000L;
    private String gatewayErrTemplate = "错误应答数超过限制";
}
