package com.zhuyanbin.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5CheckSum
{
    protected static char          hexDigits[]   = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest _messageDigest = null;

    private static MessageDigest getMessageDigest() throws NoSuchAlgorithmException
    {
        if (null == _messageDigest)
        {
            _messageDigest = MessageDigest.getInstance("MD5");
        }

        return _messageDigest;
    }

    public static String getFileMd5(String filePath) throws IOException, NoSuchAlgorithmException
    {
        File fp = new File(filePath);
        FileInputStream in = new FileInputStream(fp);

        ByteArrayOutputStream baos = new ByteArrayOutputStream((int) fp.length());
        int len = 0;
        byte[] cache = new byte[1048576];
        while (-1 != (len = in.read(cache)))
        {
            baos.write(cache, 0, len);
        }

        getMessageDigest().update(baos.toByteArray());
        in.close();
        in = null;
        fp = null;
        return bufferToHex(getMessageDigest().digest());
    }

    public static boolean md5StringIsSame(String sourceFilePath, String destFilePath) throws IOException, NoSuchAlgorithmException
    {
        if (!(isFile(sourceFilePath) && isFile(destFilePath)))
        {
            return false;
        }

        return (getFileMd5(sourceFilePath).equals(getFileMd5(destFilePath)));
    }

    private static boolean isFile(String path)
    {
        boolean result = false;
        File fp = null;
        try
        {
            fp = new File(path);
            result = fp.isFile();
            if (null != fp)
            {
                fp = null;
            }
        }
        catch (NullPointerException ex)
        {
        }
        catch (SecurityException ex)
        {
        }
        finally
        {
            if (null != fp)
            {
                fp = null;
            }
        }

        return result;
    }

    private static String bufferToHex(byte bytes[])
    {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n)
    {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++)
        {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer)
    {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
