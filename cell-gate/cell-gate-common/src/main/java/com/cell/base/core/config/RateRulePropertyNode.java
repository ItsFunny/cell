package com.cell.base.core.config;

import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:37
 */
@Data
public class RateRulePropertyNode
{
    private String method;
    private Integer resourceMode = 0; // 规则是针对 API Gateway 的 route
    private Double count = 1D; // 触发限流的数量
    private Integer intervalSec = 1; // 统计间隔，单位秒
    private Integer grade = RuleConstant.FLOW_GRADE_QPS; // 统计间隔，单位秒
    private Integer controlBehavior = RuleConstant.CONTROL_BEHAVIOR_DEFAULT;
    private Integer maxQueueingTimeoutMs = 3000; // 匀速排队模式下的最长排队时间，单位是毫秒，仅在匀速排队模式下生效

    public RateRulePropertyNode(Double count, Integer intervalSec)
    {
        super();
        this.count = count;
        this.intervalSec = intervalSec;
    }

}
