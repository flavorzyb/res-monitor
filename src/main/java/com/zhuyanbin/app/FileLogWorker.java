package com.zhuyanbin.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileLogWorker extends Thread
{
    private String _logPath;
    private String _redoLogPath;
    private String _doingLogPath;

    public FileLogWorker(String logPath, String doingLogPath, String redoLogPath)
    {
        setLogPath(logPath);
        setRedoLogPath(redoLogPath);
        setDoingLogPath(doingLogPath);
    }

    private void setLogPath(String logPath)
    {
        _logPath = logPath;
    }

    public String getLogPath()
    {
        return _logPath;
    }

    private void setDoingLogPath(String logPath)
    {
        _doingLogPath = logPath;
    }

    public String getDoingLogPath()
    {
        return _doingLogPath;
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
    public void start() throws IllegalThreadStateException
    {
        while (true)
        {
            try
            {
                if (renameLogPath())
                {
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getDoingLogPath())));
                    String buf = null;
                    while (null != (buf = br.readLine()))
                    {
                        System.out.println(buf);
                    }
                }
                else
                {
                    Thread.sleep(1000);
                }
            }
            catch (Exception ex)
            {
            }
        }
    }

    private boolean renameLogPath() throws NullPointerException, SecurityException
    {
        boolean result = false;

        File doFp = new File(getDoingLogPath());
        if (doFp.exists())
        {
            return false;
        }

        File fp = new File(getLogPath());
        if (fp.isFile())
        {
            result = fp.renameTo(doFp);
        }

        return result;
    }
}
