package com.zhuyanbin.app;

import java.io.File;

import junit.framework.TestCase;

public class LogerTest extends TestCase
{
    private Loger        classRelection;
    private final String logPath = "src/test/logs/test.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new Loger();
        deleteLogFile();
    }

    @Override
    protected void tearDown() throws Exception
    {
        deleteLogFile();
        classRelection = null;
        super.tearDown();
    }

    protected void deleteLogFile() throws NullPointerException
    {
        File fp = new File(logPath);
        if (fp.isFile())
        {
            fp.delete();
        }
    }

    protected boolean testLogFileIsExist() throws NullPointerException
    {
        File fp = new File(logPath);
        return fp.isFile();
    }

    public void testWrite() throws NullPointerException
    {
        assertFalse(testLogFileIsExist());
        Loger.write(logPath, "This is a test");
        assertTrue(testLogFileIsExist());
        Loger.write(logPath, "This is a test");
        assertTrue(testLogFileIsExist());

        Loger.write("sdfsdf/sdfsdfasdf/adsesdsf" + Math.random() + ".log", "sdsdddd");
    }
}
