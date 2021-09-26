package com.cell;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotation.HttpCmdAnno;
import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ReactorAnno;
import com.cell.application.CellApplication;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.context.IHttpCommandContext;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumStatOperateMask;
import com.cell.enums.EnumStatisticType;
import com.cell.executor.MetricsManager;
import com.cell.prometheus.HistogramStator;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import io.prometheus.client.CollectorRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * Hello world!
 */
@CellSpringHttpApplication
public class App
{
    @Bean
    public HistogramStator exceedDelayThresoldCount(CollectorRegistry registry)
    {
        return HistogramStator.
                build("ExceedDelayThresoldCount", "ExceedDelayThresoldCount")
                .labelNames(new String[]{"a", "b"})
                .average(EnumStatisticType.SECOND, 1)
                .operate(EnumStatOperateMask.MAX_VALUE, "MaxResponseDelay")
                .operate(EnumStatOperateMask.MIN_VALUE, "MinResponseDelay")
                .operate(EnumStatOperateMask.STANDARD_DEVIATION, "ResponseDelayStandardDivision")
                .operate(EnumStatOperateMask.AVERAGE, "AverageResponseDelay")
                .buckets(new double[]{1000, 2000})
                .register(registry);
    }

    @ReactorAnno
    public static class AReactor extends AbstractHttpDymanicCommandReactor
    {
        @AutoPlugin
        private HistogramStator exceedDelayThresoldCount;
    }

    @HttpCmdAnno(uri = "/prometheusDemo", httpCommandId = 1, requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class ACommand extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo)
        {
            AReactor reactor = (AReactor) ctx.getHttpReactor();
            reactor.exceedDelayThresoldCount.labels(new String[]{"1", "2"}).observe(1.0);
            ctx.response(this.createResponseWp().ret("123").build());
        }
    }

    public static void main(String[] args)
    {
        ApplicationContext start = CellApplication.builder(App.class)
                .newReactor()
                .post("/post", new CellApplication.DEFAULT_DEMO_POST()).make()
                .get("/get", new CellApplication.DEFAULT_DEMO_GET()).make()
                .done()
                .withPort(8081)
                .build()
                .start(args);
        MetricsManager instance = MetricsManager.getInstance();
    }
}
