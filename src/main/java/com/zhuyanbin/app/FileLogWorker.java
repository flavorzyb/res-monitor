package com.zhuyanbin.app;

public class FileLogWorker extends Thread
{
    private String _logPath;
    private String _redoLogPath;

    public FileLogWorker(String logPath, String redoLogPath)
    {
        setLogPath(logPath);
        setRedoLogPath(redoLogPath);
    }

    private void setLogPath(String logPath)
    {
        _logPath = logPath;
    }

    public String getLogPath()
    {
        return _logPath;
    }

    private void setRedoLogPath(String logPath)
    {
        _redoLogPath = logPath;
    }

    public String getRedoLogPath()
    {
        return _redoLogPath;
    }

    @Override
    public void start()
    {
    }
}
