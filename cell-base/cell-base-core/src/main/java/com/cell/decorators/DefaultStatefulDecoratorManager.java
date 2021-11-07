package com.cell.decorators;

import com.cell.comparators.CompareSatisfiedFunc;
import com.cell.http.gate.config.AbsReflectAbleInitOnce;
import com.cell.enums.BeeEnums;
import com.cell.enums.TypeEnums;
import com.cell.enums.GroupEnums;
import com.cell.utils.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-23 23:03
 */
public class DefaultStatefulDecoratorManager extends AbsReflectAbleInitOnce implements ITypeDecoratorManager<TypeEnums, TypeEnums, GroupEnums>
{
    private Map<BeeEnums, List<ITypeStatefulDecorator<TypeEnums>>> stateFulDecorators = new HashMap<>();
    private static final  DefaultStatefulDecoratorManager INSTANCE=new DefaultStatefulDecoratorManager();
    public static DefaultStatefulDecoratorManager getInstance()
    {
        return INSTANCE;
    }
    @Override
    public TypeStateful<TypeEnums> decorate(TypeStateful<TypeEnums> t, CompareSatisfiedFunc<TypeEnums> compareFunc)
    {
        List<ITypeStatefulDecorator<TypeEnums>> decorators = stateFulDecorators.get(t.getBee());
        if (CollectionUtils.isEmpty(decorators))return t;
        for (ITypeStatefulDecorator<TypeEnums> decorator : decorators)
        {
            if (!compareFunc.satisfied(decorator.getType()))
            {
                continue;
            }
            t = decorator.decorate(t);
        }
        return t;
    }

    @Override
    public void registerDecorator(ITypeStatefulDecorator<TypeEnums>... filter)
    {
        synchronized (this.stateFulDecorators)
        {
            for (ITypeStatefulDecorator<TypeEnums> decorator : filter)
            {
                BeeEnums bee = decorator.getBee();
//                TypeEnums type = decorator.getType();
                List<ITypeStatefulDecorator<TypeEnums>> decorators = this.stateFulDecorators.get(bee);
                if (decorators == null || decorators.size() == 0)
                {
                    decorators = new ArrayList<>();
                    this.stateFulDecorators.put(bee, decorators);
                }
                decorators.add(decorator);
            }
        }
    }

    @Override
    public GroupEnums getGroup()
    {
        return null;
    }

    @Override
    public TypeEnums getType()
    {
        return null;
    }

    @Override
    protected void register(Object o)
    {
        this.registerDecorator((ITypeStatefulDecorator<TypeEnums>) o);
    }

    @Override
    protected Class getConsumerClazz()
    {
        return ITypeStatefulDecorator.class;
    }

    @Override
    protected Class getConsumerSpecialGenesisClazzIfExist()
    {
        return TypeEnums.class;
    }
}
