package com.cell.inilizer;

import com.cell.annotation.*;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.utils.ClassUtil;
import com.cell.utils.ReflectUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-09 06:06
 */
public class RPCInilizer extends AbstractInitOnce implements ApplicationContextInitializer<ConfigurableApplicationContext>
{

    @Override
    protected void onInit(InitCTX ctx)
    {
        Class<?> mainApplicationClass = ClassUtil.getMainApplicationClass();
        CellSpringHttpApplication mergedAnnotation = ClassUtil.getMergedAnnotation(mainApplicationClass, CellSpringHttpApplication.class);
        Class<? extends Annotation>[] classes = mergedAnnotation.scanInterestAnnotations();
        Set<Class<? extends Annotation>> ans = new HashSet<>(Arrays.asList(classes));
        ans.addAll(Arrays.asList(RPCCommand.class, RPCReactor.class, RPCServer.class, RPCServerCmdAnno.class));
        Class<? extends Annotation>[] classes1 = ans.toArray(new Class<? extends Annotation>[ans.size()]);
        ReflectUtil.modify(mainApplicationClass,CellSpringHttpApplication.class,"scanInterestAnnotations",);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {

    }
}
