package com.zhuyanbin.app;

import junit.framework.TestCase;

public class FullSyncTest extends TestCase
{
    protected FullSync   classRelection;
    private final String workCopyPath = "";
    private final String sourcePath   = "";
    private final String logPath      = "";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FullSync(workCopyPath, sourcePath, logPath);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testGetWorkCopyPath()
    {
        assertEquals(workCopyPath, classRelection.getWorkCopyPath());
    }

    public void testGetSourcePath()
    {
        assertEquals(sourcePath, classRelection.getSourcePath());
    }

    public void testGetLogPath()
    {
        assertEquals(logPath, classRelection.getLogPath());
    }

    public void testRsync() throws InterruptedException
    {
        assertTrue(classRelection.rsync());
        assertFalse(classRelection.rsync());
        Thread.sleep(1000);
        assertFalse(classRelection.rsync());
        // Thread.sleep(FullSync.MAX_SLEEP_TIME - 1000);
        // assertTrue(classRelection.rsync());
    }
}
