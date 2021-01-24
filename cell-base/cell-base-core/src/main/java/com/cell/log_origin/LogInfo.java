package com.cell.log_origin;

import com.cell.decorators.TypeStateful;
import com.cell.enums.Bees;
import com.cell.enums.FilterEnums;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description 日志事件
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-10 19:14
 */
@Data
@Builder
public class LogInfo implements TypeStateful<FilterEnums>
{
    private Throwable error;
    // 模块id
    private Bees beeId;
    private String format;
    private Object[] objects;
    // 是否被过滤掉了
    private boolean filtered;

    private LogTypeEnums logTypeEnums;

    // 可以为空,不为空的话,则会判断当前线程中是否存在,不存在则会同步,同时要记得clean掉这个sequenceId
    private String sequenceId;

    @Override
    public Long getStatus()
    {
        return 0l;
    }

    @Override
    public FilterEnums getType()
    {
        return FilterEnums.LOGIC_LOG;
    }
}
