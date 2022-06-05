package com.cell.extension.mybatis.config;

import lombok.Data;

@Data
public class MysqlConfig
{
    private String mysqlKey;
    private String url;
    private String username;
    private String password;
    private String driverClass = "com.mysql.jdbc.Driver";
    private Integer initialSize = 16;
    private Integer minIdle = 16;
    private Integer maxActive = 32;
    private Long maxWait = 60000L;
    private Long timeBetweenEvictionRunsMillis = 60000L;
    private String validationQuery = "SELECT 1";
    private Boolean testWhileIdle = true;
    private Boolean testOnBorrow = false;
    private Boolean testOnReturn = false;
    private Long daoExecTime = 1000L;
    private Long daoCacheExpireTime = 1800 * 1000L;
    private Boolean asyncInit = false;
}
