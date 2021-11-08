package com.cell.http.framework.config;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 06:07
 */

public class HttpConfigFactory
{

    private static final String HTTP_CONFIG_MODULE = "httpFrameworkRest";

    private HttpConfigFactory()
    {
    }

    public static HttpConfigFactory getInstance()
    {
        return HttpConfigFactoryHolder.INSTANCE;
    }

    private static class HttpConfigFactoryHolder
    {
        private static final HttpConfigFactory INSTANCE = new HttpConfigFactory();
    }

    //防止反序列化生成新的实例
    private Object readResolve()
    {
        return HttpConfigFactoryHolder.INSTANCE;
    }

    private HttpConfig httpConfig;

    public HttpConfig getHttpConfig()
    {
        return httpConfig;
    }

//    public void init()
//    {
//        try
//        {
//            httpConfig = Configuration.getDefault().getAndMonitorConfig(HTTP_CONFIG_MODULE, HttpConfig.class, (conf) ->
//            {
//                httpConfig = conf;
//            });
//        } catch (Exception e)
//        {
//            httpConfig = new HttpConfig();
//        }
//    }
}
