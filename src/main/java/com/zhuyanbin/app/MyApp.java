package com.zhuyanbin.app;

public class MyApp extends Thread
{
    private FileLogWorker flw = null;
    private FileWatcher   fw  = null;
    @Override
    public void start()
    {
        setDaemon(false);
        String configFile = System.getProperty("user.config");

        ConfigProxy proxy = new ConfigProxy();
        Config config = null;

        // 配置文件检测,检查是否正确
        try
        {
            config = proxy.load(configFile);

            FileChecker.checkPathIsDirectory(config.getSourcePath());
            FileChecker.checkPathIsDirectory(config.getDestPath());

            ErrorLog.getInstance().setLogPath(config.getErrorLogPath());

            // svn work config
            SvnWorkConfig swc = new SvnWorkConfig(config.getDestPath(), config.getUserName(), config.getPassword());

            // 启动同步处理进程
            flw = new FileLogWorker(config.getLogPath(), config.getDoingLogPath(), config.getSourcePath(), ErrorLog.getInstance(), swc);
            flw.start();

            // 启动监控文件进程
            fw = new FileWatcher(config.getSourcePath(), config.getLogPath());
            fw.setWathSubtree(true);
            fw.addWatch();

            super.start();
        }
        catch (Exception ex)
        {
            ErrorLog.getInstance().write(ex.getMessage());
        }
    }

    @Override
    public void run()
    {
        try
        {
            if (flw instanceof FileLogWorker)
            {
                flw.join();
            }
        }
        catch (Exception ex)
        {
            ErrorLog.getInstance().write(ex.getMessage());
        }
    }

    public void stopMonitor()
    {
        try
        {
            if (fw instanceof FileWatcher)
            {
                fw.removeWatch();
            }

            if (flw instanceof FileLogWorker)
            {
                flw.setIsLoop(false);
            }
        }
        catch (Exception ex)
        {
            ErrorLog.getInstance().write(ex.getMessage());
        }
    }
}