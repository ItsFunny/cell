package com.cell.filters;

import com.cell.decorators.TypeStateful;
import com.cell.enums.FilterEnums;

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
public class DefaultStatefulFilterManager implements ITypeFilterManager<FilterEnums>
{
    // 默认是以list的形式,如果过于缓慢的话,更改为map也是可以的
    private final List<ITypeStatefulFilter<FilterEnums>> stateFulFilters = new ArrayList<>();
    private static final DefaultStatefulFilterManager INSTANCE = new DefaultStatefulFilterManager();
    public static DefaultStatefulFilterManager getInstance()
    {
        return INSTANCE;
    }

    @Override
    public void filter(TypeStateful<FilterEnums> t, FilterLogicHandlerTrue filterLogicHandler)
    {
        for (ITypeStatefulFilter<FilterEnums> filter : stateFulFilters)
        {
            if ((filter.getType().getStatus() & t.getType().getStatus()) > 0)
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
    public void registerFilter(ITypeStatefulFilter<FilterEnums>... filter)
    {
        synchronized (this)
        {
            this.stateFulFilters.addAll(Arrays.asList(filter));
        }
    }
}
