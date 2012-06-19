package com.zhuyanbin.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
                fp.delete();
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

    private void vaildMock(String[] filePaths) throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").addMockedMethod("getCommitClient").createMock();

        SVNWCClient swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).addMockedMethod("doAdd", File.class, boolean.class, boolean.class, boolean.class, SVNDepth.class, boolean.class, boolean.class).createMock();
        SVNUpdateClient suclient = EasyMock.createMock(SVNUpdateClient.class);
        SVNCommitClient scclient = EasyMock.createMock(SVNCommitClient.class);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient).anyTimes();
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient).anyTimes();
        EasyMock.expect(scm.getCommitClient()).andReturn(scclient).anyTimes();

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        swclient.doAdd(EasyMock.anyObject(File.class), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyObject(SVNDepth.class), EasyMock.anyBoolean(), EasyMock.anyBoolean());
        EasyMock.expectLastCall().anyTimes().asStub();

        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);

        EasyMock.expect(scclient.doCommit(EasyMock.anyObject(File[].class), EasyMock.anyBoolean(), EasyMock.anyObject(String.class), EasyMock.anyObject(SVNProperties.class), EasyMock.anyObject(String[].class), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyObject(SVNDepth.class))).andReturn(new SVNCommitInfo(1000L, "test", new Date()));

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
    }

    public void testUpdateSucc() throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        String[] filePaths = { "tmp/ppp/tt.txt" };
        vaildMock(filePaths);
    }

    public void testUpdateFolderWhenExist() throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        String[] filePaths = { "tmp/ppp" };
        vaildMock(filePaths);

        // when is exists
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").addMockedMethod("getCommitClient").createMock();

        SVNWCClient swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).addMockedMethod("doAdd", File.class, boolean.class, boolean.class, boolean.class, SVNDepth.class, boolean.class, boolean.class).createMock();
        SVNUpdateClient suclient = EasyMock.createMock(SVNUpdateClient.class);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient).anyTimes();
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient).anyTimes();

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        swclient.doAdd(EasyMock.anyObject(File.class), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyObject(SVNDepth.class), EasyMock.anyBoolean(), EasyMock.anyBoolean());
        EasyMock.expectLastCall().anyTimes().asStub();

        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);


        EasyMock.replay(scm);
        EasyMock.replay(swclient);
        EasyMock.replay(suclient);

        classRelection.setSVNClientManager(scm);
        classRelection.update(sourcePath, filePaths);
        EasyMock.verify(scm);
        EasyMock.verify(swclient);
        EasyMock.verify(suclient);
    }

    public void testUpdateFileSucc() throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        copyFile(sourcePath + "/tt.txt", sourcePath + "/ttt.txt");
        String[] filePaths = { "ttt.txt" };
        vaildMock(filePaths);

        // when is exists
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").addMockedMethod("getCommitClient").createMock();

        SVNWCClient swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).addMockedMethod("doAdd", File.class, boolean.class, boolean.class, boolean.class, SVNDepth.class, boolean.class, boolean.class).createMock();
        SVNUpdateClient suclient = EasyMock.createMock(SVNUpdateClient.class);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient).anyTimes();
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient).anyTimes();

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        swclient.doAdd(EasyMock.anyObject(File.class), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyObject(SVNDepth.class), EasyMock.anyBoolean(), EasyMock.anyBoolean());
        EasyMock.expectLastCall().anyTimes().asStub();

        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);

        EasyMock.replay(scm);
        EasyMock.replay(swclient);
        EasyMock.replay(suclient);

        classRelection.setSVNClientManager(scm);
        classRelection.update(sourcePath, filePaths);
        EasyMock.verify(scm);
        EasyMock.verify(swclient);
        EasyMock.verify(suclient);

        // update when is exists
        appand2File(sourcePath + "/ttt.txt");
        scm = EasyMock.createMockBuilder(SVNClientManager.class).addMockedMethod("getWCClient").addMockedMethod("getUpdateClient").addMockedMethod("getCommitClient").createMock();

        swclient = EasyMock.createMockBuilder(SVNWCClient.class).addMockedMethod("doCleanup", File.class).createMock();
        suclient = EasyMock.createMock(SVNUpdateClient.class);
        SVNCommitClient scclient = EasyMock.createMock(SVNCommitClient.class);

        EasyMock.expect(scm.getWCClient()).andReturn(swclient).anyTimes();
        EasyMock.expect(scm.getUpdateClient()).andReturn(suclient).anyTimes();
        EasyMock.expect(scm.getCommitClient()).andReturn(scclient).anyTimes();

        swclient.doCleanup(new File(swc.getWorkCopyPath()));
        EasyMock.expectLastCall().asStub();

        // swclient.doAdd(EasyMock.anyObject(File.class), EasyMock.anyBoolean(),
        // EasyMock.anyBoolean(), EasyMock.anyBoolean(),
        // EasyMock.anyObject(SVNDepth.class), EasyMock.anyBoolean(),
        // EasyMock.anyBoolean());
        // EasyMock.expectLastCall().anyTimes().asStub();

        EasyMock.expect(suclient.doUpdate(new File(swc.getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true)).andReturn(1000L);

        EasyMock.expect(scclient.doCommit(EasyMock.anyObject(File[].class), EasyMock.anyBoolean(), EasyMock.anyObject(String.class), EasyMock.anyObject(SVNProperties.class), EasyMock.anyObject(String[].class), EasyMock.anyBoolean(), EasyMock.anyBoolean(), EasyMock.anyObject(SVNDepth.class))).andReturn(new SVNCommitInfo(1000L, "test", new Date()));

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

        deleteFile(sourcePath + "/ttt.txt");
    }

    protected void appand2File(String filePath) throws IOException
    {
        FileOutputStream destFos = new FileOutputStream(new File(filePath), true);
        BufferedOutputStream destBos = new BufferedOutputStream(destFos);
        String str = "testddddsdfasdfsdf";
        destBos.write(str.getBytes());
        destBos.close();
        destFos.close();
    }

    protected void copyFile(String sourcePath, String destPath) throws NullPointerException, IOException, SecurityException
    {
        FileInputStream sourceFis = new FileInputStream(new File(sourcePath));
        BufferedInputStream sourceBis = new BufferedInputStream(sourceFis);

        FileOutputStream destFos = new FileOutputStream(new File(destPath));
        BufferedOutputStream destBos = new BufferedOutputStream(destFos);

        byte[] b = new byte[1024 * 5];
        int len = 0;

        while (-1 != (len = sourceBis.read(b)))
        {
            destBos.write(b, 0, len);
        }

        destBos.flush();

        destBos.close();
        destFos.close();
        sourceBis.close();
        sourceFis.close();
    }

}
