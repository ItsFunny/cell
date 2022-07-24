package com.cell.component.http.swagger.config;

import com.cell.base.common.utils.StringUtils;
import com.cell.node.core.context.INodeContext;
import com.cell.sdk.configuration.Configuration;
import lombok.Data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class SwaggerConfig
{
    private boolean enableSwagger;
    private List<String> controllerPackagePath;
    private String title = "swagger";
    private static final String module = "swagger";

    // TODO, 扫包
    private static List<String> defaultPath = new ArrayList<>();
    private static SwaggerConfig instance = new SwaggerConfig();

    public static SwaggerConfig getInstance()
    {
        return instance;
    }

    public void seal(INodeContext context)
    {
        SwaggerConfig config = null;
        try
        {
            config = Configuration.getDefault().getConfigValue(module).asObject(SwaggerConfig.class);
        } catch (Exception e)
        {
            if (StringUtils.isEmpty(defaultPath))
            {
                throw new RuntimeException(e);
            }
            config = new SwaggerConfig();
            config.setEnableSwagger(true);
        }
        instance = config;
        if (StringUtils.isNotEmpty(defaultPath))
        {
            instance.setControllerPackagePath(defaultPath);
        }
    }

    public static void addSwaggerPath(String... path)
    {
        defaultPath.addAll(Arrays.asList(path));
    }
}
