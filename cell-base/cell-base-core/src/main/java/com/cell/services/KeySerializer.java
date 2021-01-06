package com.cell.services;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-03 18:26
 */
public interface KeySerializer<K>
{
    String serializerKey(K key);

//    KeySerializer<String> DEFAULT_KEY_SERIALIZER = new KeySerializer<String>()
//    {
//        @Override
//        public String serializerKey(String key)
//        {
//            return key;
//        }
//    };
//    KeySerializer<String> PUBLICKEY_SERIALIZER = new KeySerializer<String>()
//    {
//        @Override
//        public String serializerKey(String key)
//        {
//            return MD5Utils.md5(key);
//        }
//    };
}
