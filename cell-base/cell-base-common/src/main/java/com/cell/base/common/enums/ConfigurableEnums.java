package com.cell.base.common.enums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-17 22:07
 */
public enum ConfigurableEnums
{
    // 作为单独的一部分抽离,需要提供一个默认的配置,或者走自己的实现
    SECTION,
    // 必须是作为整体
    INTEGRATED,
    // 兼容,优先整体
    COMPATIABLE,
    ;
}
