package com.cell.filters;

import com.cell.comparators.CompareSatisfiedFunc;
import com.cell.config.AbsReflectAbleInitOnce;
import com.cell.decorators.TypeStateful;
import com.cell.enums.BeeEnums;
import com.cell.enums.ChainEnums;
import com.cell.enums.FilterEnums;
import com.cell.enums.GroupEnums;

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
public class DefaultStatefulFilterManager extends AbsReflectAbleInitOnce implements ITypeFilterManager<ChainEnums, BeeEnums, GroupEnums>
{
    // 默认是以list的形式,如果过于缓慢的话,更改为map也是可以的
    private final List<ITypeStatefulFilter<ChainEnums>> stateFulFilters = new ArrayList<>();
    private static final DefaultStatefulFilterManager INSTANCE = new DefaultStatefulFilterManager();

    public static DefaultStatefulFilterManager getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void filter(TypeStateful<ChainEnums> t, CompareSatisfiedFunc<ChainEnums> compareSatisfiedFunc, FilterLogicHandlerTrue filterLogicHandler)
    {
        for (ITypeStatefulFilter<ChainEnums> filter : stateFulFilters)
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
    public void registerFilter(ITypeStatefulFilter<ChainEnums>... filter)
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
        this.registerFilter((ITypeStatefulFilter<ChainEnums>) o);
    }

    @Override
    protected Class getConsumerClazz()
    {
        return ITypeStatefulFilter.class;
    }

    @Override
    protected Class getConsumerSpecialGenesisClazzIfExist()
    {
        return ChainEnums.class;
    }

    public static void main(String[] args) throws Exception
    {
        DefaultStatefulFilterManager.getInstance().init();
    }

    @Override
    public GroupEnums getGroup()
    {
        return null;
    }
}
