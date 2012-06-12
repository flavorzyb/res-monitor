package com.zhuyanbin.app;

import java.io.IOException;

import junit.framework.TestCase;

public class Md5CheckSumTest extends TestCase
{
    private final String filePath = "src/test/resource/config.xml";
    private final String filePathMd5 = "src/test/resource/config_md5.xml";

    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    public void testGetFileMd5() throws IOException
    {
        assertEquals(Md5CheckSum.getFileMd5(filePath), Md5CheckSum.getFileMd5(filePathMd5));
    }
}