package com.zhuyanbin.app;

import junit.framework.TestCase;

public class FileLogWorkerTest extends TestCase
{
    private FileLogWorker classRelection;

    private final String  logPath     = "logs/work.log";
    private final String  redoLogPath = "logs/work_redo.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FileLogWorker(logPath, redoLogPath);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testInit()
    {
        assertEquals(logPath, classRelection.getLogPath());
        assertEquals(redoLogPath, classRelection.getRedoLogPath());
    }
}
