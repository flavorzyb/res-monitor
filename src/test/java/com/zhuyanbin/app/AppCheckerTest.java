package com.zhuyanbin.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

public class AppCheckerTest extends TestCase
{
    private AppChecker classRelection;
    private final String pidFile = "src/test/logs/resourceMonitor.pid";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new AppChecker(pidFile);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    private void createFile(String filePath) throws IOException
    {
        File fp = new File(filePath);
        if (fp.exists())
        {
            fp.delete();
        }

        FileOutputStream fos = new FileOutputStream(filePath);

        Long pid = new Long(AppChecker.getPID());
        fos.write(pid.toString().getBytes());
        fos.flush();
        fos.close();
    }

    public void testGetPidFile()
    {
        assertTrue(classRelection instanceof AppChecker);
        assertEquals(classRelection.getPidFile(), pidFile);
    }

    public void testGetPIDReturnLong()
    {
        assertTrue(AppChecker.getPID() > 0);
    }

    public void testHasRunningReturnBoolean()
    {
        assertFalse(classRelection.hasRunning());
    }

    public void testHasRunningReturnBooleanWithMock()
    {
        try
        {
            createFile(pidFile);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        assertTrue(classRelection.hasRunning());
    }

    public void testWritePid2FileReturnLong() throws IOException
    {
        assertEquals(AppChecker.getPID(), classRelection.writePid2File());
    }
}
