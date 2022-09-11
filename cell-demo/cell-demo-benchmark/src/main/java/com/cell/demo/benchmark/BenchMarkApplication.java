package com.cell.demo.benchmark;

import com.cell.sdk.log.LOG;
import com.cell.sdk.log.LogLevel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class BenchMarkApplication
{
    public static void main(String[] args)
    {
        try
        {
            new Thread(()->{
                try
                {
                    TimeUnit.SECONDS.sleep(10);
                    LOG.setLogLevel(LogLevel.ERROR);
                } catch (InterruptedException e)
                {
                }
            }).start();
            SpringApplication.run(BenchMarkApplication.class, args);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
