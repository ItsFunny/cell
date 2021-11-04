package com.cell.grpc.client.autoconfigurer.config;

import com.cell.Configuration;
import com.cell.com.cell.grpc.common.constants.GRPCConstants;
import com.cell.exceptions.ProgramaException;
import io.grpc.netty.shaded.io.grpc.netty.NegotiationType;
import lombok.Data;

import java.net.URI;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-04 11:41
 */
@Data
public class GRPCClientConfiguration
{
    private static GRPCClientConfiguration instance = null;
    private final Map<String, GRPCClientConfigurationNode> client = new ConcurrentHashMap<>();
    // 运行时变更,不从配置文件中读取
    private final Map<String, GRPCClientConfigurationNode> runtimeChangeableClient = new HashMap<>();

    public static GRPCClientConfiguration getInstance()
    {
        return instance;
    }
    public void updateRuntime(String key,GRPCClientConfigurationNode node){
        this.runtimeChangeableClient.put(key,node);
    }

    private static final String DEFAULT_DEFAULT_LOAD_BALANCING_POLICY = "round_robin";
    private static final boolean DEFAULT_ENABLE_KEEP_ALIVE = false;
    private static final Duration DEFAULT_KEEP_ALIVE_TIME = Duration.of(5, ChronoUnit.MINUTES);
    private static final Duration DEFAULT_KEEP_ALIVE_TIMEOUT = Duration.of(20, ChronoUnit.SECONDS);
    private static final boolean DEFAULT_KEEP_ALIVE_WITHOUT_CALLS = false;
    private static final Duration DEFAULT_SHUTDOWN_GRACE_PERIOD = Duration.ofSeconds(30);
    private static final boolean DEFAULT_FULL_STREAM_DECOMPRESSION = false;
    private static final NegotiationType DEFAULT_NEGOTIATION_TYPE = NegotiationType.TLS;
    private static final Duration DEFAULT_IMMEDIATE_CONNECT = Duration.ZERO;


    static
    {
        try
        {
            GRPCClientConfiguration grpcClientConfiguration = Configuration.getDefault().getConfigValue(GRPCConstants.DEFAULT_GRPC_CONFIG_MODULE).asObject(GRPCClientConfiguration.class);
            instance = grpcClientConfiguration;
        } catch (Exception e)
        {
            instance = new GRPCClientConfiguration();
            GRPCClientConfigurationNode node = new GRPCClientConfigurationNode();
            node.setAddress(URI.create("static://" + GRPCConstants.DEFAULT_GRPC_SERVER_ADDR + ":" + GRPCConstants.DEFAULT_GRPC_SERVER_PORT));
            instance.getClient().put(GRPCConstants.DEFAULT_GRPC_SERVER, node);
        }
    }

    public GRPCClientConfigurationNode getChannel(final String name)
    {
        GRPCClientConfigurationNode ret = this.runtimeChangeableClient.get(name);
        if (null != ret)
        {
            return ret;
        }
        ret = this.client.get(name);
        if (ret == null)
        {
            throw new ProgramaException("asd");
        }
        return ret;
    }


    @Data
    public static class GRPCClientConfigurationNode
    {
        private URI address;
        private String defaultLoadBalancingPolicy = DEFAULT_DEFAULT_LOAD_BALANCING_POLICY;
        private Boolean enableKeepAlive = DEFAULT_ENABLE_KEEP_ALIVE;
        private Long keepAliveTime = DEFAULT_KEEP_ALIVE_TIME.toMillis();
        private Long keepAliveTimeout = DEFAULT_KEEP_ALIVE_TIMEOUT.toMillis();
        private boolean keepAliveWithoutCalls = DEFAULT_KEEP_ALIVE_WITHOUT_CALLS;
        private Long shutdownGracePeriod = DEFAULT_SHUTDOWN_GRACE_PERIOD.toMillis();
        private Long maxInboundMessageSize = null;
        private boolean fullStreamDecompression;
        private NegotiationType negotiationType = DEFAULT_NEGOTIATION_TYPE;
        private Long immediateConnectTimeout = DEFAULT_IMMEDIATE_CONNECT.toMillis();

        public void copyDefaultsFrom(final GRPCClientConfigurationNode config)
        {
            if (this == config)
            {
                return;
            }
            if (this.address == null)
            {
                this.address = config.address;
            }
            if (this.defaultLoadBalancingPolicy == null)
            {
                this.defaultLoadBalancingPolicy = config.defaultLoadBalancingPolicy;
            }
            if (this.enableKeepAlive == null)
            {
                this.enableKeepAlive = config.enableKeepAlive;
            }
            if (this.keepAliveTime == null)
            {
                this.keepAliveTime = config.keepAliveTime;
            }
            if (this.keepAliveTimeout == null)
            {
                this.keepAliveTimeout = config.keepAliveTimeout;
            }
            this.keepAliveWithoutCalls = config.keepAliveWithoutCalls;
            if (this.shutdownGracePeriod == null)
            {
                this.shutdownGracePeriod = config.shutdownGracePeriod;
            }
            if (this.maxInboundMessageSize == null)
            {
                this.maxInboundMessageSize = config.maxInboundMessageSize;
            }
            this.fullStreamDecompression = config.fullStreamDecompression;
            if (this.negotiationType == null)
            {
                this.negotiationType = config.negotiationType;
            }
            if (this.immediateConnectTimeout == null)
            {
                this.immediateConnectTimeout = config.immediateConnectTimeout;
            }
        }
    }

}
