package com.cell.component.httpsecurity.security;


import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.request.AuthGetAdminTypeRequest;
import com.mi.wallet.mange.request.GetUserMenuRequest;
import com.mi.wallet.mange.response.AuthGetAdminTypeResponse;
import com.mi.wallet.mange.response.GetUserMenuResponse;
import com.mi.wallet.mange.security.models.*;

public interface IAuthService {
    GetUserAuthRespBO getUserAuth(CellContext context, GetUserAuthReqBO req);

    GetUserMenuResponse getUserMenus(CellContext context, GetUserMenuRequest req);


    AuthGetAdminTypeResponse getAdminType(CellContext context, AuthGetAdminTypeRequest req);

    AuthGetMenuRespBO getMenus(CellContext context, AuthGetMenusReqBO reqBO);

    // 添加用户,普通管理员,或者是超级管理员
    AddUserRespBO addUser(CellContext context, AddUserReqBO reqBO);

    GetRoleRespBO getRoles(CellContext context, GetRoleReqBO req);

    // 获取菜单(同时也是数据权限,及其操作权限
    GetMenuAuthRespBO getMenuAuthInfo(CellContext context, GetMenuAuthReqBO req);

    AuthAdminListRespBO adminList(CellContext context, AuthAdminListReqBO reqBO);


    AuthUpdateUserResponse updateUser(CellContext context, AuthUpdateUserRequest req);

    AuthRemoveUserResponse removeUser(CellContext context,AuthRemoveUserRequest req);
}

