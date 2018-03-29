package com.example.im;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.im.chat.ChatActivity;
import com.example.im.home.HomeActivity;

public class MainActivity extends AppCompatActivity {
    Button mBtnIn;
    Button mBtnNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtnIn = findViewById(R.id.btn_go_chat);
        mBtnNotify = findViewById(R.id.btn_notify);
        mBtnIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
            }
        });
        mBtnNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pendingIntent = new Intent(MainActivity.this, ChatActivity.class);
                pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                IMNotificationManager.getInstance(MainActivity.this).showNotification(pendingIntent);
            }
        });
    }
}
