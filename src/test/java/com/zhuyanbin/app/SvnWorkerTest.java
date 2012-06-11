package com.zhuyanbin.app;

import junit.framework.TestCase;

public class SvnWorkerTest extends TestCase
{
    private SvnWorker classRelection;
    private final String svnUrl   = "http://119.161.212.186/svn/test";
    private final String rootPath = "/Users/flavor/resource/svn_work";
    private final String userName = "yanbin";
    private final String password = "123456";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new SvnWorker(svnUrl, rootPath, userName, password);
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
        assertEquals(userName, classRelection.getUserName());
        assertEquals(password, classRelection.getPassword());
    }

    public void testUpdate()
    {
        classRelection.update("/Users/flavor/resource/svn_work.bak", "tmp/ppp/tt.txt");
        System.out.println();
        classRelection.update("/Users/flavor/resource/svn_work.bak", "ppp.txt");
        System.out.println();
        classRelection.update("/Users/flavor/resource/svn_work.bak", "bbb/psp.txt");
        System.out.println();
        classRelection.update("/Users/flavor/resource/svn_work.bak", "ppp/999");
        System.out.println();
        classRelection.update("/Users/flavor/resource/svn_work.bak", "tt.txt");
        System.out.println();
        classRelection.update("/Users/flavor/resource/svn_work.bak", "888");
        System.out.println();
    }
}
