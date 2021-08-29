package com.cell.constants;

import org.springframework.core.Ordered;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-24 05:49
 */
public interface SpringBridge
{
    int BEAN_REGISTER_ORDERER = Ordered.HIGHEST_PRECEDENCE;
    String PLUGIN_COLLECTOR = "activePluginCollector";
    String EXTENSION_FLAG_ATTR = "ActiveExtension";
    String BEAN_NAME_ATTR = "activePluginBeanName";

    String defaultPluginPrefixGroup = "activePlugin";
    String factoryBeanPostPrefix = "factoryPost";
    String beanPostPrefix="beanPostPrefix";
    String SPRING_BEAN_DEPANDENCIES = "SPRING_BEAN_DEPANDENCIES";
}
