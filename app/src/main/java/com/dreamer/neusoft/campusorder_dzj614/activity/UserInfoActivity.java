package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.R;

public class UserInfoActivity extends Activity  {
    private SharedPreferences user;
    private Intent intent;
    private ImageButton Back;
    private TextView username,uid,phone,address,note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        initView();
        initEvent();

        MyClickListener();
    }

    private void initView() {
        username=(TextView)findViewById(R.id.UserInfo_uname);
        uid=(TextView)findViewById(R.id.UserInfo_uid);
        phone=(TextView)findViewById(R.id.UserInfo_phone);
        address=(TextView)findViewById(R.id.UserInfo_address);
        note=(TextView)findViewById(R.id.UserInfo_note);
        Back=(ImageButton)findViewById(R.id.UserInfo_back);
        user= getSharedPreferences("User", MODE_PRIVATE);
    }

    private  void initEvent(){
        username.setText("用户名："+user.getString("username",""));
        uid.setText("UID:"+user.getString("userId",""));
        phone.setText("联系电话："+user.getString("phone",""));
        address.setText("配送地址："+user.getString("address",""));
        note.setText("备注信息："+user.getString("note",""));
    }

    private void MyClickListener() {

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent=new Intent(UserInfoActivity.this,ContentActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
            }
        });
    }
}
