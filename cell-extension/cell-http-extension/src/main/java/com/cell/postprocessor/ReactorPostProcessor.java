package com.cell.postprocessor;

import com.cell.adapter.IBeanPostProcessortAdapter;
import org.springframework.beans.BeansException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 07:58
 */
public class ReactorPostProcessor implements IBeanPostProcessortAdapter
{
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        return null;
    }
}
