package com.cell.inilizer;

import com.cell.annotation.CellSpringHttpApplication;
import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.annotation.RPCService;
import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.utils.ClassUtil;
import com.cell.utils.ReflectUtil;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention: // 如果后续需要兼容其他框架,需要将这个类单独的抽离到rpc-spring-framekwork module中
 * @Date 创建时间：2021-10-09 06:06
 */
public class RPCServerInilizer extends AbstractInitOnce implements ApplicationContextInitializer<ConfigurableApplicationContext>
{

    @Override
    protected void onInit(InitCTX ctx)
    {
        Class<?> mainApplicationClass = ClassUtil.getMainApplicationClass();
        CellSpringHttpApplication mergedAnnotation = ClassUtil.getMergedAnnotation(mainApplicationClass, CellSpringHttpApplication.class);
        Class<? extends Annotation>[] classes = mergedAnnotation.scanInterestAnnotations();
        Set<Class<? extends Annotation>> ans = new HashSet<>(Arrays.asList(classes));
        ans.addAll(Arrays.asList(RPCService.class, RPCServerCmdAnno.class, RPCServerReactorAnno.class));
        Class<? extends Annotation>[] classes1 = (Class<? extends Annotation>[]) Array.newInstance(Annotation.class, ans.size());
        ReflectUtil.modify(mainApplicationClass, CellSpringHttpApplication.class, "scanInterestAnnotations", classes1);
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext)
    {

    }
}
