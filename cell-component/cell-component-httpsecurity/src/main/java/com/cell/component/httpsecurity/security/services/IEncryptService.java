package com.cell.component.httpsecurity.security.services;

import lombok.Data;

public interface IEncryptService
{
    @Data
    class EncryptReq
    {
        private String value;
    }

    @Data
    class EncryptResp
    {
        private String encryptResp;
    }

    EncryptResp encrypt(EncryptReq req);
}
