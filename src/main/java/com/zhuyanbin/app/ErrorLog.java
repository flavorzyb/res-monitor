package com.zhuyanbin.app;


public class ErrorLog
{
    private static ErrorLog _instance = null;

    private String _logPath;
    protected ErrorLog()
    {
    }

    public static ErrorLog getInstance()
    {
        if (null == _instance)
        {
            _instance = new ErrorLog();
        }

        return _instance;
    }
    
    public void setLogPath(String logPath)
    {
        _logPath = logPath;
    }

    public String getLogPath()
    {
        return _logPath;
    }

    public void write(String str)
    {
        Loger.write(getLogPath(), str);
    }
}
