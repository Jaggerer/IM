package com.example.im.login;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.im.Constant;
import com.example.im.MainActivity;
import com.example.im.R;
import com.example.im.client.ConnectionService;
import com.example.im.client.http.CommonJsonCallback;
import com.example.im.client.http.CommonOkhttpClient;
import com.example.im.client.http.CommonRequest;
import com.example.im.client.http.DisposeDataHandle;
import com.example.im.client.http.DisposeDataListener;
import com.example.im.home.HomeActivity;
import com.example.im.utils.SPUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnRegister;
    private Button mBtnLogin;
    private SPUtil spUtil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spUtil = new SPUtil(this);
        initView();
        initListener();
    }

    private void initListener() {
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mEtUserName.getText().toString();
                String passWord = mEtPassword.getText().toString();
                HashMap<String, String> params = new HashMap();
                params.put("name", userName);
                params.put("password", passWord);
                CommonOkhttpClient.sendRequest(CommonRequest.createPostRequest(Constant.HTTP_HOST_URL+"/login", params), new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        Log.d("TAG", "login --> " + s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("status") == Constant.REQUEST_SUC) {
                                Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                                spUtil.putString("username", userName);
                                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败 + " + jsonObject.getString("errorMesg"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Exception reasonObj) {
                        Log.d("TAG", "login wrong --> " + reasonObj.getMessage());
                        Toast.makeText(LoginActivity.this, "登录失败:" + reasonObj.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                })));
            }
        });
    }



    private void initView() {
        mBtnRegister = findViewById(R.id.btn_signup);
        mBtnLogin = findViewById(R.id.btn_signin);
        mEtUserName = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
    }


}
