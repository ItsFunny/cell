package com.cell.component.httpsecurity.security.impl;

import com.mi.wallet.mange.context.BusinessException;
import com.mi.wallet.mange.context.DBConstants;
import com.mi.wallet.mange.context.ErrorConstant;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 6:30 下午
 */
@Data
public class SecurityUser implements UserDetails
{
    private String userId;
    //    private String email;
    private String password;
    private String userSelfName;
    private Integer adminType;
    //    private Integer status;
//    private Date createDate;
//    private Date updateDate;
    private UserAuthority userAuthority;
    // TODO
    private Date expireDate;

    public Integer getIntegUserId(){
        return Integer.parseInt(this.userId);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return null;
    }

    @Override
    public String getPassword()
    {
        return this.password;
    }

    @Override
    public String getUsername()
    {
        return this.userId;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public boolean expired(){
        // TODO
        if (this.expireDate==null){
            return false;
        }
        return this.expireDate.before(new Date());
    }

    public JWTTokenUser toTokenUser()
    {
        if (!this.getUserAuthority().isSealed())
        {
            throw new BusinessException(ErrorConstant.INTERNAL_SERVER_ERROR);
        }

        JWTTokenUser tokenUser = new JWTTokenUser();
//        tokenUser.setId(this.getId());
        tokenUser.setRoles(this.getUserAuthority().getRoles());
        tokenUser.setDataPermissionSet(this.getUserAuthority().getDataPermissionSet());
        tokenUser.setOperationPermissionSet(this.getUserAuthority().getOperationPermissionSet());

        return tokenUser;
    }

    public boolean isSuperAdmin()
    {
        return this.userAuthority != null && this.userAuthority.getRoles().contains(DBConstants.DB_AUTH_SUPER_ADAMIN) || (this.adminType != null && this.adminType.equals(DBConstants.ADMIN_LEVEL_SUPER_ADMIN));
    }
}
