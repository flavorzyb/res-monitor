package com.zhuyanbin.app;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import junit.framework.TestCase;

public class Md5CheckSumTest extends TestCase
{
    private final String filePath = "src/test/resource/config.xml";
    private final String filePathMd5 = "src/test/resource/config_md5.xml";
    private final String filePathMd5No = "src/test/resource/config_md5_no.xml";

    private Md5CheckSum  classRelection;

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        classRelection = new Md5CheckSum();
    }

    @Override
    protected void tearDown() throws Exception
    {
        classRelection = null;
        super.tearDown();
    }

    public void testGetFileMd5() throws IOException, NoSuchAlgorithmException
    {
        assertEquals(Md5CheckSum.getFileMd5(filePath), Md5CheckSum.getFileMd5(filePathMd5));
    }
    
    public void testMd5StringIsSame() throws IOException, NoSuchAlgorithmException
    {
        assertTrue(Md5CheckSum.md5StringIsSame(filePath, filePathMd5));
        assertFalse(Md5CheckSum.md5StringIsSame(filePath, filePathMd5No));
    }
}