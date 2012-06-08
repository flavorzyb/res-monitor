package com.zhuyanbin.app;

import junit.framework.TestCase;
import net.contentobjects.jnotify.JNotifyException;

public class FileWatchTest extends TestCase
{
    private FileWatch classRelection;

    private final String watchPath = "./";
    private final String logFile   = "logs/resource.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FileWatch(watchPath, logFile);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection.removeWatch();
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
            fail("can not catch JNotifyException.");
        }
        catch (JNotifyException ex)
        {
            assertTrue(ex instanceof JNotifyException);
        }

        try
        {
            classRelection.addWatch();
            classRelection.removeWatch();
            classRelection.removeWatch();
            fail("can not catch JNotifyException.");
        }
        catch (JNotifyException e)
        {
            fail(e.getMessage());
        }

        try
        {
            classRelection.addWatch();
            classRelection.addWatch();
            fail("can not catch JNotifyException.");
        }
        catch (JNotifyException e)
        {
            fail(e.getMessage());
        }
    }
}
