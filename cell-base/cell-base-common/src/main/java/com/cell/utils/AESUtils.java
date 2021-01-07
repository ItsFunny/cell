package com.cell.utils;


import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-09-14 16:46
 */
public class AESUtils
{

    private static Logger log = Logger.getLogger(AESUtils.class);
    // 定义字符集
    private static final String ENCODING = "UTF-8";

    /**
     * 根据提供的密钥生成AES专用密钥
     *
     * @param password 可以是中文、英文、16进制字符串
     * @return AES密钥
     * @throws Exception
     * @explain
     */
    public static byte[] generateKey(String password) throws Exception
    {
        byte[] keyByteArray = null;
        // 创建AES的Key生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // 利用用户密码作为随机数初始化
        // 指定强随机数的生成方式
        // 兼容linux
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password.getBytes(ENCODING));
        kgen.init(128, random);// 只能是128位
        // 根据用户密码，生成一个密钥
        SecretKey secretKey = kgen.generateKey();
        // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null。
        keyByteArray = secretKey.getEncoded();
        return keyByteArray;
    }

    public static byte[] generateKey(byte[] password) throws Exception
    {
        byte[] keyByteArray = null;
        // 创建AES的Key生产者
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        // 利用用户密码作为随机数初始化
        // 指定强随机数的生成方式
        // 兼容linux
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        random.setSeed(password);
        kgen.init(128, random);// 只能是128位
        // 根据用户密码，生成一个密钥
        SecretKey secretKey = kgen.generateKey();
        // 返回基本编码格式的密钥，如果此密钥不支持编码，则返回null。
        keyByteArray = secretKey.getEncoded();
        return keyByteArray;
    }

    /**
     * AES加密字符串
     *
     * @param content  需要被加密的字符串
     * @param password 加密需要的密码
     * @return 16进制的密文（密文的长度随着待加密字符串的长度变化而变化，至少32位）
     */
    public static byte[] encrypt(String content, String password)
    {
        byte[] cipherTextBytes = null;
        try
        {
            // 转换为AES专用密钥
            byte[] keyBytes = generateKey(password);


            SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
            // 将待加密字符串转二进制
            byte[] clearTextBytes = content.getBytes(ENCODING);
            // 创建密码器，默认参数：AES/EBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES");
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            // 加密结果
            cipherTextBytes = cipher.doFinal(clearTextBytes);
        } catch (Exception e)
        {
            e.printStackTrace();
            log.error("AES加密失败：" + e.getMessage());
        }
        return cipherTextBytes;
    }


    // ecb mode 不能使用iv
    public static byte[] ecbEncryptByKey(String content, byte[] key)
    {
        byte[] cipherTextBytes = null;
        try
        {
            // 转换为AES专用密钥
            byte[] keyBytes = key;


            SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
            // 将待加密字符串转二进制
            byte[] clearTextBytes = content.getBytes(ENCODING);
            // 创建密码器，默认参数：AES/EBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//            IvParameterSpec iv = new IvParameterSpec(ivByte);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, sks);
            // 加密结果
            cipherTextBytes = cipher.doFinal(clearTextBytes);
        } catch (Exception e)
        {
            e.printStackTrace();
            log.error("AES加密失败：" + e.getMessage());
        }
        return cipherTextBytes;
    }


    public static byte[] encryptByCFB(byte[] content, String password, byte[] ivByte)
    {
        byte[] cipherTextBytes = null;
        try
        {
            // 转换为AES专用密钥
            byte[] keyBytes = generateKey(password);
            SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
            // 将待加密字符串转二进制
            byte[] clearTextBytes = content;
            // 创建密码器，默认参数：AES/EBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CFB/NOPADDING");
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, sks, iv);
            // 加密结果
            cipherTextBytes = cipher.doFinal(clearTextBytes);
        } catch (Exception e)
        {
            e.printStackTrace();
            log.error("AES加密失败：" + e.getMessage());
        }
        return cipherTextBytes;
    }

    public static byte[] encryptByCFBWithBytes(byte[] content, byte[] key, byte[] ivByte)
    {
        byte[] cipherTextBytes = null;
        try
        {
            // 转换为AES专用密钥
            byte[] keyBytes = key;
            SecretKeySpec sks = new SecretKeySpec(keyBytes, "AES");
            // 将待加密字符串转二进制
            byte[] clearTextBytes = content;
            // 创建密码器，默认参数：AES/EBC/PKCS5Padding
            Cipher cipher = Cipher.getInstance("AES/CFB/NOPADDING");
            IvParameterSpec iv = new IvParameterSpec(ivByte);
            // 初始化为加密模式的密码器
            cipher.init(Cipher.ENCRYPT_MODE, sks, iv);
            // 加密结果
            cipherTextBytes = cipher.doFinal(clearTextBytes);
        } catch (Exception e)
        {
            e.printStackTrace();
            log.error("AES加密失败：" + e.getMessage());
        }
        return cipherTextBytes;
    }

    public static void main(String[] args) throws Exception
    {

        String text = "Marydon";
        String password = "521";
        byte[] bytes = generateKey(password);
        String s1 = Base64.encodeBase64String(bytes);
        System.out.println(s1);
//        String s = HexUtil.bytes2HexString(bytes);
//        System.out.println(s);
        String s = Base64.encodeBase64String(encrypt(text, password));
        System.out.println(s);
    }
}
