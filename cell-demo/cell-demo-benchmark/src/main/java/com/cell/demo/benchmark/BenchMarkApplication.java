package com.cell.demo.benchmark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BenchMarkApplication
{
    public static void main(String[] args)
    {
        try
        {
            SpringApplication.run(BenchMarkApplication.class, args);
        } catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
