package com.zhuyanbin.app;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

public class SvnWorkerTest extends TestCase
{
    private SvnWorker classRelection;
    private final String        sourcePath   = "src/test/svn/dest";
    private final String        workCopyPath = "src/test/svn/dest";
    private final String        userName     = "test";
    private final String        password     = "123456";
    private final SvnWorkConfig swc          = new SvnWorkConfig(workCopyPath, userName, password);
    private SVNClientManager    scm;

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

    public void testGetSvnClientManager()
    {
        assertTrue(classRelection.getSVNClientManager() instanceof SVNClientManager);
    }

    public void testUpdateWhenFilePathsIsNull() throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").createMock();

        SVNWCClient swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).addMockedMethod("doAdd", File.class, boolean.class, boolean.class, boolean.class, SVNDepth.class, boolean.class, boolean.class).createMock();
        SVNUpdateClient suclient = EasyMock.createMock(SVNUpdateClient.class);
        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient);
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient);

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        swclient.doAdd(new File("tmp/ppp/tt.txt"), true, false, false, SVNDepth.INFINITY, false, false);
        EasyMock.expectLastCall().asStub();

        EasyMock.replay(scm);
        EasyMock.replay(swclient);
        EasyMock.replay(suclient);

        classRelection.setSVNClientManager(scm);
        classRelection.update(sourcePath, null);
        EasyMock.verify(scm);
        EasyMock.verify(swclient);
        EasyMock.verify(suclient);
    }

    public void testUpdate() throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        String[] filePaths = { "tmp/ppp/tt.txt" };
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").createMock();

        SVNWCClient swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).addMockedMethod("doAdd", File.class, boolean.class, boolean.class, boolean.class, SVNDepth.class, boolean.class, boolean.class).createMock();
        SVNUpdateClient suclient = EasyMock.createMock(SVNUpdateClient.class);
        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient);
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient);

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        swclient.doAdd(new File("tmp/ppp/tt.txt"), true, false, false, SVNDepth.INFINITY, false, false);
        EasyMock.expectLastCall().asStub();

        EasyMock.replay(scm);
        EasyMock.replay(swclient);
        EasyMock.replay(suclient);

        classRelection.setSVNClientManager(scm);
        classRelection.update(sourcePath, filePaths);
        EasyMock.verify(scm);
        EasyMock.verify(swclient);
        EasyMock.verify(suclient);
        /*
         * classRelection.update("/Users/flavor/resource/svn_work.bak",
         * "ppp.txt");
         * classRelection.update("/Users/flavor/resource/svn_work.bak",
         * "bbb/psp.txt");
         * classRelection.update("/Users/flavor/resource/svn_work.bak",
         * "ppp/999");
         * classRelection.update("/Users/flavor/resource/svn_work.bak",
         * "tt.txt");
         * classRelection.update("/Users/flavor/resource/svn_work.bak", "888");
         */
    }

}
