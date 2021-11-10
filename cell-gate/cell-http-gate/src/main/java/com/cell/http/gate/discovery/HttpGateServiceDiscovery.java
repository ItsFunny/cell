package com.cell.http.gate.discovery;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.context.InitCTX;
import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.core.annotations.AutoPlugin;
import com.cell.bee.loadbalance.model.ServerCmdMetaInfo;
import com.cell.bee.transport.model.ServerMetaData;
import com.cell.gate.common.utils.GatewayUtils;
import com.cell.node.discovery.model.Instance;
import com.cell.node.discovery.nacos.discovery.IInstanceOnChange;
import com.cell.node.discovery.nacos.discovery.abs.AbstractServiceDiscovery;
import com.cell.node.discovery.nacos.discovery.abs.Snap;
import com.cell.resolver.IKeyResolver;
import com.cell.resolver.impl.DefaultStringKeyResolver;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 05:41
 */
@Data
public class HttpGateServiceDiscovery extends AbstractServiceDiscovery<DefaultStringKeyResolver.StringKeyResolver, String>
{
    private static HttpGateServiceDiscovery instance;

    @AutoPlugin
    public void setInstance(HttpGateServiceDiscovery serviceDiscovery)
    {
        HttpGateServiceDiscovery.instance = serviceDiscovery;
    }

    public static HttpGateServiceDiscovery getInstance()
    {
        return instance;
    }

    public HttpGateServiceDiscovery()
    {
        this.setCallBack(snap -> this.handleDelta(snap));
    }



    @Override
    protected Set<ServerCmdMetaInfo> doGetServerByProtocol(String method, String protocol)
    {
        return this.serverMetas.get(this.resolver.resolve(DefaultStringKeyResolver.StringKeyResolver.builder().method(method).uri(protocol).build()));
    }


    @Override
    protected void beforeInit(InitCTX ctx)
    {
        this.registerSelf(ctx);
    }

    @Override
    protected void afterInit(InitCTX ctx)
    {
        this.refreshUriRules();
    }

    private void refreshUriRules()
    {
        this.refreshUriRules(this.getRulesFromMeta(this.getServerMetas()));
    }


    private Set<RuleWp> getRulesFromMeta(Map<String, Set<ServerCmdMetaInfo>> serverMetas)
    {
        Set<String> protocols = serverMetas.keySet();
        Set<RuleWp> ruleWps = new HashSet<>();

        for (String protocol : protocols)
        {
            Set<ServerCmdMetaInfo> serverCmdMetaInfos = serverMetas.get(protocol);
            serverCmdMetaInfos.stream().forEach(s ->
            {
                List<ServerMetaData.ServerMetaReactor> reactors = s.getMetaData().getReactors();
                if (CollectionUtils.isEmpty(reactors)) return;
                reactors.stream().forEach(r ->
                {
                    List<ServerMetaData.ServerMetaCmd> cmds = r.getCmds();
                    if (CollectionUtils.isEmpty(cmds)) return;
                    cmds.stream().forEach(c ->
                    {
                        RuleWp wp = new RuleWp();
                        wp.method = EnumHttpRequestType.getStrById(c.getMethod());
                        wp.protocol = c.getProtocol();
                        ruleWps.add(wp);
                    });
                });
            });
        }
        return ruleWps;
    }


    // 注册自身,网关为硬编码注册
    private void registerSelf(InitCTX ctx)
    {
        String domain = (String) ctx.getData().get("domain");
        String cluster = (String) ctx.getData().get("cluster");
        String ip = (String) ctx.getData().get("ip");
        Integer port = (Integer) ctx.getData().get("port");
        String serviceName = (String) ctx.getData().get("serviceName");
        ServerMetaData serverMetaData = new ServerMetaData();
        ServerMetaData.ServerExtraInfo extraInfo = new ServerMetaData.ServerExtraInfo();
        extraInfo.setDomain(domain);
        serverMetaData.setExtraInfo(extraInfo);
        Map<String, String> metadatas = ServerMetaData.toMetaData(serverMetaData);
        Instance instance = Instance.builder()
                .weight((byte) 1)
                .metaData(metadatas)
                .clusterName(cluster)
                .ip(ip)
                .healthy(true)
                .enable(true)
                .port(port)
                .serviceName(serviceName)
                .build();
        nodeDiscovery.registerServerInstance(instance);
    }

    @Override
    protected IKeyResolver<DefaultStringKeyResolver.StringKeyResolver, String> newKeyResolver()
    {
        return new DefaultStringKeyResolver();
    }

    @Override
    protected String resolve(IKeyResolver<DefaultStringKeyResolver.StringKeyResolver, String> resolver, ServerMetaData.ServerMetaCmd c)
    {
        return resolver
                .resolve(DefaultStringKeyResolver
                        .StringKeyResolver.builder()
                        .uri(c.getProtocol())
                        .method(EnumHttpRequestType.getStrById(c.getMethod())).
                                build());
    }

    @Override
    protected byte filterType()
    {
        return ProtocolConstants.TYPE_HTTP;
    }

    class RuleWp
    {
        String method;
        String protocol;
    }

    private void handleDelta(Snap snap)
    {
        this.refreshUriRules(this.getRulesFromMeta(this.serverMetas));
    }

    private void refreshUriRules(Set<RuleWp> ruleWps)
    {
        Set<GatewayFlowRule> rules = new HashSet<>();
        for (RuleWp ruleWp : ruleWps)
        {
            GatewayFlowRule rule = GatewayUtils.createGatewayFlowRule(ruleWp.method, ruleWp.protocol);
            rules.add(rule);
        }
        GatewayRuleManager.loadRules(rules);
    }
}