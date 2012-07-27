package com.zhuyanbin.app;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
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
            result = true;
        }

        return result;
    }

    public boolean isNeedRsync(long ctime)
    {
        return ((ctime - getLastRunTime()) > MAX_SLEEP_TIME);
    }

    public long getLastRunTime()
    {
        return _lastRunTime;
    }

    protected void writeLog(String rootPath, String newName)
    {
        try
        {
            Date dt = new Date();
            Timestamp ts = new Timestamp(dt.getTime());
            String msg = ts + "|" + newName + "\n";
            writeFile(getLogPath(), msg);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    protected void writeFile(String filePath, String msg) throws FileNotFoundException, SecurityException, IOException
    {
        FileOutputStream fos = new FileOutputStream(filePath, true);
        fos.write(msg.getBytes());
        fos.flush();
        fos.close();
    }
}
