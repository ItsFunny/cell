/**
 * @author joker
 * @date 创建时间：2018年6月22日 上午10:21:19
 */
package com.cell.utils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 *
 * @author joker
 * @date 创建时间：2018年6月22日 上午10:21:19
 */
public class UrlUtils
{
    /**
     * Gets redirect url.
     * 获取重定向的url,如果之前的url有? 则会自动在后面添加& ,否则添加的就是?
     * @param request the request
     * @return the redirect url
     */
    public static String getRedirectUrl(HttpServletRequest request)
    {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();

        if (null == queryString && !"".equals(queryString))
        {
            requestURL.append("?").append(queryString);
        }
        String t = requestURL.toString();
        return (t += t.contains("?") ? "&" : "?");
    }

    /**
     * Gets server add with port.
     * 获取当前服务器的域名和端口号:http://localhost:8088
     * @param request the request
     * @return the server add with port
     */
    public static String getServerAddWithPort(HttpServletRequest request)
    {
        return request.getScheme() + "://" + request.getServerName()
                + ":" + request.getServerPort();
    }

    public static String decode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
