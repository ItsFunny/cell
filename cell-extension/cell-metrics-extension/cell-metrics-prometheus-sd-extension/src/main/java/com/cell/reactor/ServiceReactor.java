package com.cell.reactor;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.reactor.commands.AllServiceCommand;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.sd.RegistrationService;
import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-22 21:27
 */
@ReactorAnno(group = "/v1/catalog/")
@Data
public class ServiceReactor extends AbstractHttpDymanicCommandReactor
{
    public static final String prometheusServiceReactor = "/v1/catalog/";

    @AutoPlugin
    private RegistrationService registrationService;

    @Override
    public List<Class<? extends IHttpCommand>> getHttpCommandList()
    {
        return Arrays.asList(AllServiceCommand.class);
    }

}
