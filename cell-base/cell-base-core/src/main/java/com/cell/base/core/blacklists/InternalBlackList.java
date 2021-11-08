package com.cell.base.core.blacklists;

import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-02-05 05:55
 */
public class InternalBlackList
{
    public static final List<String> logInternalBlackList = Arrays.asList(
            "com.cell.log.impl.CellLoggerContext",
            "com.cell.log.impl.AbstractCellLogger",
            "Slf4JLogger",
            "org.apache.cxf.common.logging",
            "com.cell.log.bridge.LOGLoggerWrapper"
    );

}
