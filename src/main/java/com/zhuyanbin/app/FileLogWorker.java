package com.zhuyanbin.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

public class FileLogWorker extends Thread
{
    private final static int STATUS_IS_DOING = 0;

    private final static int STATUS_IS_SLEEP = 1;

    private String           _logPath;
    private String           _doingLogPath;
    private String           _sourcePath;
    private SvnWorkConfig    _swc;
    private ErrorLog         _errorLog;
    private boolean          _isLoop         = true;
    private int              _status         = STATUS_IS_SLEEP;

    public FileLogWorker(String logPath, String doingLogPath, String sourcePath, ErrorLog errorLog, SvnWorkConfig swc)
    {
        setLogPath(logPath);
        setDoingLogPath(doingLogPath);
        setSourcePath(sourcePath);
        setErrorLog(errorLog);
        setSvnWorkConfig(swc);
    }

    private void setLogPath(String logPath)
    {
        _logPath = logPath;
    }

    public String getLogPath()
    {
        return _logPath;
    }

    private void setSourcePath(String sourcePath)
    {
        _sourcePath = sourcePath;
    }

    public String getSourcePath()
    {
        return _sourcePath;
    }

    private void setDoingLogPath(String logPath)
    {
        _doingLogPath = logPath;
    }

    public String getDoingLogPath()
    {
        return _doingLogPath;
    }

    private void setSvnWorkConfig(SvnWorkConfig swc)
    {
        _swc = swc;
    }

    public SvnWorkConfig getSvnWorkConfig()
    {
        return _swc;
    }

    private void setErrorLog(ErrorLog errorLog)
    {
        _errorLog = errorLog;
    }

    public ErrorLog getErrorLog()
    {
        return _errorLog;
    }

    private void setStatus(int status)
    {
        switch (status)
        {
            case STATUS_IS_DOING:
            case STATUS_IS_SLEEP:
                break;
            default:
                status = getStatus();
        }

        _status = status;
    }

    public int getStatus()
    {
        return _status;
    }

    @Override
    public void start()
    {
        doingLogToLogFile();
        super.start();
    }

    private void doingLogToLogFile()
    {
        File fp = new File(getDoingLogPath());
        try
        {
            if (fp.exists())
            {
                FileInputStream in = new FileInputStream(fp);
                BufferedInputStream inBis = new BufferedInputStream(in);

                FileOutputStream fos = new FileOutputStream(getLogPath(), true);

                byte[] buff = new byte[1024 * 5];
                int len = 0;
                while (-1 != (len = inBis.read(buff)))
                {
                    fos.write(buff, 0, len);
                }

                inBis.close();
                in.close();
                fos.flush();
                fos.close();

                fp.delete();
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void run() throws IllegalThreadStateException
    {
        while (isLoop())
        {
            try
            {
                if (renameLogPath())
                {
                    setStatus(STATUS_IS_DOING);
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(getDoingLogPath())));
                    String buf = null;
                    while (null != (buf = br.readLine()))
                    {
                        String file = getFilePathFromString(buf);
                        SvnWorker sw = new SvnWorker(getSvnWorkConfig());
                        sw.update(getSourcePath(), file);
                    }

                    deleteDoingLog();
                }
                else
                {
                    setStatus(STATUS_IS_SLEEP);
                    sleep(1000);
                }
            }
            catch (Exception ex)
            {
                getErrorLog().write(ex.getMessage());
            }
        }
    }

    private String getFilePathFromString(String msg)
    {
        String result = "";
        if (null != msg)
        {
            int index = msg.indexOf("|");
            if (index >= 0)
            {
                if (index >= msg.length())
                {
                    index = msg.length() -1;
                }

                result = msg.substring(index + 1);
            }
        }

        return result.trim();
    }

    public void setIsLoop(boolean isLoop)
    {
        _isLoop = isLoop;
    }

    public boolean isLoop()
    {
        return _isLoop;
    }

    private boolean renameLogPath() throws NullPointerException, SecurityException
    {
        boolean result = false;

        File doFp = new File(getDoingLogPath());
        if (doFp.exists())
        {
            return false;
        }

        File fp = new File(getLogPath());
        if (fp.isFile())
        {
            result = fp.renameTo(doFp);
        }

        return result;
    }

    private void deleteDoingLog() throws NullPointerException, SecurityException
    {
        File fp = new File(getDoingLogPath());
        if (fp.exists())
        {
            fp.delete();
        }
    }

    public boolean isSleep()
    {
        return (getStatus() == STATUS_IS_SLEEP);
    }
}
