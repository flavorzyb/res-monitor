package com.zhuyanbin.app;

import java.io.File;

import junit.framework.TestCase;

public class FullSyncTest extends TestCase
{
    protected FullSync   classRelection;
    private final String workCopyPath = "src/test/FullSync";
    private final String sourcePath   = "src/main/java";
    private final String logPath      = "src/test/logs/fullSync.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        deleteFile(workCopyPath);
        deleteFile(logPath);
        classRelection = new FullSync(workCopyPath, sourcePath, logPath);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        deleteFile(workCopyPath);
        super.tearDown();
    }

    protected void deleteFile(String path)
    {
        File fp = new File(path);

        if (fp.exists())
        {
            if (fp.isFile())
            {
                fp.delete();
            }
            else if (fp.isDirectory())
            {
                File[] files = fp.listFiles();
                int len = files.length;
                for (int i = 0; i < len; i++)
                {
                    deleteFile(files[i].getAbsolutePath());
                }
                fp.delete();
            }
        }
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
    }
}
