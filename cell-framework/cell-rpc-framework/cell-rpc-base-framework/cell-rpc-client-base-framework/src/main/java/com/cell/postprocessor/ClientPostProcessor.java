package com.cell.postprocessor;

import com.cell.adapter.IBeanPostProcessortAdapter;
import org.springframework.beans.BeansException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-27 10:09
 */
public class ClientPostProcessor implements IBeanPostProcessortAdapter
{

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        return null;
    }
}
