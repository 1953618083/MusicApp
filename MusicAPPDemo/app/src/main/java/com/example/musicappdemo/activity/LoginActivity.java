package com.example.musicappdemo.activity;

import static com.example.musicappdemo.activity.MainActivity.HTTP_CLIENT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicappdemo.R;
import com.example.musicappdemo.utils.NetWorkAPi;
import com.example.musicappdemo.utils.SharedPreferenceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;    //登录按钮
    private TextView btn_register; //注册按钮
    private EditText et_account; //账号输入框
    private EditText et_password;//密码输入框

    private boolean isRefuse = false;
    static final int REQUEST_CODE = 66;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestPermissions();
        SharedPreferenceUtil.init(this, "user_preference");
        String account = SharedPreferenceUtil.getString("account", "");
        if (!TextUtils.isEmpty(account)){
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            //创建意图对象，进行跳转
            startActivity(intent);
            //销毁该活动
            finish();
        }
        initView();
    }

    private void requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !isRefuse) {
            if (!Environment.isExternalStorageManager()) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CAMERA}, REQUEST_CODE);
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                this.startActivityForResult(intent, 1024);
            }
        }
    }

    public void initView() {
        //去除标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定控件
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);

        //匿名内部类方式实现按钮点击事件
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = et_account.getText().toString().trim();
                String pass = et_password.getText().toString().trim();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, String> loginMap = new HashMap<>();
                            loginMap.put("account", acc);
                            loginMap.put("password", pass);

                            JSONObject jsonObject = new JSONObject(loginMap);

                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                            Request request = new Request.Builder()
                                    .url(NetWorkAPi.doLogin)
                                    .method("POST", body)
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Accept", "application/json")
                                    .build();
                            System.out.println("timeout"+NetWorkAPi.doLogin);
                            Log.d("LOGIN", "Using endpoint: " + NetWorkAPi.doLogin);
                            Response response = null;
                            response = HTTP_CLIENT.newCall(request).execute();
                            String json = response.body().string();
                            JSONObject resultObject = new JSONObject(json);
                            int code = resultObject.getInt("code");
                            String msg = resultObject.getString("msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (code == 200) {
                                            JSONObject data = resultObject.getJSONObject("data");
                                            String account = data.getString("account");
                                            String userId = data.getString("userId");
                                            SharedPreferenceUtil.putString("account", account);
                                            SharedPreferenceUtil.putString("userId", userId);
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            //创建意图对象，进行跳转
                                            startActivity(intent);
                                            //销毁该活动
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }
                                }
                            });

                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).start();

            }
        });
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            //判断结果码是否等于1，等于1则接受返回数据。
            if (requestCode == 1 && resultCode == 1) {
                String name = data.getStringExtra("acc");
                String password = data.getStringExtra("pass");
                et_account.setText(name);
                et_password.setText(password);
            }
        }
    }


}