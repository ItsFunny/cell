package com.cell.component.httpsecurity.security.config;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 1:51 下午
 */
@Data
public class SecurityConfigProperty
{
    private List<String> whiteUrlList = Arrays.asList("/demo");
    private String loginUrl = "/login";
    private String authUrlPrefix = "/api/v1/auth/**";
    private boolean authEnable;


    SecurityConfigProperty()
    {

    }

}
