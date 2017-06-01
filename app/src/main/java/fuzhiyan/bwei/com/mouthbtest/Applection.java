package fuzhiyan.bwei.com.mouthbtest;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/5/28.
 * time:
 * author:付智焱
 */

public class Applection extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
