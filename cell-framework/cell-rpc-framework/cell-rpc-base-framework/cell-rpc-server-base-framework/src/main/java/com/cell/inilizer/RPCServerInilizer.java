package com.cell.inilizer;

import com.cell.annotation.RPCServerCmdAnno;
import com.cell.annotation.RPCServerReactorAnno;
import com.cell.annotation.RPCService;
import com.cell.initializer.CellSpringInitializer;

import java.lang.annotation.Annotation;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention: // 如果后续需要兼容其他框架,需要将这个类单独的抽离到rpc-spring-framekwork module中
 * @Date 创建时间：2021-10-09 06:06
 */
public class RPCServerInilizer implements CellSpringInitializer
{

//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//        Class<?> mainApplicationClass = ClassUtil.getMainApplicationClass();
//        CellSpringHttpApplication mergedAnnotation = ClassUtil.getMergedAnnotation(mainApplicationClass, CellSpringHttpApplication.class);
//        Class<? extends Annotation>[] classes = mergedAnnotation.scanInterestAnnotations();
//        Set<Class<? extends Annotation>> ans = new HashSet<>(Arrays.asList(classes));
//        ans.addAll(Arrays.asList(RPCService.class, RPCServerCmdAnno.class, RPCServerReactorAnno.class));
//        Class<? extends Annotation>[] classes1 = (Class<? extends Annotation>[]) Array.newInstance(Annotation.class, ans.size());
//        ReflectUtil.modify(mainApplicationClass, CellSpringHttpApplication.class, "scanInterestAnnotations", classes1);
//    }
//
//    @Override
//    public void initialize(ConfigurableApplicationContext applicationContext)
//    {
//        this.initOnce(null);
//    }

    @Override
    public Class<? extends Annotation>[] getInterestAnnotation()
    {
        return new Class[]{RPCService.class, RPCServerCmdAnno.class, RPCServerReactorAnno.class};
    }
}
