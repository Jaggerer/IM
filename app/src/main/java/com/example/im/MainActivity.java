package com.example.im;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.example.im.chat.ChatActivity;
import com.example.im.client.ConnectionService;
import com.example.im.home.HomeActivity;
import com.example.im.login.LoginActivity;
import com.orhanobut.logger.Logger;

public class MainActivity extends AppCompatActivity {
    Button mBtnLogin;
    Button mBtnIn;
    Button mBtnNotify;
    Button mBtnStartService;
    Button mBtnStopService;
    ConnectionService.ClientBinder mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnLogin = findViewById(R.id.btn_go_login);
        mBtnIn = findViewById(R.id.btn_go_chat);
        mBtnNotify = findViewById(R.id.btn_notify);
        mBtnStartService = findViewById(R.id.btn_start_service);
        mBtnStopService = findViewById(R.id.btn_stop_service);
        mBtnLogin.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, LoginActivity.class)));
        mBtnIn.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this, HomeActivity.class));
        });
        mBtnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pendingIntent = new Intent(MainActivity.this, ChatActivity.class);
                pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//                IMNotificationManager.getInstance(MainActivity.this).showNotification(pendingIntent);
            }
        });
        mBtnStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bindIntent = new Intent(MainActivity.this, ConnectionService.class);
                bindService(bindIntent, connection, BIND_AUTO_CREATE);
            }
        });
        mBtnStopService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unbindService(connection);
            }
        });
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ConnectionService.ClientBinder) iBinder;
//            mBinder.connectService();
            Log.d("tag", "onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("tag", "onServiceDisconnected");
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
