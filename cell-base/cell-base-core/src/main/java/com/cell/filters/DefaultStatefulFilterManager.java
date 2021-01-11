package com.cell.filters;

import com.cell.config.AbstractInitOnce;
import com.cell.decorators.TypeStateful;
import com.cell.enums.FilterEnums;
import com.cell.exceptions.ConfigException;
import com.cell.services.IDataDecorator;
import org.reflections.Reflections;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.swing.tree.AbstractLayoutCache;
import java.io.IOException;
import java.lang.reflect.Type;
import java.security.AlgorithmConstraints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail filter: true 执行下一步,否则
 * @Attention:
 * @Date 创建时间：2021-01-07 17:10
 */
public class DefaultStatefulFilterManager extends AbstractInitOnce implements ITypeFilterManager<FilterEnums>
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
        Reflections reflections = new Reflections(this.getClass().getName());
        Set<Class<? extends ITypeStatefulFilter>> subTypesOf = reflections.getSubTypesOf(ITypeStatefulFilter.class);
        for (Class<? extends ITypeStatefulFilter> aClass : subTypesOf)
        {
            Type[] genericInterfaces = aClass.getGenericInterfaces();
            String filterEnumsName = FilterEnums.class.getName();
            for (Type genericInterface : genericInterfaces)
            {
                ParameterizedTypeImpl parameterizedType= (ParameterizedTypeImpl) genericInterface;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments)
                {
                    Class c= (Class) actualTypeArgument;
                    boolean assignableFrom = c.isAssignableFrom(FilterEnums.class);
                    String name = c.getName();
                    String typeName = genericInterface.getTypeName();
                    System.out.println(typeName);
                }
            }
            ITypeStatefulFilter filter = null;
            try
            {
                filter = aClass.newInstance();
            } catch (Exception e)
            {
                throw new ConfigException("反射初始化失败:" + e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws Exception
    {
        DefaultStatefulFilterManager.getInstance().init();
    }

}
