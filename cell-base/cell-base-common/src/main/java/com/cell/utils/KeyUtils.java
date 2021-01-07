/**
 * @author joker
 * @date 创建时间：2018年3月11日 下午5:28:19
 */
package com.cell.utils;

import com.sm2.SM2Utils;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.BigIntegers;
import org.cesecore.keys.util.KeyTools;

import java.io.IOException;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 用于生成一些独一的key
 *
 * @author joker
 * @date 创建时间：2018年3月11日 下午5:28:19
 */
public class KeyUtils
{

    public static String generateRandomSequence()
    {
        Random random = new Random(47);
        char[] arr =
                {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'H', 'i', 'j', 'k', 'n', 'm', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                        'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'h', 'I', 'J', 'K', 'N', 'M', 'O', 'P', 'Q', 'R', 'S',
                        'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '-', '_'};

        StringBuffer str = new StringBuffer();
        for (int i = 0; i < 8; ++i)
        {
            int index = random.nextInt(arr.length);
            char c = arr[index];
            str.append(c);
        }
        int number = random.nextInt(10000);
        str.append(number);
        return new String(str);
    }

    /**
     * MD5加密
     *
     * @param password
     * @return
     * @author joker
     * @date 创建时间：2018年3月5日 下午7:26:17
     */
    public static String md5Encrypt(String password)
    {
        try
        {
            // 得到一个信息摘要器
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(password.getBytes());
            StringBuffer buffer = new StringBuffer();
            // 把每一个byte 做一个与运算 0xff;
            for (byte b : result)
            {
                // 与运算
                int number = b & 0xff;// 加盐
                String str = Integer.toHexString(number);
                if (str.length() == 1)
                {
                    buffer.append("0");
                }
                buffer.append(str);
            }

            // 标准的md5加密后的结果
            return new String(buffer);
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
            return "";
        }
    }


    public static enum EnumSm2ProducerType
    {
        BIAOXIN,
        SANWEI;

        private EnumSm2ProducerType()
        {
        }
    }

    public static byte[] parseSM2PrvK(String base64Str) throws Exception
    {
        if (base64Str.contains("BEGIN"))
        {
            base64Str = replace("PRIVATE KEY", base64Str);
        }
        return formatPrvKey(Base64Utils.decode(base64Str));
    }

    public static String replace(String type, String str)
    {
        str = str
                .replace("-----BEGIN " + type + "-----", "")
                .replace("-----END " + type + "-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replaceAll("[\\s\\t\\n\\r]", "");
        return str;
    }


    public static byte[] formatPrvKey(byte[] prvKey)
    {
        if (prvKey.length == 32)
        {
            return prvKey;
        }
        if (prvKey.length == 33)
        {
            return java.util.Arrays.copyOfRange(prvKey, 1, 33);
        }
        PrivateKeyInfo bb = PrivateKeyInfo.getInstance(prvKey);
        byte[] keyDer = new byte[0];
        try
        {
            keyDer = bb.parsePrivateKey().toASN1Primitive().getEncoded("DER");
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        ECPrivateKey ecPrivateKey = ECPrivateKey.getInstance(keyDer);
        return BigIntegers.asUnsignedByteArray(32, ecPrivateKey.getKey());
    }

    public static byte[] formatPubKey(EnumSm2ProducerType producerType, byte[] pubKey)
    {
        byte[] result = pubKey;
        if (result.length > 65)
        {
            result = SubjectPublicKeyInfo.getInstance(result).getPublicKeyData().getBytes();
        }
        if (result.length == 65)
        {
            result = Arrays.copyOfRange(result, 1, 65);
        }

        switch (producerType)
        {
            case BIAOXIN:
                result = Arrays.concatenate(new byte[]{0x04}, result);
                break;
            default:
                break;
        }
        return result;
    }

    public static final byte[] USER_ID = "1234567812345678".getBytes();

    // 返回的是hex
    public static String standardSMSignWithHexReturn(byte[] prvK, byte[] sourceData)
    {
        return SM2Utils.sign(USER_ID, prvK, sourceData);
    }

    public static boolean standardSM2VerifyWithHexString(byte[] pubK, byte[] sourceData, String hexString)
    {
        return SM2Utils.verifySign_ZA(USER_ID, pubK, sourceData, hexString);
    }

    public static KeyPair generateSM2StandardKeyPair()
    {
        boolean b = false;
        KeyPair keyPair = null;
        while (true)
        {
            try
            {
                keyPair = KeyTools.genKeys("sm2p256v1", "ECDSA");
                byte[] encoded = keyPair.getPrivate().getEncoded();
                byte[] bytes = KeyUtils.formatPrvKey(encoded);
                System.out.println(Base64Utils.encode(bytes));
                byte[] con = "123".getBytes();
                String sign = SM2Utils.sign(USER_ID, bytes, con);
                System.out.println(sign);
                byte[] encoded1 = keyPair.getPublic().getEncoded();
                byte[] pubBytes = KeyUtils.formatPubKey(KeyUtils.EnumSm2ProducerType.BIAOXIN, encoded1);
                b = SM2Utils.verifySign_ZA(USER_ID, pubBytes, con, sign);
                System.out.println(b);
                if (b)
                {
                    return keyPair;
                }
            } catch (Exception e)
            {
                b = false;
            }
        }
    }
}
