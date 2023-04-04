package com.cell.component.httpsecurity.security.models;

import com.cell.base.common.utils.CollectionUtils;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.IValidBasic;
import com.mi.wallet.mange.utils.ValidUtils;
import lombok.Data;

import java.util.List;

@Data
public class AuthRemoveUserRequest implements IValidBasic {
    private List<Integer> userIdList;

    @Override
    public void validBasic(CellContext context) throws IllegalArgumentException {
        if (CollectionUtils.isEmpty(this.userIdList)) {
            throw new IllegalArgumentException();
        }
        for (Integer integer : userIdList) {
            ValidUtils.check(integer);
        }
    }
}
