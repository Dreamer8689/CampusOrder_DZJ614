package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.UserService;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.User;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegActivity extends Activity implements View.OnClickListener {
 private EditText username,password,password1,phone,address,note;
   private CookApi cookapi;
    private UserService userservice;
    private  String uname,pwd,pwd1,phonenum,ads,note1;
    private Button reg_btn;
    private  ImageButton reg_back;
    private Intent intent;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);
        initView();
        MyClickListener();
    }

    private void initView() {
        username=(EditText) findViewById(R.id.reg_username);
        password=(EditText) findViewById(R.id.reg_password);
        password1=(EditText) findViewById(R.id.reg_password1);
        phone=(EditText) findViewById(R.id.reg_phone);
        address=(EditText) findViewById(R.id.reg_address);
        note=(EditText) findViewById(R.id.reg_note);
        reg_btn=(Button) findViewById(R.id.reg_btn);
        reg_back=(ImageButton)findViewById(R.id.reg_back);
        cookapi=new CookApi(0);
        userservice=cookapi.getService();
    }

    private void initData() {
        uname =username.getText().toString();
        pwd=password.getText().toString();
        pwd1=password1.getText().toString();
        phonenum=phone.getText().toString();
        ads=address.getText().toString();
        note1=note.getText().toString();


    }

    private  void reset(){
        username.setText("");
       password.setText("");
        password1.setText("");
        phone.setText("");
        address.setText("");
        note.setText("");
    }

    private void MyClickListener() {

        reg_back.setOnClickListener(this);
        reg_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.reg_back:
                goToBack();
                break;
            case R.id.reg_btn:
                goToReg();
                break;

        }
    }

    private void goToBack() {
        intent=new Intent(RegActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    private void goToReg() {
        initData();
        if(!(uname.isEmpty()||pwd.isEmpty()||
                pwd1.isEmpty()||phonenum.isEmpty()||
                ads.isEmpty())){

                 if(pwd.equals(pwd1)){
                     Call<User> call =userservice.toReg(uname,pwd,phonenum,ads,note1);
                     call.enqueue(new Callback<User>() {
                         @Override
                         public void onResponse(Call<User> call, Response<User> response) {
                                if(response.isSuccessful()){
                                    user=response.body();
                                    if(user.getSuccess().toString().equals("1")){
                                        intent=new Intent(RegActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(RegActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    }else{
                                        reset();
                                        Toast.makeText(RegActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                             else{
                                    Toast.makeText(RegActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                                }
                         }

                         @Override
                         public void onFailure(Call<User> call, Throwable t) {
                             Toast.makeText(RegActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                         }
                     });
                 }
            else{
                     Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
                 }
        }
        else{
            Toast.makeText(this, "请正确填写信息", Toast.LENGTH_SHORT).show();
        }

    }
}
