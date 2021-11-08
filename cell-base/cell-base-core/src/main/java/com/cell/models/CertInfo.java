package com.cell.models;

public class CertInfo
{
    private String serialNum;
    private Long fromTime;
    private Long toTime;
    private String issuer;
    private String pubKey;
    private String version;
    private String signatureAlgorithm;
    private String subject;

    public CertInfo()
    {
        super();
    }

    public CertInfo(String serialNum, Long fromTime, Long toTime, String issuer, String pubKey, String version,
                    String signatureAlgorithm, String subject)
    {
        super();
        this.serialNum = serialNum;
        this.fromTime = fromTime;
        this.toTime = toTime;
        this.issuer = issuer;
        this.pubKey = pubKey;
        this.version = version;
        this.signatureAlgorithm = signatureAlgorithm;
        this.subject = subject;
    }

    public String getSerialNum()
    {
        return serialNum;
    }

    public void setSerialNum(String serialNum)
    {
        this.serialNum = serialNum;
    }

    public Long getFromTime()
    {
        return fromTime;
    }

    public void setFromTime(Long fromTime)
    {
        this.fromTime = fromTime;
    }

    public Long getToTime()
    {
        return toTime;
    }

    public void setToTime(Long toTime)
    {
        this.toTime = toTime;
    }

    public String getIssuer()
    {
        return issuer;
    }

    public void setIssuer(String issuer)
    {
        this.issuer = issuer;
    }

    public String getPubKey()
    {
        return pubKey;
    }

    public void setPubKey(String pubKey)
    {
        this.pubKey = pubKey;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public String getSignatureAlgorithm()
    {
        return signatureAlgorithm;
    }

    public void setSignatureAlgorithm(String signatureAlgorithm)
    {
        this.signatureAlgorithm = signatureAlgorithm;
    }

    public String getSubject()
    {
        return subject;
    }

    public void setSubject(String subject)
    {
        this.subject = subject;
    }

    @Override
    public String toString()
    {
        return "CertInfo [serialNum=" + serialNum + ", fromTime=" + fromTime + ", toTime=" + toTime + ", issuer="
                + issuer + ", pubKey=" + pubKey + ", version=" + version + ", signatureAlgorithm=" + signatureAlgorithm
                + ", subject=" + subject + "]";
    }
}
