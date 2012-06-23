package com.zhuyanbin.app;

import org.tanukisoftware.wrapper.WrapperListener;
import org.tanukisoftware.wrapper.WrapperManager;

public class ResourceMonitorService implements WrapperListener
{
    private MyApp _app;

    private MyApp getApp()
    {
        if (null == _app)
        {
            _app = new MyApp();
        }

        return _app;
    }
    @Override
    public void controlEvent(int event)
    {
        if (!((event == WrapperManager.WRAPPER_CTRL_LOGOFF_EVENT) && (WrapperManager.isLaunchedAsService() || WrapperManager.isIgnoreUserLogoffs())))
        {
            WrapperManager.stop(0);
        }
    }

    @Override
    public Integer start(String[] args)
    {
        getApp().start();
        return null;
    }

    @Override
    public int stop(int exitCode)
    {
        getApp().stopMonitor();
        return exitCode;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        WrapperManager.start(new ResourceMonitorService(), args);
    }
}
