package com.cell.shell.impl;

import com.cell.shell.Shell;

public class UnixShellBuilder extends AbstractShellBuilder
{
    @Override
    public Shell buildTextShell(String text) throws Exception
    {
        LOGGER.info("开始调用命令:{}", text);
        if (!text.startsWith("#!"))
        {
            text = "#!/bin/bash\n" + text;
        }
        String file = createFile(text);
        return Shell.build("bash", file);
    }
}
