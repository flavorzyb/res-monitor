package com.zhuyanbin.app;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ConfigProxy 
{
	private XmlLoader _xmlLoader;
	
	public ConfigProxy()
	{
	}
	
	public ConfigProxy(String xmlFile)
	{
		setXmlLoader(new XmlLoader(xmlFile));
	}

	private void setXmlLoader(XmlLoader ld)
	{
		_xmlLoader = ld;
	}
	
	private XmlLoader getXmlLoader()
	{
		return _xmlLoader;
	}
	
    private Config createConfigVO()
    {
        Config result = new Config();
        Document doc = getXmlLoader().getDocument();

        result.setSourcePath(doc.getElementsByTagName("sourcepath").item(0).getAttributes().getNamedItem("path").getNodeValue());
        result.setDestPath(doc.getElementsByTagName("destpath").item(0).getAttributes().getNamedItem("path").getNodeValue());

        NodeList nl = doc.getElementsByTagName("svn").item(0).getChildNodes();
        int len = nl.getLength();
        String name;

        for (int i = 0; i < len; i++)
        {
            name = nl.item(i).getNodeName();
            if ("bin" == name)
            {
                result.setSvnBinPath(nl.item(i).getAttributes().getNamedItem("path").getNodeValue());
            }
            else if ("params" == name)
            {
                result.setSvnParams(nl.item(i).getAttributes().getNamedItem("param").getNodeValue());
            }
        }

        nl = doc.getElementsByTagName("logs").item(0).getChildNodes();
        len = nl.getLength();

        for (int i = 0; i < len; i++)
        {
            name = nl.item(i).getNodeName();
            if ("logpath" == name)
            {
                result.setLogPath(nl.item(i).getAttributes().getNamedItem("path").getNodeValue());
            }
            else if ("redolog" == name)
            {
                result.setRedoLogPath(nl.item(i).getAttributes().getNamedItem("path").getNodeValue());
            }
        }

        return result;
    }

    public Config load() throws IOException, IllegalArgumentException, ParserConfigurationException, SAXException
    {

        if (null == getXmlLoader())
        {
            throw new IllegalArgumentException("xml file path can not be null.");
        }
        getXmlLoader().load();

        return createConfigVO();
	}
	
    public Config load(String xmlFile) throws IOException, IllegalArgumentException, ParserConfigurationException, SAXException
	{

		if (null == getXmlLoader())
		{
			setXmlLoader(new XmlLoader(xmlFile));
		}
		else
		{
			getXmlLoader().setXmlFilePath(xmlFile);
		}
		
        return load();
	}
}
