package com.cell;

import com.cell.constants.ContextConstants;
import com.cell.factory.ReactoryFactory;
import com.cell.protocol.ContextResponseWrapper;
import com.cell.reactor.IMapDynamicHttpReactor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */

@SpringBootApplication(scanBasePackages = {"com.cell"})
public class App
{
    public static void main(String[] args) throws Exception
    {
        ReactoryFactory.builder()
                .withGroup("/demo")
                .newCommand()
                .withUri("/getUserName")
                .withBuzzHandler((ctx) ->
                {
                    IMapDynamicHttpReactor reactor = (IMapDynamicHttpReactor) ctx.getContext().getHttpReactor();
                    ctx.success("getUserName");
                    return null;
                })
                .newCommand()
                .withUri("/getFile")
                .withBuzzHandler((ctx) ->
                {
                    ctx.success("getFile");
                    return null;
                }).make().build();

        SpringApplication.run(App.class, args);
    }
}
