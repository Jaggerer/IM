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
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im.Constant;
import com.example.im.R;
import com.example.im.client.ConnectionService;
import com.example.im.db.bean.UserBean;
import com.example.im.entity.Contact;
import com.example.im.utils.UserUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class HomeActivity extends AppCompatActivity {
    ConnectionService.ClientBinder mBinder;

    RecyclerView mRvContact;
    List<String> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    HomeAdapter homeAdapter;

    ImageView mIvback;
    TextView mTvTitle;
    Realm mRealm;
    String currentUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService();
        initView();
        initListener();
        initData();
    }

    private void initData() {
        mRealm = Realm.getDefaultInstance();
        currentUser = UserUtils.getCurrentUser(this);
        mTvTitle.setText("最近联系人");
        searchRecentUser();
        layoutManager = new LinearLayoutManager(this);
        mRvContact.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(this, mList);
        mRvContact.setAdapter(homeAdapter);
    }

    private void searchRecentUser() {
        RealmResults<UserBean> userList = mRealm.where(UserBean.class)
                .equalTo("currentUserName", currentUser).findAll();
        if (userList.size() > 0) {
            UserBean userBean = userList.get(0);
            mList.clear();
            mList.addAll(userBean.getRecentUserName());
            homeAdapter.notifyDataSetChanged();
        }
    }

    private void initListener() {
        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
            }
        });
    }

    private void initView() {
        mRvContact = findViewById(R.id.rc_view);
        mIvback = findViewById(R.id.iv_left);
        mTvTitle = findViewById(R.id.tv_title);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    private void startService() {
        Intent bindIntent = new Intent(HomeActivity.this, ConnectionService.class);
        bindService(bindIntent, connection, BIND_AUTO_CREATE);
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mBinder = (ConnectionService.ClientBinder) iBinder;
            mBinder.connectService(mRealm);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d("tag", "onServiceDisconnected");
        }
    };

}
