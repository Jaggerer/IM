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

import com.example.im.MainActivity;
import com.example.im.R;
import com.example.im.client.ConnectionService;

public class LoginActivity extends AppCompatActivity {
    ConnectionService.ClientBinder mBinder;
    private EditText mEtUserName;
    private EditText mEtPassword;
    private Button mBtnRegister;
    private Button mBtnLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initListener();
        Intent bindIntent = new Intent(LoginActivity.this, ConnectionService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
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

//                mBinder.sendTextMessage();
            }
        });
    }

    private void initView() {
        mBtnRegister = findViewById(R.id.btn_signup);
        mBtnLogin = findViewById(R.id.btn_signin);
        mEtUserName = findViewById(R.id.et_username);
        mEtPassword = findViewById(R.id.et_password);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ConnectionService.ClientBinder) iBinder;
            Log.d("tag", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("tag", "onServiceDisconnected");
        }
    };
}
