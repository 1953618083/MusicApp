package com.example.musicappdemo.page;

import static android.app.Activity.RESULT_OK;
import static com.example.musicappdemo.page.SongPage.HTTP_CLIENT;
import static com.example.musicappdemo.page.SongPage.musicPicList;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.musicappdemo.R;
import com.example.musicappdemo.activity.LoginActivity;
import com.example.musicappdemo.activity.MainActivity;
import com.example.musicappdemo.activity.RegisterActivity;
import com.example.musicappdemo.entity.vo.MusicVO;
import com.example.musicappdemo.utils.FileHelper;
import com.example.musicappdemo.utils.NetWorkAPi;
import com.example.musicappdemo.utils.SharedPreferenceUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class UserInfoFragment extends Fragment {
    private View view;

    private Button submitButton;
    private EditText edtAccount;
    private EditText edtPassword;
    private EditText edtName;
    private EditText edtSex;
    private EditText edtAge;

    private ImageView avatarView;

    private Button btnSubmit;

    private String avatarUrl = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_user_info_page, container, false);
        SharedPreferenceUtil.init(getContext(), "user_preference");

        initView(view);
        return view;
    }

    private void initView(View view) {
        edtAccount = view.findViewById(R.id.et_account);
        edtPassword = view.findViewById(R.id.et_password);
        edtName = view.findViewById(R.id.et_name);
        edtSex = view.findViewById(R.id.et_sex);
        edtAge = view.findViewById(R.id.et_age);
        avatarView = view.findViewById(R.id.avatarImg);
        btnSubmit = view.findViewById(R.id.btn_submit);
        avatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, 1);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String acc = edtAccount.getText().toString().trim();
                String pass = edtPassword.getText().toString().trim();
                String age = edtAge.getText().toString().trim();
                String sex = edtSex.getText().toString().trim();
                String name = edtName.getText().toString().trim();
                if (TextUtils.isEmpty(acc)) {
                    Toast.makeText(getContext(), "账号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pass)) {
                    Toast.makeText(getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(age)) {
                    Toast.makeText(getContext(), "年龄不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(sex)) {
                    Toast.makeText(getContext(), "性别不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getContext(), "姓名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Map<String, Object> registerMap = new HashMap<>();
                            registerMap.put("id", SharedPreferenceUtil.getString("userId", ""));
                            registerMap.put("account", acc);
                            registerMap.put("password", pass);
                            registerMap.put("name", name);
                            registerMap.put("sex", sex);
                            registerMap.put("age", Integer.valueOf(age));
                            registerMap.put("avatar", avatarUrl);
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (code == 200) {
                                        initUser();
                                        Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT).show();
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

        initUser();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String path = FileHelper.getFileAbsolutePath(getContext(), uri);
            File file = new File(path);
            if (file.exists() && file != null) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            RequestBody requestBody = new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                                    .build();

                            Request request = new Request.Builder()
                                    .url(NetWorkAPi.uploadAvatar) // 替换为您的上传图片的URL
                                    .post(requestBody)
                                    .build();

                            Response response = HTTP_CLIENT.newCall(request).execute();

                            String json = response.body().string();
                            JSONObject resultObject = new JSONObject(json);
                            int code = resultObject.getInt("code");
                            String msg = resultObject.getString("msg");
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        if (code == 200) {
                                            String data = resultObject.getString("data");
                                            avatarUrl = data;
                                            Glide.with(getContext()).load(data).transform(new CircleCrop()).into(avatarView);
                                        } else {
                                            Toast.makeText(getActivity(), msg + "", Toast.LENGTH_SHORT).show();
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
        }
    }


    private void initUser() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "");

                    Request request = new Request.Builder().url(NetWorkAPi.userInfo + SharedPreferenceUtil.getString("account", "")).method("POST", body).addHeader("Content-Type", "application/json").addHeader("Accept", "application/json").build();
                    Response response = null;
                    response = HTTP_CLIENT.newCall(request).execute();
                    String json = response.body().string();
                    JSONObject resultObject = new JSONObject(json);
                    JSONObject data = resultObject.getJSONObject("data");
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String name = data.getString("name");
                                String account = data.getString("account");
                                String password = data.getString("password");
                                String sex = data.getString("sex");
                                int age = data.getInt("age");
                                String avatar = data.getString("avatar");
                                edtName.setText(name + "");
                                edtAccount.setText(account + "");
                                edtPassword.setText(password + "");
                                edtSex.setText(sex + "");
                                edtAge.setText(age + "");
                                if (!TextUtils.isEmpty(avatar)) {
                                    avatarUrl = avatar;
                                    Glide.with(getContext()).load(avatar).transform(new CircleCrop()).into(avatarView);
                                }else {
                                    avatarView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();


    }
}