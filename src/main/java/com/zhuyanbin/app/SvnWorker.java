package com.zhuyanbin.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SvnWorker
{
    private String _svnUrl;
    private String _rootPath;

    public SvnWorker(String svnUrl, String rootPath)
    {
        setSvnUrl(svnUrl);
        setRootPath(rootPath);
    }

    private void setSvnUrl(String svnUrl)
    {
        _svnUrl = svnUrl;
    }

    public String getSvnUrl()
    {
        return _svnUrl;
    }

    private void setRootPath(String rootPath)
    {
        _rootPath = rootPath;
    }

    public String getRootPath()
    {
        return _rootPath;
    }

    private boolean isFile(String sourcePath, String filePath) throws NullPointerException, SecurityException
    {
        File fp = new File(sourcePath + "/" + filePath);
        return fp.isFile();
    }
    
    private void mkdirs(String filePath) throws NullPointerException, SecurityException
    {
        File fp = new File(filePath);
        if (!fp.exists())
        {
            fp.mkdirs();
        }
    }

    private boolean doUpdate()
    {
        return true;
    }

    private boolean doCommit()
    {
        return true;
    }

    public boolean update(String sourcePath, String filePath) throws NullPointerException, SecurityException
    {
        boolean result = false;
        // 先更新整个svn仓库
        doUpdate();

        boolean isFile = isFile(sourcePath, filePath);
        String newFilePath = getRootPath() + "/" + filePath;
        File fp = new File(newFilePath);
        String needCreateDir = fp.getAbsolutePath();
        boolean needAddFile2Svn = false;
        
        if (isFile)
        {
            needCreateDir = fp.getParent();
        }

        // 找出需要add的文件或目录
        String needAddDir = getNeedAddDir(sourcePath, filePath);

        // 创建所必需目录
        mkdirs(needCreateDir);

        // 如果是新增目录
        if ("" != needAddDir)
        {
            // svn add needAddDir
        }
        else
        {
            // 如果是新增文件
            if (!isFile(getRootPath(), filePath))
            {
                needAddFile2Svn = true;
            }
        }
        
        if (isFile)
        {
            // 拷贝文件
            // copyFile(sourcePath, filePath);
            if (needAddFile2Svn)
            {
                // svn add file
            }
        }

        // 提交到svn
        result = doCommit();

        return result;
    }

    private void copyFile(String sourcePath, String filePath) throws NullPointerException, IOException, FileNotFoundException, SecurityException
    {
        FileInputStream sourceFis = new FileInputStream(new File(sourcePath + "/" + filePath));
        BufferedInputStream sourceBis = new BufferedInputStream(sourceFis);

        FileOutputStream destFos = new FileOutputStream(new File(getRootPath() + "/" + filePath));
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

    private String getNeedAddDir(String sourcePath, String filePath) throws NullPointerException, SecurityException
    {
        File fp = new File(getRootPath() + "/" + filePath);
        String path = fp.getAbsolutePath();
        if (isFile(sourcePath, filePath))
        {
            path = fp.getParent();
        }

        String result = getAddDir(path);

        int len = getRootPath().length() + 1;
        if (len > result.length())
        {
            len = result.length();
        }

        return result.substring(len);
    }

    private String getAddDir(String filePath) throws NullPointerException, SecurityException
    {
        String result = "";
        File fp = new File(filePath);

        if (!fp.exists())
        {
            String ppath = fp.getParent();
            File pfp = new File(ppath);
            if (pfp.exists())
            {
                result = fp.getAbsolutePath();
            }
            else
            {
                result = getAddDir(ppath);
            }
        }

        return result;
    }
}
