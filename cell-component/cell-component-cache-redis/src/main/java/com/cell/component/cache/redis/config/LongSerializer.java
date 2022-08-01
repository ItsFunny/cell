package com.cell.component.cache.redis.config;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

public class LongSerializer implements RedisSerializer<Long> {
	private LongSerializer() {
	}

	public static LongSerializer getInstance() {
		return LongSerializerHolder.INSTANCE;
	}

	private static class LongSerializerHolder {
		private static final LongSerializer INSTANCE = new LongSerializer();
	}

	//防止反序列化生成新的实例
	private Object readResolve() {
		return LongSerializerHolder.INSTANCE;
	}

	@Override
	public byte[] serialize(Long t) throws SerializationException {
		if (t == null) {
			return new byte[0];
		}
		return t.toString().getBytes();
	}

	@Override
	public Long deserialize(byte[] bytes) throws SerializationException {
		if (bytes != null && bytes.length > 0) {
			return Long.parseLong(new String(bytes));
		}
		return null;
	}

}
