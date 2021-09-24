package com.cell.service.impl;

import lombok.Data;
import org.junit.Test;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PropertyPlaceholderHelper;

import java.util.Map;
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

    @Test
    public void test123()
    {
        AntPathMatcher matcher = new AntPathMatcher();
        String patten = "{uuid}";
        String path = "xxx";
        Map<String, String> stringStringMap = matcher.extractUriTemplateVariables(patten, path);
        System.out.println(stringStringMap);
    }

    @Data
    public static class A
    {
        private String name;
        private Integer age;
        private int sex;
    }

    @Test
    public void testRangeQuery()
    {
        BeanWrapper beanWrapper = new BeanWrapperImpl(A.class);
        beanWrapper.setPropertyValue("name", "asd");
        beanWrapper.setPropertyValue("age", "ad");
        beanWrapper.setPropertyValue("sex", 0);
        Object wrappedInstance = beanWrapper.getWrappedInstance();
        System.out.println(wrappedInstance);
    }

}