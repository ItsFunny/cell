package com.cell.utils;

import com.cell.models.CertInfo;
import com.sansec.mobileshield.bx.asym.impl.SM2Impl;
import com.sm2.SM2Utils;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.gm.GMNamedCurves;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.asn1.sec.ECPrivateKey;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPrivateKeyParameters;
import org.bouncycastle.jcajce.provider.asymmetric.ec.BCECPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.math.ec.custom.gm.SM2P256V1Curve;
import org.bouncycastle.util.BigIntegers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-09-30 13:06
 */
@Slf4j
public class GMUtil
{
    private static SM2Impl impl = new SM2Impl();

    public static byte[] encrypt(byte[] pubKey, byte[] data) {
        return impl.publicKeyEncrypt(pubKey, data);
    }

    public static byte[] encrypt(String pubKeyBase64, byte[] data) {
        byte[] pubKey = base64Decode(pubKeyBase64);
        return encrypt(pubKey, data);
    }

    public static byte[] decrypt(byte[] priKey, byte[] encData) {
        return impl.privateKeyDecrypt(priKey, encData);
    }

    public static byte[] decrypt(String priKeyBase64, byte[] encData) {
        byte[] priKey = base64Decode(priKeyBase64);
        return impl.privateKeyDecrypt(priKey, encData);
    }

