package com.cell.plugin.develop.context;

import java.util.function.Consumer;

/**
 * @author Charlie
 * @When
 * @Description 只用于普通的实体类
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 12:01
 */
public interface ITO<T>
{
    default T to(Consumer<T> consumer)
    {
        T ret = this.onTo();
        if (consumer != null)
        {
            consumer.accept(ret);
        }
        return ret;
    }

    T onTo();
}
