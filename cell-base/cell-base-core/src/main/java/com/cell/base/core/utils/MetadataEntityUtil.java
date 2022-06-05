package com.cell.base.core.utils;

import com.alibaba.fastjson.JSON;
import com.cell.base.common.utils.StringUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MetadataEntityUtil
{
    public static <K, V> Map<K, V> listToMap(Collection<V> list, Function<V, K> funtion)
    {
        if (StringUtils.isEmpty(list))
        {
            return Collections.emptyMap();
        }
        Map<K, V> map = new HashMap<>(list.size());
        for (V t : list)
        {
            map.put(funtion.apply(t), t);
        }
        return map;
    }
}
