package com.map.test.study.demo;

import android.app.Application;

import com.tencent.bugly.crashreport.CrashReport;

/**
 * @author jikun
 * Created by jikun on 2018/7/23.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "ab842b3d51", true);
    }
}
