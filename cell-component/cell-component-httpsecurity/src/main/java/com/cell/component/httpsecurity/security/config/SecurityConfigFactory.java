package com.cell.component.httpsecurity.security.config;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.sdk.log.LOG;
import com.mi.wallet.mange.context.ModuleEnums;
import lombok.Data;

@Data
public class SecurityConfigFactory extends AbstractInitOnce
{
    private static final SecurityConfigFactory instance = new SecurityConfigFactory();
    private static final String moduleName = "env.security.property";

    private SecurityConfigProperty property;

    @Override
    protected void onInit(InitCTX initCTX)
    {
        try
        {
            this.property = defaultProperty();
//            this.property = Configuration.getDefault().getAndMonitorConfig(moduleName, SecurityConfigProperty.class, (l) ->
//            {
//                if (this.property.isAuthEnable() != l.isAuthEnable())
//                {
//                    WhiteListAntPathMatcher.setAuthEnable(l.isAuthEnable());
//                }
//                this.property = l;
//            });
        } catch (Exception e)
        {
            LOG.error(ModuleEnums.WEB_SECURITY, e, "获取配置失败,采用默认配置");
            this.property = defaultProperty();
        }
    }

    private SecurityConfigFactory()
    {

    }

    public static SecurityConfigFactory getInstance()
    {
        return instance;
    }

    private SecurityConfigProperty defaultProperty()
    {
        return new SecurityConfigProperty();
    }
}
