/**
 * @author joker
 * @date 创建时间：2018年2月3日 下午3:51:08
 */
package com.cell.base.common.utils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author joker
 * @date 创建时间：2018年2月3日 下午3:51:08
 */
public class CollectionUtils
{
    public static <T> List<T> collection2List(Collection<T> collection, Class<T> type)
    {
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(type, collection.size());
        T[] array = collection.toArray(arr);
        return Arrays.asList(array);
    }

    /**
     * 根据map中的value,返回对应的key
     *
     * @param map
     * @param v
     * @return
     * @author joker
     * @date 创建时间：2018年3月14日 下午7:20:25
     */
    public static <K, V> K getKeyByValueFromMap(Map<K, V> map, V v)
    {
        Set<Entry<K, V>> entrySet = map.entrySet();
        for (Entry<K, V> entry : entrySet)
        {
            V value = entry.getValue();
            if (value.equals(v))
            {
                return entry.getKey();
            }
        }
        return null;
    }

    public static boolean isEmpty(Collection collection)
    {
        return collection == null || collection.isEmpty();
    }

    public static boolean isNotEmpty(Collection collection)
    {
        return collection != null && !collection.isEmpty();
    }


    public static <T> String join(Iterable<T> collection, String conjunction) {
        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;
        for (T item : collection) {
            if (isFirst) {
                isFirst = false;
            } else {
                sb.append(conjunction);
            }
            sb.append(item);
        }
        return sb.toString();
    }
}
