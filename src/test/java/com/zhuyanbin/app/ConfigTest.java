package com.zhuyanbin.app;

import junit.framework.TestCase;

public class ConfigTest extends TestCase 
{
	private Config classRelection;
	
	protected void setUp() throws Exception 
	{
		super.setUp();
		classRelection = new Config();
	}

	protected void tearDown() throws Exception 
	{
		classRelection = null;
		super.tearDown();
	}

	public void testSourcePathIsMutable() 
	{
		assertNull(classRelection.getSourcePath());
		String path = "./";
		classRelection.setSourcePath(path);
		assertEquals(path, classRelection.getSourcePath());
	}

	public void testDestPathIsMutable() 
	{
		assertNull(classRelection.getDestPath());
		String path = "./";
		classRelection.setDestPath(path);
		assertEquals(path, classRelection.getDestPath());
	}

	public void testSvnBinPathIsMutable() 
	{
		assertNull(classRelection.getSvnBinPath());
		String path = "./";
		classRelection.setSvnBinPath(path);
		assertEquals(path, classRelection.getSvnBinPath());
	}

	public void testSvnParamsIsMutable() 
	{
		assertNull(classRelection.getSvnParams());
		String params = "--username xxxx --passwordxxxxx";
		classRelection.setSvnParams(params);
		assertEquals(params, classRelection.getSvnParams());
	}

	public void testLogPathIsMutable() 
	{
		assertNull(classRelection.getLogPath());
		String path = "./";
		classRelection.setLogPath(path);
		assertEquals(path, classRelection.getLogPath());
	}

	public void testErrorLogPathIsMutable() 
	{
		assertNull(classRelection.getErrorLogPath());
		String path = "./";
		classRelection.setErrorLogPath(path);
		assertEquals(path, classRelection.getErrorLogPath());
	}

	public void testRedoLogPathIsMutable() 
	{
		assertNull(classRelection.getRedoLogPath());
		String path = "./";
		classRelection.setRedoLogPath(path);
		assertEquals(path, classRelection.getRedoLogPath());
	}

}
