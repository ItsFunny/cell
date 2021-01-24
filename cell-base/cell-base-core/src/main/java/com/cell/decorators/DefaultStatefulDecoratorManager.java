package com.cell.decorators;

import com.cell.comparators.CompareSatisfiedFunc;
import com.cell.config.AbsReflectAbleInitOnce;
import com.cell.enums.ChainEnums;
import com.cell.enums.GroupEnums;
import com.cell.enums.TypeEnums;

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
public class DefaultStatefulDecoratorManager extends AbsReflectAbleInitOnce implements ITypeDecoratorManager<ChainEnums, TypeEnums, GroupEnums>
{
    private Map<ChainEnums, List<ITypeStatefulDecorator<ChainEnums>>> stateFulDecorators = new HashMap<>();

    @Override
    public TypeStateful<ChainEnums> decorate(TypeStateful<ChainEnums> t, CompareSatisfiedFunc<ChainEnums> compareFunc)
    {
        List<ITypeStatefulDecorator<ChainEnums>> decorators = stateFulDecorators.get(t.getType());
        for (ITypeStatefulDecorator<ChainEnums> decorator : decorators)
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
    public void registerDecorator(ITypeStatefulDecorator<ChainEnums>... filter)
    {
        synchronized (this.stateFulDecorators)
        {
            for (ITypeStatefulDecorator<ChainEnums> decorator : filter)
            {
                ChainEnums type = decorator.getType();
                List<ITypeStatefulDecorator<ChainEnums>> decorators = this.stateFulDecorators.get(type);
                if (decorators == null || decorators.size() == 0)
                {
                    decorators = new ArrayList<>();
                    this.stateFulDecorators.put(type, decorators);
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
        this.registerDecorator((ITypeStatefulDecorator<ChainEnums>) o);
    }

    @Override
    protected Class getConsumerClazz()
    {
        return ITypeStatefulDecorator.class;
    }

    @Override
    protected Class getConsumerSpecialGenesisClazzIfExist()
    {
        return ChainEnums.class;
    }
}
