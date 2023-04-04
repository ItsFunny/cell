package com.cell.component.httpsecurity.security.filter;

import com.mi.wallet.mange.context.CellContext;
import lombok.Data;
import org.springframework.security.core.Authentication;

public interface IAuthRuleEngine
{
    boolean analyse(CellContext context, Authentication authentication);


    RuleResp generateRule(CellContext context, RuleReq req);

    @Data
    class RuleReq
    {
        private String protocolId;
        private String operationType;

    }

    @Data
    class RuleResp
    {
        private String opRule;
    }
}
