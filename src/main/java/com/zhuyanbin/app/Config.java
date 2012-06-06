package com.zhuyanbin.app;
import java.io.File;
import java.lang.Exception;

/**
 * 配置
 * @author flavor
 *
 */
public class Config 
{
	private String _xmlFile;
	
	public Config()
	{
	}
	
	public Config(String xmlFile) throws SecurityException, Exception
	{
		setXmlFilePath(xmlFile);
	}
	
	private void setXmlFilePath(String xmlFile) throws SecurityException, Exception
	{
		File fp = new File(xmlFile);
		if (!fp.isFile())
		{
			throw new Exception("config xml file is not a normal file.");
		}
		
		_xmlFile = xmlFile;
	}
	
	private String getXmlFilePath()
	{
		return _xmlFile;
	}
	
	public void load() throws Exception
	{
		if (null == getXmlFilePath())
		{
			throw new Exception("config xml file path can not be null.");
		}
	}
	
	public void load(String xmlFile) throws Exception
	{
		setXmlFilePath(xmlFile);
		load();
	}
}
