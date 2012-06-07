package com.zhuyanbin.app;
import java.io.File;
import java.io.IOException;
import java.lang.Exception;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 配置
 * @author flavor
 *
 */
public class Config 
{
	private String _xmlFile;
	private String _sourcePath;
	private String _destpath;
	
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
			throw new Exception("config xml file(" + fp.getAbsolutePath() +") is not a normal file.");
		}
		
		_xmlFile = xmlFile;
	}
	
	private String getXmlFilePath()
	{
		return _xmlFile;
	}
	
	private void setSourcePath(String path)
	{
		_sourcePath = path;
	}
	
	public String getSourcePath()
	{
		return _sourcePath;
	}
	
	private void setDestPath(String path)
	{
		_destpath = path;
	}
	
	public String getDestPath()
	{
		return _destpath;
	}
	
	public void load() throws SecurityException, IOException, Exception
	{
		if (null == getXmlFilePath())
		{
			throw new Exception("config xml file path can not be null.");
		}
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		Document doc = db.parse(getXmlFilePath());
		setSourcePath(doc.getElementsByTagName("sourcepath").item(0).getAttributes().getNamedItem("path").getNodeValue());
		setDestPath(doc.getElementsByTagName("destpath").item(0).getAttributes().getNamedItem("path").getNodeValue());
	}
	
	public void load(String xmlFile) throws Exception
	{
		setXmlFilePath(xmlFile);
		load();
	}
}
