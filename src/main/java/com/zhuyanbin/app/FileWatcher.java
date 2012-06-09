package com.zhuyanbin.app;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

public class FileWatcher
{
    private static final int INVAILD_WATCH_ID = -1;

    private String           _path;

    private int              _wathId          = INVAILD_WATCH_ID;

    private boolean          _watchSubtree    = false;
    
    private String           _logFile;

    public FileWatcher(String path, String logFile)
    {
        setPath(path);
        setLogFile(logFile);
    }

    public String getPath()
    {
        return _path;
    }

    private void setPath(String path)
    {
        _path = path;
    }

    public void setWathSubtree(boolean flag)
    {
        _watchSubtree = flag;
    }

    public boolean isWatchSubtree()
    {
        return _watchSubtree;
    }

    private void setLogFile(String logFile)
    {
        _logFile = logFile;
    }

    public String getLogFile()
    {
        return _logFile;
    }

    private void setWatchId(int wd)
    {
        _wathId = wd;
    }

    public int getWatchId()
    {
        return _wathId;
    }

    public void addWatch() throws JNotifyException
    {
        if (INVAILD_WATCH_ID != getWatchId())
        {
            return;
        }
        // watch mask, specify events you care about,
        // or JNotify.FILE_ANY for all events.
        int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;

        // add actual watch
        setWatchId(JNotify.addWatch(getPath(), mask, isWatchSubtree(), new Listener(getLogFile())));
    }

    public boolean removeWatch() throws JNotifyException
    {
        boolean res = false;
        try
        {
            res = JNotify.removeWatch(getWatchId());
        }
        catch (JNotifyException ex)
        {
            throw ex;
        }
        finally
        {
            setWatchId(INVAILD_WATCH_ID);
        }

        return res;
    }
}
