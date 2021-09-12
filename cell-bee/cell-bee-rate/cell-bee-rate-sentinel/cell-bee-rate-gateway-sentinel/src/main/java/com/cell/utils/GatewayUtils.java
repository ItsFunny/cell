package com.cell.utils;

import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.cell.config.GatewayConfiguration;
import com.cell.config.RateRulePropertyNode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-11 13:44
 */
public class GatewayUtils
{
    public static Mono<Void> fastFinish(ServerWebExchange exchange, Object ret)
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.OK);
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String fastResult = JSONUtil.obj2Json(ret);
        DataBuffer dataBuffer = response.bufferFactory().allocateBuffer().write(fastResult.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

    public static GatewayFlowRule createGatewayFlowRule(String method, String uri)
    {
        RateRulePropertyNode flowRule = GatewayConfiguration.getInstance().getServerRatePropertyNode().getFlowRule(method, uri);
        double count = flowRule.getCount();
        return new GatewayFlowRule(uri)
                .setCount(count) //
                .setIntervalSec(flowRule.getIntervalSec()) //
                .setGrade(flowRule.getGrade()) //
                .setMaxQueueingTimeoutMs(flowRule.getMaxQueueingTimeoutMs()) //
                .setResourceMode(flowRule.getResourceMode());
    }


}
