package com.cell.nameresolver;

import io.grpc.EquivalentAddressGroup;
import io.grpc.NameResolver;
import io.grpc.NameResolverProvider;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-28 04:50
 */
public class StaticNameResolverProvider extends NameResolverProvider
{
    /**
     * The constant containing the scheme that will be used by this factory.
     */
    public static final String STATIC_SCHEME = "static";

    private static final Pattern PATTERN_COMMA = Pattern.compile(",");

    @Nullable
    @Override
    public NameResolver newNameResolver(final URI targetUri, final NameResolver.Args args)
    {
        if (STATIC_SCHEME.equals(targetUri.getScheme()))
        {
            return of(targetUri.getAuthority(), args.getDefaultPort());
        }
        return null;
    }

    /**
     * Creates a new {@link NameResolver} for the given authority and attributes.
     *
     * @param targetAuthority The authority to connect to.
     * @param defaultPort     The default port to use, if none is specified.
     * @return The newly created name resolver for the given target.
     */
    private NameResolver of(final String targetAuthority, int defaultPort)
    {
        requireNonNull(targetAuthority, "targetAuthority");
        // Determine target ips
        final String[] hosts = PATTERN_COMMA.split(targetAuthority);
        final List<EquivalentAddressGroup> targets = new ArrayList<>(hosts.length);
        for (final String host : hosts)
        {
            final URI uri = URI.create("//" + host);
            int port = uri.getPort();
            if (port == -1)
            {
                port = defaultPort;
            }
            targets.add(new EquivalentAddressGroup(new InetSocketAddress(uri.getHost(), port)));
        }
        if (targets.isEmpty())
        {
            throw new IllegalArgumentException("Must have at least one target, but was: " + targetAuthority);
        }
        return new StaticNameResolver(targetAuthority, targets);
    }

    @Override
    public String getDefaultScheme()
    {
        return STATIC_SCHEME;
    }

    @Override
    protected boolean isAvailable()
    {
        return true;
    }

    @Override
    protected int priority()
    {
        return 4; // Less important than DNS
    }

    @Override
    public String toString()
    {
        return "StaticNameResolverProvider [scheme=" + getDefaultScheme() + "]";
    }

}
