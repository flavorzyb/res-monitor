package com.zhuyanbin.app;

import java.io.File;

import org.tmatesoft.svn.core.SVNCommitInfo;
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

            boolean force = true;
            boolean mkdir = true;
            boolean climbUnversionedParents = false;
            boolean includeIgnored = false;
            boolean makeParents = false;
            scm.getWCClient().doAdd(new File(path + "/ddd"), force, mkdir, climbUnversionedParents, SVNDepth.INFINITY, includeIgnored, makeParents);

            scm.getWCClient().doAdd(new File(path + "/ddd/kkk"), force, mkdir, climbUnversionedParents, SVNDepth.INFINITY, includeIgnored, makeParents);

            scm.getWCClient().doAdd(new File(path + "/ddd/kkk/pp.txt"), force, false, climbUnversionedParents, SVNDepth.INFINITY, includeIgnored, makeParents);

            File[] files = { new File(path + "/ddd"), new File(path + "/ddd/kkk"), new File(path + "/ddd/kkk/pp.txt") };
            String[] changelist = {};
            SVNCommitInfo sci = scm.getCommitClient().doCommit(files, false, "test111", null, changelist, true, true, SVNDepth.INFINITY);
            System.out.println("rev:" + sci.getNewRevision());
        }
        catch (SVNException e)
        {
            e.printStackTrace();
        }

    }
}
