package com.zhuyanbin.app;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

public class Loger
{
    public static void write(String path, String str)
    {
        Date dt = new Date();
        Timestamp ts = new Timestamp(dt.getTime());
        String msg = ts + "|" + str + "\n";

        try
        {
            FileOutputStream fos = new FileOutputStream(path, true);
            fos.write(msg.getBytes());
            fos.flush();
            fos.close();
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}
