package com.cell.protocol.impl;

import com.cell.protocol.ICommand;
import com.cell.protocol.IHead;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import com.cell.utils.CommandUtils;
import lombok.Data;

import java.io.IOException;
import java.nio.ByteOrder;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 12:52
 */
@Data
public abstract class AbstractBaseHeader implements IHead
{
    //    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private short commandId;

    private String sequenceId;

    public AbstractBaseHeader(ICommand command)
    {
//         用于跟踪command
        this.commandId = CommandUtils.getCommandAnno(command.getClass()).commandId();
    }

    @Override
    public void read(IInputArchive input) throws IOException
    {
//        final Integer byteOrderInt =  input.readInteger("byteOrder");
//        if (Integer.valueOf(1).equals(byteOrderInt)){
//            this.byteOrder = ByteOrder.LITTLE_ENDIAN;
//        }else{
//            this.byteOrder = ByteOrder.BIG_ENDIAN;
//        }
        this.commandId = input.readShort("commandId");
        this.sequenceId = input.readString("sequenceId");
    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {
        output.writeShort("commandId", commandId);
        output.writeString("sequenceId", sequenceId);
    }
}
