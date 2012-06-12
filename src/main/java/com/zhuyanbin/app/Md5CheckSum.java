package com.zhuyanbin.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class Md5CheckSum
{
    protected static char          hexDigits[]   = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    protected static MessageDigest messageDigest = null;
    static
    {
        try
        {
            messageDigest = MessageDigest.getInstance("MD5");
        }
        catch (NoSuchAlgorithmException e)
        {
            System.err.println(Md5CheckSum.class.getName() + "初始化失败，MessageDigest不支持MD5Util.");
            e.printStackTrace();
        }
    }

    public static String getFileMd5(String filePath) throws IOException
    {
        FileInputStream in = new FileInputStream(filePath);
        FileChannel ch = in.getChannel();
        MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, filePath.length());
        messageDigest.update(byteBuffer);
        return bufferToHex(messageDigest.digest());
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
