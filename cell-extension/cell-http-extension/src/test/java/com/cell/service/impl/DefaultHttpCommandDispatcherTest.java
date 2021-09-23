package com.cell.service.impl;

import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Properties;

import static org.junit.Assert.*;

public class DefaultHttpCommandDispatcherTest
{

    @Test
    public void testAnt()
    {
        String patten = "/**";
        AntPathMatcher matcher = new AntPathMatcher();
        boolean match = matcher.match(patten, "/asd");
        boolean pattern = matcher.isPattern("/asd");
        System.out.println(pattern);
        System.out.println(match);
    }

    @Test
    public void testMore()
    {
        String pattern = "/ad/{name}";
        AntPathMatcher matcher = new AntPathMatcher();
        boolean match = matcher.match(pattern, "/ad/asd");
        System.out.println(match);
    }

    @Test
    public void testpRO()
    {
        PropertyPlaceholderHelper helper = new PropertyPlaceholderHelper("{", "}");
        String str = "asdd,{name},asd";
        Properties properties = new Properties();
        properties.put("name", "charlie");
        String s = helper.replacePlaceholders(str, properties);
        System.out.println(s);
    }

}