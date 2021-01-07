package com.cell.utils;//package com.charlie.utils;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpGet;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.concurrent.FutureCallback;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.entity.mime.HttpMultipartMode;
//import org.apache.http.entity.mime.MultipartEntityBuilder;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
//import org.apache.http.impl.nio.client.HttpAsyncClients;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.springframework.util.StreamUtils;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.*;
//import java.net.URL;
//import java.net.URLConnection;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Future;
//  bs 的util
//public class HttpUtils
//{
//
//
//	public static String getBody(Object params) {
//		if (params == null) {
//			return null;
//		}
//
//		if (params instanceof String) {
//			return (String) params;
//		}
//		return JSONUtil.toJsonString(params);
//	}
//
//    public static String getIpAddress(HttpServletRequest request) {
//        String ip = request.getHeader("X-Forwarded-For");
//        if (ip == null || ip.length() == 0) {
//            ip = request.getHeader("Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0) {
//            ip = request.getHeader("WL-Proxy-Client-IP");
//        }
//        if (ip == null || ip.length() == 0) {
//            ip = request.getRemoteAddr();
//        }
//        if (ip != null) {
//            String[] temp = ip.split(",");
//            if (temp.length > 1)
//                ip = temp[0];
//        }
//        return ip.trim();
//    }
//
//    public static String createGetParams(String... params) {
//        if (params == null || params.length == 0) {
//            return "";
//        }
//
//        if (params.length % 2 != 0) {
//            throw new IllegalArgumentException("参数必须是偶数！");
//        }
//
//        StringBuilder result = new StringBuilder();
//        for (int i = 0; i < params.length; i += 2) {
//            result.append(params[i]);
//            result.append("=");
//            result.append(params[i + 1]);
//            if (i + 2 != params.length) {
//                result.append("&");
//            }
//
//        }
//        return result.toString();
//    }
//
//    /**
//     * 向指定 URL 发送POST方法的请求
//     *
//     * @param url   发送请求的 URL
//     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
//     * @return 所代表远程资源的响应结果
//     */
//    @Deprecated
//    private static String sendPost(String url, String param, String charsetName) {
//        PrintWriter out = null;
//        BufferedReader in = null;
//        String result = "";
//        try {
//            URL realUrl = new URL(url);
//            // 打开和URL之间的连接
//            URLConnection conn = realUrl.openConnection();
//            // 设置通用的请求属性
//            conn.setRequestProperty("accept", "*/*");
//            conn.setRequestProperty("connection", "Keep-Alive");
//            //conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 发送POST请求必须设置如下两行
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            // 获取URLConnection对象对应的输出流
//            out = new PrintWriter(conn.getOutputStream());
//            // 发送请求参数
//            out.print(param);
//            // flush输出流的缓冲
//            out.flush();
//            if (charsetName != null && !"".equals(charsetName)) {
//                // 定义BufferedReader输入流来读取URL的响应
//                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), charsetName));
//            } else {
//                // 定义BufferedReader输入流来读取URL的响应
//                in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//            }
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输出流、输入流
//        finally {
//            try {
//                if (out != null) {
//                    out.close();
//                }
//                if (in != null) {
//                    in.close();
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    public static String sendGet(String url, Map<String, String> params) {
//        String param = createGetParams(mapToArr(params));
//        return sendGet(url, param);
//    }
//
//    private static String[] mapToArr(Map<String, String> params) {
//        String[] arr = new String[params.size() * 2];
//        int idx = 0;
//        for (Map.Entry<String, String> entry : params.entrySet()) {
//            arr[idx] = entry.getKey();
//            idx++;
//            arr[idx] = entry.getValue();
//            idx++;
//        }
//        return arr;
//    }
//
//    public static String sendGet(String url) {
//        return sendGet(url, (String) null);
//    }
//
//    public static String sendGet(String url, String param) {
//        String result = "";
//        BufferedReader in = null;
//        String urlNameString = null;
//        try {
//            if (param != null) {
//                urlNameString = url + "?" + param;
//            } else {
//                urlNameString = url;
//            }
//            URL realUrl = new URL(urlNameString);
//            // 打开和URL之间的连接
//            URLConnection connection = realUrl.openConnection();
//            // 设置通用的请求属性
//            connection.setRequestProperty("accept", "*/*");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
//            // 建立实际的连接
//            connection.connect();
//            // 定义 BufferedReader输入流来读取URL的响应
//            in = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream()));
//            String line;
//            while ((line = in.readLine()) != null) {
//                result += line;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        // 使用finally块来关闭输入流
//        finally {
//            try {
//                if (in != null) {
//                    in.close();
//                }
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        return result;
//    }
//
//    private static final int SOCKET_TIMEOUT = 1000 * 60;
//    private static final int CONNECT_TIMEOUT = 1000 * 30;
//    private static final int CONNECTION_REQUEST_TIMEOUT = 1000 * 30;
//    private static final int STATUS_CODE_OK = 200;
//
//    private static final Integer MAX_TOTAL = 300;              // 连接池最大连接数
//    private static final Integer MAX_PER_ROUTE = 300;          // 单个路由默认最大连接数
//
//    public static final RequestConfig REQUEST_CONFIG_DEFAULT = RequestConfig.custom()
//            .setSocketTimeout(SOCKET_TIMEOUT) // socket读数据超时时间：从服务器获取响应数据的超时时间
//            .setConnectTimeout(CONNECT_TIMEOUT)
//            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT) // 从连接池中获取连接的超时时间
//            .build();
//
//    public static final RequestConfig REQUEST_CONFIG_MINUTE_3 = RequestConfig.custom()
//            .setSocketTimeout(1000 * 60 * 3)
//            .setConnectTimeout(CONNECT_TIMEOUT)
//            .setConnectionRequestTimeout(CONNECTION_REQUEST_TIMEOUT)
//            .build();
//
//	private static final CloseableHttpClient client;
//	private static final CloseableHttpAsyncClient asyncClient;
//
//	static {
//        client = HttpClients.custom().setMaxConnTotal(MAX_TOTAL).setMaxConnPerRoute(MAX_PER_ROUTE).setDefaultRequestConfig(REQUEST_CONFIG_DEFAULT).build();
//        asyncClient = HttpAsyncClients.custom().setMaxConnTotal(MAX_TOTAL).setMaxConnPerRoute(MAX_PER_ROUTE).build();
//    }
//
//	private static String getGetUrl(String urlString, Map<String, String> parameters){
//        StringBuilder urlBuilder = new StringBuilder(urlString);
//        if (parameters != null && parameters.size() > 0){
//            if (!urlString.contains("?")){
//                urlBuilder.append("?");
//            }
//            int index = 0;
//            for (Map.Entry<String, String> entry : parameters.entrySet()) {
//                if (index != 0){
//                    urlBuilder.append("&");
//                }
//                urlBuilder.append(entry.getKey()).append("=").append(entry.getValue());
//                index ++;
//            }
//        }
//        return urlBuilder.toString();
//    }
//
//	public static String getString(String urlString, Map<String, String> parameters){
//        String getUrl = getGetUrl(urlString, parameters);
//
//        HttpGet httpGet = new HttpGet(getUrl);
//        httpGet.setConfig(REQUEST_CONFIG_DEFAULT);
//
//        try {
//            final CloseableHttpResponse response = client.execute(httpGet);
//            final int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == STATUS_CODE_OK) {
//                final String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//                EntityUtils.consume(response.getEntity());
//                return responseString;
//            }else{
//                httpGet.abort();
//            }
//        } catch (IOException e) {
////            LOG.warning(BSModule.COMMON, e, "Get error, url: [%s], parameters: [%s]", urlString, parameters);
//            httpGet.abort();
//            throw new RuntimeException(e);
//        }
//        return null;
//    }
//
////    public static Future<HttpResponse> asyncPostJson(String urlString, Object params, RequestConfig requestConfig, AsyncHttpJsonResponseCallback callback){
////        return asyncPostJson(urlString, params, (Map<String, String>)null, requestConfig, callback);
////    }
//
////    public static Future<HttpResponse> asyncPostJson(String urlString, Object params, String token, RequestConfig requestConfig, AsyncHttpJsonResponseCallback callback){
////        Map<String, String> headers = new HashMap<>();
////        headers.put("token", token);
////        return asyncPostJson(urlString, params, headers, requestConfig, callback);
////    }
//
//    private static String getJsonBody(String urlString, Object params) {
//        if (params == null) {
//            return null;
//        }
//
//        if (params instanceof String) {
//            return (String) params;
//        }
//        return JSONUtil.toJsonString(params);
//    }
////    public static Future<HttpResponse> asyncPostJson(String urlString, Object params, Map<String, String> headers, RequestConfig requestConfig, AsyncHttpJsonResponseCallback callback){
////        final String sequenceId = getSequenceId();
////        final HttpPost httpPost = new HttpPost(urlString);
////        httpPost.setConfig(requestConfig);
////
////        String body = getJsonBody(urlString, params);
////        LOG.info(BSModule.COMMON, "Async post json, sequenceId = %s, url = %s, body = %s, headers = %s", sequenceId, urlString, body, headers);
////
////        StringEntity requestEntity = new StringEntity(body,"utf-8");
////        requestEntity.setContentEncoding("UTF-8");
////        httpPost.setHeader("Content-type", "application/json");
////        if (headers != null){
////            for (Map.Entry<String, String> entry : headers.entrySet()) {
////                httpPost.addHeader(entry.getKey(), entry.getValue());
////            }
////        }
////        httpPost.setEntity(requestEntity);
////
////        asyncClient.start();
////        Future<HttpResponse> future = asyncClient.execute(httpPost, new FutureCallback<HttpResponse>() {
////
////            @Override
////            public void completed(HttpResponse response) {
////                final int statusCode = response.getStatusLine().getStatusCode();
////                try {
////                    String responseString = null;
////                    if (statusCode == STATUS_CODE_OK) {
////                        if (callback != null){
////                            responseString = EntityUtils.toString(response.getEntity());
////                            EntityUtils.consume(response.getEntity());
////                        }
////                        LOG.info(BSModule.COMMON, "Async post json complete, sequenceId = %s, url = %s, responseString = %s", sequenceId, urlString, responseString);
////                    }else{
////                        LOG.warning(BSModule.COMMON, "Async post json error, sequenceId = %s, url = %s, statusCode = %s", sequenceId, urlString, statusCode);
////                        httpPost.abort();
////                    }
////                    if (callback != null){
////                        callback.onResponse(statusCode, responseString, null);
////                    }
////                } catch (IOException e) {
////                    LOG.warning(BSModule.COMMON, e, "Async post json error, sequenceId = %s, url = %s, parameters = %s", sequenceId, urlString, params);
////                    httpPost.abort();
////                    if (callback != null){
////                        callback.onResponse(statusCode, null, e);
////                    }
////                }
////            }
////
////            @Override
////            public void failed(Exception e) {
////                httpPost.abort();
////                if (callback != null){
////                    callback.onResponse(0, null, e);
////                }
////            }
////
////            @Override
////            public void cancelled() {
////                httpPost.abort();
////                if (callback != null){
////                    callback.onResponse(0, null, null);
////                }
////            }
////        });
////        return future;
////    }
//
//    public static String postJson(String urlString, Object params){
//        return postJson(urlString, params, null, REQUEST_CONFIG_DEFAULT);
//    }
//
//    public static String postJson(String urlString, Object params, Map<String, String> headers){
//        return postJson(urlString, params, headers, REQUEST_CONFIG_DEFAULT);
//    }
//
//    public static String postJson(String urlString, Object params, String token){
//        Map<String, String> headers = new HashMap<>();
//        headers.put("token", token);
//        return postJson(urlString, params, headers, REQUEST_CONFIG_DEFAULT);
//    }
//
//    public static String postJson(String urlString, Object params, Map<String, String> headers, RequestConfig requestConfig){
//        final HttpPost httpPost = new HttpPost(urlString);
//        httpPost.setConfig(requestConfig);
//
//        String body = getJsonBody(urlString, params);
////        LOG.info(BSModule.COMMON, "Post json, sequenceId = %s, url = %s, body = %s, headers = %s", sequenceId, urlString, body, headers);
//
//        if (body != null && body != ""){
//            StringEntity requestEntity = new StringEntity(body,"utf-8");
//            requestEntity.setContentEncoding("UTF-8");
//            httpPost.setEntity(requestEntity);
//        }
//
//        httpPost.setHeader("Content-type", "application/json");
//        if (headers != null){
//            for (Map.Entry<String, String> entry : headers.entrySet()) {
//                httpPost.addHeader(entry.getKey(), entry.getValue());
//            }
//        }
//
//        String responseString = null;
//        try {
//            final CloseableHttpResponse response = client.execute(httpPost);
//            final int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode == STATUS_CODE_OK) {
//                responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
//                EntityUtils.consume(response.getEntity());
////                LOG.info(BSModule.COMMON, "Post json complete, sequenceId = %s, url = %s, responseString = %s, body = %s, headers = %s", sequenceId, urlString, responseString, body, headers);
//            }else{
////                LOG.warning(BSModule.COMMON, "Post json error, sequenceId = %s, url = %s, statusCode = %s, body = %s, headers = %s", sequenceId, urlString, statusCode, body, headers);
//                httpPost.abort();
//            }
//        } catch (IOException e) {
////            LOG.warning(BSModule.COMMON, e, "Post json error, sequenceId = %s, url = %s, body = %s, headers = %s", sequenceId, urlString, body, headers);
//            httpPost.abort();
//            throw new RuntimeException(e);
//        }
//        return responseString;
//    }
//
//
////    public static String getForm(String urlString, Map<String, String> parameters, Map<String, String> headers) {
////        final String sequenceId = getSequenceId();
////        LOG.info(BSModule.COMMON, "Get form, sequenceId = %s, url = %s, parameters = %s, headers = %s", sequenceId, urlString, parameters, headers);
////
////        String getUrl = getGetUrl(urlString, parameters);
////        final HttpGet httpGet = new HttpGet(getUrl);
////        httpGet.setConfig(REQUEST_CONFIG_DEFAULT);
////
////        if (headers != null){
////            for (Map.Entry<String, String> entry : headers.entrySet()) {
////                httpGet.addHeader(entry.getKey(), entry.getValue());
////            }
////        }
////
////        try {
////            final CloseableHttpResponse response = client.execute(httpGet);
////            final int statusCode = response.getStatusLine().getStatusCode();
////            if (statusCode == STATUS_CODE_OK) {
////                final String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
////                EntityUtils.consume(response.getEntity());
////                LOG.info(BSModule.COMMON, "Get form complete, sequenceId = %s, url = %s, parameters = %s, responseString = %s", sequenceId, urlString, parameters, responseString);
////                return responseString;
////            }else{
////                LOG.warning(BSModule.COMMON, "Get form error, sequenceId = %s, url = %s, parameters = %s, statusCode = %s", sequenceId, urlString, parameters, statusCode);
////                httpGet.abort();
////            }
////        } catch (IOException e) {
////            LOG.warning(BSModule.COMMON, e, "Get form error, sequenceId = %s, url = %s, parameters = %s", sequenceId, urlString, parameters);
////            httpGet.abort();
////        }
////        return null;
////    }
//
////	public static String postForm(String urlString, Map<String, String> parameters) {
////        final String sequenceId = getSequenceId();
////        LOG.info(BSModule.COMMON, "Post form, sequenceId = %s, url = %s, parameters = %s", sequenceId, urlString, parameters);
////
////        final HttpPost httpPost = new HttpPost(urlString);
////        httpPost.setConfig(REQUEST_CONFIG_DEFAULT);
////
////        final List<NameValuePair> params = new ArrayList<>();
////        for (Map.Entry<String, String> entry : parameters.entrySet()) {
////            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
////        }
////        try {
////            httpPost.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
////
////            final CloseableHttpResponse response = client.execute(httpPost);
////            final int statusCode = response.getStatusLine().getStatusCode();
////            if (statusCode == STATUS_CODE_OK) {
////                final String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
////                EntityUtils.consume(response.getEntity());
////                LOG.info(BSModule.COMMON, "Post form complete, sequenceId = %s, url = %s, parameters = %s, responseString = %s", sequenceId, urlString, parameters, responseString);
////                return responseString;
////            }else{
////                LOG.warning(BSModule.COMMON, "Post form error, sequenceId = %s, url = %s, parameters = %s, statusCode = %s", sequenceId, urlString, parameters, statusCode);
////                httpPost.abort();
////            }
////        } catch (IOException e) {
////        	LOG.warning(BSModule.COMMON, e, "Post form error, sequenceId = %s, url = %s, parameters = %s", sequenceId, urlString, parameters);
////            httpPost.abort();
////        }
////        return null;
////    }
//
////    public static String postFile(String urlString, Map<String, String> parameters, String fileParamName, File file) {
////	    return postFile(urlString, parameters, fileParamName, file, null);
////    }
//
////    public static String postFile(String urlString, Map<String, String> parameters, String fileParamName, File file, Map<String, String> headers){
////        final String sequenceId = getSequenceId();
////        LOG.info(BSModule.COMMON, "Post file, sequenceId = %s, url = %s, parameters = %s, headers = %s", sequenceId, urlString, parameters, headers);
////
////        final HttpPost httpPost = new HttpPost(urlString);
////        httpPost.setConfig(REQUEST_CONFIG_DEFAULT);
////
////        if (headers != null){
////            for (Map.Entry<String, String> entry : headers.entrySet()) {
////                httpPost.addHeader(entry.getKey(), entry.getValue());
////            }
////        }
////
////        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
////        builder.setCharset(Charset.forName("utf-8"));
////        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);//加上此行代码解决返回中文乱码问题
////        builder.addBinaryBody(fileParamName, file, ContentType.MULTIPART_FORM_DATA, file.getName());// 文件流
////        for (Map.Entry<String, String> entry : parameters.entrySet()) {
////            builder.addTextBody(entry.getKey(), entry.getValue());
////        }
////
////        try {
////            httpPost.setEntity(builder.build());
////
////            final CloseableHttpResponse response = client.execute(httpPost);
////            final int statusCode = response.getStatusLine().getStatusCode();
////            if (statusCode == STATUS_CODE_OK) {
////                final String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
////                EntityUtils.consume(response.getEntity());
////                LOG.info(BSModule.COMMON, "Post file complete, sequenceId = %s, url = %s, responseString = %s, parameters = %s, headers = %s", sequenceId, urlString, responseString, parameters, headers);
////                return responseString;
////            }else{
////                LOG.warning(BSModule.COMMON, "Post file error, sequenceId = %s, url = %s, statusCode = %s, parameters = %s, headers = %s", sequenceId, urlString, statusCode, parameters, headers);
////                httpPost.abort();
////            }
////        } catch (IOException e) {
////            LOG.warning(BSModule.COMMON, e, "Post file error, sequenceId = %s, url = %s, parameters = %s, headers = %s", sequenceId, urlString, parameters, headers);
////            httpPost.abort();
////        }
////        return null;
////    }
//
//    private static final int cache = 10 * 1024;
//
////    public static boolean downloadFile(String urlString, String savePath){
////        final String sequenceId = getSequenceId();
////        LOG.info(BSModule.COMMON, "Download file begin, sequenceId = %s, url = %s, savePath = %s", sequenceId, urlString, savePath);
////        boolean success = false;
////        FileOutputStream fileout = null;
////        InputStream is = null;
////        HttpGet httpget = new HttpGet(urlString);
////        try {
////            HttpResponse response = client.execute(httpget);
////            if (response.getStatusLine().getStatusCode() == 200){
////                HttpEntity entity = response.getEntity();
////                is = entity.getContent();
////
////                File file = new File(savePath);
////                file.getParentFile().mkdirs();
////                fileout = new FileOutputStream(file);
////
////                byte[] buffer = new byte[cache];
////                int ch = 0;
////                while ((ch = is.read(buffer)) != -1) {
////                    fileout.write(buffer, 0, ch);
////                }
////            }else{
////                httpget.abort();
////            }
////        } catch (Exception e) {
////            LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s, savePath = %s", sequenceId, urlString, savePath);
////            httpget.abort();
////        }finally {
////            try {
////                is.close();
////            }catch (IOException e){
////                LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s, savePath = %s", sequenceId, urlString, savePath);
////            }
////            try {
////                fileout.flush();
////            } catch (IOException e) {
////                LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s, savePath = %s", sequenceId, urlString, savePath);
////            }
////            try {
////                fileout.close();
////            } catch (IOException e) {
////                LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s, savePath = %s", sequenceId, urlString, savePath);
////            }
////
////            File saveFile = new File(savePath);
////            if (saveFile.exists()){
////                success = true;
////            }
////        }
////
////        LOG.info(BSModule.COMMON, "Download file finish, sequenceId = %s, url = %s, savePath = %s, success = %s", sequenceId, urlString, savePath, success);
////        return success;
////    }
//
////    public static byte[] downloadFile(String urlString) throws IOException{
////        final String sequenceId = getSequenceId();
////        LOG.info(BSModule.COMMON, "Download file begin, sequenceId = %s, url = %s", sequenceId, urlString);
////        ByteArrayOutputStream bos = null;
////        InputStream is = null;
////        HttpGet httpget = new HttpGet(urlString);
////        try {
////            HttpResponse response = client.execute(httpget);
////            final int statusCode = response.getStatusLine().getStatusCode();
////            if (statusCode == STATUS_CODE_OK) {
////                HttpEntity entity = response.getEntity();
////                is = entity.getContent();
////
////                bos = new ByteArrayOutputStream();
////                byte[] buffer = new byte[cache];
////                int ch = 0;
////                while ((ch = is.read(buffer)) != -1) {
////                    bos.write(buffer, 0, ch);
////                }
////                byte[] data = bos.toByteArray();
////                return data;
////            }else{
////                httpget.abort();
////                return null;
////            }
////        } catch (Exception e) {
////            LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s", sequenceId, urlString);
////            httpget.abort();
////            throw new IOException(e);
////        }finally {
////            try {
////                if (is != null){
////                    is.close();
////                }
////            }catch (IOException e){
////                LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s", sequenceId, urlString);
////            }
////            try {
////                if (bos != null){
////                    bos.close();
////                }
////            } catch (IOException e) {
////                LOG.warning(BSModule.COMMON, e, "Download file failed, sequenceId = %s, url = %s", sequenceId, urlString);
////            }
////        }
////    }
//}
