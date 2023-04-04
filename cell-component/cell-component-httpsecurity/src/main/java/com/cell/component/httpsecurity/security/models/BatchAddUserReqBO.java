package com.cell.component.httpsecurity.security.models;

import com.mi.wallet.mange.context.IFromV4;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class BatchAddUserReqBO
{
    private List<BatchAddUserNode> items;

    @Data
    public static class BatchAddUserNode implements IFromV4<AddUserReqBO>
    {
        private String userName;
        private String password;
        private int adminType = 1;
        // 这个用户所赋予的角色
        private Set<Integer> roleIdList;
        // 所属部门
        private Integer groupId;
        private String mask;

        @Override
        public void onFrom(AddUserReqBO addUserReqBO) {
            this.password=addUserReqBO.getPassword();
            this.userName=addUserReqBO.getUserName();
            this.mask=addUserReqBO.getMask();
            this.adminType=addUserReqBO.getAdminType();
        }
    }
}
