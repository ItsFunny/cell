package com.cell.nameresolver;

import com.google.common.collect.ImmutableList;
import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;

import java.util.Collection;

import static java.util.Objects.requireNonNull;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 04:49
 */
public class StaticNameResolver extends NameResolver
{
    private final String authority;
    private final ResolutionResult result;

    public StaticNameResolver(final String authority, final EquivalentAddressGroup target)
    {
        this(authority, ImmutableList.of(requireNonNull(target, "target")));
    }

    public StaticNameResolver(final String authority, final Collection<EquivalentAddressGroup> targets)
    {
        this.authority = requireNonNull(authority, "authority");
        if (requireNonNull(targets, "targets").isEmpty())
        {
            throw new IllegalArgumentException("Must have at least one target");
        }
        this.result = ResolutionResult.newBuilder()
                .setAddresses(ImmutableList.copyOf(targets))
                .build();
    }

    public StaticNameResolver(final String authority, final ResolutionResult result)
    {
        this.authority = requireNonNull(authority, "authority");
        this.result = requireNonNull(result, "result");
    }

    @Override
    public String getServiceAuthority()
    {
        return this.authority;
    }

    @Override
    public void start(final Listener2 listener)
    {
        listener.onResult(this.result);
    }

    @Override
    public void refresh()
    {
        // Does nothing
    }

    @Override
    public void shutdown()
    {
        // Does nothing
    }

    @Override
    public String toString()
    {
        return "StaticNameResolver [authority=" + this.authority + ", result=" + this.result + "]";
    }
}
