package com.cell;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotations.Command;
import com.cell.utils.ClassUtil;
import com.cell.utils.ReflectUtil;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-25 15:16
 */
public class AppTest
{
    @RPCServerCmdAnno(buzzClz = List.class)
    public static class AAAA
    {

    }

    @Test
    public void testasddd()
    {
        AAAA a = new AAAA();
        Annotation[] annotations = AAAA.class.getAnnotations();
        for (Annotation annotation : annotations)
        {
            System.out.println(annotation);
        }
        Command anno = ClassUtil.getMergedAnnotation(a.getClass(), Command.class);
        System.out.println(anno.buzzClz());
        ReflectUtil.modify(a.getClass(), Command.class, "buzzClz", Integer.class);

        RPCServerCmdAnno anno2 = ClassUtil.getMergedAnnotation(a.getClass(), RPCServerCmdAnno.class);
        System.out.println(anno2.buzzClz());

        Command anno3 = ClassUtil.getMergedAnnotation(a.getClass(), Command.class);
        System.out.println(anno3.buzzClz());
    }
}