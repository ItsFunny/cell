package com.cell.component.httpsecurity.security;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cell.base.common.utils.DateUtils;
import com.cell.base.common.utils.StringUtils;
import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.context.*;
import com.mi.wallet.mange.entity.SecurityUserInfo;
import com.mi.wallet.mange.security.filter.DefaultLoginAuthenticationFilter;
import com.mi.wallet.mange.security.impl.AbstractUserDetailService;
import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.impl.UserAuthority;
import com.mi.wallet.mange.security.models.DefaultTokenInfo;
import com.mi.wallet.mange.security.models.GetUserAuthReqBO;
import com.mi.wallet.mange.security.models.GetUserAuthRespBO;
import com.mi.wallet.mange.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 4:45 下午
 */
public class DefaultUserNamePasswordServiceImpl extends AbstractUserDetailService {
    private SecurityUserInfoService azerUserService;
    private SecurityUserRoleService userRoleService;
    private SecurityGroupRoleService azerGroupRoleService;

    private SecurityRolePermissionService rolePermissionService;
    private SecurityPermissionService permissionService;
    private SecurityMenuResourceService menuResourceService;
    //    private AzerPermissionMenuResourceService permissionMenuResourceService;
    private SecurityOperationResourceService operationResourceService;

    private SecurityRoleService roleService;

    public DefaultUserNamePasswordServiceImpl(SecurityUserInfoService azerUserService,
                                              SecurityUserRoleService userRoleService,
                                              SecurityGroupRoleService azerGroupRoleService,
                                              SecurityPermissionService permissionService,
                                              SecurityMenuResourceService menuResourceService,
                                              SecurityOperationResourceService operationResourceService,
                                              SecurityRolePermissionService rolePermissionService,
//                                              SecurityPermissionMenuResourceService permissionMenuResourceService,
                                              SecurityRoleService roleService
    ) {
        this.azerUserService = azerUserService;
        this.userRoleService = userRoleService;
        this.azerGroupRoleService = azerGroupRoleService;
        this.permissionService = permissionService;
        this.menuResourceService = menuResourceService;
        this.operationResourceService = operationResourceService;
        this.rolePermissionService = rolePermissionService;
        this.roleService = roleService;
    }

    @Autowired
    private IAuthService authService;

    @Override
    public UserDetails loadUserByUsername(JSONObject jsonObject, Integer loginType) throws UsernameNotFoundException {
        Object email = jsonObject.get(DefaultTokenInfo.LOGIN_KEY);
        Object password = jsonObject.get(DefaultTokenInfo.LOGIN_PASSWORD);
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            LOG.erroring(ModuleEnums.WEB_SECURITY, "账户名或者密码为空");
            return null;
        }
        QueryWrapper<SecurityUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_name", email);
        queryWrapper.eq("password", password);
        SecurityUserInfo one = azerUserService.getOne(queryWrapper);
        if (one == null) {
            throw new UsernameNotFoundException("account :" + email + ",not exists");
        }
        if (one.getStatus() == null || one.getStatus().equals(DBConstants.DB_USER_STATUS_DISABLE)) {
            throw new BusinessException(ErrorConstant.USER_FORBIDDEN);
        }
        Integer userId = one.getUserId();
        GetUserAuthReqBO getUserAuthReqBO = new GetUserAuthReqBO();
        getUserAuthReqBO.setUserId(userId);
        getUserAuthReqBO.setDataPermission(true);
        getUserAuthReqBO.setOperationPermission(true);
        GetUserAuthRespBO userAuth = authService.getUserAuth(CellContext.emptyContext(), getUserAuthReqBO);
        UserAuthority userAuthority = new UserAuthority();
        userAuthority.seal(userAuth.getItem());

        SecurityUser ret = new SecurityUser();
        ret.setAdminType(one.getType());
        ret.setUserAuthority(userAuthority);
        ret.setUserId(userId + "");
        ret.setPassword(one.getPassword());
        ret.setUserSelfName(one.getUserName());
        Date expireDate = DateUtils.getDateAfter(new Date(), 7);
        ret.setExpireDate(expireDate);
        return ret;
    }

    @Override
    public boolean validIsMine(Integer type) {
        return (type & DefaultLoginAuthenticationFilter.USERNAME_PWD_LOGIN) >= DefaultLoginAuthenticationFilter.USERNAME_PWD_LOGIN;
    }
}
