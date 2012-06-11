package com.zhuyanbin.app;

import junit.framework.TestCase;

public class SvnWorkerTest extends TestCase
{
    private SvnWorker classRelection;
    private final String svnUrl   = "http://119.161.212.186/svn/test";
    private final String rootPath = "/Users/flavor/resource/svn_work";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new SvnWorker(svnUrl, rootPath);
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testInit()
    {
        assertEquals(svnUrl, classRelection.getSvnUrl());
        assertEquals(rootPath, classRelection.getRootPath());
    }

    public void testUpdate()
    {
        classRelection.update("/Users/flavor/resource/svn_work.bak", "tmp/ppp/tt.txt");
        classRelection.update("/Users/flavor/resource/svn_work.bak", "ppp.txt");
    }
}
