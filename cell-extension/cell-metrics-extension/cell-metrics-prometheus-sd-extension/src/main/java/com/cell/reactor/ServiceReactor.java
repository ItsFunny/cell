package com.cell.reactor;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ReactorAnno;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.sd.RegistrationService;
import lombok.Data;

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
    private RegistrationService registrationService;
}
