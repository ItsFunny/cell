package com.cell.component.cache.redis.service.impl;

import com.cell.base.common.models.Module;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.common.utils.StringUtils;
import com.cell.component.cache.redis.service.ICacheRedisService;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.INodeContext;
import com.cell.sdk.log.LOG;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class RedisCacheServiceImpl implements ICacheService<String, String> , ICacheRedisService
{
    @Autowired
    public void setTemplate(StringRedisTemplate template)
    {
        this.template = template;
    }

    private StringRedisTemplate template;

    public StringRedisTemplate getTemplate(){
        return this.template;
    }


    @Override
    public void start(INodeContext context)
    {
    }

//    private List<String> keys(String pattern)
//    {
//        List<String> keys = new ArrayList<>();
//        this.scan(pattern, item ->
//        {
//            //符合条件的key
//            String key = new String(item);
//            keys.add(key);
//        });
//        return keys;
//    }
//
//    /**
//     * 组装 scan 的结果集
//     */
    public Set<String> keys(String pattern, Long limit) {
        HashSet<String> set = new HashSet<>();
        Cursor<String> cursor = scan(template, pattern, limit);
        while (cursor.hasNext()) {
            set.add(cursor.next());
        }
        try {
            cursor.close();
        } catch (Exception e) {
            LOG.error(Module.ALL,e,"关闭 redis connection 失败");
        }
        return set;
    }
//    /**
//     * 自定义 redis scan 操作
//     */
    private Cursor<String> scan(StringRedisTemplate redisTemplate, String pattern, Long limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection)
                    throws org.springframework.dao.DataAccessException {
                return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
            }
        });
    }

//    private void scan(String pattern, Consumer<byte[]> consumer)
//    {
//        this.template.execute((RedisConnection connection) ->
//        {
//            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().count(Long.MAX_VALUE).match(pattern).build()))
//            {
//                cursor.forEachRemaining(consumer);
//                return null;
//            } catch (IOException e)
//            {
//                throw new RuntimeException(e);
//            }
//        });
//    }

    @Override
    public String get(String s)
    {
        return this.template.opsForValue().get(s);
    }

    @Override
    public void set(String s, String s2, int delaySeconds)
    {
        this.template.opsForValue().set(s, s2, delaySeconds);
    }

    @Override
    public void set(String s, String s2)
    {
        this.template.opsForValue().set(s, s2);
    }

    @Override
    public Boolean setIfAbsent(String s, String s2)
    {
        return this.template.opsForValue().setIfAbsent(s,s2);
    }

    @Override
    public Boolean setIfAbsent(String s, String s2, int delaySeconds)
    {
        return this.template.opsForValue().setIfAbsent(s,s2, Duration.ofSeconds(delaySeconds));
    }

    @Override
    public String delete(String s)
    {
        String str = this.template.opsForValue().get(s);
        if (StringUtils.isNotEmpty(str))
        {
            this.template.delete(s);
        }
        return str;
    }

    @Override
    public Map<String, String> deleteBatchWithReturn(List<String> strings)
    {
        Map<String,String>ret=new HashMap<>();
        for (String string : strings)
        {
            String delete = this.delete(string);
            if (StringUtils.isEmpty(delete)){
                continue;
            }
            ret.put(string,delete);
        }
        return ret;
    }

    @Override
    public void deleteBatch(Collection<String> strings)
    {
        this.template.delete(strings);
    }

    @Override
    public boolean contains(String s)
    {
        return this.template.hasKey(s);
    }

    @Override
    public Set<String> keysByPattern(String pattern, Integer limit)
    {
        return this.keys(pattern,limit.longValue());
    }

    @Override
    public void deleteByPattern(String pattern, Integer limit)
    {
        Set<String> strings = this.keysByPattern(pattern, limit);
        if (CollectionUtils.isEmpty(strings)){
            return;
        }
        this.deleteBatch(strings);
    }
}
