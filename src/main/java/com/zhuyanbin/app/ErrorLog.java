package com.zhuyanbin.app;

import java.io.FileOutputStream;
import java.sql.Timestamp;
import java.util.Date;

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
        try
        {
            Date dt = new Date();
            Timestamp ts = new Timestamp(dt.getTime());
            String msg = ts + "|" + str + "\n";
            FileOutputStream fos = new FileOutputStream(getLogPath(), true);
            fos.write(msg.getBytes());
            fos.flush();
            fos.close();
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