    public static CertInfo parseSM2CertStrFromFile(String path)
    {
        try
        {
            byte[] bytes = FileUtils.readFileToByteArray(new File(path));
            return parseSM2CertStr(new String(bytes));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    // 将pem 证书转换为公钥
    @SuppressWarnings("resource")
    public static CertInfo parseSM2CertStr(String sm2Cert)
    {
        if (StringUtils.isNullOrEmpty(sm2Cert))
        {
            throw new RuntimeException("参数不可为空");
        }
        if (sm2Cert.contains("BEGIN"))
        {
            sm2Cert = replace("CERTIFICATE", sm2Cert);
        }
        byte[] csCert = Base64Utils.decode(sm2Cert);
        InputStream inStream = new ByteArrayInputStream(csCert);
        ASN1InputStream aIn = new ASN1InputStream(inStream);
        Certificate cert = null;
        try
        {
            cert = Certificate.getInstance(aIn.readObject());
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        SubjectPublicKeyInfo subjectPublicKeyInfo = cert.getSubjectPublicKeyInfo();
        DERBitString publicKey = subjectPublicKeyInfo.getPublicKeyData();
        String publicKeyString = Base64Utils.encode(dealPubKey(publicKey.getBytes()));

//        LOG.info(BSModule.CA_COMPONENT, "SerialNumber = " + cert.getSerialNumber());

        String serialNum = cert.getSerialNumber().toString();
        Long fromTime = cert.getStartDate().getDate().getTime();
        Long toTime = cert.getEndDate().getDate().getTime();
        String issuer = cert.getIssuer().toString();
        String pubKey = publicKeyString;
        String version = cert.getVersion().toString();
        String signatureAlgorithm = cert.getSignatureAlgorithm().getAlgorithm().getId();
        String subject = cert.getSubject().toString();
        return new CertInfo(serialNum, fromTime, toTime, issuer, pubKey, version, signatureAlgorithm, subject);
    }

//    public static boolean verify(byte[] pubKey, byte[] content, byte[] sign)
//    {
////        pubKey = formatPubKey((byte) 0, pubKey);
//        return StandardSM2Util.verify(pubKey, content, sign);
//    }

//    public static byte[] sign(byte[] prvKey, byte[] content)
//    {
//        return StandardSM2Util.sign(prvKey, content);
//    }

    private static byte[] dealPubKey(byte[] pubKey)
    {
        if (pubKey.length == 64)
        {
            return pubKey;
        }
        byte[] result = new byte[64];
        System.arraycopy(pubKey, 1, result, 0, 64);
        return result;
    }

    public static byte[] parseSM2PrvK(String base64Str) throws Exception
    {
        if (base64Str.contains("BEGIN"))
        {
            base64Str = replace("PRIVATE KEY", base64Str);
        }
        try
        {
            return formatPriKey(Base64Utils.decode(base64Str));
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static byte[] formatPrvKey(String path) throws IOException
    {
        File file = new File(path);
        String key = new String(Files.readAllBytes(file.toPath()), Charset.defaultCharset());
        String prvKeyStr = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replace("-----END PRIVATE KEY-----", "");
        return formatPriKey(Base64Utils.decode(prvKeyStr));
    }

    // 读取私钥
    public static byte[] formatPriKey(byte[] priKey) throws IOException
    {
        if (priKey.length == 32)
        {
            return priKey;
        }
        if (priKey.length == 33)
        {
            return Arrays.copyOfRange(priKey, 1, 33);
        }
        PrivateKeyInfo bb = PrivateKeyInfo.getInstance(priKey);
        byte[] keyDer = bb.parsePrivateKey().toASN1Primitive().getEncoded("DER");
        ECPrivateKey ecPrivateKey = ECPrivateKey.getInstance(keyDer);
        return BigIntegers.asUnsignedByteArray(32, ecPrivateKey.getKey());
    }


    public static byte[] formatPubKey(String pubKeyStr)
    {
        byte[] decode = Base64Utils.decode(pubKeyStr);
        return formatPubKey(0, decode);
    }

    public static byte[] formatPubKey(int byteEnum, byte[] pubKey)
    {
        byte[] result = pubKey;
        if (result.length > 65)
        {
            result = SubjectPublicKeyInfo.getInstance(result).getPublicKeyData().getBytes();
        }
        if (result.length == 65)
        {
            result = org.bouncycastle.util.Arrays.copyOfRange(result, 1, 65);
        }

        switch (byteEnum)
        {
            case 0:
                result = org.bouncycastle.util.Arrays.concatenate(new byte[]{0x04}, result);
                break;
            default:
                break;
        }
        return result;
    }

//    public static byte[] formatPubKey(byte[] pubKey)
//    {
//        byte[] result = pubKey;
//        if (pubKey.length > 65)
//        {
//            result = SubjectPublicKeyInfo.getInstance(pubKey).getPublicKeyData().getBytes();
//        }
//        if (result.length == 64)
//        {
//            result = org.bouncycastle.util.Arrays.concatenate(new byte[]{4}, result);
//        }
//
//        return result;
//    }


    private static final byte[] USERID = "1234567812345678".getBytes(Charset.forName("UTF-8"));

    static
    {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    }

//    private static SM2Impl impl = new SM2Impl();
//
//    public static byte[] encrypt(byte[] pubKey, byte[] data)
//    {
//        return impl.publicKeyEncrypt(pubKey, data);
//    }
//
//    public static byte[] encrypt(String pubKeyBase64, byte[] data)
//    {
//        byte[] pubKey = base64Decode(pubKeyBase64);
//        return encrypt(pubKey, data);
//    }
//
//    public static byte[] decrypt(byte[] priKey, byte[] encData)
//    {
//        return impl.privateKeyDecrypt(priKey, encData);
//    }
//
//    public static byte[] decrypt(String priKeyBase64, byte[] encData)
//    {
//        byte[] priKey = base64Decode(priKeyBase64);
//        return impl.privateKeyDecrypt(priKey, encData);
//    }

//    public static byte[] sign(byte[] priKey, byte[] content) {
//        String signature = SM2Utils.sign(USERID, priKey, content);
//        return StringUtil.hexStringToBytes(signature);
//    }
//
//    public static boolean verify(byte[] publicKey, byte[] content, byte[] sign) {
//        return SM2Utils.verifySign_ZA(USERID, publicKey, content, StringUtil.bytesToHexString(sign));
//    }

    public static String base64Encode(byte[] data)
    {
        return Base64Utils.encode(data);
    }

    public static byte[] base64Decode(String base64Str)
    {
        return Base64Utils.decode(base64Str);
    }

    public static ECPrivateKeyParameters convertPrivateKeyToParameters(BCECPrivateKey ecPriKey)
    {
        ECParameterSpec parameterSpec = ecPriKey.getParameters();
        ECDomainParameters domainParameters = new ECDomainParameters(parameterSpec.getCurve(), parameterSpec.getG(),
                parameterSpec.getN(), parameterSpec.getH());
        return new ECPrivateKeyParameters(ecPriKey.getD(), domainParameters);
    }

//    public static void main(String[] args) throws IOException {
//        String pfxFile = "E:/sm2p256v1.pfx";
//        String password = "12345678";
//        String origin = "message digest";
////		byte[] orgData = Strings.toByteArray(origin);
//
//        try {
////    		FileInputStream fis = new FileInputStream(pfxFile);
////            KeyStore ks2 = KeyStore.getInstance("PKCS12", "BC");
////            ks2.load(fis, password.toCharArray());
////    		Enumeration enum1 = ks2.aliases();
////            String keyAlias = null;
////            if (enum1.hasMoreElements()) // we are readin just one certificate.
////            {
////                keyAlias = (String)enum1.nextElement();
////                System.out.println("alias=[" + keyAlias + "]");
////            }
////
////    		PrivateKey prikey = (PrivateKey) ks2.getKey(keyAlias, password.toCharArray());
////
////			X509Certificate cert = (X509Certificate) ks2.getCertificate(keyAlias);
////			PublicKey pub = cert.getPublicKey();
////
////			ECPublicKeyParameters pubKeyParem = (ECPublicKeyParameters) ECUtil.generatePublicKeyParameter(pub);
////			ECPrivateKeyParameters aPriv = (ECPrivateKeyParameters) ECUtil.generatePrivateKeyParameter(prikey);
////			byte[] pubKey = pubKeyParem.getQ().getEncoded(false);
////			if (pubKey.length > 64) {
////				pubKey = Arrays.copyOfRange(pubKey, 1, 65);
////			}
////			byte[] priKey = aPriv.getD().toByteArray();
////			if (priKey.length > 32) {
////				priKey = Arrays.copyOfRange(priKey, 1, 33);
////			}
////			System.out.println("SM2 publicKey = " + Base64Util.encode(pubKey));
////			System.out.println("SM2 privateKey = " + Base64Util.encode(priKey));
////
////			SM2EngineGM sm2Engine = new SM2EngineGM();
////			sm2Engine.init(true, new ParametersWithRandom(pubKeyParem));
////			byte[] enc = sm2Engine.processBlock(orgData, 0, orgData.length);
////			System.out.println(Base64Util.encode(enc));
////
////			sm2Engine.init(false, aPriv);
////			byte[] dec = sm2Engine.processBlock(enc, 0, enc.length);
////			System.out.println(Base64Util.encode(dec));
////			if (!Arrays.areEqual(orgData, dec)) {
////				System.out.println("dec wrong");
////				return;
////			}
////			System.out.println(new String(dec));
//
//
////			byte[] privateKey = Base64Util.decode("GUn9yskQOSJ77Wfao/aGfq5czl1KXR6/nr1qubLy/WA=");
////			byte[] publicKey = Base64Util.decode("10u6rD8oByD5vQSEWpQq3NSLgU45LSxY8s4I9ckQb3oof0XfE1KH65E5PXhpplUppiD89ZhseV1tbzxIJfennQ==");
////			Sm2KeyPair pair = SM2Utils.generateKeyPair();
////			byte[] privateKey = pair.getPriKey();
////			byte[] publicKey = pair.getPubKey();
////			publicKey = Arrays.concatenate(new byte[] { 0x04 }, publicKey);
////			String userID = "1234567812345678";
////			String pri = StringUtil.bytesToHexString(privateKey);
////			String signData = SM2Utils.Signature(userID, pri, dataString);
////			String pub = StringUtil.bytesToHexString(publicKey);
////			Boolean flag = SM2Utils.verifySignSM2_ZA(userID, pub, dataString, signData);
////			System.out.println("签名验证是否成功：" + flag);
//
////			String dataString = "123asda森岛帆高";
////			byte[] data = StringUtil.strToBytes(dataString);
////			byte[] encData = sm2Engine.processBlock(data, 0, data.length);
////			byte[] decData = sm2Engine.processBlock(privateKey, encData);
////			System.out.println(new String(decData));
////			byte[] decData = impl.privateKeyDecrypt(priKey, enc);
////			System.out.println("广东加密，三未解密：" + new String(decData));
//
////			decData = SM2Utils.decryptC1C2C3(priKey, enc);
////			System.out.println(new String(decData));
//
////			byte[] decData = SM2Utils.decryptC1C3C2(priKey, enc);
////			System.out.println("广东加密，世纪数码C1C3C2解密：" + new String(decData));
//
////			enc = SM2Utils.encryptC1C2C3(pubKey, orgData);
////			decData = sm2Engine.processBlock(enc, 0, enc.length);
////			System.out.println("世纪数码C1C2C3加密，广东解密：" + new String(decData));
//            byte[] pubKey = Base64Util.decode("rcwJPXiBByX1p+7mLvbp9MFL5f9fZIfC/peLFl+gEd9INIYO4NAZpG4Lc70ZuKExQBQigqdaxK69wknT8280Zg==");
//            byte[] priKey = Base64Util.decode("4Dah9MBtD4ahe2N2/zYB+L3lBdFVQI2HVGRrW74nLzg=");
//            SM2Impl impl = new SM2Impl();
//            int count = 100;
//            ExecutorService executorService = Executors.newFixedThreadPool(count);
//            CountDownLatch countDownLatch = new CountDownLatch(count);
//            for (int i = 0; i < count; i++) {
//                executorService.execute(() -> {
//                    try {
//                        int random = RandomUtil.randomInt(100, 10000);
//
//                        byte[] orgData = getBytes(random);
//                        String dataStr = Base64Util.encode(orgData);
//                        byte[] enc = SM2Utils.encryptC1C3C2(pubKey, orgData);
//                        byte[] decData = impl.privateKeyDecrypt(priKey, java.util.Arrays.copyOfRange(enc, 1, enc.length));
//                        Assert.isTrue(dataStr.equals(Base64Util.encode(decData)), String.format("decrypt fail, origin = %s, decData = %s", dataStr, Base64Util.encode(decData)));
//
//                        enc = impl.publicKeyEncrypt(pubKey, orgData);
//                        decData = SM2Utils.decryptC1C3C2(priKey, StringUtil.hexStringToBytes("04" + StringUtil.bytesToHexString(enc)));
//                        Assert.isTrue(dataStr.equals(Base64Util.encode(decData)), String.format("decrypt fail, origin = %s, decData = %s", dataStr, Base64Util.encode(decData)));
//                    } catch (Exception e) {
//                        LOG.error(BSModule.COMMON, e, "验证加解密失败");
//                    } finally {
//                        countDownLatch.countDown();
//                    }
//                });
//            }
//            countDownLatch.await();
//            LOG.info(BSModule.COMMON, "验证加解密成功");
//            executorService.shutdown();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    private static byte[] getBytes(int random) {
//        byte[] result = new byte[random];
//        for (int i = 0; i < random; i++) {
//            result[i] = Byte.parseByte(RandomUtil.randomInt(-128, 127) + "");
//        }
//        return result;
//    }


    public static String replace(String type, String str)
    {
        str = str
                .replace("-----BEGIN " + type + "-----", "")
                .replace("-----END " + type + "-----", "")
                .replaceAll(System.lineSeparator(), "")
                .replaceAll("[\\s\\t\\n\\r]", "");
        return str;
    }

    public static byte[] sign(byte[] prvKey, byte[] content)
    {
        return SM2Utils.sign(USERID, prvKey, content).getBytes();
    }

    // 用于与fabric 交互,会hex 转码
    public static boolean verify(byte[] pubKey, byte[] content, byte[] sign)
    {
        pubKey = formatPubKey((byte) 0, pubKey);
        return SM2Utils.verifySign_ZA(USERID, pubKey, content,HexUtil.bytes2HexString(sign));
    }
    // 用于自验自签
    public static boolean verifyOwner(byte[] pubKey, byte[] content, byte[] sign)
    {
        pubKey = formatPubKey((byte) 0, pubKey);
        return SM2Utils.verifySign_ZA(USERID, pubKey, content, new String(sign));
    }
    // 用于与fabric 交互,会hex 转码
    public static boolean verify(String base64Pubkey, byte[] content, byte[] sign)
    {
        return verify(Base64Utils.decode(base64Pubkey), content, sign);
    }
    // 用于自验自签
    public static boolean verifyOwner(String base64Pubkey, byte[] content, byte[] sign)
    {
        return verifyOwner(Base64Utils.decode(base64Pubkey), content, sign);
    }


    // 将字节转换为私钥



    private static X9ECParameters x9ECParameters = GMNamedCurves.getByName("sm2p256v1");
    private static ECDomainParameters ecDomainParameters = new ECDomainParameters(x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN());
    public static final SM2P256V1Curve CURVE = new SM2P256V1Curve();
    public final static BigInteger SM2_ECC_GX = new BigInteger(
            "32C4AE2C1F1981195F9904466A39C9948FE30BBFF2660BE1715A4589334C74C7", 16);
    public final static BigInteger SM2_ECC_GY = new BigInteger(
            "BC3736A2F4F6779C59BDCEE36B692153D0A9877CC62A474002DF32E52139F0A0", 16);
    public static final ECPoint G_POINT = CURVE.createPoint(SM2_ECC_GX, SM2_ECC_GY);
    public static  final ECParameterSpec ecParameterSpec=new ECParameterSpec(ecDomainParameters.getCurve(),ecDomainParameters.getG(),ecDomainParameters.getN(),ecDomainParameters.getH() );

    // 将标准的32位的byte转换为私钥
    public static PrivateKey parseStandard32SM2PrivateKey(byte[] privatekey)
            throws InvalidKeySpecException, NoSuchAlgorithmException, NoSuchProviderException
    {
        KeyFactory factory = KeyFactory.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
        if (privatekey.length != 32)
            throw new RuntimeException("err key length,must be 32");
        org.bouncycastle.jce.spec.ECPrivateKeySpec  keySpec = new  org.bouncycastle.jce.spec.ECPrivateKeySpec (new BigInteger(1, privatekey),ecParameterSpec);
        return factory.generatePrivate(keySpec);
    }



}
