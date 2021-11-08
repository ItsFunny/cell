package com.cell.rpc.grpc.client.nameresolver;

import io.grpc.NameResolverProvider;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 04:54
 */
public class FacacedNameResolverProvider extends NameResolverProvider
{
    private List<INameResolverProvider> providers;

    @Override
    protected boolean isAvailable()
    {
        return false;
    }

    /**
     * A priority, from 0 to 10 that this provider should be used, taking the current environment into
     * consideration. 5 should be considered the default, and then tweaked based on environment
     * detection. A priority of 0 does not imply that the provider wouldn't work; just that it should
     * be last in line.
     *
     * @since 1.0.0
     */
    @Override
    protected int priority()
    {
        return 0;
    }

    @Override
    public String getDefaultScheme()
    {
        return null;
    }
}
