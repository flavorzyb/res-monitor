package com.zhuyanbin.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;

import org.tmatesoft.svn.core.SVNCommitInfo;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SvnWorker
{
    private String _userName;
    private String _password;
    private String _svnUrl;
    private String _rootPath;
    private SVNClientManager _scm;

    private Vector<SvnItem>  _item;

    public SvnWorker(String svnUrl, String rootPath, String username, String password)
    {
        setSvnUrl(svnUrl);
        setRootPath(rootPath);
        setUserName(username);
        setPassword(password);
    }

    public SVNClientManager getSVNClientManager()
    {
        if (null == _scm)
        {
            SVNClientManager scm = SVNClientManager.newInstance();
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(getUserName(), getPassword());
            scm.setAuthenticationManager(authManager);
            setSVNClientManager(scm);
        }

        return _scm;
    }

    public void setSVNClientManager(SVNClientManager scm)
    {
        _scm = scm;
    }

    private void setUserName(String username)
    {
        _userName = username;
    }

    public String getUserName()
    {
        return _userName;
    }

    private void setPassword(String password)
    {
        _password = password;
    }

    public String getPassword()
    {
        return _password;
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

    private void doUpdate() throws SVNException, NullPointerException
    {
        getSVNClientManager().getUpdateClient().doUpdate(new File(getRootPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
    }

    private boolean doCommit(String filePath) throws SVNException, NullPointerException
    {
        File[] files = { new File(filePath) };
        String[] changelist = { "" };
        SVNCommitInfo sci = getSVNClientManager().getCommitClient().doCommit(files, false, "auto commit by system", null, changelist, true, true, SVNDepth.INFINITY);
        return (sci.getNewRevision() > 0);
    }

    private void doAdd(String filePath, boolean isFile) throws SVNException
    {
        boolean force = true;
        boolean mkdir = (!isFile);
        boolean climbUnversionedParents = false;
        boolean includeIgnored = false;
        boolean makeParents = false;

        getSVNClientManager().getWCClient().doAdd(new File(filePath), force, mkdir, climbUnversionedParents, SVNDepth.INFINITY, includeIgnored, makeParents);
    }

    private void addFile2SVN(String sourcePath, String filePath) throws NullPointerException, SecurityException, IOException, SVNException
    {
        String fullPath = getRootPath() + "/" + filePath;
        File fp = new File(fullPath);
        if (!fp.exists())
        {
            File pfp = new File(fp.getParent());

            if (pfp.exists())
            {
                if (isFile(sourcePath, filePath))
                {
                    copyFile(sourcePath, filePath);
                    _item.add(new SvnItem(fullPath, true, true));
                }
                else
                {
                    fp.mkdir();
                    _item.add(new SvnItem(fullPath, true, false));
                }
            }
            else
            {
                String parentPath = fp.getParent().substring(getRootPath().length() + 1);
                addFile2SVN(sourcePath, parentPath);
                pfp.mkdir();
                boolean isFile = isFile(sourcePath, filePath);
                _item.add(new SvnItem(fullPath, true, isFile));
            }
        }
        else
        {
            if (fp.isFile())
            {
                copyFile(sourcePath, filePath);
                _item.add(new SvnItem(fullPath + "_4", false, true));
            }
        }
    }

    private void resetSvnItemQueue()
    {
        if (null != _item)
        {
            _item.clear();
        }
        else
        {
            _item = new Vector<SvnItem>();
        }
    }

    public boolean update(String sourcePath, String filePath)
    {
        boolean result = false;
        try
        {
            // 先更新整个svn仓库
            doUpdate();
    
            // 重置svn操作列表
            resetSvnItemQueue();

            // 将文件(目录)拷贝到workcopy
            addFile2SVN(sourcePath, filePath);

            // 添加svn事件
            System.out.println("size:" + _item.size());
            for (SvnItem si : _item)
            {
                System.out.println("path:" + si.getPath() + ", isadd:" + si.isAdd() + ", isFile:" + si.isFile());
            }
        }
        catch (SVNException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (NullPointerException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (SecurityException ex)
        {
            System.out.println(ex.getMessage());
        }
        
        return result;
    }

    private void copyFile(String sourcePath, String filePath) throws NullPointerException, IOException, SecurityException
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
}
