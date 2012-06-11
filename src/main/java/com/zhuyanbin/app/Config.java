package com.zhuyanbin.app;

/**
 * 配置
 * @author flavor
 *
 */
public class Config 
{
	private String _sourcePath;
	private String _destpath;
	private String _userName;
	private String _password;
	private String _logPath;
    private String _doingLogPath;
	private String _errorLogPath;

	public Config()
	{
	}
	
	public void setSourcePath(String path)
	{
		_sourcePath = path;
	}
	
	public String getSourcePath()
	{
		return _sourcePath;
	}
	
	public void setDestPath(String path)
	{
		_destpath = path;
	}
	
	public String getDestPath()
	{
		return _destpath;
	}
	
    public void setUserName(String username)
	{
        _userName = username;
	}
	
	public String getUserName()
	{
		return _userName;
	}
	
    public void setPassword(String password)
	{
        _password = password;
	}
	
	public String getPassword()
	{
		return _password;
	}
	
	public void setLogPath(String logPath)
	{
		_logPath = logPath;
	}
	
	public String getLogPath()
	{
		return _logPath;
	}
	
    public void setDoingLogPath(String logPath)
    {
        _doingLogPath = logPath;
    }

    public String getDoingLogPath()
    {
        return _doingLogPath;
    }

	public String getErrorLogPath() 
	{
		return _errorLogPath;
	}

	public void setErrorLogPath(String redoLogPath) 
	{
		_errorLogPath = redoLogPath;
	}
}
