package com.zhuyanbin.app;

import junit.framework.TestCase;

public class FileCheckerTest extends TestCase
{
    private FileChecker classRelection;
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new FileChecker();
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testCheckPathIsDirectory()
    {
        String path = "./";
        try
        {
            FileChecker.checkPathIsDirectory(path);
        }
        catch (NullPointerException ex)
        {
            fail(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            fail(ex.getMessage());
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

        // NullPointerException
        try
        {
            FileChecker.checkPathIsDirectory(null);
        }
        catch (NullPointerException ex)
        {
            assertTrue(ex instanceof NullPointerException);
        }
        catch (SecurityException ex)
        {
            fail(ex.getMessage());
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

        // NullPointerException
        try
        {
            FileChecker.checkPathIsDirectory("adsfasdfadf");
        }
        catch (NullPointerException ex)
        {
            fail(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            fail(ex.getMessage());
        }
        catch (Exception ex)
        {
            assertTrue(ex instanceof Exception);
        }
    }

    public void testCheckPathIsFile()
    {
        String path = "pom.xml";
        try
        {
            FileChecker.checkPathIsFile(path);
        }
        catch (NullPointerException ex)
        {
            fail(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            fail(ex.getMessage());
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

        // NullPointerException
        try
        {
            FileChecker.checkPathIsFile(null);
        }
        catch (NullPointerException ex)
        {
            assertTrue(ex instanceof NullPointerException);
        }
        catch (SecurityException ex)
        {
            fail(ex.getMessage());
        }
        catch (Exception ex)
        {
            fail(ex.getMessage());
        }

        // NullPointerException
        try
        {
            FileChecker.checkPathIsFile("adsfasdfadf");
        }
        catch (NullPointerException ex)
        {
            fail(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            fail(ex.getMessage());
        }
        catch (Exception ex)
        {
            assertTrue(ex instanceof Exception);
        }
    }
}
