package com.cell.metrics.prometheus.sd.extension.reactor;

import com.cell.base.core.annotations.AutoPlugin;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.http.framework.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.metrics.prometheus.sd.extension.model.ChangeItem;
import com.cell.metrics.prometheus.sd.extension.sd.IPrometheusServiceDiscovery;
import com.cell.node.discovery.nacos.discovery.IServiceDiscovery;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-22 21:27
 */
@ReactorAnno(group = "/v1")
@Data
public class ServiceReactor extends AbstractHttpDymanicCommandReactor
{
    public static final String prometheusServiceReactor = "prometheus_sd";
    @AutoPlugin
    private IPrometheusServiceDiscovery registrationService;

    private static final String CONSUL_IDX_HEADER = "X-Consul-Index";

    public static MultiValueMap<String, String> createHeaders(long index)
    {
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add(CONSUL_IDX_HEADER, "" + index);
        return headers;
    }

    public static <T> ResponseEntity createResponseEntity(ChangeItem result)
    {
        return new ResponseEntity<>(result.getItem(), createHeaders(result.getChangeIndex()), HttpStatus.OK);
    }

}
