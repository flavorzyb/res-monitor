package com.zhuyanbin.app;

public class FileRedoWorker extends Thread
{
    private String _redoLogPath;
    private String _logPath;

    public FileRedoWorker(String redoLogPath, String logPath)
    {
        setRedoLogPath(redoLogPath);
        setLogPath(logPath);
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
