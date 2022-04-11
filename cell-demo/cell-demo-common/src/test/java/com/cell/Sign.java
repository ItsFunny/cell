package com.cell;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

public class Sign
{

    public static void main(String[] args) throws UnsupportedEncodingException {
        // 请替换⾃⼰的 accessKey, accessSecert
        // signatureNonce 为随机字符串, 每次请求都应重新随机⼀个字符串
        String accessKey = "gCgBfncbyOHRzpCXKKHxYOZB";
        String accessSecert = "morTwRTMrssnVXvySKAIeUlU";
        String signatureNonce = "12345678iuytrrewwqqw";
        long timestamp = System.currentTimeMillis();
        System.out.println("timestamp----------------"+timestamp);
        String params = "accessKey=" + accessKey + "&signatureNonce=" + signatureNonce + "&timestamp=" + timestamp;
        MessageDigest md = DigestUtils.getSha256Digest();
        md.update(accessSecert.getBytes("UTF-8"));
        byte[] result = md.digest(params.getBytes("UTF-8"));
        String signautre = Hex.encodeHexString(result);
        System.out.println("signautre----------------"+signautre);
    }
}
