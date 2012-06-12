package com.zhuyanbin.app;

public class SvnWorkConfig
{
    private String _userName;
    private String _password;
    private String _workCopy;

    public SvnWorkConfig(String workCopy, String username, String password)
    {
        setWorkCopyPath(workCopy);
        setUserName(username);
        setPassword(password);
    }
    private void setUserName(String username)
    {
        _userName = username;
    }

    public String getUserName()
    {
        return _userName;
    }

    private void setPassword(String password)
    {
        _password = password;
    }

    public String getPassword()
    {
        return _password;
    }

    private void setWorkCopyPath(String workCopy)
    {
        _workCopy = workCopy;
    }

    public String getWorkCopyPath()
    {
        return _workCopy;
    }
}
