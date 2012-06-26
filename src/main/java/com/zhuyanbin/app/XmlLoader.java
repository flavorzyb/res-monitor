package com.zhuyanbin.app;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XmlLoader
{
	private String _xmlFile;
	private Document _doc;
	
    public XmlLoader()
    {
    }

	public XmlLoader(String xmlFile)
	{
		setXmlFilePath(xmlFile);
	}
	
	public void setXmlFilePath(String xmlFile)
	{
		_xmlFile = xmlFile;
	}
	
	public String getXmlFilePath()
	{
		return _xmlFile;
	}
	
	public void load() throws IOException, IllegalArgumentException, ParserConfigurationException, SAXException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = factory.newDocumentBuilder();
		_doc = db.parse(getXmlFilePath());
	}
	
	public void load(String xmlFile) throws IOException, IllegalArgumentException, ParserConfigurationException, SAXException
	{
		setXmlFilePath(xmlFile);
		load();
	}
	
	public Document getDocument()
	{
		return _doc;
	}
}
