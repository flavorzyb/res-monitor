package com.zhuyanbin.app;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNProperties;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNCommitClient;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;

public class SvnWorkerTest extends TestCase
{
    private SvnWorker classRelection;
    private final String        sourcePath   = "src/test/svn/source";
    private final String        workCopyPath = "src/test/svn/dest";
    private final String        userName     = "test";
    private final String        password     = "123456";
    private final SvnWorkConfig swc          = new SvnWorkConfig(workCopyPath, userName, password);
    private SVNClientManager    scm;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        deleteFile(workCopyPath);
        createDir(workCopyPath);
        classRelection = new SvnWorker(swc);
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    protected void createDir(String path)
    {
        File fp = new File(path);
        if (!fp.exists())
        {
            fp.mkdirs();
        }
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
            }
        }
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
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").addMockedMethod("getCommitClient").createMock();

        SVNWCClient swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).addMockedMethod("doAdd", File.class, boolean.class, boolean.class, boolean.class, SVNDepth.class, boolean.class, boolean.class).createMock();
        SVNUpdateClient suclient = EasyMock.createMock(SVNUpdateClient.class);
        // SVNCommitClient sccient =
        // EasyMock.createMockBuilder(SVNCommitClient.class).addMockedMethod("doCommit",
        // File[].class, boolean.class, String.class, SVNProperties.class,
        // String[].class, boolean.class, boolean.class,
        // SVNDepth.class).createMock();
        SVNCommitClient scclient = EasyMock.createMock(SVNCommitClient.class);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient).anyTimes();
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient).anyTimes();
        EasyMock.expect(scm.getCommitClient()).andReturn(scclient).anyTimes();

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        swclient.doAdd(new File(workCopyPath + "/tmp/ppp/tt.txt"), true, false, false, SVNDepth.INFINITY, false, false);
        EasyMock.expectLastCall().asStub();

        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);

        String[] changelists = {};
        File[] files = { new File(workCopyPath + "/tmp/ppp/tt.txt") };

        EasyMock.expect(scclient.doCommit(EasyMock.anyObject(files.getClass()), EasyMock.anyBoolean(), EasyMock.anyObject(String.class), EasyMock.anyObject(SVNProperties.class), EasyMock.anyObject(changelists.getClass()), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyObject(SVNDepth.class))).andReturn(new SVNCommitInfo(1000L, "test", new Date()));

        EasyMock.replay(scm);
        EasyMock.replay(swclient);
        EasyMock.replay(suclient);
        EasyMock.replay(scclient);

        classRelection.setSVNClientManager(scm);
        classRelection.update(sourcePath, filePaths);
        EasyMock.verify(scm);
        EasyMock.verify(swclient);
        EasyMock.verify(suclient);
        EasyMock.verify(scclient);
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
