package com.zhuyanbin.app;

import java.io.File;
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
        if (args.length < 1)
        {
            System.out.println("need config file path.");
            return;
        }

        ConfigProxy proxy = new ConfigProxy();
        Config config = null;

        try
        {
            config = proxy.load(args[0]);

            checkPathIsDirectory(config.getSourcePath());
            checkPathIsDirectory(config.getDestPath());
            checkPathIsFile(config.getSvnBinPath());
            checkPathcanExecute(config.getSvnBinPath());

            checkPathIsDirectory(config.getLogPath());
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

    private static void checkPathIsDirectory(String path) throws NullPointerException, SecurityException, Exception
    {
        File fp = new File(path);
        if (!fp.isDirectory())
        {
            throw new Exception(fp.getAbsolutePath() + " is not a Directory.");
        }
    }

    private static void checkPathIsFile(String path) throws NullPointerException, SecurityException, Exception
    {
        File fp = new File(path);
        if (!fp.isFile())
        {
            throw new Exception(fp.getAbsolutePath() + " is not a file.");
        }
    }

    private static void checkPathcanExecute(String path) throws NullPointerException, SecurityException, Exception
    {
        File fp = new File(path);
        if (!fp.canExecute())
        {
            throw new Exception(fp.getAbsolutePath() + " is not a file.");
        }
    }
}
