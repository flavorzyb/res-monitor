package com.zhuyanbin.app;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

public class FullSync
{
    public static final long MAX_SLEEP_TIME = 900000;
    private String _workCopyPath;
    private String _sourcePath;
    private String _logPath;
    private long   _lastRunTime = 0;

    public FullSync(String wcPath, String sourcePath, String logPath)
    {
        setWorkCopyPath(wcPath);
        setSourcePath(sourcePath);
        setLogPath(logPath);
    }

    protected void setWorkCopyPath(String wcPath)
    {
        _workCopyPath = wcPath;
    }

    public String getWorkCopyPath()
    {
        return _workCopyPath;
    }

    protected void setSourcePath(String path)
    {
        _sourcePath = path;
    }
    
    public String getSourcePath()
    {
        return _sourcePath;
    }

    protected void setLogPath(String path)
    {
        _logPath = path;
    }

    public String getLogPath()
    {
        return _logPath;
    }

    protected void setLastRunTime(long time)
    {
        _lastRunTime = time;
    }

    public boolean rsync()
    {
        boolean result = false;
        Date d = new Date();
        long currTime = d.getTime();
        if (isNeedRsync(currTime))
        {
            setLastRunTime(currTime);
            work(getSourcePath());
            result = true;
        }
        return result;
    }

    protected void work(String path)
    {
        File fp = new File(path);
        if (fp.isDirectory())
        {
            File[] files = fp.listFiles();
            if (null != files)
            {
                int len = files.length;
                for (int i = 0; i < len; i++)
                {
                    if (files[i].isDirectory())
                    {
                        work(files[i].getPath());
                    }
                    else
                    {
                        int sourceLen = getSourcePath().length();
                        int fLen = files[i].getPath().length();
                        if (fLen > sourceLen)
                        {
                            String cPath = files[i].getPath().substring(sourceLen + 1);
                            try
                            {
                                if (!Md5CheckSum.md5StringIsSame(getSourcePath() + "/" + cPath, getWorkCopyPath() + "/" + cPath))
                                {
                                    writeLog(cPath);
                                }
                            }
                            catch (NoSuchAlgorithmException ex)
                            {
                                ex.printStackTrace();
                            }
                            catch (IOException ex)
                            {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }

        fp = null;
    }

    public boolean isNeedRsync(long ctime)
    {
        return ((ctime - getLastRunTime()) > MAX_SLEEP_TIME);
    }

    public long getLastRunTime()
    {
        return _lastRunTime;
    }
    
    protected void writeLog(String fileName)
    {
        Loger.write(getLogPath(), fileName);
    }
}
