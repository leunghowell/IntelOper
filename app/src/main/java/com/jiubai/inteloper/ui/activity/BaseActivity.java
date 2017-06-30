package com.jiubai.inteloper.ui.activity;

import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.MobclickAgent;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}