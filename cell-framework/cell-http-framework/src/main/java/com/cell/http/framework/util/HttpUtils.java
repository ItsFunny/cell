package com.cell.http.framework.util;

import com.alibaba.fastjson.JSON;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.JSONUtil;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.base.core.protocol.ICommand;
import com.cell.http.framework.command.IHttpCommand;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.sdk.log.LOG;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public static String getRequestPath(HttpServletRequest request)
    {
        String url = request.getServletPath();
        String pathInfo = request.getPathInfo();
        if (pathInfo != null)
        {
            url = org.springframework.util.StringUtils.hasLength(url) ? url + pathInfo : pathInfo;
        }
        return url;
    }

    public static class HttpResult
    {
        private int code;
        private String body;

        public HttpResult(int code, String body)
        {
            this.code = code;
            this.body = body;
        }

        public int getCode()
        {
            return code;
        }

        public String getBody()
        {
            return body;
        }

        public boolean isOK()
        {
            return code == HttpStatus.SC_OK;
        }

        @Override
        public String toString()
        {
            return "HttpResult{" +
                    "code=" + code +
                    ", body='" + body + '\'' +
                    '}';
        }
    }

    private static CloseableHttpClient httpClient = HttpClients.createDefault();// 注意！未处理多线程问题

    /**
     * get
     *
     * @param url
     * @param map
     * @return
     * @throws Exception
     */
    public static HttpResult doGet(String url, Map<String, Object> map) throws Exception
    {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (map != null)
        {
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpGet);
        HttpResult httpResult = null;
        if (response.getEntity() != null)
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));
        } else
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(), "");
        }
        LOG.info(Module.HTTP_CLIENT, "调用http:{},ret:{}", url, httpResult);
        return httpResult;
    }

    public static HttpResult doGet(String url) throws Exception
    {
        HttpResult httpResult = doGet(url, null);
        return httpResult;
    }

    public static HttpResult doPost(String url, Map<String, Object> reqMap, Map<String, String> headersMap) throws Exception
    {
        HttpPost httpPost = new HttpPost(url);
        for (Map.Entry<String, String> entry : headersMap.entrySet())
            httpPost.addHeader(entry.getKey(), entry.getValue());
        if (reqMap != null)
        {
            StringEntity formEntity = new StringEntity(JSON.toJSONString(reqMap), "UTF-8");
            httpPost.setEntity(formEntity);
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpResult httpResult = null;
        if (response.getEntity() != null)
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));
        } else
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(), "");
        }

        return httpResult;
    }

    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();

    public static void setTimeout(int socketTimeout, int connectTimeout)
    {
        requestConfig = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).build();
    }

    public static HttpResult doPost(String url, String requestJsonBody, Map<String, String> headersMap) throws Exception
    {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setConfig(requestConfig);
        for (Map.Entry<String, String> entry : headersMap.entrySet())
            httpPost.addHeader(entry.getKey(), entry.getValue());
        if (StringUtils.isNotEmpty(requestJsonBody))
        {
            StringEntity entity = new StringEntity(requestJsonBody, "UTF-8");
            entity.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            httpPost.setEntity(entity);
        }
        CloseableHttpResponse response = httpClient.execute(httpPost);
        HttpResult httpResult;
        if (response.getEntity() != null)
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));
        } else
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(), "");
        }
        LOG.info(Module.HTTP_CLIENT, "调用http:{},ret:{}", url, httpResult);
        return httpResult;
    }

    public static HttpResult doPost(String url) throws Exception
    {
        HttpResult httpResult = doPost(url, "", null);
        return httpResult;
    }

    public static HttpResult doPut(String url, Map<String, Object> map) throws Exception
    {

        HttpPut httpPut = new HttpPut(url);

        if (map != null)
        {
            List<NameValuePair> params = new ArrayList<NameValuePair>();

            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                params.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
            }

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "UTF-8");
            httpPut.setEntity(formEntity);
        }

        CloseableHttpResponse response = httpClient.execute(httpPut);
        HttpResult httpResult = null;
        if (response.getEntity() != null)
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));
        } else
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(), "");
        }
        return httpResult;
    }

    public static HttpResult doDelete(String url, Map<String, Object> map) throws Exception
    {
        URIBuilder uriBuilder = new URIBuilder(url);
        if (map != null)
        {
            for (Map.Entry<String, Object> entry : map.entrySet())
            {
                uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
            }
        }
        HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
        CloseableHttpResponse response = httpClient.execute(httpDelete);
        HttpResult httpResult = null;
        if (response.getEntity() != null)
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(),
                    EntityUtils.toString(response.getEntity(), "UTF-8"));
        } else
        {
            httpResult = new HttpResult(response.getStatusLine().getStatusCode(), "");
        }
        return httpResult;
    }

    public static Map<String, String> jsonHeader()
    {
        Map<String, String> map = new HashMap<>();
        map.put("Content-Type", "application/json");
        return map;
    }

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

    public static String readStringFromPostRequest(HttpServletRequest request) throws IOException
    {
        int length = request.getContentLength();
        if (length <= 0)
        {
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

    public static String fromHttpRequest(HttpServletRequest request) throws IOException
    {
        Map<String, Object> parameterMap = new HashMap<>();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements())
        {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        return JSONUtil.toJsonString(parameterMap);
    }

    private static final AntPathMatcher MATCHER = new AntPathMatcher();

    public static Map<String, String> getRegexValues(String patten, String uri)
    {
        return MATCHER.extractUriTemplateVariables(patten, uri);
    }

    public static Optional<List<Class<? extends IHttpCommand>>> getReactorCommands(IHttpReactor reactor)
    {
        ReactorAnno annotation = reactor.getClass().getAnnotation(ReactorAnno.class);
        Class<? extends ICommand>[] cmds = annotation.cmds();
        if (cmds.length == 0) return Optional.empty();
        return Optional.of(Stream.of(cmds).map(c -> (Class<? extends IHttpCommand>) c).collect(Collectors.toList()));
    }

    /**
     * 根据地址获得数据的字节流
     *
     * @param strUrl 网络连接地址
     * @return
     */
    public static byte[] getImageFromNetByUrl(String strUrl)
    {
        try
        {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 从输入流中获取数据
     *
     * @param inStream 输入流
     * @return
     * @throws Exception
     */
    public static byte[] readInputStream(InputStream inStream) throws Exception
    {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1)
        {
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}
