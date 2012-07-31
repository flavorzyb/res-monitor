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
    private String           _redologPath;
    private String           _doingLogPath;
    private String           _sourcePath;
    private SvnWorkConfig    _swc;
    private ErrorLog         _errorLog;
    private boolean          _isLoop         = true;
    private int              _status         = STATUS_IS_SLEEP;
    private SvnWorker        _svnWorker;

    public FileLogWorker(String logPath, String doingLogPath, String redoLogPath, String sourcePath, ErrorLog errorLog, SvnWorkConfig swc)
    {
        setLogPath(logPath);
        setDoingLogPath(doingLogPath);
        setRedoLogPath(redoLogPath);
        setSourcePath(sourcePath);
        setErrorLog(errorLog);
        setSvnWorkConfig(swc);
    }

    private void setRedoLogPath(String logPath)
    {
        _redologPath = logPath;
    }

    public String getRedoLogPath()
    {
        return _redologPath;
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
            ex.printStackTrace();
            getErrorLog().write(ex.getMessage());
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
                getErrorLog().write(ex.getMessage());
            }
        }
    }

    public SvnWorker getSvnWorker()
    {
        if (null == _svnWorker)
        {
            _svnWorker = new SvnWorker(getSvnWorkConfig(), getRedoLogPath());
        }

        return _svnWorker;
    }

    public void work(String logPath) throws Exception
    {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        BufferedReader br = null;

        setStatus(STATUS_IS_DOING);
        Vector<String> updateFiles = null;
        try
        {
            File fp = new File(logPath);
            if (!fp.isFile())
            {
                return;
            }

            fis = new FileInputStream(logPath);
            isr = new InputStreamReader(fis);
            br = new BufferedReader(isr);

            String buf = null;
            String file;
            updateFiles = new Vector<String>();
            while (null != (buf = br.readLine()))
            {
                file = getFilePathFromString(buf);
                if (pathIsExists(getSourcePath() + "/" + file))
                {
                    updateFiles.add(file);
                }
            }
            getSvnWorker().update(getSourcePath(), updateFiles);

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
        catch (Exception ex)
        {
            writeRedoLog(updateFiles);
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

    @Override
    public void run() throws IllegalThreadStateException
    {
        FullSync fs = new FullSync(getSvnWorkConfig().getWorkCopyPath(), getSourcePath(), getLogPath());
        while (isLoop())
        {
            try
            {
                // 先做redo log
                if (renameRedoLogPath())
                {
                    work(getRedoLogPath());
                }

                // 做全量同步
                fs.rsync();

                if (renameLogPath())
                {
                    work(getDoingLogPath());
                }
                else
                {
                    setStatus(STATUS_IS_SLEEP);
                    sleep(1000);
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
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

    protected boolean renamePath(String logPath) throws NullPointerException, SecurityException
    {
        boolean result = false;

        File doFp = new File(getDoingLogPath());
        if (doFp.exists())
        {
            return false;
        }

        File fp = new File(logPath);
        if (fp.isFile())
        {
            result = fp.renameTo(doFp);
        }

        return result;
    }

    protected boolean renameLogPath() throws NullPointerException, SecurityException
    {
        return renamePath(getLogPath());
    }
    
    protected boolean renameRedoLogPath() throws NullPointerException, SecurityException
    {
        return renamePath(getRedoLogPath());
    }

    protected boolean pathIsExists(String path)
    {
        File fp = new File(path);
        return fp.exists();
    }

    protected void deleteDoingLog() throws NullPointerException, SecurityException
    {
        File fp = new File(getDoingLogPath());
        if (fp.exists())
        {
            fp.delete();
        }
    }

    protected void writeRedoLog(Vector<String> updateFiles)
    {
        if (null != updateFiles)
        {
            int len = updateFiles.size();
            if (len > 0)
            {
                String file = null;

                for (int i = 0; i < len; i++)
                {
                    file = updateFiles.get(i);
                    if (null != file)
                    {
                        Loger.write(getRedoLogPath(), file);
                    }
                }
            }
        }
    }
    
    public boolean isSleep()
    {
        return (getStatus() == STATUS_IS_SLEEP);
    }
}
