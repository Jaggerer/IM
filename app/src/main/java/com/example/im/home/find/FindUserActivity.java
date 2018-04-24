package com.example.im.home.find;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.im.Constant;
import com.example.im.R;
import com.example.im.entity.Contact;
import com.example.im.entity.User;
import com.example.im.home.HomeAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FindUserActivity extends AppCompatActivity {
    private EditText mEtUserName;
    private Button mBtnSearch;
    private RecyclerView mRvUser;

    List<User> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    FindUserAdapter findUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_user);
        initView();
        initAdapter();
    }

    private void initAdapter() {

        layoutManager = new LinearLayoutManager(this);
        mRvUser.setLayoutManager(layoutManager);

        findUserAdapter = new FindUserAdapter(this, mList);
        mRvUser.setAdapter(findUserAdapter);
    }

    private List<User> getContactListFromServer() {
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setName("Jagger");
        user.setCreateTime(new Date(System.currentTimeMillis()));
        list.add(user);
        return list;
    }

    private void initView() {
        mEtUserName = findViewById(R.id.et_find_name);
        mBtnSearch = findViewById(R.id.btn_search);
        mRvUser = findViewById(R.id.rv_online_user);
        mBtnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mList.clear();
                mList.addAll(getContactListFromServer());
                findUserAdapter.notifyDataSetChanged();
            }
        });
    }
}
