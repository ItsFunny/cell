package com.cell.constants;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 20:27
 */
public interface StatConstants
{

    long TIME_MULTIPLY = 1000L;
    long SECOND_SECTION = 1L;
    long MINUTE_SECTION = 60L;
    long HOUR_SECTION = 3600L;
    long DAY_SECTION = 86400L;

    String[] COMMON_LABELS = new String[]{"nodeName", "cluster", "module"};

    String[] HTTP_GATE_LABELS = new String[]{"nodeName", "cluster", "module", "uri", "method"};
}
