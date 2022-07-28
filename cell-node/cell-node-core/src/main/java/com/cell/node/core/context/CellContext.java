package com.cell.node.core.context;

import com.cell.base.core.protocol.IServerRequest;
import com.cell.base.core.protocol.IServerResponse;
import com.cell.node.core.config.NodeConfig;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CellContext
{
    public static final String CELL_CONTEXT = "cell_context";
    public static final Long FLAG_SKIP_AUTH_AOP = Long.valueOf(1 << 0);
    public static final Long FLAG_SKIP_DB_CHECK = Long.valueOf(1 << 1);
    public static final Long FLAG_DB_CHECK_ROLE = Long.valueOf((1 << 2));
    public static final Long FLAG_DB_CHECK_PERMISSION = Long.valueOf(1 << 3);
    public static final Long FLAG_SKIP_CHECK_ARGUMENT = Long.valueOf(1 << 4);
    private IServerRequest request;
    private IServerResponse response;

    private IElapsedTimeInfo trace;

    private String sequenceId;
    private String protocolId;
    private String method;


    private Map<String, Object> attributes;

    private long flag;

    public static CellContext emptyContext()
    {
        CellContext build = CellContext.builder().build();
        if (NodeConfig.getInstance().isTraceEnable())
        {
            build.trace = new ElapsedTimeInfos();
        } else
        {
            build.trace = new DoNoThingTrace();
        }
        return build;
    }

    public void or(Long status)
    {
        this.flag |= status;
    }

    public void revert(long flag)
    {
        this.flag ^= flag;
    }

    public boolean isDbCheckRole()
    {
        return (flag & FLAG_DB_CHECK_ROLE) >= FLAG_DB_CHECK_ROLE;
    }

    public boolean isDbCheckPermission()
    {
        return (flag & FLAG_DB_CHECK_PERMISSION) >= FLAG_DB_CHECK_PERMISSION;
    }

    public boolean skipDbCheck()
    {
        return (flag & FLAG_SKIP_DB_CHECK) >= FLAG_SKIP_DB_CHECK;
    }

    public boolean skipCheckArgument()
    {
        return (flag & FLAG_SKIP_CHECK_ARGUMENT) >= FLAG_SKIP_CHECK_ARGUMENT;
    }

    public boolean skipAuth()
    {
        return (flag & FLAG_SKIP_AUTH_AOP) >= FLAG_SKIP_AUTH_AOP;
    }

    @Override
    public String toString()
    {
        return String.format("protocol:%s,method:%s,sequenceId:%s,", this.protocolId, this.method, this.sequenceId);
    }
}
