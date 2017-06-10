package com.dreamer.neusoft.campusorder_dzj614.Base;

import com.dreamer.neusoft.campusorder_dzj614.javaBean.User;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.UserInfoQQ;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DZJ-PC on 2017/6/1.
 */

public class ThirdPartyToRegister {
    private CookApi cookapi;
    private UserService userservice;
    private  String uname,pwd,phonenum,ads,note;
    private   String  success,Userid;
    private UserInfoQQ userInfoQQ;
    public ThirdPartyToRegister(UserInfoQQ user){
        this.userInfoQQ=user;
    }
    public  void inilit(){
        uname=userInfoQQ.getNickname();
        pwd=userInfoQQ.getNickname();
        phonenum="133XXXX6666";
        ads=userInfoQQ.getProvince()+userInfoQQ.getCity();
        note="";
        cookapi=new CookApi(0);
        userservice=cookapi.getService();

    }

    public  String  Register(){

        Call<User> call=userservice.toReg(uname,pwd,phonenum,ads,note);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if(response.body().getSuccess().equals("1")){
                        success="1";
                    }else{
                        success="0";
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
           return success;
    }

    public  String login(){

        Call<User> call=userservice.toLogin(uname,pwd);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    if(!response.body().getUserid().equals("0")){
                        Userid=response.body().getUserid();
                    }else{
                        Userid="0";
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        return Userid;
    }
}
