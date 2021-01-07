package com.cell.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * 对称加密之AES加密算法，调用java的API
 *
 */
public class AesCBCUtil
{

	/**
	 * 加密
	 * 
	 * @param content
	 * @param strKey
	 * @return
	 * @throws Exception
	 */
	public static byte[] encrypt(String content, String strKey) throws Exception
    {
		SecretKeySpec skeySpec = getKey(strKey);// 密钥
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// 向量空间
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(content.getBytes());// 加密
		return encrypted;
	}

	public static byte[] encrypt(byte[] content, String strKey) throws Exception
    {
		SecretKeySpec skeySpec = getKey(strKey);// 密钥
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());// 向量空间
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
		byte[] encrypted = cipher.doFinal(content);// 加密
		return encrypted;
	}

	/**
	 * 解密
	 * 
	 * @param strKey
	 * @param content
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(byte[] content, String strKey) throws Exception
    {
		SecretKeySpec skeySpec = getKey(strKey);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		byte[] original = cipher.doFinal(content);
		String originalString = new String(original);
		return originalString;
	}

	public static byte[] decryptByteContent(byte[] content, String strKey) throws Exception
    {
		SecretKeySpec skeySpec = getKey(strKey);
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		IvParameterSpec iv = new IvParameterSpec("0102030405060708".getBytes());
		cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
		return cipher.doFinal(content);
	}

	private static SecretKeySpec getKey(String strKey) throws Exception
    {
		byte[] arrBTmp = strKey.getBytes();
		byte[] arrB = new byte[16]; // 创建一个空的16位字节数组（默认值为0）

		for (int i = 0; i < arrBTmp.length && i < arrB.length; i++) {
			arrB[i] = arrBTmp[i];
		}

		SecretKeySpec skeySpec = new SecretKeySpec(arrB, "AES");

		return skeySpec;
	}

	/**
	 * base 64 encode
	 * 
	 * @param bytes 待编码的byte[]
	 * @return 编码后的base 64 code
	 */
	public static String base64Encode(byte[] bytes) {
		return Base64Utils.encode(bytes);
	}

	/**
	 * base 64 decode
	 * 
	 * @param base64Code 待解码的base 64 code
	 * @return 解码后的byte[]
	 * @throws Exception
	 */
	public static byte[] base64Decode(String base64Code) throws Exception
    {
		return Base64Utils.decode(base64Code);
	}

	/**
	 * 将加密后的数据进行base 64编码
	 * 
	 * @param content    待加密的内容
	 * @param encryptKey 加密密钥
	 * @return 加密后的base 64 code
	 * @throws Exception
	 */
	public static String aesEncrypt(String content, String encryptKey) throws Exception
    {
		return base64Encode(encrypt(content, encryptKey));
	}

	/**
	 * 将密文base 64解码后再解密
	 * 
	 * @param encryptStr 密文
	 * @param decryptKey 解密密钥
	 * @return 解密后的string
	 * @throws Exception
	 */
	public static String aesDecrypt(String encryptStr, String decryptKey) throws Exception
    {
		return encryptStr.isEmpty() ? null : decrypt(base64Decode(encryptStr), decryptKey);
	}

	public static void main(String[] args) throws Exception
    {
		String input = "搞事情";
		String key = UUIDUtil.uuid2() + UUIDUtil.uuid2() + UUIDUtil.uuid2() + UUIDUtil.uuid2();

		// AES
		String encrypt = aesEncrypt(input, key);
	
		System.out.println(aesDecrypt(encrypt, key));
	}

}
