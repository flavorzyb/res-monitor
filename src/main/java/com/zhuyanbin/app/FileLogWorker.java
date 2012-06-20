package com.zhuyanbin.app;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Vector;

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
    private SvnWorker        _svnWorker;

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

    protected void setStatus(int status)
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

    protected void doingLogToLogFile()
    {
        File fp = null;

        FileInputStream in = null;
        BufferedInputStream inBis = null;
        FileOutputStream fos = null;

        try
        {
            fp = new File(getDoingLogPath());
            if (fp.exists())
            {
                in = new FileInputStream(fp);
                inBis = new BufferedInputStream(in);

                fos = new FileOutputStream(getLogPath(), true);

                byte[] buff = new byte[1024 * 5];
                int len = 0;
                while (-1 != (len = inBis.read(buff)))
                {
                    fos.write(buff, 0, len);
                }
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
        finally
        {
            try
            {
                if (null != inBis)
                {
                    inBis.close();
                    inBis = null;
                }

                if (null != in)
                {
                    in.close();
                    in = null;
                }

                if (null != fos)
                {
                    fos.flush();
                    fos.close();
                    fos = null;
                }

                if (null != fp)
                {
                    fp.delete();
                    fp = null;
                }
            }
            catch (Exception ex)
            {
            }
        }
    }

    public SvnWorker getSvnWorker()
    {
        if (null == _svnWorker)
        {
            _svnWorker = new SvnWorker(getSvnWorkConfig());
        }

        return _svnWorker;
    }

    @Override
    public void run() throws IllegalThreadStateException
    {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        while (isLoop())
        {
            try
            {
                if (renameLogPath())
                {
                    setStatus(STATUS_IS_DOING);
                    try
                    {
                        fis = new FileInputStream(getDoingLogPath());
                        isr = new InputStreamReader(fis);
                        br = new BufferedReader(isr);

                        String buf = null;
                        String file;
                        Vector<String> updateFiles = new Vector<String>();
                        while (null != (buf = br.readLine()))
                        {
                            file = getFilePathFromString(buf);
                            updateFiles.add(file);
                        }
                        getSvnWorker().update(getSourcePath(), updateFiles);
                    }
                    catch (Exception ex)
                    {
                        throw ex;
                    }
                    finally
                    {
                        if (null != fis)
                        {
                            fis.close();
                            fis = null;
                        }

                        if (null != isr)
                        {
                            isr.close();
                            isr = null;
                        }

                        if (null != br)
                        {
                            br.close();
                            br = null;
                        }
                    }
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
            finally
            {
                deleteDoingLog();
            }
        }
    }

    protected String getFilePathFromString(String msg)
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

    protected boolean renameLogPath() throws NullPointerException, SecurityException
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

    protected void deleteDoingLog() throws NullPointerException, SecurityException
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
