package com.example.musicappdemo.activity;

import static com.example.musicappdemo.activity.MainActivity.HTTP_CLIENT;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicappdemo.R;
import com.example.musicappdemo.utils.NetWork;
import com.example.musicappdemo.utils.NetWorkAPi;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etAccount;
    private EditText etPassword;
    private EditText etSex;
    private EditText etAge;
    private EditText etName;
    private TextView btnLogin;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    public void initView() {
        //去除标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //绑定控件
        etAccount = findViewById(R.id.et_account);
        etPassword = findViewById(R.id.et_password);
        etSex = findViewById(R.id.et_sex);
        etAge = findViewById(R.id.et_age);
        etName = findViewById(R.id.et_name);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //点击事件
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String acc = etAccount.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String age = etAge.getText().toString().trim();
                String sex = etSex.getText().toString().trim();
                String name = etName.getText().toString().trim();
                if (TextUtils.isEmpty(acc)) {
                    Toast.makeText(RegisterActivity.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(RegisterActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(RegisterActivity.this, "年龄不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(sex)) {
                    Toast.makeText(RegisterActivity.this, "性别不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(RegisterActivity.this, "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, Object> registerMap = new HashMap<>();
                            registerMap.put("account", acc);
                            registerMap.put("password", pass);
                            registerMap.put("name", name);
                            registerMap.put("sex", sex);
                            registerMap.put("age", Integer.valueOf(age));
                            registerMap.put("avatar", "");
                            registerMap.put("status", 1);

                            JSONObject jsonObject = new JSONObject(registerMap);

                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create(mediaType, jsonObject.toString());
                            Request request = new Request.Builder()
                                    .url(NetWorkAPi.doRegister)
                                    .method("POST", body)
                                    .addHeader("Content-Type", "application/json")
                                    .addHeader("Accept", "application/json")
                                    .build();
                            Response response = null;
                            response = HTTP_CLIENT.newCall(request).execute();
                            String json = response.body().string();
                            JSONObject resultObject = new JSONObject(json);
                            int code = resultObject.getInt("code");
                            String msg = resultObject.getString("msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (code == 200) {
                                        Toast.makeText(RegisterActivity.this, "注册成功！", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
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
    }
}