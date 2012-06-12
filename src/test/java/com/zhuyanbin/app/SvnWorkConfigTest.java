package com.zhuyanbin.app;

import junit.framework.TestCase;

public class SvnWorkConfigTest extends TestCase
{
    private final String  workCopy = "src/test/logs";
    private final String  username = "test";
    private final String  password = "test1111";
    private SvnWorkConfig classRelection;
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new SvnWorkConfig(workCopy, username, password);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testGetUserName()
    {
        assertEquals(username, classRelection.getUserName());
    }

    public void testGetPassword()
    {
        assertEquals(password, classRelection.getPassword());
    }

    public void testGetWorkCopyPath()
    {
        assertEquals(workCopy, classRelection.getWorkCopyPath());
    }

}
