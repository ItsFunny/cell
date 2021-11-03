package com.cell.discovery.nacos.config;

import com.cell.annotations.CellAutoAble;
import com.cell.context.InitCTX;
import com.cell.enums.GroupEnums;
import com.cell.exceptions.ConfigException;
import com.cell.services.GroupFul;
import com.cell.utils.ReflectionUtils;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-23 23:40
 */
public abstract class AbsReflectAbleInitOnce extends AbstractInitOnce implements GroupFul<GroupEnums>
{
    // 如果没有注解,也会自动被注入到filters中
    protected boolean allowAnnotationMissable;

    protected abstract void register(Object o);

    protected abstract Class getConsumerClazz();

    protected abstract Class getConsumerSpecialGenesisClazzIfExist();

    @Override
    protected void onInit(InitCTX ctx)
    {
        List<Class> allGenesisClassByInterface = ReflectionUtils.getAllGenesisClassByInterface(getConsumerClazz(), this.getConsumerSpecialGenesisClazzIfExist(), (c) ->
        {
            CellAutoAble annotation = c.getAnnotation(CellAutoAble.class);
            if (annotation == null)
            {
                return this.allowAnnotationMissable;
            }
            if (!annotation.active())
            {
                return false;
            }
            if (this.getGroup().getId() - annotation.group() == 0)
            {
                return true;
            }
            return false;
        });
        for (Class aClass : allGenesisClassByInterface)
        {
            try
            {
                Object o = aClass.newInstance();
                if (o instanceof IInitOnce)
                {
                    IInitOnce iInitOnce = (IInitOnce) o;
                    iInitOnce.initOnce(ctx);
                }
//                this.registerFilter((ITypeStatefulFilter<TypeEnums>) o);
                this.register(o);
            } catch (Exception e)
            {
                throw new ConfigException("失败:" + e.getMessage());
            }
        }
    }
}
