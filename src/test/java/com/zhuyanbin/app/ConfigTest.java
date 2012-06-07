package com.zhuyanbin.app;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;

public class ConfigTest extends TestCase 
{
	private Config classRelection;
	private String xmlFile = "src/test/resource/config.xml";
	protected void setUp()
	{
		classRelection = new Config();
	}
	
	protected void tearDown()
	{
		classRelection = null;
	}
	
	public void testMutilConstruct()
	{
		assertTrue(classRelection instanceof Config);
		Config conf;
		
		try 
		{
			conf = new Config(xmlFile);
			assertTrue(conf instanceof Config);
		}
		catch (SecurityException e) 
		{
			fail(e.getMessage());
		}
		catch (Exception e) 
		{
			fail(e.getMessage());
		}
	}
	
	public void testLoad() 
	{
		try
		{
			classRelection.load();
			fail("cat not catch load Exception.");
		}
		catch(Exception ex)
		{
			assertEquals("config xml file path can not be null.", ex.getMessage());
		}
		
		File fp = new File("adfasdfsdfasde2fds/config.xml");
		
		try
		{
			classRelection.load("adfasdfsdfasde2fds/config.xml");
			fail("cat not catch load Exception.");
		}
		catch(Exception ex)
		{
			assertEquals("config xml file(" + fp.getAbsolutePath() +") is not a normal file.", ex.getMessage());
		}
		
		try
		{
			classRelection.load(xmlFile);
		}
		catch(Exception ex)
		{
			fail("can not catch Exception " + ex.getMessage());
		}
		
		try
		{
			classRelection = new Config(xmlFile);
			classRelection.load();
		}
		catch(SecurityException ex)
		{
			fail("can not catch SecurityException " + ex.getMessage());
		}
		catch(Exception ex)
		{
			fail("can not catch Exception " + ex.getMessage());
		}
	}
	
	public void testGetSourcePathAndGetDestPathReturnString()
	{
		assertNull(classRelection.getSourcePath());
		assertNull(classRelection.getDestPath());
		
		try
		{
			classRelection = new Config(xmlFile);
			classRelection.load();
			assertEquals("src/main", classRelection.getSourcePath());
			assertEquals("src/test", classRelection.getDestPath());
		}
		catch(IOException ex)
		{
			fail("can not catch SecurityException " + ex.getMessage());
		}
		catch(SecurityException ex)
		{
			fail("can not catch SecurityException " + ex.getMessage());
		}
		catch(Exception ex)
		{
			fail("can not catch Exception " + ex.getMessage());
		}
	}
}
