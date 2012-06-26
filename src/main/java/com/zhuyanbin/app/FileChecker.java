package com.zhuyanbin.app;

import java.io.File;

public class FileChecker
{
    public static void checkPathIsDirectory(String path) throws NullPointerException, SecurityException, Exception
    {
        File fp = new File(path);
        if (!fp.isDirectory())
        {
            throw new Exception(fp.getAbsolutePath() + " is not a Directory.");
        }
    }

    public static void checkPathIsFile(String path) throws NullPointerException, SecurityException, Exception
    {
        File fp = new File(path);
        if (!fp.isFile())
        {
            throw new Exception(fp.getAbsolutePath() + " is not a file.");
        }
    }
}
