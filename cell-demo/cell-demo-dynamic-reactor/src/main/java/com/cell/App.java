package com.cell;

import com.cell.constants.ContextConstants;
import com.cell.factory.ReactoryFactory;
import com.cell.protocol.ContextResponseWrapper;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */

@SpringBootApplication(scanBasePackages = {"com.cell"})
public class App
{
    public static void main(String[] args)
    {
        ReactoryFactory.builder()
                .withGroup("/demo")
                .newCommand()
                .withUri("/getUserName")
                .withBuzzHandler((ctx, bo) ->
                {
                    ctx.response(ContextResponseWrapper.builder()
                            .ret("getUserName")
                            .status(ContextConstants.SUCCESS)
                            .msg("success")
                            .build());
                    return null;
                });
    }
}
