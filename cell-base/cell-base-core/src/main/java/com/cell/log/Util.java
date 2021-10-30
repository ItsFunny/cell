package com.cell.log;

import ch.qos.logback.classic.Level;
import com.cell.utils.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-04 16:05
 */
public class Util
{
    public static String getAbsLogFolder(String folder)
    {
        if (StringUtils.isNullEmpty(folder))
        {
            folder = System.getProperty("sun.java.command");
            if (folder.indexOf("apache") != -1)
            {
                folder = "logs/tomcat_logs_" + System.currentTimeMillis();
            } else
            {

                int spaceIndex = folder.indexOf(' ');
                if (spaceIndex != -1)
                {
                    folder = folder.substring(0, spaceIndex);
                }

                int slashIndex = folder.lastIndexOf('/');
                if (slashIndex != -1)
                {
                    folder = folder.substring(slashIndex + 1, folder.length());
                }

                slashIndex = folder.lastIndexOf('\\');
                if (slashIndex != -1)
                {
                    folder = folder.substring(slashIndex + 1, folder.length());
                }

                if (folder.isEmpty())
                {
                    folder = "logs/vwvo_" + System.currentTimeMillis();
                } else
                {
                    folder = "logs/" + folder;
                }

                folder = folder.replace('.', '_');
            }
        }
        return getAbsolutePathRelated(".", folder);
    }

    public static Level logLevel2LogBackLevel(LogLevel l)
    {
        switch (l)
        {
            case DEBUG:
                return Level.DEBUG;
            case INFO:
                return Level.INFO;
            case WARN:
                return Level.WARN;
            case ERROR:
                return Level.ERROR;
            default:
                return Level.INFO;
        }
    }
    /**
     * 返回一个相对于relatedPath的路径 如果path为绝对路径，那么使用path
     * 如果path为相对路径，那么返回相对于releatedPath的绝对路径 *
     */
    public static String getAbsolutePathRelated(String relatedPath, String path)
    {
        if (new File(path).isAbsolute())
        {
            return path;
        }

        File relatedFile = new File(relatedPath);
        if (relatedFile.isDirectory())
        {
            try
            {
                return new File(relatedFile + "/" + path).getCanonicalPath();
            } catch (IOException e)
            {
                return null;
            }
        } else
        {
            try
            {
                return new File(relatedFile.getParentFile() + "/" + path).getCanonicalPath();
            } catch (IOException e)
            {
                return null;
            }
        }
    }

}
