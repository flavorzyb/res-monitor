package com.zhuyanbin.app;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import junit.framework.TestCase;

public class FileLogWorkerTest extends TestCase
{
    private FileLogWorker classRelection;

    private final String  logPath      = "src/test/logs/work.log";
    private final String  doingLogPath = "src/test/logs/work_doing.log";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FileLogWorker(logPath, doingLogPath, ErrorLog.getInstance());
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
    }

    public void testStart() throws IOException, InterruptedException
    {
        createFile(logPath);

        WorkChecker wc = new WorkChecker(classRelection);
        classRelection.start();
        wc.start();
        wc.join();
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