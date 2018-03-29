package com.example.im.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.im.Constant;
import com.example.im.R;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    RecyclerView mRvContact;
    List<Contact> mList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    HomeAdapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mRvContact = findViewById(R.id.rc_view);
        mList = getContactListFromServer();

        layoutManager = new LinearLayoutManager(this);
        mRvContact.setLayoutManager(layoutManager);

        homeAdapter = new HomeAdapter(this, mList);
        mRvContact.setAdapter(homeAdapter);


    }

    public List<Contact> getContactListFromServer() {
        List<Contact> list = new ArrayList<>();
        Contact contact = new Contact();
        contact.setUserID(Constant.OTHER_USERID);
        list.add(contact);
        return list;
    }
}
