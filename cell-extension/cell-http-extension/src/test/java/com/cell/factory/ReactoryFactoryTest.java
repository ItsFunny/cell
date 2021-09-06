package com.cell.factory;

import com.cell.annotations.Command;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.application.CellApplication;
import com.cell.command.IHttpCommand;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.utils.ClassUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.core.annotation.AnnotationAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ReactoryFactoryTest
{
    @Test
    public void testCreateCmd() throws Exception
    {
//        final String uri = "/ads";
//        IHttpCommand execute = ReactoryFactory.HttpCommandBuilderBuilder()
//                .withUri(uri)
//                .withJsonHandler((ctx, bo) ->
//                {
//                    System.out.println("execute");
//                    return null;
//                }).build();
//        AnnotationAttributes mergedAnnotationAttributes = AnnotatedElementUtils.getMergedAnnotationAttributes(execute.getClass(), Command.class);
//        Assert.assertTrue(mergedAnnotationAttributes != null);
//        AnnotationAttributes attributes = AnnotatedElementUtils.getMergedAnnotationAttributes(execute.getClass(), HttpCmdAnno.class);
//        Assert.assertTrue(attributes != null);
//        Assert.assertEquals(attributes.get("uri"), uri);
//        System.out.println(execute);
//        System.out.println(execute.getClass().getName());
    }

    static class C
    {

    }

    @Test
    public void testCreateReactor() throws Exception
    {
        {
            final String group = "/group";
            List<String> uris = Arrays.asList("/uri1", "/uri2");
            final AtomicInteger count = new AtomicInteger();

            IDynamicHttpReactor reactor = CellApplication.builder(ReactoryFactoryTest.class).newReactor()
                    .withGroup(group)
                    .withBean(C.class)
                    .newCommand()
                    .withUri(uris.get(0))
                    .withBuzzHandler((ctx) ->
                    {
                        count.incrementAndGet();
                        System.out.println("uri1 execute");
                        return null;
                    })
                    .newCommand()
                    .withUri(uris.get(1))
                    .withBuzzHandler((ctx) ->
                    {
                        count.incrementAndGet();
                        System.out.println("uri2 execute");
                        return null;
                    }).make().build();
            Assert.assertNotNull(reactor);
            System.out.println(reactor);
            System.out.println(reactor.getClass());

            AnnotationAttributes reactorAnno = ClassUtil.getMergedAnnotationAttributes(reactor.getClass(), ReactorAnno.class);
            Assert.assertNotNull(reactorAnno);
            Assert.assertTrue(!reactorAnno.isEmpty());
            Assert.assertEquals(group, reactorAnno.get("group"));

            List<Class<? extends IHttpCommand>> httpCommandList = reactor.getHttpCommandList();
            Assert.assertNotNull(httpCommandList);
            for (int i = 0; i < httpCommandList.size(); i++)
            {
                Class<? extends IHttpCommand> aClass = httpCommandList.get(i);
                System.out.println(aClass.getName());
                AnnotationAttributes anno = ClassUtil.getMergedAnnotationAttributes(aClass, Command.class);
                Assert.assertNotNull(anno);
                anno = ClassUtil.getMergedAnnotationAttributes(aClass, HttpCmdAnno.class);
                Assert.assertNotNull(anno);
                Assert.assertTrue(!anno.isEmpty());

                String uri = (String) anno.get("uri");
                System.out.println(uri);
                Assert.assertNotNull(uri);
                Assert.assertEquals(uri, uris.get(i));
            }
        }

    }


}