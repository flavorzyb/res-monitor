package com.zhuyanbin.app;

import junit.framework.TestCase;

public class ConfigTest extends TestCase 
{
	private Config classRelection;
	
	@Override
    protected void setUp() throws Exception 
	{
		super.setUp();
		classRelection = new Config();
	}

	@Override
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

    public void testUserNameIsMutable()
	{
		assertNull(classRelection.getUserName());
        String username = "xxxddd";
        classRelection.setUserName(username);
        assertEquals(username, classRelection.getUserName());
	}

    public void testPasswordIsMutable()
	{
		assertNull(classRelection.getPassword());
        String password = "sswordxxxxx";
        classRelection.setPassword(password);
        assertEquals(password, classRelection.getPassword());
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

    public void testDoingLogPathIsMutable()
    {
        assertNull(classRelection.getDoingLogPath());
        String path = "./";
        classRelection.setDoingLogPath(path);
        assertEquals(path, classRelection.getDoingLogPath());
    }

    public void testReDoLogPathIsMutable()
    {
        assertNull(classRelection.getRedoLogPath());
        String path = "./";
        classRelection.setRedoLogPath(path);
        assertEquals(path, classRelection.getRedoLogPath());
    }
    public void testPidPathIsMutable()
    {
        assertNull(classRelection.getPidPath());
        String path = "./";
        classRelection.setPidPath(path);
        assertEquals(path, classRelection.getPidPath());
    }
}
