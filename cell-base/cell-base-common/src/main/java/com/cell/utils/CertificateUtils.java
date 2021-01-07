package com.cell.utils;

import com.cell.constants.PemConstant;
import com.cell.model.CertInfo;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERBitString;
import org.bouncycastle.asn1.x509.Certificate;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-10-31 13:06
 */
public class CertificateUtils
{
    public static CertInfo parseCertStr2CertInfo(String certStr)
    {
        if (certStr.contains("BEGIN"))
        {
            certStr = PemUtils.replace(PemConstant.CERTIFICATE, certStr);
        }
        try
        {
            byte[] csCert = Base64Utils.decode(certStr);
            InputStream inStream = new ByteArrayInputStream(csCert);
            ASN1InputStream aIn = new ASN1InputStream(inStream);
            Certificate cert = Certificate.getInstance(aIn.readObject());
            SubjectPublicKeyInfo subjectPublicKeyInfo = cert.getSubjectPublicKeyInfo();
            DERBitString publicKey = subjectPublicKeyInfo.getPublicKeyData();
            String publicKeyString = Base64Utils.encode(KeyUtils.formatPubKey(KeyUtils.EnumSm2ProducerType.BIAOXIN, publicKey.getBytes()));
            String serialNum = cert.getSerialNumber().toString();
            Long fromTime = cert.getStartDate().getDate().getTime();
            Long toTime = cert.getEndDate().getDate().getTime();
            String issuer = cert.getIssuer().toString();
            String version = cert.getVersion().toString();
            String signatureAlgorithm = cert.getSignatureAlgorithm().getAlgorithm().getId();
            String subject = cert.getSubject().toString();
            return new CertInfo(serialNum, fromTime, toTime, issuer, publicKeyString, version, signatureAlgorithm, subject);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }
}
