package com.example.im.home;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.im.Constant;
import com.example.im.R;
import com.example.im.client.ConnectionService;
import com.example.im.entity.Contact;
import com.example.im.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ConnectionService.ClientBinder mBinder;

    RecyclerView mRvContact;
    List<Contact> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService();
        mRvContact = findViewById(R.id.rc_view);
        mList = getContactListFromServer();

        layoutManager = new LinearLayoutManager(this);
        mRvContact.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(this, mList);
        mRvContact.setAdapter(homeAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public List<Contact> getContactListFromServer() {
        List<Contact> list = new ArrayList<>();
        Contact contact = new Contact();
        contact.setUserID(Constant.OTHER_USERID);
        list.add(contact);
        return list;
    }

    private void startService() {
        Intent bindIntent = new Intent(HomeActivity.this, ConnectionService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);

    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ConnectionService.ClientBinder) iBinder;
            mBinder.connectService();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("tag", "onServiceDisconnected");
        }
    };

}
