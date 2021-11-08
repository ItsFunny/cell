package com.cell.base.core.log.factory;

import com.cell.base.core.log.Util;
import com.cell.base.core.log.config.Log4j2ConfigBuilder;
import org.apache.logging.log4j.core.LoggerContext;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-04 22:45
 */
public class Factory
{
    private static String logFileName = null;

    static
    {
        try
        {
            String logFolder = Util.getAbsLogFolder(null);
            refreshLog4j(Arrays.asList(logFolder), logFileName);
        } catch (Exception e)
        {
            throw e;
        }
    }

    private static void refreshLog4j(List<String> newAbsFolder, String logFileName)
    {
        try
        {

            File fakeLog4jConfig = File.createTempFile("log4j", ".tmp");

            FileWriter fileWriter = null;
            fileWriter = new FileWriter(fakeLog4jConfig, true);
            fileWriter.write(getLog4jConfigString(newAbsFolder, logFileName));
            fileWriter.close();
            // Configurator.initialize("test",
            // fakeLog4jConfig.getAbsolutePath());
            System.setProperty("log4j.configurationFile", fakeLog4jConfig.getAbsolutePath());
            LoggerContext.getContext(false).reconfigure();
            LoggerContext.getContext(false).updateLoggers();


            fakeLog4jConfig.delete();
        } catch (Throwable e)
        {
        }
    }

    private static String getLog4jConfigString(List<String> folder, String logFileName)
    {
        String str = Log4j2ConfigBuilder.getInstance().setLogFolders(folder, logFileName).build();
        // System.out.println(str);
        return str;
    }
}
