package com.cell.extension.mybatis.config;

import com.cell.base.common.models.Module;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.utils.MetadataEntityUtil;
import com.cell.extension.mybatis.constants.DBConstants;
import com.cell.sdk.configuration.Configuration;
import com.cell.sdk.configuration.model.IConfigValue;
import com.cell.sdk.log.LOG;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Data
public class MybatisConfig
{

    public final String DAO_CONFIG_MODULE_NAME = "env.account";
    private IConfigValue accountConfig;
    private MysqlConfig mysqlConfig;
    public final String REDIS_KEY = "redis";

    private MybatisConfig()
    {
    }

    public static MybatisConfig getInstance()
    {
        return MybatisConfigHolder.INSTANCE;
    }

    private static class MybatisConfigHolder
    {
        private static final MybatisConfig INSTANCE = new MybatisConfig();
    }

    //防止反序列化生成新的实例
    private Object readResolve()
    {
        return MybatisConfigHolder.INSTANCE;
    }

    private List<MysqlConfig> configs;
    private Map<String, MysqlConfig> mysqlMap;
    private MysqlSummary summary;
    private String mysqlKey = DBConstants.MYSQL_KEY;
    private List<String> defaultJsonFields = Arrays.asList("envelopeExtension", "signatureExtension");
    private int pageLimit = 100000;

    public List<String> getDefaultJsonFields()
    {
        return defaultJsonFields;
    }

    public void setDefaultJsonFields(List<String> defaultJsonFields)
    {
        this.defaultJsonFields = defaultJsonFields;
    }

    public String getMysqlKey()
    {
        return mysqlKey;
    }

    public void setMysqlKey(String mysqlKey)
    {
        if (!StringUtils.isEmpty(mysqlKey))
        {
            this.mysqlKey = mysqlKey;
        }
    }

    public MysqlConfig getDefaultMysqlConfig()
    {
        return mysqlMap.get(mysqlKey);
    }

    public List<MysqlConfig> getMysqlList()
    {
        return configs;
    }

    public MysqlSummary getSummary()
    {
        return summary;
    }


    public MysqlConfig getMysqlConfig(String sqlKey)
    {
        return mysqlMap.get(sqlKey);
    }

    public void initConfig()
    {
        try
        {
            accountConfig = Configuration.getDefault().getConfigValue(DAO_CONFIG_MODULE_NAME);
            mysqlConfig = accountConfig.getObject(DBConstants.MYSQL_KEY).asObject(MysqlConfig.class);

            summary = Configuration.getDefault().getAndMonitorConfig(DAO_CONFIG_MODULE_NAME, MysqlSummary.class, null);
            refresh(accountConfig);
        } catch (Exception e)
        {
            LOG.error(Module.DYNAMIC_DAO, e, "读取配置失败， module = {}", DAO_CONFIG_MODULE_NAME);
            throw new RuntimeException(e);
        }
    }

    private void refresh(IConfigValue configValue)
    {
        List<MysqlConfig> list = new ArrayList<>();
        if (summary != null && !StringUtils.isEmpty(summary.getMysqlDbList()))
        {
            for (String key : summary.getMysqlDbList())
            {
                IConfigValue cv = configValue.getObject(key);
                if (cv == null)
                {
                    throw new RuntimeException("未知的mysql配置：" + key);
                }
                MysqlConfig config = cv.asObject(MysqlConfig.class);
                config.setMysqlKey(key);
                list.add(config);
            }
        } else
        {
            mysqlConfig.setMysqlKey(DBConstants.MYSQL_KEY);
            list.add(mysqlConfig);
        }
        configs = list;
        mysqlMap = MetadataEntityUtil.listToMap(list, MysqlConfig::getMysqlKey);
    }
}
