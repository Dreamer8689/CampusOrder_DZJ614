package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.User;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeInfoActivity extends Activity implements View.OnClickListener {
    private UserService userservice;
    private SharedPreferences user;
    private SharedPreferences.Editor editor;
    private CookApi cookapi;
private Intent intent;
    private  ImageButton  Back;
    private EditText username,password,phone,address;
    private Button ChangeBtn;
    private  String uname,pwd,phonenum,ads,userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        initView();
        initEvent();

        MyClickListener();

    }
    private  void initEvent(){
        username.setText(user.getString("username",""));
        password.setText(user.getString("passwd",""));
        phone.setText(user.getString("phone",""));
        address.setText(user.getString("address",""));

    }
    private void MyClickListener() {
        ChangeBtn.setOnClickListener(this);
        Back.setOnClickListener(this);
    }
    private void getUserInfo() {
        uname =username.getText().toString();
        pwd=password.getText().toString();
        phonenum=phone.getText().toString();
        ads=address.getText().toString();
        userid=user.getString("userId","");

    }
    private void ChangeInfo() {
        getUserInfo();
        Call<User> call=userservice.toChangeUserInfo(userid,uname,pwd,phonenum,ads);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()&&response.body().getSuccess().equals("1")){
                    Toast.makeText(ChangeInfoActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                    saveUser();
                    intent=new Intent(ChangeInfoActivity.this,ContentActivity.class);
                    intent.putExtra("type",4);
                    startActivity(intent);

                }
                else{
                    Toast.makeText(ChangeInfoActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(ChangeInfoActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initView() {
        user= getSharedPreferences("User", MODE_PRIVATE);
        cookapi=new CookApi(0);
        userservice=cookapi.getService();

        username=(EditText)findViewById(R.id.ChangeInfo_username);
        password=(EditText)findViewById(R.id.ChangeInfo_password);
        phone=(EditText)findViewById(R.id.ChangeInfo_phone);
        address=(EditText)findViewById(R.id.ChangeInfo_address);
        ChangeBtn=(Button)findViewById(R.id.ChangeInfo_btn) ;
        Back=(ImageButton) findViewById(R.id.ChangeInfo_back);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.ChangeInfo_btn:
                ChangeInfo();
                break;
            case R.id.ChangeInfo_back:
                goBack();
                break;
        }
    }

    private void goBack() {
        intent=new Intent(ChangeInfoActivity.this,ContentActivity.class);
        intent.putExtra("type",4);
        startActivity(intent);
    }

    private  void  saveUser(){
        editor=user.edit();

        editor.putString("username",uname);
        editor.putString("passwd",pwd);
        editor.putString("phone",phonenum);
        editor.putString("address",ads);
        editor.commit();

    }
}
