package com.cell.base.core.serialize;


import com.cell.base.common.exceptions.MessageNotDoneException;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:29
 */
public interface ISerializable
{

    // 默认为json
    default byte[] toBytes() throws IOException
    {
        JsonOutput output = new JsonOutput().createArchive();
        this.write(output);
        return output.toBytes();
    }


    default void fromBytes(byte[] bytes) throws IOException
    {
        JsonInput jsonInput = new JsonInput(bytes);
        this.read(jsonInput);
    }

    void read(IInputArchive input) throws IOException;

    void write(IOutputArchive output) throws IOException;

    default void validateComplete() throws MessageNotDoneException
    {
    }
}
