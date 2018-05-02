package com.example.im.home.find;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.im.Constant;
import com.example.im.R;
import com.example.im.client.http.CommonJsonCallback;
import com.example.im.client.http.CommonOkhttpClient;
import com.example.im.client.http.CommonRequest;
import com.example.im.client.http.DisposeDataHandle;
import com.example.im.client.http.DisposeDataListener;
import com.example.im.entity.OnlineUser;
import com.example.im.entity.User;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class FindUserActivity extends AppCompatActivity {
    private EditText mEtUserName;
    private Button mBtnSearch;
    private RecyclerView mRvUser;

    ImageView mIvback;
    TextView mTvTitle;

    List<OnlineUser.OnLineUserData> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    FindUserAdapter findUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        initView();
        initAdapter();
        initData();
    }

    private void initData() {
        mIvback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTvTitle.setText("在线用户");
        setContactList();
    }

    private void initAdapter() {

        layoutManager = new LinearLayoutManager(this);
        mRvUser.setLayoutManager(layoutManager);

        findUserAdapter = new FindUserAdapter(this, mList);
        mRvUser.setAdapter(findUserAdapter);
    }


    private void initView() {
        mIvback = findViewById(R.id.iv_left);
        mTvTitle = findViewById(R.id.tv_title);
        mEtUserName = findViewById(R.id.et_find_name);
        mBtnSearch = findViewById(R.id.btn_search);
        mRvUser = findViewById(R.id.rv_online_user);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = mEtUserName.getText().toString();
                for (OnlineUser.OnLineUserData data : mList) {
                    if (data.getName().equals(userName)) {
                        mList.clear();
                        mList.add(data);
                        findUserAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    private void setContactList() {
        CommonOkhttpClient.sendRequest(CommonRequest.createGetRequest(Constant.HTTP_HOST_URL + "/allLoginUser", null), new CommonJsonCallback(new DisposeDataHandle(new DisposeDataListener<OnlineUser>() {
            @Override
            public void onSuccess(OnlineUser s) {
                if (s.isSuccess()) {
                    mList.clear();
                    mList.addAll(s.getData());
                    findUserAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Exception reasonObj) {
                Logger.d(reasonObj.getMessage());
            }
        }, OnlineUser.class)));
    }
}
