package com.zhuyanbin.app;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
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
    private SVNClientManager _scm;
    private SvnWorkConfig    _swc;
    private Vector<SvnItem>  _item;

    public SvnWorker(SvnWorkConfig swc)
    {
        setSvnWorkConfig(swc);
    }
    
    private void setSvnWorkConfig(SvnWorkConfig swc)
    {
        _swc = swc;
    }
    
    public SvnWorkConfig getSvnWorkConfig()
    {
        return _swc;
    }

    public SVNClientManager getSVNClientManager()
    {
        if (null == _scm)
        {
            SVNClientManager scm = SVNClientManager.newInstance();
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(getSvnWorkConfig().getUserName(), getSvnWorkConfig().getPassword());
            scm.setAuthenticationManager(authManager);
            setSVNClientManager(scm);
        }

        return _scm;
    }

    public void setSVNClientManager(SVNClientManager scm)
    {
        _scm = scm;
    }
    private boolean isFile(String sourcePath, String filePath) throws NullPointerException, SecurityException
    {
        File fp = new File(sourcePath + "/" + filePath);
        return fp.isFile();
    }

    private void doCleanUp() throws SVNException, NullPointerException
    {
        getSVNClientManager().getWCClient().doCleanup(new File(getSvnWorkConfig().getWorkCopyPath()));
    }

    private void doUpdate() throws SVNException, NullPointerException
    {
        getSVNClientManager().getUpdateClient().doUpdate(new File(getSvnWorkConfig().getWorkCopyPath()), SVNRevision.HEAD, SVNDepth.INFINITY, true, true);
    }

    private boolean doCommit(String[] filePaths) throws SVNException, NullPointerException
    {
        File[] files;

        if (null == filePaths)
        {
            return false;
        }

        int len = filePaths.length;
        files = new File[len];
        for (int i = 0; i < len; i++)
        {
            files[i] = new File(filePaths[i]);
        }

        String[] changelist = {};
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

    private void addFile2SVN(String sourcePath, String filePath) throws NullPointerException, SecurityException, IOException, SVNException, NoSuchAlgorithmException
    {
        String fullPath = getSvnWorkConfig().getWorkCopyPath() + "/" + filePath;
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
                String parentPath = fp.getParent().substring(getSvnWorkConfig().getWorkCopyPath().length() + 1);
                addFile2SVN(sourcePath, parentPath);
                boolean isFile = isFile(sourcePath, filePath);
                if (isFile)
                {
                    if (!(fp.exists() && Md5CheckSum.md5StringIsSame(sourcePath + "/" + filePath, fullPath)))
                    {
                        copyFile(sourcePath, filePath);
                        _item.add(new SvnItem(fullPath, true, isFile));
                    }
                }
                else
                {
                    fp.mkdir();
                    _item.add(new SvnItem(fullPath, true, isFile));
                }
            }
        }
        else
        {
            boolean isFile = isFile(sourcePath, filePath);
            if (fp.isFile() && isFile && (!Md5CheckSum.md5StringIsSame(sourcePath + "/" + filePath, fullPath)))
            {
                copyFile(sourcePath, filePath);
                _item.add(new SvnItem(fullPath, false, true));
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

    public boolean update(String sourcePath, String[] filePaths) throws SVNException, NullPointerException, IOException, SecurityException, NoSuchAlgorithmException
    {
        boolean result = false;
        // clean up workcopy
        doCleanUp();
        // 先更新整个svn仓库
        doUpdate();

        // 重置svn操作列表
        resetSvnItemQueue();

        // 将文件(目录)拷贝到workcopy
        if (null == filePaths)
        {
            return result;
        }
        
        int i = 0;
        int len = filePaths.length;
        for (i = 0; i < len; i++)
        {
            addFile2SVN(sourcePath, filePaths[i]);
        }

        // 添加svn事件
        if (_item.size() < 1)
        {
            return result;
        }

        String commitFilePaths[] = new String[_item.size()];
        i = 0;
        for (SvnItem si : _item)
        {
            if (si.isAdd())
            {
                doAdd(si.getPath(), si.isFile());
            }

            commitFilePaths[i] = si.getPath();
            i++;
        }

        result = doCommit(commitFilePaths);

        return result;
    }

    protected void copyFile(String sourcePath, String filePath) throws NullPointerException, IOException, SecurityException
    {
        FileInputStream sourceFis = new FileInputStream(new File(sourcePath + "/" + filePath));
        BufferedInputStream sourceBis = new BufferedInputStream(sourceFis);

        FileOutputStream destFos = new FileOutputStream(new File(getSvnWorkConfig().getWorkCopyPath() + "/" + filePath));
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
