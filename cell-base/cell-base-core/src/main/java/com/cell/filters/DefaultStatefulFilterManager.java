package com.cell.filters;

import com.cell.annotations.CellFilter;
import com.cell.config.AbstractInitOnce;
import com.cell.config.IInitOnce;
import com.cell.decorators.TypeStateful;
import com.cell.enums.FilterEnums;
import com.cell.enums.GroupEnums;
import com.cell.exceptions.ConfigException;
import com.cell.services.IDataDecorator;
import com.cell.utils.ReflectionUtils;

import java.io.IOException;
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
public class DefaultStatefulFilterManager extends AbstractInitOnce implements ITypeFilterManager<FilterEnums, GroupEnums>
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

    @Override
    public GroupEnums getType()
    {
        return GroupEnums.GLOBAL;
    }


    static class AAA implements ITypeStatefulFilter<FilterEnums>, IDataDecorator<String>
    {

        @Override
        public FilterEnums filter(TypeStateful<FilterEnums> filterEnumsTypeStateful)
        {
            return null;
        }

        @Override
        public FilterEnums getType()
        {
            return null;
        }

        @Override
        public byte[] decorate(String data) throws IOException
        {
            return new byte[0];
        }
    }

    @Override
    protected void init() throws ConfigException
    {
        List<Class> allGenesisClassByInterface = ReflectionUtils.getAllGenesisClassByInterface(ITypeStatefulFilter.class, FilterEnums.class, (c) ->
        {
            CellFilter annotation = c.getAnnotation(CellFilter.class);
            if (annotation == null)
            {
                return true;
            }
            if (!annotation.active())
            {
                return false;
            }
            if (this.getType().getId() - annotation.filterGroup() == 0)
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
                    iInitOnce.initOnce();
                }
                this.registerFilter((ITypeStatefulFilter<FilterEnums>) o);
            } catch (Exception e)
            {
                throw new ConfigException("失败:" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        DefaultStatefulFilterManager.getInstance().init();
    }

}
