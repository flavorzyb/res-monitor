package com.zhuyanbin.app;

import java.sql.Timestamp;
import java.util.Date;

import net.contentobjects.jnotify.JNotifyListener;

public class Listener implements JNotifyListener
{
    private String _logFile;

    public Listener(String logPath)
    {
        setLogFile(logPath);
    }

    private void setLogFile(String logPath)
    {
        _logFile = logPath;
    }

    public String getLogFile()
    {
        return _logFile;
    }

    @Override
    public void fileRenamed(int wd, String rootPath, String oldName, String newName)
    {
        writeLog(rootPath, newName);
    }

    @Override
    public void fileModified(int wd, String rootPath, String name)
    {
        writeLog(rootPath, name);
    }

    @Override
    public void fileDeleted(int wd, String rootPath, String name)
    {
    }

    @Override
    public void fileCreated(int wd, String rootPath, String name)
    {
        writeLog(rootPath, name);
    }
    
    protected void writeFile(String filePath, String msg)
    {
        Loger.write(filePath, msg);
    }

    protected void writeLog(String rootPath, String newName)
    {
        Date dt = new Date();
        Timestamp ts = new Timestamp(dt.getTime());
        String msg = ts + "|" + newName + "\n";
        writeFile(getLogFile(), msg);
    }
}
