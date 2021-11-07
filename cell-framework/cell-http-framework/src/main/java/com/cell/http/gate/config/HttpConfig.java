package com.cell.http.gate.config;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 06:07
 */
@Data
public class HttpConfig
{
    private Integer connectTimeout = 10000;
    private Integer requestTimeout = 10000;
    private Integer readTimeout = 30000;
    private Integer maxTotal = 1000;
    private Integer defaultMaxPerRoute = 500;
}
