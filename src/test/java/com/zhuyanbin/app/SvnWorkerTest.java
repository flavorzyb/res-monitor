package com.zhuyanbin.app;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

import org.tmatesoft.svn.core.SVNException;

public class SvnWorkerTest extends TestCase
{
    private SvnWorker classRelection;
    private final String workCopyPath = "/Users/flavor/resource/svn_work";
    private final String userName = "yanbin";
    private final String password = "123456";
    private final SvnWorkConfig swc          = new SvnWorkConfig(workCopyPath, userName, password);

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();

        classRelection = new SvnWorker(swc);
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testInit()
    {
        assertEquals(swc, classRelection.getSvnWorkConfig());
    }

    public void testUpdate() throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        // classRelection.update("/Users/flavor/resource/svn_work.bak",
        // "tmp/ppp/tt.txt");
        // classRelection.update("/Users/flavor/resource/svn_work.bak",
        // "ppp.txt");
        // classRelection.update("/Users/flavor/resource/svn_work.bak",
        // "bbb/psp.txt");
        // classRelection.update("/Users/flavor/resource/svn_work.bak",
        // "ppp/999");
        // classRelection.update("/Users/flavor/resource/svn_work.bak",
        // "tt.txt");
        // classRelection.update("/Users/flavor/resource/svn_work.bak", "888");
    }
}
