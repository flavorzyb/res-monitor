package com.zhuyanbin.app;

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
        print("renamed " + rootPath + " : " + oldName + " -> " + newName);
    }

    @Override
    public void fileModified(int wd, String rootPath, String name)
    {
        print("modified " + rootPath + " : " + name);
    }

    @Override
    public void fileDeleted(int wd, String rootPath, String name)
    {
        print("deleted " + rootPath + " : " + name);
    }

    @Override
    public void fileCreated(int wd, String rootPath, String name)
    {
        print("created " + rootPath + " : " + name);
    }

    private void writeLog(String msg)
    {
    }

    void print(String msg)
    {
        System.err.println(msg);
    }
}
