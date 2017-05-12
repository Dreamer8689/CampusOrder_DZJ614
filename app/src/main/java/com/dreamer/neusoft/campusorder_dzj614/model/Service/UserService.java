package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.javaBean.User;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.UserInfo;
import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/3/19.
 */

public interface UserService {


    @GET(Constant.ApiServiceUrl.LOGINSERVICE)   //登陆
    Call<User> toLogin(@Query("username") String username,
                       @Query("userpass") String userpass);

    @GET(Constant.ApiServiceUrl.REGSERVICE)   //注册
    Call<User> toReg(@Query("username") String username,
                        @Query("userpass") String userpass,
                        @Query("mobilenum")String mobilenum,
                        @Query("address") String address,
                        @Query("comment") String comment);



    @GET(Constant.ApiServiceUrl.CHANGEUSERINFO)  //修改个人信息
    Call<User> toChangeUserInfo(@Query("user_id") String user_id,
                                  @Query("username") String username,
                                  @Query("userpass") String userpass,
                                  @Query("mobilenum") String mobilenum,
                                  @Query("address") String address);

    @GET(Constant.ApiServiceUrl.GETUSERINFO)  //获取当前用户信息
    Call<UserInfo> getUserInfo(@Query("user_id") String userid);
}

