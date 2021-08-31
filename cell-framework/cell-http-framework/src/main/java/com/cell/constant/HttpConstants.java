package com.cell.constant;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-27 22:51
 */
public interface HttpConstants
{
    String PLATFORM = "X-Platform";
    String VERSION_INT = "X-VersionInt";
    String VERSION = "X-Version";
    String SYSTEM_VERSION_INT = "X-SystemVersionInt";
    String SYSTEM_VERSION = "X-SystemVersion";
    String SYSTEM_MODEL = "X-SystemModel";
    String SYSTEM_BRAND = "X-SystemBrand";
    String DEVICE_ID = "X-DeviceId";
    String NETWORK_TYPE = "X-NetworkType";
    String SEQUENCE_ID = "sequenceId";
    String SUBUNDERWRITING_ID = "subunderwritingId";
    String EBIDSUN_APP_ID = "ebidsunAppId";
    String SIGN_HEADER_KEY = "signature";
    String SIGN_PUBLIC_KEY_HEADER_KEY = "signPublicKey";
    long DEFAULT_RESULT_TIME_OUT = 60 * 1000;
    long HTTP_ARCHIVE_NOT_READY = 1;


    String HTTP_HEADER_CODE = "code";
    String HTTP_HEADER_MSG = "msg";

}
