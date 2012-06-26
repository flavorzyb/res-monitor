package com.zhuyanbin.app;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AppChecker
{
    private String _pidFile;

    public AppChecker(String pidFile)
    {
        setPidFile(pidFile);
    }

    public boolean hasRunning()
    {
        boolean result = false;
        try
        {
            long appid = readPidFromFile();
            if (appid > 0)
            {
                result = pidIsExists(appid);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }

        return result;
    }

    public long writePid2File() throws IOException
    {
        long result = getPID();

        File fp = new File(getPidFile());
        if (fp.exists())
        {
            fp.delete();
        }

        FileOutputStream fos = new FileOutputStream(getPidFile());
        Long pid = new Long(result);
        fos.write(pid.toString().getBytes());
        fos.flush();
        fos.close();
        return result;
    }

    public String getPidFile()
    {
        return _pidFile;
    }

    private void setPidFile(String pidFile)
    {
        _pidFile = pidFile;
    }

    public static long getPID()
    {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }
    
    protected String getOS()
    {
        return System.getProperty("os.name");
    }

    protected boolean pidIsExists(long pid) throws IOException
    {
        boolean result = false;
        String os = getOS();
        String cmd = "ps -p " + pid;

        if (os.indexOf("Windows") >= 0)
        {
            cmd = "tasklist";
        }
        else
        {
            InputStream is = Runtime.getRuntime().exec(cmd).getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String str = null;
            String[] ss = null;
            while (null != (str = br.readLine()))
            {
                ss = str.split("\\s");
                try
                {
                    if ((ss.length > 2) && (ss[1].length() > 0) && (Long.parseLong(ss[1]) == pid))
                    {
                        result = true;
                        break;
                    }
                }
                catch (NumberFormatException ex)
                {
                    ex.printStackTrace();
                }

            }

            br.close();
            is.close();
        }

        return result;
    }

    protected long readPidFromFile() throws IOException
    {
        long result = -1;
        File fp = new File(getPidFile());
        if (fp.exists() && fp.isFile())
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fp)));
            String str = br.readLine();
            br.close();
            result = Long.parseLong(str);
        }
        return result;
    }
}
