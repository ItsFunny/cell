package com.cell.log;

import com.cell.models.Module;

import java.io.Serializable;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-15 20:07
 */
public interface ICellLogger
{
    void info(LogTypeEnums logType,  Throwable err, String format, Object... data);
    void warn(LogTypeEnums logType,  Throwable err, String format, Object... data);
    void error(LogTypeEnums logType,  Throwable err, String format, Object... data);
}
