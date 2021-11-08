package com.cell.base.core.serialize;

import com.cell.base.common.utils.JSONUtil;
import org.springframework.beans.BeanUtils;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-31 09:48
 */
public class DefaultSelfJsonSerialize implements ISerializable
{

    @Override
    public void read(IInputArchive input) throws IOException
    {
        String json = input.readString("json");
        DefaultSelfJsonSerialize ret = JSONUtil.json2Obj(json, this.getClass());
        BeanUtils.copyProperties(ret, this);
    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {
        String jsonData = JSONUtil.obj2Json(this);
        output.writeString("json", jsonData);
    }
}
