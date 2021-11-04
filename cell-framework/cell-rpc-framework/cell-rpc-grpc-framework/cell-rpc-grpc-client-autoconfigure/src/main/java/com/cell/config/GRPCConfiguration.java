//package com.cell.config;
//
//import lombok.Data;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-10-27 19:00
// */
//@Data
//public class GRPCConfiguration
//{
//    private static final GRPCConfiguration instance = new GRPCConfiguration();
//    private final Map<String, GrpcChannelProperties> client = new ConcurrentHashMap<>();
//
//    private GrpcChannelProperties getRawChannel(final String name) {
//        return this.client.computeIfAbsent(name, key -> new GrpcChannelProperties());
//    }
//    public GrpcChannelProperties getChannel(final String name) {
//        final GrpcChannelProperties properties = getRawChannel(name);
//        properties.copyDefaultsFrom(getGlobalChannel());
//        return properties;
//    }
//    public static final String GLOBAL_PROPERTIES_KEY = "GLOBAL";
//    public final GrpcChannelProperties getGlobalChannel() {
//        return getRawChannel(GLOBAL_PROPERTIES_KEY);
//    }
//
//    public static GRPCConfiguration getInstance(){
//        return instance;
//    }
//
//
//
//
//}
