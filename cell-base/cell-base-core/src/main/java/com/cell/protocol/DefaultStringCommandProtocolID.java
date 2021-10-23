package com.cell.protocol;

import com.cell.exceptions.ProgramaException;
import lombok.Data;

import java.util.StringJoiner;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 17:06
 */
@Data
public class DefaultStringCommandProtocolID implements CommandProtocolID
{
    private String group;
    private float version;
    private String logicCmd;
    private String multiAddr;

    public DefaultStringCommandProtocolID(String multiAddr)
    {
        this.multiAddr=multiAddr;
        String[] split = multiAddr.split("/");
        if (split.length < 3)
        {
            throw new ProgramaException("address 错误:" + multiAddr);
        }
        this.group = split[0];
        this.version = Float.parseFloat(split[split.length - 1]);
        // TODO
        StringJoiner joiner = new StringJoiner("/");
        for (int i = 1; i < split.length - 2; i++)
        {
            joiner.add(split[i]);
        }
        this.logicCmd = joiner.toString();
    }

    @Override
    public float getVersion()
    {
        return this.version;
    }

    @Override
    public String id()
    {
        return this.multiAddr;
    }

    @Override
    public String getGroup()
    {
        return this.group;
    }
}
