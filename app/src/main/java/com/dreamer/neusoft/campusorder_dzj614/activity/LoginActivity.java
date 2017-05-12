package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.UserService;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends Activity implements View.OnClickListener {
private Button login_reg_btn;
    private  Button login_btn;
    private EditText UserName;
    private EditText Passwd;
    private Switch rememberPwd;
    private SharedPreferences RemPwd;
    private SharedPreferences.Editor editor;
    private  String uname;
    private  String pass;
    private CookApi cookapi;
    private UserService userservice;
    private  User user;
    private  Intent intent;
     private  String userid;
    private  String[] UserInfo=new String[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        RememberPwd();
        MyClickListener();
    }

    private void initData() {
        uname =UserName.getText().toString();
        pass=Passwd.getText().toString();


    }

    private void RememberPwd() {
        RemPwd = getSharedPreferences("User", MODE_PRIVATE);
        if (RemPwd.getBoolean("Remember",false)){
            String uname=RemPwd.getString("username","");
            String Pwd=RemPwd.getString("passwd","");
            UserName.setText(uname);
            Passwd.setText(Pwd);
            rememberPwd.setChecked(true);
        }
    }

    private void initView() {
        login_reg_btn=(Button)findViewById(R.id.login_reg_btn);
        UserName=(EditText)findViewById(R.id.login_username);
        Passwd=(EditText) findViewById(R.id.login_password);
        rememberPwd=(Switch)findViewById(R.id.login_RemPass);
        login_btn=(Button)findViewById(R.id.login_btn);
        cookapi=new CookApi(0);
        userservice=cookapi.getService();
    }

    private void MyClickListener() {

        login_reg_btn.setOnClickListener(this);
        login_btn.setOnClickListener(this);
    }


    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.login_reg_btn:
                goToReg();
                break;
            case R.id.login_btn:
                goToLogin(1);
                break;
            case R.id.login_qq:
                goToLogin(2);
                break;
            case R.id.login_wechat:
                goToLogin(3);
                break;

            case R.id.login_weibo:
                goToLogin(4);
                break;
        }
    }

    private void goToReg() {
        Intent intent =new Intent(this, RegActivity.class);
        startActivity(intent);
    }

    private  void  saveUser(String[] UserInfo,SharedPreferences Remember){
        editor=Remember.edit();

        editor.putString("username",UserInfo[0]);
        editor.putString("passwd",UserInfo[1]);
        editor.putString("userId",UserInfo[2]);
        if (rememberPwd.isChecked()) {

            editor.putBoolean("Remember",true);

        }
        else{
            editor.putBoolean("Remember",false);

        }

    }


    private void goToLogin(int type) {
        if(type==1){//本地登陆
            initData();
            Call<User> call=userservice.toLogin(uname,pass);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.isSuccessful()){
                        user=response.body();
                        userid=user.getUserid().toString();

                       if(!((uname.equals(""))||(pass.equals("")))){
                           if(user.getUserid().equals("0")){
                               Toast.makeText(LoginActivity.this, "密码或用户名错误", Toast.LENGTH_SHORT).show();
                               UserName.setText("");
                               Passwd.setText("");
                           }else{
                               RemPwd=getSharedPreferences("User", Context.MODE_PRIVATE);
                               UserInfo[0]=uname;
                               UserInfo[1]=pass;
                               UserInfo[2]=userid;
                               saveUser(UserInfo,RemPwd);
                               editor.commit();
                               intent=new Intent(LoginActivity.this,ContentActivity.class);
                               startActivity(intent);
                           }
                       }
                        else{
                           Toast.makeText(LoginActivity.this, "密码或用户名未填写", Toast.LENGTH_SHORT).show();
                       }

                    }
                    else{
                        Toast.makeText(LoginActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,"网络请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }  //本地登陆结束
        else if(type==0){

        }
        else if(type==0){

        }
        else if(type==0){

        }else{

        }
    }

}
