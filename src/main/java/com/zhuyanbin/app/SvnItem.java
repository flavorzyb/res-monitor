package com.zhuyanbin.app;

public class SvnItem
{
    private String        _path;
    private boolean _isAdd = false;
    private boolean _isFile = false;

    public SvnItem(String path)
    {
        setPath(path);
    }

    public SvnItem(String path, boolean isAdd)
    {
        setPath(path);
        setIsAdd(isAdd);
    }

    public SvnItem(String path, boolean isAdd, boolean isFile)
    {
        setPath(path);
        setIsAdd(isAdd);
        setIsFile(isFile);
    }

    private void setPath(String path)
    {
        _path = path;
    }

    public String getPath()
    {
        return _path;
    }

    private void setIsAdd(boolean isAdd)
    {
        _isAdd = isAdd;
    }

    public boolean isAdd()
    {
        return _isAdd;
    }

    private void setIsFile(boolean isFile)
    {
        _isFile = isFile;
    }

    public boolean isFile()
    {
        return _isFile;
    }
}
