package com.zhuyanbin.app;

import junit.framework.TestCase;

public class ListenerTest extends TestCase
{

    private Listener classRelection;
    private final String logFile = "logs/resource.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new Listener(logFile);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testGetLogPath()
    {
        classRelection = new Listener(logFile);
        assertTrue(classRelection instanceof Listener);
        assertEquals(logFile, classRelection.getLogFile());
    }

    public void testFileRenamed()
    {
        classRelection.fileRenamed(111, "./", "aaa.jpg", "bbb.jpg");
    }

    public void testFileModified()
    {
        classRelection.fileModified(111, "./", "aaa.jpg");
    }

    public void testFileCreated()
    {
        classRelection.fileCreated(111, "./", "aaa.jpg");
    }

    public void testFileDeleted()
    {
        classRelection.fileDeleted(111, "./", "aaa.jpg");
    }
}
