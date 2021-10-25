package com.cell.utils;

import com.cell.constants.GRPCConstants;
import io.grpc.MethodDescriptor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 11:40
 */
public class GRPCUtils
{


    public static String extractDomainSocketAddressPath(final String address)
    {
        if (!address.startsWith(GRPCConstants.DOMAIN_SOCKET_ADDRESS_PREFIX))
        {
            throw new IllegalArgumentException(address + " is not a valid domain socket address.");
        }
        String path = address.substring(GRPCConstants.DOMAIN_SOCKET_ADDRESS_PREFIX.length());
        if (path.startsWith("//"))
        {
            path = path.substring(2);
        }
        return path;
    }

    public static String extractServiceName(final MethodDescriptor<?, ?> method)
    {
        return MethodDescriptor.extractFullServiceName(method.getFullMethodName());
    }

    public static String extractMethodName(final MethodDescriptor<?, ?> method)
    {
        final String fullMethodName = method.getFullMethodName();
        final int index = fullMethodName.lastIndexOf('/');
        if (index == -1)
        {
            return fullMethodName;
        }
        return fullMethodName.substring(index + 1);
    }
}
