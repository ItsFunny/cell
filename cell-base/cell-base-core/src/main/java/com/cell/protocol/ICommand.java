package com.cell.protocol;

import com.cell.serialize.BinaryOutput;
import com.cell.serialize.ISerializable;
import com.cell.serialize.JsonOutput;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:03
 */
public interface ICommand extends  ISerializable
{
    void execute(IBuzzContext ctx);

    // 获取对等方
    ICommand couple();

    IHead getHead();


    void setHead(IHead head);


//    void setCtx(IContext ctx);
//
//    IContext getCtx();

    // 用于创建业务对象


    void setCurrent(ICommand caller);

    default String getId()
    {
        return "0";
    }

    void setId(String id);


    default String toJson() throws IOException
    {
        JsonOutput output = JsonOutput.createArchive();
        this.write(output);
        return new String(output.toBytes());
    }


    default byte[] toByte() throws IOException
    {
        BinaryOutput output = BinaryOutput.createArchive();
        this.write(output);
        return output.toBytes();
    }
}
