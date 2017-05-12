package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.ChangeInfoActivity;
import com.dreamer.neusoft.campusorder_dzj614.activity.CollectionActivity;
import com.dreamer.neusoft.campusorder_dzj614.activity.CommentActivity;
import com.dreamer.neusoft.campusorder_dzj614.activity.LoginActivity;
import com.dreamer.neusoft.campusorder_dzj614.activity.RegActivity;
import com.dreamer.neusoft.campusorder_dzj614.activity.UserInfoActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.UserInfo;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.UserService;
import com.thinkcool.circletextimageview.CircleTextImageView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements View.OnClickListener {
    private LinearLayout addressLayout;
    private LinearLayout collectionLayout;
    private LinearLayout comment;
    private LinearLayout userinfoLayout;
    private LinearLayout changeUserInfo;
    private LinearLayout layoutMeLogin;


    private ChangeInfoActivity changeInfoActivity;
    private CollectionActivity collectionActivity;
    private CommentActivity commentActivity;
    private LoginActivity loginActivity;
    private RegActivity regActivity;
    private UserInfoActivity userInfoActivity;

    private UserInfo  userInfo;
    private TextView meFont;
    private ImageView imageMeClock;
    private ImageView imageMeMore;
    private CircleTextImageView profileImage;
    private View view;
    private CookApi cookapi;
    private UserService userservice;
    private SharedPreferences user;
    private SharedPreferences.Editor editor;
    private String userid;
    private TextView me_head_tv,me_address_tv;
    public MeFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
        initView();
        getUserInfo();
        MyClickListener();
        return view;
    }

    private void getUserInfo() {
        userid=user.getString("userId","");
        Call<UserInfo> call=userservice.getUserInfo(userid);
        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                if(response.isSuccessful()){
                    userInfo=response.body();
                    initEvent(userInfo);
                    saveUserInfo(userInfo);
                }
            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

            }
        });
    }

    private void saveUserInfo(UserInfo userInfo) {
        editor=user.edit();
        editor.putString("note",userInfo.getComment());
        editor.putString("address",userInfo.getAddress());
        editor.putString("phone",userInfo.getMobilenum());
        editor.commit();
    }

    private  void initEvent(UserInfo userInfo){
        me_head_tv.setText(userInfo.getUsername().toString());
        me_address_tv.setText("收货地址："+userInfo.getAddress().toString());

    }

    private void initView() {
        layoutMeLogin=(LinearLayout) view.findViewById(R.id.Layout_Me_Login);
        addressLayout = (LinearLayout) view.findViewById(R.id.Layout_address);
        collectionLayout = (LinearLayout) view.findViewById(R.id.Layout_collection);
        comment = (LinearLayout) view.findViewById(R.id.Layout_comment);
        userinfoLayout = (LinearLayout) view.findViewById(R.id.Layout_userinfo);
        changeUserInfo = (LinearLayout) view.findViewById(R.id.Layout_changeUserInfo);
        meFont = (TextView) view.findViewById(R.id.me_font);
        imageMeClock = (ImageView) view.findViewById(R.id.image_me_clock);
        imageMeMore = (ImageView) view.findViewById(R.id.image_me_more);
        me_head_tv=(TextView) view.findViewById(R.id.me_head_tv);
        me_address_tv=(TextView) view.findViewById(R.id.me_address_tv);
        profileImage = (CircleTextImageView) view.findViewById(R.id.profile_image);
        user= getActivity().getSharedPreferences("User", MODE_PRIVATE);
        cookapi=new CookApi(0);
        userservice=cookapi.getService();

    }

    private void MyClickListener() {
        addressLayout.setOnClickListener(this);
        collectionLayout.setOnClickListener(this);
        comment.setOnClickListener(this);
        userinfoLayout.setOnClickListener(this);
        changeUserInfo.setOnClickListener(this);
        layoutMeLogin.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.Layout_address:
               goToAddress();
                break;
            case R.id.Layout_collection:
                goToCollection();
                break;
            case R.id.Layout_comment:
                goToComment();
                break;
            case R.id.Layout_userinfo:
                goToUserinfo();
                break;
            case R.id.Layout_changeUserInfo:
                goToChangeUserInfo();
                break;

            case R.id.Layout_Me_Login:
                goToLogin();
                break;
        }
    }

    private void goToAddress() {
    }

    private void goToCollection() {
        if (user.getString("userId","").isEmpty()){
            Toast.makeText(getActivity(), "您尚未登录，请登录！", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =new Intent(getActivity(),CollectionActivity.class);
            startActivity(intent);
        }
    }

    private void goToChangeUserInfo() {
        if (user.getString("userId","").isEmpty()){
            Toast.makeText(getActivity(), "您尚未登录，请登录！", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =new Intent(getActivity(),ChangeInfoActivity.class);
            startActivity(intent);
        }
    }

    private void goToComment() {
        if (user.getString("userId","").isEmpty()){
            Toast.makeText(getActivity(), "您尚未登录，请登录！", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =new Intent(getActivity(),CommentActivity.class);
            startActivity(intent);
        }
    }

    private void goToUserinfo() {
        if (user.getString("userId","").isEmpty()){
            Toast.makeText(getActivity(), "您尚未登录，请登录！", Toast.LENGTH_SHORT).show();
        }else{
            Intent intent =new Intent(getActivity(),UserInfoActivity.class);
            startActivity(intent);
        }
    }

    private void goToLogin() {
        if (user.getString("userId","").isEmpty()){
            Intent intent =new Intent(getActivity(),LoginActivity.class);
            startActivity(intent);
        }else{
             Toast.makeText(getActivity(), "您已登录", Toast.LENGTH_SHORT).show();
        }
    }
}
