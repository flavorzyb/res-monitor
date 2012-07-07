package com.zhuyanbin.app;


/**
 * 启动程序
 */
public class App
{
    public static void main( String[] args )
    {
        String configFile = System.getProperty("user.config");

        ConfigProxy proxy = new ConfigProxy();
        Config config = null;

        // 配置文件检测,检查是否正确
        try
        {
            config = proxy.load(configFile);

            FileChecker.checkPathIsDirectory(config.getSourcePath());
            FileChecker.checkPathIsDirectory(config.getDestPath());

            AppChecker appChecker = new AppChecker(config.getPidPath());
            if (appChecker.hasRunning())
            {
                System.out.println("Application is running now.");
                return;
            }

            appChecker.writePid2File();

            ErrorLog.getInstance().setLogPath(config.getErrorLogPath());

            // svn work config
            SvnWorkConfig swc = new SvnWorkConfig(config.getDestPath(), config.getUserName(), config.getPassword());

            // 启动同步处理进程
            FileLogWorker flw = new FileLogWorker(config.getLogPath(), config.getDoingLogPath(), config.getRedoLogPath(), config.getSourcePath(), ErrorLog.getInstance(), swc);
            flw.start();

            // 启动监控文件进程
            FileWatcher fw = new FileWatcher(config.getSourcePath(), config.getLogPath());
            fw.setWathSubtree(true);
            fw.addWatch();

            flw.join();
        }
        catch (Exception ex)
        {
            ErrorLog.getInstance().write(ex.getMessage());
        }
    }

    private static long getPID()
    {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }

    private static boolean isRunning()
    {
        return false;
    }
}
