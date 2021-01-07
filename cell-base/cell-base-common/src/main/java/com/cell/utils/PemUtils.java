package com.cell.utils;

import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.File;
import java.io.FileWriter;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-04 20:53
 */
public class PemUtils
{
    public static void pemWrite(String type, String filePath, byte[] content) throws Exception
    {
        JcaPEMWriter pemWriter = new JcaPEMWriter(new FileWriter(new File(filePath)));
        pemWriter.writeObject(new PemObject(type, content));
        pemWriter.close();
    }

    public static String replace(String type, String str)
    {
        str = str
                .replace("-----BEGIN " + type + "-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END " + type + "-----", "");
        return str;
    }

    public static byte[] replace(String type, byte[] data)
    {
        String str = new String(data);
//        str = str
//                .replace("-----BEGIN PRIVATE KEY-----", "")
//                .replaceAll(System.lineSeparator(), "")
//                .replace("-----END PRIVATE KEY-----", "");
        str = str
                .replace("-----BEGIN " + type + "-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END " + type + "-----", "");
        return str.getBytes();
    }

}
