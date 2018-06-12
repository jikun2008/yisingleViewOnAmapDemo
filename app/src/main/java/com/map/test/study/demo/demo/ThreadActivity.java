package com.map.test.study.demo.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.map.test.study.demo.R;
import com.yisingle.amapview.lib.utils.CustomAnimator;


/**
 * @author jikun
 * Created by jikun on 2018/6/6.
 */
public class ThreadActivity extends Activity {

    private CustomAnimator customAnimator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_thread);
        customAnimator = new CustomAnimator();
    }

    public void start(View view) {
        customAnimator.start();

    }


    public void cancle(View view) {
        customAnimator.end();

    }
}
