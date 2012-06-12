package com.zhuyanbin.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileLogWorker extends Thread
{
    private final static int STATUS_IS_DOING = 0;

    private final static int STATUS_IS_SLEEP = 1;

    private String   _logPath;
    private String   _doingLogPath;
    private ErrorLog _errorLog;
    private boolean  _isLoop = true;
    private int              _status         = STATUS_IS_SLEEP;

    public FileLogWorker(String logPath, String doingLogPath, ErrorLog errorLog)
    {
        setLogPath(logPath);
        setDoingLogPath(doingLogPath);
        setErrorLog(errorLog);
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

    private void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public ErrorLog getErrorLog()
    {
        return _errorLog;
    }

    private void setStatus(int status)
    {
        switch (status)
        {
            case STATUS_IS_DOING:
            case STATUS_IS_SLEEP:
                break;
            default:
                status = getStatus();
        }

        _status = status;
    }

    public int getStatus()
    {
        return _status;
    }

    @Override
    public void run() throws IllegalThreadStateException
    {
        while (isLoop())
        {
            try
            {
                if (renameLogPath())
                {
                    setStatus(STATUS_IS_DOING);
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getDoingLogPath())));
                    String buf = null;
                    while (null != (buf = br.readLine()))
                    {
                        System.out.println(buf);
                    }

                    deleteDoingLog();
                }
                else
                {
                    setStatus(STATUS_IS_SLEEP);
                    sleep(1000);
                }
            }
            catch (Exception ex)
            {
                getErrorLog().write(ex.getMessage());
            }
        }
    }

    public void setIsLoop(boolean isLoop)
    {
        _isLoop = isLoop;
    }

    public boolean isLoop()
    {
        return _isLoop;
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

    private void deleteDoingLog() throws NullPointerException, SecurityException
    {
        File fp = new File(getDoingLogPath());
        if (fp.exists())
        {
            fp.delete();
        }
    }

    public boolean isSleep()
    {
        return (getStatus() == STATUS_IS_SLEEP);
    }
}
