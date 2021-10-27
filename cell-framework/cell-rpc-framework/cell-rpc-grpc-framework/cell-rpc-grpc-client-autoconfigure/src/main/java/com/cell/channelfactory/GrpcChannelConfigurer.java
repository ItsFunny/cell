package com.cell.channelfactory;

import io.grpc.ManagedChannelBuilder;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 18:57
 */
public interface GrpcChannelConfigurer extends BiConsumer<ManagedChannelBuilder<?>, String>
{
    @Override
    default GrpcChannelConfigurer andThen(final BiConsumer<? super ManagedChannelBuilder<?>, ? super String> after)
    {
        requireNonNull(after, "after");
        return (l, r) ->
        {
            accept(l, r);
            after.accept(l, r);
        };
    }
}
