package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dreamer.neusoft.campusorder_dzj614.R;

/**
 * Created by DZJ-PC on 2017/5/10.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected int layout_file= R.layout.activity_login;
    abstract void initViews();
    abstract void initEvents();
    abstract void initData();
    void setLayout(int layout_file)
    {
        setContentView(layout_file);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLayout(layout_file);
        initViews();
        initEvents();
        initData();
    }
    public String getUser_id()
    {
        SharedPreferences sp;
        sp =getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        return sp.getString("user_id", "");
    }
}
