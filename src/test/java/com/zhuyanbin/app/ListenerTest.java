package com.zhuyanbin.app;

import java.io.File;

import junit.framework.TestCase;

public class ListenerTest extends TestCase
{

    private Listener classRelection;
    private final String logFile = "src/test/logs/resource.log";
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
        cleanUp();
        super.tearDown();
    }

    private void cleanUp()
    {
        File fp = new File(logFile);
        if (fp.exists())
        {
            fp.delete();
        }
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
