package com.cell.base.core.config;

import com.cell.base.core.annotations.ActivePlugin;
import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.common.models.Module;
import com.cell.base.core.log.LOG;
import com.cell.server.IHttpServer;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 06:06
 */
@ActivePlugin
public class SpringConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory>
{

    @AutoPlugin
    protected IHttpServer server;

    @Bean
    public RestTemplate restTemplate()
    {
//        HttpConfig config = HttpConfigFactory.getInstance().getHttpConfig();
        HttpConfig config = new HttpConfig();
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(config.getConnectTimeout());// 设置超时
        requestFactory.setReadTimeout(config.getReadTimeout());
        HttpComponentsClientHttpRequestFactory factory = new
                HttpComponentsClientHttpRequestFactory();
        factory.setConnectionRequestTimeout(config.getRequestTimeout());
        factory.setConnectTimeout(config.getConnectTimeout());
        factory.setReadTimeout(config.getReadTimeout());
        // 设置绕过HTTPS
        SSLContextBuilder builder = new SSLContextBuilder();
        RestTemplate restTemplate = null;
        try
        {
            builder.loadTrustMaterial(null, (X509Certificate[] x509Certificates, String s) -> true);
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(builder.build(), new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null, NoopHostnameVerifier.INSTANCE);
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("http", new PlainConnectionSocketFactory())
                    .register("https", socketFactory).build();
            PoolingHttpClientConnectionManager phccm = new PoolingHttpClientConnectionManager(registry);
            phccm.setMaxTotal(config.getMaxTotal());
            phccm.setDefaultMaxPerRoute(config.getDefaultMaxPerRoute());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory).setConnectionManager(phccm).setConnectionManagerShared(true).build();
            factory.setHttpClient(httpClient);

            restTemplate = new RestTemplate(factory);
            LOG.info(Module.HTTP_FRAMEWORK, "创建SSL RestTemplate成功！");
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e)
        {
            LOG.error(Module.HTTP_FRAMEWORK, e, "创建SSLBuilder失败！");
        }
        return restTemplate == null ? new RestTemplate(requestFactory) : restTemplate;
    }

    @Override
    public void customize(ConfigurableWebServerFactory factory)
    {
        factory.setPort(server.getPort());
    }
}
