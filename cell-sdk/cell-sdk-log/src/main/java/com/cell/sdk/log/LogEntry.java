package com.cell.sdk.log;

import com.cell.base.common.decorators.TypeStateful;
import com.cell.base.common.enums.BeeEnums;
import com.cell.base.common.enums.TypeEnums;
import com.cell.base.common.models.ModuleInterface;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description 日志封装对象
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-18 23:20
 */
@Data
@Builder
public class LogEntry implements TypeStateful<TypeEnums>
{
    private Map<String, Object> fields;
    private LogLevel logLevel;
    private Object[] objects;
    private Long logType;
    private String message;
    private ModuleInterface module;

    private List<ILogHook> hooks;
    private BeeEnums beeEnums;

    @Override
    public Long getState()
    {
        return null;
    }

    @Override
    public TypeEnums getType()
    {
        return TypeEnums.LOG;
    }

    @Override
    public BeeEnums getBee()
    {
        return beeEnums;
    }
}
