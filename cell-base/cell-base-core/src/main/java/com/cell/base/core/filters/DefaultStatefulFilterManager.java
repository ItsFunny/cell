package com.cell.base.core.filters;

import com.cell.base.common.comparators.CompareSatisfiedFunc;
import com.cell.base.common.context.AbsReflectAbleInitOnce;
import com.cell.base.common.decorators.TypeStateful;
import com.cell.base.common.enums.BeeEnums;
import com.cell.base.common.enums.GroupEnums;
import com.cell.base.common.enums.TypeEnums;
import com.cell.base.core.enums.FilterEnums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail filter: true 执行下一步,否则
 * @Attention:
 * @Date 创建时间：2021-01-07 17:10
 */
public class DefaultStatefulFilterManager extends AbsReflectAbleInitOnce implements ITypeFilterManager<TypeEnums, BeeEnums, GroupEnums>
{
    // 默认是以list的形式,如果过于缓慢的话,更改为map也是可以的
    private final List<ITypeStatefulFilter<TypeEnums>> stateFulFilters = new ArrayList<>();
    private static final DefaultStatefulFilterManager INSTANCE = new DefaultStatefulFilterManager();

    public static DefaultStatefulFilterManager getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void filter(TypeStateful<TypeEnums> t, CompareSatisfiedFunc<TypeEnums> compareSatisfiedFunc, FilterLogicHandlerTrue filterLogicHandler)
    {
        for (ITypeStatefulFilter<TypeEnums> filter : stateFulFilters)
        {
            if (compareSatisfiedFunc.satisfied(filter.getType()))
            {
                FilterEnums filterEnums = filter.filter(t);
                if (filterEnums.equals(FilterEnums.UN_SATISFIED))
                {
                    continue;
                }
                if (filterLogicHandler.handlerTrue().equals(FilterEnums.BREAK))
                {
                    break;
                }
            }
        }
    }

    @Override
    public void registerFilter(ITypeStatefulFilter<TypeEnums>... filter)
    {
        synchronized (this)
        {
            this.stateFulFilters.addAll(Arrays.asList(filter));
        }
    }

    @Override
    public BeeEnums getType()
    {
        return null;
    }


    @Override
    protected void register(Object o)
    {
        this.registerFilter((ITypeStatefulFilter<TypeEnums>) o);
    }

    @Override
    protected Class getConsumerClazz()
    {
        return ITypeStatefulFilter.class;
    }

    @Override
    protected Class getConsumerSpecialGenesisClazzIfExist()
    {
        return TypeEnums.class;
    }

    public static void main(String[] args) throws Exception
    {
        DefaultStatefulFilterManager.getInstance().initOnce(null);
    }

    @Override
    public GroupEnums getGroup()
    {
        return GroupEnums.AS_PART;
    }
}
