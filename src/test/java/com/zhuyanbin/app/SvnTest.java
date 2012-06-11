package com.zhuyanbin.app;

import java.io.File;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class SvnTest
{
    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String path = "/Users/flavor/resource/svn_work";
        try
        {
            // SVNRepository sr =
            // SVNRepositoryFactory.create(SVNURL.parseURIDecoded(path));
            SVNClientManager scm = SVNClientManager.newInstance();
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager("yanbin", "123456");
            scm.setAuthenticationManager(authManager);
            scm.getUpdateClient().doUpdate(new File(path), SVNRevision.HEAD, SVNDepth.INFINITY, true, true);

            // scm.getStatusClient().do

            File[] files = { new File(path) };
            String[] changelist = { "" };
            scm.getCommitClient().doCommit(files, false, "test111", null, changelist, true, true, SVNDepth.INFINITY);
        }
        catch (SVNException e)
        {
            e.printStackTrace();
        }

    }
}
