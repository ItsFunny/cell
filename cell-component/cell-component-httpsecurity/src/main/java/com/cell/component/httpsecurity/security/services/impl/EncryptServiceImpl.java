package com.cell.component.httpsecurity.security.services.impl;

import com.mi.wallet.mange.security.services.IEncryptService;
import org.springframework.stereotype.Service;

@Service
public class EncryptServiceImpl implements IEncryptService
{

    @Override
    public EncryptResp encrypt(EncryptReq req)
    {
        EncryptResp ret=new EncryptResp();
        ret.setEncryptResp(req.getValue());
        return ret;
    }
}
