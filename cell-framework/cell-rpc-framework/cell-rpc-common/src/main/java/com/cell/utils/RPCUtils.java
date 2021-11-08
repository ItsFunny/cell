package com.cell.utils;

import com.cell.base.common.models.Module;
import com.cell.base.core.log.LOG;
import com.cell.base.core.protocol.IServerRequest;
import org.springframework.util.StreamUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:23
 */
public class RPCUtils
{
    public static String readStringFromRequest(IServerRequest request) throws IOException
    {
        int length = request.getContentLength();
        if (length <= 0)
        {
            LOG.debug(Module.RPC, "empty content received, len: [{}]", request.getContentLength());
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        StreamUtils.copy(request.getInputStream(), out);
        byte[] bytes = out.toByteArray();
        LOG.info(Module.RPC, "readStringFromRequest, len: [{}]", bytes.length);
        String message = new String(bytes, StandardCharsets.UTF_8);
        return message;
    }


}
