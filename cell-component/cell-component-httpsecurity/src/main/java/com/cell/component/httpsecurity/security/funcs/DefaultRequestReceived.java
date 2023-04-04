package com.cell.component.httpsecurity.security.funcs;

import com.cell.base.common.utils.StringUtils;
import com.mi.wallet.mange.security.funs.OnRequestReceived;
import com.mi.wallet.mange.security.models.DefaultTokenInfo;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 3:53 下午
 */
public class DefaultRequestReceived implements OnRequestReceived {
    @Override
    public Authentication onRecv(HttpServletRequest request) {
        DefaultTokenInfo authRequest = new DefaultTokenInfo(null, null);
        String ltStr = request.getParameter(DefaultTokenInfo.LOGIN_TYPE);
        String userName = request.getParameter(DefaultTokenInfo.LOGIN_KEY);
        String password = request.getParameter(DefaultTokenInfo.LOGIN_PASSWORD);
        authRequest.setLoginKey(userName);
        authRequest.setLoginPwd(password);
        authRequest.setLoginType(StringUtils.isEmpty(ltStr) ? DefaultTokenInfo.USERNAME_PWD_LOGIN : Integer.parseInt(ltStr));
//        authRequest.setVCode(request.getParameter("vCode"));
//        String wechatId = request.getParameter("wechatId");
//        authRequest.setWechatId(wechatId);
//        authRequest.setPhone(request.getParameter("phone"));
//        authRequest.setPassword(request.getParameter("password"));
        return authRequest;
    }
}
