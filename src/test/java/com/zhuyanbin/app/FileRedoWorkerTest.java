package com.zhuyanbin.app;

import junit.framework.TestCase;

public class FileRedoWorkerTest extends TestCase
{
    private FileRedoWorker classRelection;
    private final String   redoLogPath = "logs/redo.log";
    private final String   logPath     = "logs/log.log";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FileRedoWorker(redoLogPath, logPath);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testInit()
    {
        assertEquals(redoLogPath, classRelection.getRedoLogPath());
        assertEquals(logPath, classRelection.getLogPath());
    }
}
