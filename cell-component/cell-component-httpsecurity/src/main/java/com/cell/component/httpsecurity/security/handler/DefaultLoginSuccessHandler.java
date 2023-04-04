package com.cell.component.httpsecurity.security.handler;

import com.cell.base.common.utils.JSONUtil;
import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.common.bean.ResultDO;
import com.mi.wallet.mange.context.ModuleEnums;
import com.mi.wallet.mange.security.impl.SecurityUser;
import com.mi.wallet.mange.security.utils.SecurityUtils;
import lombok.Data;
import org.springframework.security.core.Authentication;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 2:14 下午
 */
public class DefaultLoginSuccessHandler implements LoginSuccessHandler {
    @Data
    class LoginResult {
        private String token;
        private boolean superAdmin;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        LOG.info(ModuleEnums.WEB_SECURITY, "登录成功,{}", authentication);
        SecurityUser user = (SecurityUser) authentication.getPrincipal();
        LoginResult result = new LoginResult();
        result.setSuperAdmin(user.isSuperAdmin());
        String token = SecurityUtils.createSecurityToken(user);
        result.setToken(token);
        ResultDO<LoginResult> success = success(result);
        httpServletResponse.getWriter().write(JSONUtil.obj2Json(success));
    }

    public static <T> ResultDO<T> success(T data) {
        ResultDO<T> ret = new ResultDO<>();
        ret.setSuccess(true);
        ret.setData(data);
        return ret;
    }
}
