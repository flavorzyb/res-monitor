package com.zhuyanbin.app;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import net.contentobjects.jnotify.JNotifyException;

import org.xml.sax.SAXException;

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
            FileChecker.checkPathIsFile(config.getSvnBinPath());
            FileChecker.checkPathcanExecute(config.getSvnBinPath());

            // 启动同步处理进程
            FileLogWorker flw = new FileLogWorker(config.getLogPath(), config.getDoingLogPath(), config.getRedoLogPath());
            flw.start();

            // 启动redo同步处理进程
            FileRedoWorker frw = new FileRedoWorker(config.getRedoLogPath(), config.getLogPath());
            frw.start();

            // 启动监控文件进程
            FileWatcher fw = new FileWatcher(config.getSourcePath(), config.getLogPath());
            fw.setWathSubtree(true);
            fw.addWatch();

            while (true)
            {
                Thread.sleep(10000);
            }
        }
        catch (JNotifyException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (IOException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (IllegalArgumentException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (ParserConfigurationException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (SAXException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (NullPointerException ex)
        {
            System.out.println(ex.getMessage());
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
        }
    }
}
