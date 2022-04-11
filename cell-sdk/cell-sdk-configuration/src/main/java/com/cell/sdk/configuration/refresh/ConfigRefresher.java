package com.cell.sdk.configuration.refresh;

import com.cell.sdk.configuration.Configuration;
import com.cell.sdk.configuration.parser.IConfigurationParser;
import io.netty.util.concurrent.EventExecutor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class ConfigRefresher implements Runnable
{

    private EventExecutor executor;
    private ConcurrentHashMap<String, ConfigRefreshData> configurationListeners = new ConcurrentHashMap<>();
    private Configuration configuration;
    private List<IRefreshExceptionListener> exceptionListeners;

    class ConfigRefreshData
    {
        List<IConfigListener> listener;
        long lastEditDate;
        File file;
        IConfigurationParser parser;

        public ConfigRefreshData()
        {
            super();
            listener = new ArrayList<>();
        }

        public List<IConfigListener> getListener()
        {
            return listener;
        }

        public void setListener(List<IConfigListener> listener)
        {
            this.listener = listener;
        }

        public long getLastEditDate()
        {
            return lastEditDate;
        }

        public void setLastEditDate(long lastEditDate)
        {
            this.lastEditDate = lastEditDate;
        }

        public File getFile()
        {
            return file;
        }

        public void setFile(File file)
        {
            this.file = file;
        }

        public IConfigurationParser getParser()
        {
            return parser;
        }

        public void setParser(IConfigurationParser parser)
        {
            this.parser = parser;
        }
    }

    private int checkIntervalSeconds;
    private volatile boolean running;

    public ConfigRefresher(EventExecutor executor, int checkIntervalSeconds, Configuration configuration)
    {
        this.executor = executor;
        this.checkIntervalSeconds = checkIntervalSeconds;
        this.configuration = configuration;
        this.exceptionListeners = new ArrayList<>();
    }

    @Override
    public void run()
    {
        if (configurationListeners.isEmpty())
        {
            synchronized (this)
            {
                if (running)
                {
                    executor.schedule(this, checkIntervalSeconds, TimeUnit.SECONDS);
                }
            }
            return;
        }

        try
        {
            for (Map.Entry<String, ConfigRefreshData> entry : configurationListeners.entrySet())
            {
                try
                {
                    long lastEdit = entry.getValue().getFile().lastModified();
                    if (lastEdit > entry.getValue().getLastEditDate())
                    {
                        entry.getValue().setLastEditDate(lastEdit);
                        configuration.clearCache(entry.getKey());
                        entry.getValue().getParser().clearCache(entry.getKey());
//						LOG.debug(Module.CONFIGURATION, "Configuration for module [%s] is refreshed.",
//								entry.getKey());
                        for (IConfigListener listener : entry.getValue().getListener())
                        {
                            try
                            {
                                listener.configRefreshed(entry.getKey());
                            } catch (Throwable e)
                            {
                                for (IRefreshExceptionListener exceptionListener : exceptionListeners)
                                {
                                    exceptionListener.exception(entry.getKey(), e);
                                }
//								LOG.warning(Module.CONFIGURATION, e, "Exception caught in config listener.");
                            }
                        }
                    }
                } catch (Throwable e)
                {
//					LOG.warning(Module.CONFIGURATION, e, "Exception caught checking last edit time of [%s].",
//							entry.getKey());
                }
            }
        } finally
        {
            synchronized (this)
            {
                if (running)
                {
                    executor.schedule(this, checkIntervalSeconds, TimeUnit.SECONDS);
                }
            }
        }
    }

    public void start()
    {
        synchronized (this)
        {
            if (running)
            {
                return;
            }
            running = true;
            run();
        }
    }

    public void close()
    {
        synchronized (this)
        {
            running = false;
        }
    }

    public void registerListener(String moduleName, String filePath, IConfigListener configListener,

                                 IConfigurationParser parser)
    {
        synchronized (this)
        {
            ConfigRefreshData data = configurationListeners.get(moduleName);
            if (data == null)
            {
                configurationListeners.put(moduleName, data = new ConfigRefreshData());
            }

            // TODO : Do not check all files at same times.
            data.setFile(new File(filePath));
            data.getListener().add(configListener);
            data.setLastEditDate(data.getFile().lastModified());
            data.setParser(parser);

        }
    }

    public IConfigListener unregisterListener(String moduleName)
    {
        synchronized (this)
        {
            ConfigRefreshData refreshData = configurationListeners.remove(moduleName);
            if (refreshData == null)
            {
                return null;
            }
            return refreshData.getListener().get(0);
        }
    }

    public void addExceptionListener(IRefreshExceptionListener listener)
    {
        exceptionListeners.add(listener);
    }

    public void removeExceptionListener(IRefreshExceptionListener listener)
    {
        exceptionListeners.remove(listener);
    }

}
