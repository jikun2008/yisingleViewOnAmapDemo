package com.map.test.study.demo.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * @author jikun
 * Created by jikun on 2018/6/12.
 */
public class BaseActivity extends AppCompatActivity {

    private ProgressDialog alertDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alertDialog = new ProgressDialog(this);
        alertDialog.setMessage("加载中");
    }


    protected void showLoadingDialog() {

        alertDialog.show();


    }

    protected void dissmissLoadingDialog() {
        alertDialog.dismiss();
    }
}
