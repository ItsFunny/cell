package com.cell.rpc.grpc.client.channelfactory.impl;

import com.cell.base.common.models.Module;
import com.cell.rpc.grpc.client.channelfactory.GRPCChannelFactory;
import com.cell.rpc.grpc.client.channelfactory.GrpcChannelConfigurer;
import com.cell.rpc.grpc.client.grpc.client.autoconfigurer.config.GRPCClientConfiguration;
import com.cell.rpc.grpc.client.interceptor.GlobalClientInterceptorRegistry;
import com.cell.sdk.log.LOG;
import com.google.common.collect.Lists;
import io.grpc.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 18:54
 */
public abstract class AbstractChannelFactory<T extends ManagedChannelBuilder<T>> implements GRPCChannelFactory
{
    protected final GlobalClientInterceptorRegistry globalClientInterceptorRegistry;
    protected final List<GrpcChannelConfigurer> channelConfigurers;


    private final Map<String, ManagedChannel> channels = new ConcurrentHashMap<>();
    private final Map<String, ConnectivityState> channelStates = new ConcurrentHashMap<>();
    private boolean shutdown = false;

    public AbstractChannelFactory(final GlobalClientInterceptorRegistry globalClientInterceptorRegistry,
                                  final List<GrpcChannelConfigurer> channelConfigurers)
    {
        this.globalClientInterceptorRegistry =
                requireNonNull(globalClientInterceptorRegistry, "globalClientInterceptorRegistry");
        this.channelConfigurers = requireNonNull(channelConfigurers, "channelConfigurers");
    }

    @Override
    public final Channel createChannel(final String name)
    {
        return createChannel(name, Collections.emptyList());
    }

    @Override
    public Channel createChannel(final String name, final List<ClientInterceptor> customInterceptors,
                                 final boolean sortInterceptors)
    {
        final Channel channel;
        synchronized (this)
        {
            if (this.shutdown)
            {
                throw new IllegalStateException("GrpcChannelFactory is already closed!");
            }
            channel = this.channels.computeIfAbsent(name, this::newManagedChannel);
        }
        final List<ClientInterceptor> interceptors =
                Lists.newArrayList(this.globalClientInterceptorRegistry.getClientInterceptors());
        interceptors.addAll(customInterceptors);
        if (sortInterceptors)
        {
            this.globalClientInterceptorRegistry.sortInterceptors(interceptors);
        }
        return ClientInterceptors.interceptForward(channel, interceptors);
    }

    protected ManagedChannel newManagedChannel(final String name)
    {
        final T builder = newChannelBuilder(name);
        configure(builder, name);
        final ManagedChannel channel = builder.build();
        final Long immediateConnectTimeout = GRPCClientConfiguration.getInstance().getChannel(name).getImmediateConnectTimeout();
        if (immediateConnectTimeout != 0)
        {
            connectOnStartup(name, channel, immediateConnectTimeout);
        }
        watchConnectivityState(name, channel);
        return channel;
    }

    protected void watchConnectivityState(final String name, final ManagedChannel channel)
    {
        final ConnectivityState state = channel.getState(false);
        this.channelStates.put(name, state);
        if (state != ConnectivityState.SHUTDOWN)
        {
            channel.notifyWhenStateChanged(state, () -> watchConnectivityState(name, channel));
        }
    }

    private void connectOnStartup(final String name, final ManagedChannel channel, final long timeout)
    {
        LOG.info(Module.GRPC_CLIENT, "Initiating connection to channel {}", name);
        channel.getState(true);

        final CountDownLatch readyLatch = new CountDownLatch(1);
        waitForReady(channel, readyLatch);
        boolean connected;
        try
        {
            LOG.info(Module.GRPC_CLIENT, "Waiting for connection to channel {}", name);
            connected = !readyLatch.await(timeout, TimeUnit.MILLISECONDS);
        } catch (final InterruptedException e)
        {
            Thread.currentThread().interrupt();
            connected = false;
        }
        if (connected)
        {
            throw new IllegalStateException("Can't connect to channel " + name);
        }
        LOG.info(Module.GRPC_CLIENT, "Successfully connected to channel {}", name);
    }

    private void waitForReady(final ManagedChannel channel, final CountDownLatch readySignal)
    {
        final ConnectivityState state = channel.getState(false);
        LOG.info(Module.GRPC_CLIENT, "Waiting for ready state. Currently in {}", state);
        if (state == ConnectivityState.READY)
        {
            readySignal.countDown();
        } else
        {
            channel.notifyWhenStateChanged(state, () -> waitForReady(channel, readySignal));
        }
    }

    protected abstract T newChannelBuilder(String name);


    protected void configure(final T builder, final String name)
    {
        configureKeepAlive(builder, name);
//        configureSecurity(builder, name);
        configureLimits(builder, name);
        configureCompression(builder, name);
        for (final GrpcChannelConfigurer channelConfigurer : this.channelConfigurers)
        {
            channelConfigurer.accept(builder, name);
        }
    }

    protected void configureCompression(final T builder, final String name)
    {
        GRPCClientConfiguration.GRPCClientConfigurationNode properties = getPropertiesFor(name);
        if (properties.isFullStreamDecompression())
        {
            builder.enableFullStreamDecompression();
        }
    }

    protected void configureLimits(final T builder, final String name)
    {
        GRPCClientConfiguration.GRPCClientConfigurationNode properties = getPropertiesFor(name);
        final Long maxInboundMessageSize = properties.getMaxInboundMessageSize();
        if (maxInboundMessageSize != null)
        {
            builder.maxInboundMessageSize(maxInboundMessageSize.intValue());
        }
    }


    protected void configureKeepAlive(final T builder, final String name)
    {
        GRPCClientConfiguration.GRPCClientConfigurationNode properties = getPropertiesFor(name);
        if (properties.getEnableKeepAlive())
        {
            builder.keepAliveTime(properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
                    .keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.MINUTES)
                    .keepAliveWithoutCalls(properties.isKeepAliveWithoutCalls());
        }
    }

    protected final GRPCClientConfiguration.GRPCClientConfigurationNode getPropertiesFor(final String name)
    {
        return GRPCClientConfiguration.getInstance().getChannel(name);
//        return this.properties.getChannel(name);
    }
}
