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
	private String _svnBinPath;
	private String _svnParams;
	private String _logPath;
    private String _doingLogPath;
	private String _redoLogPath;

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
	
	public void setSvnBinPath(String path)
	{
		_svnBinPath = path;
	}
	
	public String getSvnBinPath()
	{
		return _svnBinPath;
	}
	
	public void setSvnParams(String params)
	{
		_svnParams = params;
	}
	
	public String getSvnParams()
	{
		return _svnParams;
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

	public String getRedoLogPath() 
	{
		return _redoLogPath;
	}

	public void setRedoLogPath(String redoLogPath) 
	{
		_redoLogPath = redoLogPath;
	}
}
