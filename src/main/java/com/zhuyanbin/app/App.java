package com.zhuyanbin.app;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

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

            FileChecker.checkPathIsDirectory(config.getLogPath());
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

        // 启动监控文件进程
    }
}
