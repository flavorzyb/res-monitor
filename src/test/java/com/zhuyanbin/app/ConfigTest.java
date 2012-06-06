package com.zhuyanbin.app;

import junit.framework.TestCase;

public class ConfigTest extends TestCase {

	private Config classRelection;
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
			conf = new Config("resource/config.xml");
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
	}
}
