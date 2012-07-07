package com.zhuyanbin.app;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.xml.sax.SAXException;

public class ConfigProxyTest extends TestCase 
{
	private ConfigProxy classRelection;
	private final String xmlFile = "src/test/resource/config.xml";
	@Override
    protected void setUp() throws Exception 
	{
		super.setUp();
		classRelection = new ConfigProxy();
	}

	@Override
    protected void tearDown() throws Exception 
	{
		classRelection = null;
		super.tearDown();
	}

	public void testConfigProxyString() 
	{
		classRelection = new ConfigProxy(xmlFile);
	}

	public void testLoadFail() 
	{
		// xml file is not setting
		try
		{
			classRelection.load();
			fail("can not catch Exception");
		}
		catch(IOException ex)
		{
			fail(ex.getMessage());
		}
		catch(IllegalArgumentException ex)
		{
			assertTrue(ex instanceof IllegalArgumentException);
		}
		catch(ParserConfigurationException ex)
		{
			fail(ex.getMessage());
		}
		catch(SAXException ex)
		{
			fail(ex.getMessage());
		}
		
		// xml file is not exist
		try
		{
			classRelection.load("../asdfasfsd/sdfsdf.xml");
			fail("can not catch Exception");
		}
		catch(IOException ex)
		{
			assertTrue(ex instanceof IOException);
		}
		catch(IllegalArgumentException ex)
		{
			fail(ex.getMessage());
		}
		catch(ParserConfigurationException ex)
		{
			fail(ex.getMessage());
		}
		catch(SAXException ex)
		{
			fail(ex.getMessage());
		}
	}
	
	public void testLoadAndGetConfig() 
	{
	    Config conf = null;
		try
		{
            conf = classRelection.load(xmlFile);
            assertTrue(conf instanceof Config);
            conf = classRelection.load(xmlFile);
            checkConfig(conf);
		}
		catch(IOException ex)
		{
			fail(ex.getMessage());
		}
		catch(IllegalArgumentException ex)
		{
			fail(ex.getMessage());
		}
		catch(ParserConfigurationException ex)
		{
			fail(ex.getMessage());
		}
		catch(SAXException ex)
		{
			fail(ex.getMessage());
		}
		
		classRelection = new ConfigProxy(xmlFile);
		try
		{
            conf = classRelection.load(xmlFile);
            assertTrue(conf instanceof Config);
            checkConfig(conf);
		}
		catch(IOException ex)
		{
			fail(ex.getMessage());
		}
		catch(IllegalArgumentException ex)
		{
			fail(ex.getMessage());
		}
		catch(ParserConfigurationException ex)
		{
			fail(ex.getMessage());
		}
		catch(SAXException ex)
		{
			fail(ex.getMessage());
		}
	}

    private void checkConfig(Config conf)
    {
        Config result = new Config();
        result.setSourcePath("src/main");
        result.setDestPath("src/test");
        result.setUserName("test");
        result.setPassword("password");
        result.setLogPath("/data1/resmonitor/resmonitor.log");
        result.setDoingLogPath("/data1/resmonitor/resmonitor_doing.log");
        result.setErrorLogPath("/data1/resmonitor/resmonitor_error.log");
        result.setPidPath("/data1/resmonitor/resmonitor.pid");
        result.setRedoLogPath("/data1/resmonitor/resmonitor_redo.log");

        assertEquals(result.getSourcePath(), conf.getSourcePath());
        assertEquals(result.getDestPath(), conf.getDestPath());
        assertEquals(result.getUserName(), conf.getUserName());

        assertEquals(result.getPassword(), conf.getPassword());
        assertEquals(result.getLogPath(), conf.getLogPath());
        assertEquals(result.getDoingLogPath(), conf.getDoingLogPath());
        assertEquals(result.getErrorLogPath(), conf.getErrorLogPath());
        assertEquals(result.getPidPath(), conf.getPidPath());
        assertEquals(result.getRedoLogPath(), conf.getRedoLogPath());
    }
}
