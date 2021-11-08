package com.cell.grpc.common.config;

import com.cell.utils.CollectionUtils;
import lombok.Data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description server的限流配置
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:36
 */
@Data
public class ServerRatePropertyNode
{
    private boolean fastFinish = false;
    public static final String URI_SEPERATE = "/";
    private RateRulePropertyNode defaultFlowRule = new RateRulePropertyNode(1D, 1);
    // 相同uri的放在一起,进行返回
    private Map<String, List<RateRulePropertyNode>> fullPathFlowRuleMap = new HashMap<>();
    private Boolean interceptFullPath = true;
    private Boolean logOnException = true;
    private List<String> whitelist = Arrays.asList();

    public RateRulePropertyNode getFlowRule(String method, String uri)
    {
        List<RateRulePropertyNode> roles = fullPathFlowRuleMap.get(uri);
        if (CollectionUtils.isEmpty(roles))
        {
            return defaultFlowRule;
        }
        return roles.stream().filter(p -> p.getMethod().equalsIgnoreCase(method)).findFirst().get();
    }
}
