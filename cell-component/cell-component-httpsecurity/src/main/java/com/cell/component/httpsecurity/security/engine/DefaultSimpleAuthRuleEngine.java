package com.cell.component.httpsecurity.security.engine;

import com.amazonaws.HttpMethod;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mi.wallet.mange.common.db.DbCenter;
import com.mi.wallet.mange.context.BusinessException;
import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.ErrorConstant;
import com.mi.wallet.mange.context.WrapContextException;
import com.mi.wallet.mange.entity.SecurityMenuResource;
import com.mi.wallet.mange.entity.SecurityOperationResource;
import com.mi.wallet.mange.security.constants.SecurityConstants;
import com.mi.wallet.mange.security.filter.IAuthRuleEngine;
import com.mi.wallet.mange.security.services.IAuthUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.ArrayList;
import java.util.List;

public class DefaultSimpleAuthRuleEngine implements IAuthRuleEngine, InitializingBean {
    private IAuthUserService authUserService;

    private List<AntPathRequestMatcher> regexMatcher;


    public DefaultSimpleAuthRuleEngine(IAuthUserService authUserService) {
        this.authUserService = authUserService;
    }


    @Override
    public boolean analyse(CellContext CellContext, Authentication authentication) {
        String protocolId = CellContext.getProtocolId();
        String method = CellContext.getRequest().getMethod();
        String auth = this.generate(CellContext, protocolId, method);
        return this.authUserService.hashOperationPermission(auth);
    }

    @Override
    public RuleResp generateRule(CellContext CellContext, RuleReq req) {
        RuleResp ret = new RuleResp();
        String operationType = req.getOperationType();
        operationType = operationType.toUpperCase();
        String method = HttpMethod.GET.name();
        if (operationType.equals(SecurityConstants.DB_PERMISSION_OPERATION_ADD)) {
            method = HttpMethod.POST.name();
        } else if (operationType.equals(SecurityConstants.DB_PERMISSION_OPERATION_UPDATE)) {
            method = HttpMethod.PUT.name();
        } else if (operationType.equals(SecurityConstants.DB_PERMISSION_OPERATION_DELETE)) {
            method = HttpMethod.DELETE.name();
        } else {
            throw new WrapContextException(CellContext, new BusinessException(ErrorConstant.BAD_REQUEST));
        }
        ret.setOpRule(this.generate(CellContext, req.getProtocolId(), method));
        return ret;
    }

    private String generate(CellContext CellContext, String protocol, String method) {
        for (AntPathRequestMatcher antPathRequestMatcher : regexMatcher) {
            if (antPathRequestMatcher.matches(CellContext.getRequest())) {
                return antPathRequestMatcher.getPattern();
            }
        }
        return String.format("%s-%s", protocol, method);
    }

    public void seal() {
        List<SecurityMenuResource> meanus = DbCenter.dbCenter.menuResourceService.list();
        List<String> paths = new ArrayList<>();
        // for 循环启动没关系
        for (SecurityMenuResource resource : meanus) {
            Integer permissionId = resource.getPermissionId();
            QueryWrapper<SecurityOperationResource> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("permission_id", permissionId);
            SecurityOperationResource operaiton = DbCenter.dbCenter.operationResourceService.getOne(queryWrapper);
            if (operaiton == null) {
                continue;
            }
            paths.add(operaiton.getOpRule());
        }
        // TODO
        this.regexMatcher = new ArrayList<>();
        for (String an : paths) {
            if (StringUtils.isEmpty(an)) {
                continue;
            }
            this.regexMatcher.add(new AntPathRequestMatcher(an));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.seal();
    }
    //
}
