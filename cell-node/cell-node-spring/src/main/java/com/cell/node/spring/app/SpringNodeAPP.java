package com.cell.app;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-10 21:21
 */
public class SpringNodeAPP extends DefaultApp implements ISpringAPP
{
    private SpringApplication app;
    private ConfigurableApplicationContext appContext;
    private ConfigurableEnvironment environment;

    public SpringNodeAPP()
    {
        super();
    }

    public SpringApplication getApp()
    {
        return app;
    }

    public void setApp(SpringApplication app)
    {
        this.app = app;
    }

    public ConfigurableApplicationContext getAppContext()
    {
        return appContext;
    }

    public void setAppContext(ConfigurableApplicationContext appContext)
    {
        this.appContext = appContext;
    }

    public ConfigurableEnvironment getEnvironment()
    {
        return environment;
    }

    public void setEnvironment(ConfigurableEnvironment environment)
    {
        this.environment = environment;
    }
}
