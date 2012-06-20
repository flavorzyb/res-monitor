package com.zhuyanbin.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Vector;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.tmatesoft.svn.core.SVNException;

public class FileLogWorkerTest extends TestCase
{
    private FileLogWorker classRelection;
    private SvnWorker           svnworker;

    private final String        logPath      = "src/test/logs/work.log";
    private final String        doingLogPath = "src/test/logs/work_doing.log";
    private final String        sourcePath   = "src/test/svn/source";
    private final String        wcPath       = "src/test/svn/dest";
    private final String        userName     = "test";
    private final String        password     = "test111";
    private final Vector<String> updateFiles  = new Vector<String>();

    private final SvnWorkConfig swc          = new SvnWorkConfig(wcPath, userName, password);

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        svnworker = EasyMock.createMockBuilder(SvnWorker.class).addMockedMethod("update", String.class, Vector.class).createMock();
        classRelection = new FileLogWorker(logPath, doingLogPath, sourcePath, ErrorLog.getInstance(), swc);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    private void createFile(String filePath) throws IOException
    {
        File fp = new File(filePath);
        if (fp.exists())
        {
            fp.delete();
        }

        Date dt = new Date();
        Timestamp ts = new Timestamp(dt.getTime());

        FileOutputStream fos = new FileOutputStream(filePath, true);

        String msg = ts + "|" + "aaa.jpg" + "\n";
        fos.write(msg.getBytes());
        msg = ts + "|" + "ccc/aaa.jpg" + "\n";
        fos.write(msg.getBytes());

        fos.flush();
        fos.close();
    }

    public void testInit()
    {
        assertEquals(logPath, classRelection.getLogPath());
        assertEquals(doingLogPath, classRelection.getDoingLogPath());
        assertEquals(ErrorLog.getInstance(), classRelection.getErrorLog());
        assertEquals(swc, classRelection.getSvnWorkConfig());
        assertEquals(sourcePath, classRelection.getSourcePath());
    }

    @SuppressWarnings("unchecked")
    public void testStartWithDoingLog() throws IOException, InterruptedException, SVNException, NullPointerException, SecurityException, NoSuchAlgorithmException
    {
        createFile(doingLogPath);
        createFile(logPath);

        classRelection = EasyMock.createMockBuilder(FileLogWorker.class).withConstructor(logPath, doingLogPath, sourcePath, ErrorLog.getInstance(), swc).addMockedMethod("getSvnWorker").createMock();
        EasyMock.expect(classRelection.getSvnWorker()).andReturn(svnworker).anyTimes();
        EasyMock.expect(svnworker.update(EasyMock.anyObject(String.class), EasyMock.anyObject(updateFiles.getClass()))).andReturn(true).anyTimes();

        EasyMock.replay(classRelection);
        EasyMock.replay(svnworker);

        WorkChecker wc = new WorkChecker(classRelection);
        classRelection.setDaemon(true);
        classRelection.start();
        wc.setDaemon(true);
        wc.start();
        wc.join();
        classRelection.join();

        EasyMock.verify(svnworker);
        EasyMock.verify(classRelection);

    }
    
    public void testStart() throws IOException, InterruptedException, SVNException, NullPointerException, SecurityException, NoSuchAlgorithmException
    {
        createFile(logPath);

        classRelection = EasyMock.createMockBuilder(FileLogWorker.class).withConstructor(logPath, doingLogPath, sourcePath, ErrorLog.getInstance(), swc).addMockedMethod("getSvnWorker").createMock();
        EasyMock.expect(classRelection.getSvnWorker()).andReturn(svnworker).anyTimes();
        EasyMock.expect(svnworker.update(EasyMock.anyObject(String.class), EasyMock.anyObject(updateFiles.getClass()))).andReturn(true).anyTimes();

        EasyMock.replay(classRelection);
        EasyMock.replay(svnworker);

        WorkChecker wc = new WorkChecker(classRelection);
        classRelection.setDaemon(true);
        classRelection.start();
        wc.setDaemon(true);
        wc.start();
        wc.join();
        classRelection.join();

        EasyMock.verify(svnworker);
        EasyMock.verify(classRelection);
    }

    public void testStartWithThrowException() throws IOException, InterruptedException, SVNException, NullPointerException, SecurityException, NoSuchAlgorithmException
    {
        createFile(logPath);

        classRelection = EasyMock.createMockBuilder(FileLogWorker.class).withConstructor(logPath, doingLogPath, sourcePath, ErrorLog.getInstance(), swc).addMockedMethod("getSvnWorker").createMock();
        EasyMock.expect(classRelection.getSvnWorker()).andReturn(svnworker).anyTimes();
        EasyMock.expect(svnworker.update(EasyMock.anyObject(String.class), EasyMock.anyObject(updateFiles.getClass()))).andThrow(new IOException("throw a IOException when svnworker update"));

        EasyMock.replay(classRelection);
        EasyMock.replay(svnworker);

        WorkChecker wc = new WorkChecker(classRelection);
        classRelection.setDaemon(true);
        classRelection.start();
        wc.setDaemon(true);
        wc.start();
        wc.join();
        classRelection.join();

        EasyMock.verify(svnworker);
        EasyMock.verify(classRelection);
    }

    public void testGetSvnWorker()
    {
        assertTrue(classRelection.getSvnWorker() instanceof SvnWorker);
        assertTrue(classRelection.getSvnWorker() instanceof SvnWorker);
    }
}

class WorkChecker extends Thread
{
    private final FileLogWorker _fileLogWorker;

    public WorkChecker(FileLogWorker flw)
    {
        _fileLogWorker = flw;
    }
    
    @Override
    public void run()
    {
        // System.out.
        boolean isExit = false;
        try
        {
            while (!isExit)
            {
                sleep(1000);
                isExit = _fileLogWorker.isSleep();
            }

            _fileLogWorker.setIsLoop(false);
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}