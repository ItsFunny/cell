package com.cell.component.httpsecurity.security.models;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.PageRequestDTO;
import lombok.Data;

@Data
public class AuthAdminListReqBO extends PageRequestDTO
{
    private AuthPermissionBaseReq permissionReq;

    @Override
    public void validBasic(CellContext context) throws IllegalArgumentException
    {
        super.validBasic(context);
    }

    @Override
    protected QueryWrapper doGetQueryWrapper() {
        return null;
    }
}
