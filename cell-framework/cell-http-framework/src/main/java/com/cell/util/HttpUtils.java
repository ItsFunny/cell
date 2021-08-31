package com.cell.util;

import com.cell.command.IHttpCommand;
import com.cell.log.LOG;
import com.cell.models.Module;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:38
 */
public class HttpUtils
{
    public static String getIpAddress(HttpServletRequest request)
    {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0)
        {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0)
        {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0)
        {
            ip = request.getRemoteAddr();
        }
        if (ip != null)
        {
            String[] temp = ip.split(",");
            if (temp.length > 1)
            {
                ip = temp[0];
            }
        }
        return ip.trim();
    }
    public static String readStringFromRequest(HttpServletRequest request) throws IOException
    {
        int length = request.getContentLength();
        if (length <= 0) {
            LOG.debug(Module.HTTP_FRAMEWORK, "empty content received, len: [{}]", request.getContentLength());
            return null;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream(length);
        StreamUtils.copy(request.getInputStream(), out);
        byte[] bytes = out.toByteArray();
        LOG.info(Module.HTTP_FRAMEWORK, "readStringFromRequest, len: [{}]", bytes.length);
        String message = new String(bytes, StandardCharsets.UTF_8);
        return message;
    }

}
