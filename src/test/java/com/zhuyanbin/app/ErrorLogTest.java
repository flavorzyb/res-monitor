package com.zhuyanbin.app;

import junit.framework.TestCase;

public class ErrorLogTest extends TestCase
{
    private ErrorLog classRelection;
    private final String errorLog = "logs/error.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = ErrorLog.getInstance();
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testInit()
    {
        assertTrue(classRelection instanceof ErrorLog);
    }

    public void testLogPathIsMutable()
    {
        assertEquals(null, classRelection.getLogPath());
        classRelection.setLogPath(errorLog);
        assertEquals(errorLog, classRelection.getLogPath());
    }

    public void testWrite()
    {
        classRelection.setLogPath(errorLog);
        classRelection.write("this is a test message.");

        classRelection.setLogPath("asdfsdokldsafjsdf/sadfiasodfsdf/adsfoasdfsdf.log");
        classRelection.write("this is a test message.");
    }
}
