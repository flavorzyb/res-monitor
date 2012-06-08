package com.zhuyanbin.app;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

public class FileNotifyThread
{
    private String _path;

    private int     _wathId       = 0;

    private boolean _watchSubtree = false;
    
    public FileNotifyThread(String path)
    {
        setPath(path);
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

    public boolean getWatchSubtree()
    {
        return _watchSubtree;
    }

    public void addWatch() throws JNotifyException
    {

        // watch mask, specify events you care about,
        // or JNotify.FILE_ANY for all events.
        int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;

        // add actual watch
        _wathId = JNotify.addWatch(getPath(), mask, getWatchSubtree(), new Listener(""));
    }

    public boolean removeWatch() throws JNotifyException
    {
        return JNotify.removeWatch(_wathId);
    }
}
