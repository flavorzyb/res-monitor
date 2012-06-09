package com.zhuyanbin.app;

import junit.framework.TestCase;
import net.contentobjects.jnotify.JNotifyException;

public class FileWatcherTest extends TestCase
{
    private FileWatcher classRelection;

    private final String watchPath = "./";
    private final String logFile   = "logs/resource.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FileWatcher(watchPath, logFile);
    }

    @Override
    protected void tearDown() throws Exception
    {
        try
        {
            classRelection.removeWatch();
        }
        catch (JNotifyException ex)
        {
            System.out.println(ex.getMessage());
        }
        classRelection = null;
        super.tearDown();
    }

    public void testInitWhenConstruct()
    {
        assertEquals(watchPath, classRelection.getPath());
        assertEquals(logFile, classRelection.getLogFile());
    }

    public void testWathSubtreeIsMutable()
    {
        assertFalse(classRelection.isWatchSubtree());
        classRelection.setWathSubtree(true);
        assertTrue(classRelection.isWatchSubtree());

        classRelection.setWathSubtree(false);
        assertFalse(classRelection.isWatchSubtree());
    }

    public void testAddWatchAndRemoveWatchSucc()
    {
        try
        {
            classRelection.addWatch();
        }
        catch (JNotifyException e)
        {
            fail(e.getMessage());
        }

        try
        {
            assertTrue(classRelection.removeWatch());
        }
        catch (JNotifyException ex)
        {
            fail(ex.getMessage());
        }
    }

    public void testAddWatchAndRemoveWatchFail()
    {
        try
        {
            classRelection.removeWatch();
        }
        catch (JNotifyException ex)
        {
            fail(ex.getMessage());
        }

        try
        {
            classRelection.addWatch();
            classRelection.removeWatch();
            classRelection.removeWatch();
        }
        catch (JNotifyException e)
        {
            fail(e.getMessage());
        }

        try
        {
            classRelection.addWatch();
            classRelection.addWatch();
        }
        catch (JNotifyException e)
        {
            fail(e.getMessage());
        }
    }
}
