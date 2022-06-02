package com.cell.utils;

import com.cell.base.common.utils.ExportUtil;
import com.cell.base.common.utils.FileUtils;
import com.cell.base.common.utils.StringUtils;
import org.junit.Test;

import java.io.File;

public class ExportUtilTest
{
    @Test
    public void testFloat()throws Exception{
        Integer a=1;
        Integer v=10;
        System.out.println(1/10.0);
    }
    @Test
    public void testReadExcel() throws Exception
    {
        byte[] data = FileUtils.readFileToByteArray(new File("/Users/lvcong/Desktop/b.txt"));
        System.out.println(new String(data));
        String dataStr = new String(data);
        String[] datas = dataStr.split("\n");
        for (String s : datas)
        {
            String[] data2 = s.split("\t");
            System.out.println(data2);
        }
        System.out.println(datas);
    }

}
