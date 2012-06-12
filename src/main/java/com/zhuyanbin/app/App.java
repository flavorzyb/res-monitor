package com.zhuyanbin.app;


/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String configFile = System.getProperty("user.config");

        System.out.println("config file:" + configFile);

        System.out.println("path:" + System.getProperty("java.library.path"));

        ConfigProxy proxy = new ConfigProxy();
        Config config = null;

        // 配置文件检测,检查是否正确
        try
        {
            config = proxy.load(configFile);

            FileChecker.checkPathIsDirectory(config.getSourcePath());
            FileChecker.checkPathIsDirectory(config.getDestPath());

            ErrorLog.getInstance().setLogPath(config.getErrorLogPath());

            // 启动同步处理进程
            FileLogWorker flw = new FileLogWorker(config.getLogPath(), config.getDoingLogPath(), ErrorLog.getInstance());
            flw.start();

            // 启动监控文件进程
            FileWatcher fw = new FileWatcher(config.getSourcePath(), config.getLogPath());
            fw.setWathSubtree(true);
            fw.addWatch();

            while (true)
            {
                Thread.sleep(10000);
            }
        }
        catch (Exception ex)
        {
            ErrorLog.getInstance().write(ex.getMessage());
        }
    }
}
