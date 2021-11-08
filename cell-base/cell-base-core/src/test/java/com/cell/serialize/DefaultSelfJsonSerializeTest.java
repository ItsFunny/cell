package com.cell.serialize;

import com.cell.base.core.serialize.DefaultSelfJsonSerialize;
import lombok.Data;
import org.junit.Test;


public class DefaultSelfJsonSerializeTest
{
    @Data
    public static class AAASerialize extends DefaultSelfJsonSerialize
    {
        private String name;
    }

    @Test
    public void testSerialize() throws Exception
    {
        AAASerialize a = new AAASerialize();
        a.setName("charlie");
        byte[] bytes = a.toBytes();
        System.out.println(new String(bytes));

        AAASerialize b = new AAASerialize();
        b.fromBytes(bytes);
    }

}