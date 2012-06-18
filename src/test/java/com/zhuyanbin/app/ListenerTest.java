package com.zhuyanbin.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.easymock.EasyMock;

public class ListenerTest extends TestCase
{

    private Listener classRelection;
    private final String logFile = "src/test/logs/resource.log";
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new Listener(logFile);
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        cleanUp();
        super.tearDown();
    }

    private void cleanUp()
    {
        File fp = new File(logFile);
        if (fp.exists())
        {
            fp.delete();
        }
    }

    public void testGetLogPath()
    {
        classRelection = new Listener(logFile);
        assertTrue(classRelection instanceof Listener);
        assertEquals(logFile, classRelection.getLogFile());
    }

    public void testFileRenamed()
    {
        classRelection.fileRenamed(111, "./", "aaa.jpg", "bbb.jpg");
    }

    public void testFileModified()
    {
        classRelection.fileModified(111, "./", "aaa.jpg");
    }

    public void testFileCreated()
    {
        classRelection.fileCreated(111, "./", "aaa.jpg");
    }

    public void testFileDeleted()
    {
        classRelection.fileDeleted(111, "./", "aaa.jpg");
    }

    public void testThrowsExceptions()
    {
        String file = "aaa.jpg";

        try
        {
            classRelection = EasyMock.createMockBuilder(Listener.class).withConstructor(logFile).addMockedMethod("writeFile", String.class, String.class).createMock();
            classRelection.writeFile(EasyMock.isA(String.class), EasyMock.isA(String.class));
            EasyMock.expectLastCall().andThrow(new IOException("i am a IOException"));
            EasyMock.replay(classRelection);
            classRelection.fileCreated(111, "./", file);
            EasyMock.verify(classRelection);

            classRelection = EasyMock.createMockBuilder(Listener.class).withConstructor(logFile).addMockedMethod("writeFile", String.class, String.class).createMock();
            classRelection.writeFile(EasyMock.isA(String.class), EasyMock.isA(String.class));
            EasyMock.expectLastCall().andThrow(new SecurityException("i am a SecurityException"));
            EasyMock.replay(classRelection);
            classRelection.fileCreated(111, "./", file);
            EasyMock.verify(classRelection);

            classRelection = EasyMock.createMockBuilder(Listener.class).withConstructor(logFile).addMockedMethod("writeFile", String.class, String.class).createMock();
            classRelection.writeFile(EasyMock.isA(String.class), EasyMock.isA(String.class));
            EasyMock.expectLastCall().andThrow(new FileNotFoundException("i am a FileNotFoundException"));
            EasyMock.replay(classRelection);
            classRelection.fileCreated(111, "./", file);
            EasyMock.verify(classRelection);
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }
    }
}