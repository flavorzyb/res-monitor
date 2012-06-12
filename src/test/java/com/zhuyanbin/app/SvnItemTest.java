package com.zhuyanbin.app;

import junit.framework.TestCase;

public class SvnItemTest extends TestCase
{
    private final String  path   = "test/test.jpg";
    private final boolean isAdd  = true;
    private final boolean isFile = true;

    private SvnItem       classRelection;
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new SvnItem(path, isAdd, isFile);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testInitClass()
    {
        assertEquals(path, classRelection.getPath());
        assertEquals(isAdd, classRelection.isAdd());
        assertEquals(isFile, classRelection.isFile());

        classRelection = new SvnItem(path);
        assertEquals(path, classRelection.getPath());
        assertEquals(false, classRelection.isAdd());
        assertEquals(false, classRelection.isFile());

        classRelection = new SvnItem(path, isAdd);
        assertEquals(path, classRelection.getPath());
        assertEquals(isAdd, classRelection.isAdd());
        assertEquals(false, classRelection.isFile());
    }
}
