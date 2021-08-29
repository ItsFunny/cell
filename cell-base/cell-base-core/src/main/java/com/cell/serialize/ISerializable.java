package com.cell.serialize;

import com.cell.exceptions.MessageNotDoneException;

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

    // 先序列化为json，然后再转byte[]
    default byte[] toJsonBytes() throws IOException
    {
        JsonOutput output = new JsonOutput().createArchive();
        this.write(output);
        return output.toBytes();
    }


//	default void fromBytes(byte[] bytes) throws IOException{
////	}

    void read(IInputArchive input) throws IOException;

    void write(IOutputArchive output) throws IOException;

    default void validateComplete() throws MessageNotDoneException
    {
    }
}
