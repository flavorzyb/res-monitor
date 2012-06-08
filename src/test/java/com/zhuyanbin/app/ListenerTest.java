package com.zhuyanbin.app;

import junit.framework.TestCase;

public class ListenerTest extends TestCase
{

    private Listener classRelection;
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new Listener("logs");
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testGetLogPath()
    {
        String path = "logs/resource.log";
        classRelection = new Listener(path);
        assertTrue(classRelection instanceof Listener);
        assertEquals(path, classRelection.getLogFile());
    }

    public void testFileRenamed()
    {
    }

    public void testFileModified()
    {
    }

    public void testFileDeleted()
    {
    }

    public void testFileCreated()
    {
    }
}
