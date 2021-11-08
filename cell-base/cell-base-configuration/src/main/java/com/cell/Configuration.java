package com.cell;

import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.FileUtils;
import com.cell.base.common.utils.JSONUtil;
import com.cell.exception.ConfigurationException;
import com.cell.sdk.log.LOG;
import com.cell.model.ConfigValueJson;
import com.cell.model.IConfigValue;
import com.cell.parser.ConfigurationParserJson;
import com.cell.parser.IConfigurationParser;
import com.cell.refresh.ConfigRefresher;
import com.cell.refresh.IConfigListener;
import com.cell.refresh.ILoadConfigListener;
import com.cell.refresh.IRefreshExceptionListener;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.EventExecutor;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Configuration
{
    static
    {
    }

    private static final int REFRESH_CHECK_INTERVAL_SECONDS = 10;

    private ConcurrentHashMap<String, IConfigurationParser> configurationParsers = new ConcurrentHashMap<>();
    private ConfigRefresher configRefresher = null;

    private ConcurrentHashMap<String, ArrayList<String>> configModulesMap = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String, ConfigModule> modules = new ConcurrentHashMap<>();
    private List<String> configTypes = null;
    private String repoRoot = null;
    private String configType = null;
    private boolean initialized = false;

    @Deprecated
    public Configuration()
    {
        registerParser("json", new ConfigurationParserJson());
    }

    private final String CONFIG_FILE_TAILFIX = "root.json";

    private static Configuration DEFAULT_INSTANCE = new Configuration();

    public static void autoInitialize()
    {
        Path currentRelativePath = Paths.get("");
        String path = currentRelativePath.toAbsolutePath().toString() + currentRelativePath.getFileSystem().getSeparator() + "config";
        String separator = currentRelativePath.getFileSystem().getSeparator();
        String[] split = path.split(separator);
        int endIndex = 0;
        String fixPath = separator;
        for (int i = 0; i < split.length - 1; i++)
        {
            fixPath += split[i] + separator;
            if (split[i].equalsIgnoreCase("cell"))
            {
                endIndex = i;
                break;
            }
        }
        if (endIndex == 0)
        {
            throw new ProgramaException("asd");
        }
        fixPath += "config" + separator;
        try
        {
            DEFAULT_INSTANCE.initialize(fixPath, "Default");
        } catch (ConfigurationException e)
        {
            throw new ProgramaException(e);
        }
    }

    public static Configuration getDefault()
    {
        return DEFAULT_INSTANCE;
    }

    public void initialize(String repoRoot) throws ConfigurationException
    {
        initialize(repoRoot, null, null);
    }

    public void initialize(String repoRoot, String configType) throws ConfigurationException
    {
        initialize(repoRoot, configType, null);
    }

    public void initialize(String repoRoot, String configType, EventExecutor refreshChecker)
            throws ConfigurationException
    {
        synchronized (this)
        {
            if (initialized)
            {
                throw new IllegalStateException("Configuration has already been initliazed");
            }
            try
            {
                this.repoRoot = FileUtils.getAbsolutePath(repoRoot);
                if (this.repoRoot == null)
                {
                    throw new ConfigurationException("Repo root: " + repoRoot + " is not exist");
                }
                Map<String, RootConfig> repos = new HashMap<>();
                String rootConfigPath = this.repoRoot + "/" + CONFIG_FILE_TAILFIX;
                RootConfig rootConfig = JSONUtil.jsonFileToObj(rootConfigPath, RootConfig.class);

                this.configType = configType == null || configType.length() == 0 ? rootConfig.getDefaultType()
                        : configType;
                if (this.configType == null)
                {
                    throw new ConfigurationException("Default config type is not specified.");
                }

                repos.put(this.repoRoot, rootConfig);
                HashMap<String, String> types = rootConfig.getConfigTypes();
                if (!types.containsKey(this.configType))
                {
                    throw new ConfigurationException("Config type not exist.");
                }

                Set<String> pluginPaths = rootConfig.getPluginPaths(rootConfigPath);
                for (String pluginPath : pluginPaths)
                {
                    if (!repos.containsValue(pluginPath))
                    {
                        repos.put(pluginPath,
                                JSONUtil.jsonFileToObj(pluginPath + "/" + CONFIG_FILE_TAILFIX, RootConfig.class));
                    }
                }

                List<String> inheritance = buildInheritanceList(types);

                buildModulePathMap(repos, inheritance);

                if (refreshChecker == null)
                {
                    refreshChecker = new DefaultEventExecutor();
                }

                configTypes = new ArrayList<>(types.keySet());

                configRefresher = new ConfigRefresher(refreshChecker, REFRESH_CHECK_INTERVAL_SECONDS, this);
                configRefresher.start();

                initialized = true;

            } catch (ConfigurationException e)
            {
                throw e;
            } catch (Throwable e)
            {
                uninitialize();
                throw new ConfigurationException(e);
            }
        }
    }

    public List<String> getAvailableConfigTypes()
    {
        return configTypes;
    }

    public Map<String, String> getConfigModulePaths()
    {
        Map<String, String> ret = new HashMap<>();

        for (Map.Entry<String, ConfigModule> entry : modules.entrySet())
        {
            ret.put(entry.getKey(), entry.getValue().getModuleFullPath());
        }

        return ret;
    }

    public Map<String, String> getConfigModuleDuePaths()
    {
        Map<String, String> ret = new HashMap<>();

        for (Map.Entry<String, ConfigModule> entry : modules.entrySet())
        {
            ret.put(entry.getKey(), entry.getValue().getModuleDueFullPath());
        }

        return ret;
    }

    public boolean isInitialized()
    {
        synchronized (this)
        {
            return initialized;
        }
    }

    public String getConfigType()
    {
        return configType;
    }

    public String getRepoRoot()
    {
        return repoRoot;
    }

    public void uninitialize()
    {
        synchronized (this)
        {
            modules.clear();
            configType = null;
            repoRoot = null;
            initialized = false;

            if (configRefresher != null)
            {
                configRefresher.close();
            }
            configRefresher = null;
        }
    }

    public void registerModuleListener(String moduleName, IConfigListener listener) throws IOException
    {
        ConfigModule module = getModule(moduleName);

        if (module.getModuleFullPath() == null)
        {
            throw new IOException("no valid config file for module [" + moduleName + "]");
        }

        IConfigurationParser parser = getParser(module.getSchema());
        configRefresher.registerListener(moduleName, module.getModuleFullPath(), listener, parser);
    }

    public final ConfigModule getModule(String moduleName)
    {
        ConfigModule module = modules.get(moduleName);
        if (module == null)
        {
            throw new IllegalArgumentException("Module name [" + moduleName + "] is not exist.");
        }
        return module;
    }

    public final ConfigModule loadModule(String moduleName) throws IOException
    {
        ConfigModule module = modules.get(moduleName);
        if (module == null)
        {
            throw new IllegalArgumentException("Module name [" + moduleName + "] is not exist.");
        }

        if (module.getConfigValue() == null)
        {

            switch (module.getSchema())
            {
                case "json":
                    module.setConfigValue(getConfigObject(ConfigValueJson.class, moduleName));
                    break;
            }
        }

        return module;
    }

    public void addRefreshExceptionListener(IRefreshExceptionListener listener)
    {
        configRefresher.addExceptionListener(listener);
    }

    public IConfigListener unregisterModuleListener(String moduleName)
    {
        return configRefresher.unregisterListener(moduleName);
    }

    public void registerParser(String schema, IConfigurationParser parser)
    {
        configurationParsers.put(schema, parser);
    }

    public IConfigurationParser getParser(String schema)
    {
        IConfigurationParser parser = configurationParsers.get(schema);
        if (parser == null)
        {
            throw new IllegalArgumentException("Parser for schema [" + schema + "]is not exist;");
        }

        return parser;
    }

    public IConfigurationParser unregisterParser(String schema)
    {
        return configurationParsers.remove(schema);
    }

    public void clearCache(String moduleName)
    {
        getModule(moduleName).setConfigValue(null);
    }

    public List<String> getConfigModules(String schema)
    {
        String realSchema = schema == null ? "" : schema;
        ArrayList<String> configModules = configModulesMap.get(realSchema);
        if (configModules != null)
        {
            return configModules;
        }

        configModules = new ArrayList<>();

        for (Entry<String, ConfigModule> entry : modules.entrySet())
        {
            if (realSchema == "")
            {
                configModules.add(entry.getKey());
            } else if (realSchema == entry.getValue().getSchema())
            {
                configModules.add(entry.getKey());
            }
        }

        configModulesMap.put(schema, configModules);
        return configModules;
    }

    private List<String> buildInheritanceList(Map<String, String> configTypes)
    {
        List<String> inheritance = new ArrayList<>();
        inheritance.add(configType);

        for (String parent = configTypes.get(configType); parent != null; parent = configTypes.get(parent))
        {
            inheritance.add(0, parent);
        }
        return inheritance;
    }

    private void buildModulePathMap(Map<String, RootConfig> repoMap, List<String> configTypeInheritance)
            throws ConfigurationException
    {
        for (Entry<String, RootConfig> entry : repoMap.entrySet())
        {

            Map<String, ConfigModule> pluginModules = entry.getValue().getModules();
            for (Entry<String, ConfigModule> module : pluginModules.entrySet())
            {

                String moduleFilePathUnderConfigType = null;
                for (String type : configTypeInheritance)
                {
                    String tmpPath = FileUtils.getAbsolutePath(entry.getKey() + "/" + type + "/" + module.getValue().getModuleFullPath());
                    if (module.getValue().getModuleDueFullPath() == null)
                    {
                        module.getValue().setModuleDueFullPath(tmpPath);
                    }

                    if (tmpPath == null)
                    {
                        continue;
                    }

                    moduleFilePathUnderConfigType = tmpPath;
                }
                // if (moduleFilePathUnderConfigType == null)
                // throw new InvalidRepoException("No available config file for
                // module: " + module.getKey());

                module.getValue().setModuleFullPath(moduleFilePathUnderConfigType);
            }
            modules.putAll(pluginModules);
        }
    }

    public IConfigValue getConfigValue(String moduleName) throws IOException
    {

        ConfigModule module = getModule(moduleName);

        if (module.getConfigValue() != null)
        {
            return module.getConfigValue();
        }

        switch (module.getSchema())
        {
            case "json":
                module.setConfigValue(getConfigObject(ConfigValueJson.class, moduleName));
                break;
        }

        return module.getConfigValue();
    }

    public <T> T getConfigObject(Class<T> clazz, String moduleName) throws IOException
    {
        return getConfigObject(clazz, moduleName, null);
    }

    public <T> T getConfigObject(Class<T> clazz, String moduleName, Object userData) throws IOException
    {
        return buildConfigObject(clazz, moduleName, getModule(moduleName), userData);
    }

    private <T> T buildConfigObject(Class<T> clazz, String moduleName, ConfigModule module, Object userData)
            throws IOException
    {

        IConfigurationParser parser = configurationParsers.get(module.getSchema());
        if (parser == null)
        {
            return null;
        }

        if (module.getModuleFullPath() == null)
        {
            throw new IOException("no valid config file for module [" + moduleName + "]");
        }

        return parser.parseFrom(this, clazz, moduleName, module.getModuleFullPath(), userData);
    }

    public <T> T getAndMonitorConfig(String moduleName, Class<T> t, ILoadConfigListener<T> configListener)
    {
        try
        {
            IConfigValue configValue;
            configValue = Configuration.getDefault().getConfigValue(moduleName);
            T obj = configValue.asObject(t);
            if (configListener != null)
            {
                IConfigListener listener = (modeul) ->
                {
                    IConfigValue configValue2 = Configuration.getDefault().getConfigValue(moduleName);
                    T obj2 = configValue2.asObject(t);
                    configListener.load(obj2);
                    LOG.info(Module.CONFIGURATION, "刷新配置文件成功, moduleName = {}, value = {}", moduleName, JSONUtil.toJsonString(obj2));
                };
                Configuration.getDefault().registerModuleListener(moduleName, listener);
            }
            LOG.info(Module.CONFIGURATION, "Configuration for module [{}] has been loaded: {}", moduleName, JSONUtil.toJsonString(obj));
            return obj;
        } catch (Exception e)
        {
//			LOG.error(Module.COMMON, e, "读取配置文件失败， moduleName = {}", moduleName);
            throw new RuntimeException(e);
        }
    }

    public IConfigValue getAndMonitorConfig(String moduleName, ILoadConfigListener<IConfigValue> configListener)
    {
        try
        {
            IConfigValue configValue;
            configValue = Configuration.getDefault().getConfigValue(moduleName);
            if (configListener != null)
            {
                IConfigListener listener = (modeul) ->
                {
                    IConfigValue configValue2 = Configuration.getDefault().getConfigValue(moduleName);
                    configListener.load(configValue2);
                    LOG.info(Module.CONFIGURATION, "刷新配置文件成功：{}", configValue2);
                };
                Configuration.getDefault().registerModuleListener(moduleName, listener);
            }
            LOG.info(Module.CONFIGURATION, "Configuration for module [{}] has been loaded: {}", moduleName, configValue);
            return configValue;
        } catch (Exception e)
        {
            LOG.error(Module.COMMON, e, "读取配置文件失败， moduleName = {}", moduleName);
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws ConfigurationException
    {
    }
}
