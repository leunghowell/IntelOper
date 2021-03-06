package com.jiubai.inteloper.ui.activity;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.jiubai.inteloper.R;
import com.jiubai.inteloper.common.UtilBox;
import com.jiubai.inteloper.net.VolleyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.ButterKnife;

;

/**
 * Created by Larry Howell on 2016/9/26.
 *
 * 意见反馈activity
 */

public class FeedbackActivity extends BaseActivity {
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.editText)
    EditText mEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_feedback);

        ButterKnife.bind(this);

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_feedback, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                UtilBox.toggleSoftInput(mEditText, false);
                if ("".equals(mEditText.getText().toString())) {
                    UtilBox.showSnackbar(this, "请输入反馈内容");
                } else {
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setCancelable(false);
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setMessage("正在上传反馈内容");
                    progressDialog.show();

                    String deviceInfo = Build.VERSION.SDK_INT + "_"
                            + UtilBox.getPackageInfo(this).versionCode + "_"
                            + android.os.Build.MODEL;

                    Log.i("info", deviceInfo);

                    String[] keys = {"a", "content", "equipment", "cookie_secretid", "site_name"};
                    String[] values = {"app_feedback", mEditText.getText().toString(), deviceInfo, "aea8ccf0dfd3988c7feab064d087ccc3", "dyjk"};

                    VolleyUtil.request("http://ucenter.jiubaiwang.cn/app_api.php", keys, values,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String s) {
                                    Log.i("info", s);

                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                        }
                                    }, 500);

                                    try {
                                        JSONObject jsonObject = new JSONObject(s);

                                        int code = jsonObject.getInt("code");
                                        String info = jsonObject.getString("msg");
                                        UtilBox.showSnackbar(FeedbackActivity.this, info);

                                        if (code == 200) {
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    finish();
                                                }
                                            }, 1500);
                                        }
                                    } catch (JSONException e) {
                                        UtilBox.showSnackbar(FeedbackActivity.this, e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    progressDialog.dismiss();

                                    UtilBox.showSnackbar(FeedbackActivity.this, "上传失败，请重试");

                                    Log.i("info", volleyError.getMessage());
                                }
                            });
                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FeedbackActivity.this.onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        UtilBox.returnActivity(this);
    }
}

