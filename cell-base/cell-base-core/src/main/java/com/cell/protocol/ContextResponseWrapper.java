package com.cell.protocol;

import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IReactor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;
import sun.tools.jconsole.inspector.IconManager;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 15:20
 */
//@Data
@Builder
public class ContextResponseWrapper
{
    private long status;
    private String msg;
    private ICommandReactor reactor;
    private Exception exception;
    private ICommand cmd;
    private Object ret;

    // 从哪里调用了该 wp
    private String from;

    private Map<String, Object> other;

    @Tolerate
    public ContextResponseWrapper()
    {
        this.other=new HashMap<>(1);
    }

    public long getStatus()
    {
        return status;
    }

    public void setStatus(long status)
    {
        this.status = status;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public ICommandReactor getReactor()
    {
        return reactor;
    }

    public void setReactor(ICommandReactor reactor)
    {
        this.reactor = reactor;
    }

    public Exception getException()
    {
        return exception;
    }

    public void setException(Exception exception)
    {
        this.exception = exception;
    }

    public ICommand getCmd()
    {
        return cmd;
    }

    public void setCmd(ICommand cmd)
    {
        this.cmd = cmd;
    }

    public Object getRet()
    {
        return ret;
    }

    public void setRet(Object ret)
    {
        this.ret = ret;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public Map<String, Object> getOther()
    {
        return other;
    }

    public void setOther(Map<String, Object> other)
    {
        this.other = other;
    }
}
