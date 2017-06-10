package com.dreamer.neusoft.campusorder_dzj614.listener;

import android.content.Context;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.javaBean.UserInfoQQ;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import static com.dreamer.neusoft.campusorder_dzj614.activity.LoginActivity.mHandlerofQQ;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

/**
 * Created by DZJ-PC on 2017/5/31.
 */

public class BaseUiListener implements IUiListener {
    private Context mContext;
    private Tencent mTencent;
    private UserInfoQQ userInfoQQ;
    private Gson gson;
    private GsonBuilder builder;
    private UserInfo mUserInfo;
    public BaseUiListener(Context Context,Tencent mTencent){
        this.mContext=Context;
        this.mTencent=mTencent;
       GsonBuilder builder=new GsonBuilder();
        gson=builder.create();
    }
    @Override
    public void onComplete(Object response) {
        Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
        Log.e(TAG, "response:" + response);
        JSONObject obj = (JSONObject) response;
         try {
            String openID = obj.getString("openid");
            String accessToken = obj.getString("access_token");
            String expires = obj.getString("expires_in");
            mTencent.setOpenId(openID);
            mTencent.setAccessToken(accessToken,expires);
            QQToken qqToken = mTencent.getQQToken();
            mUserInfo = new UserInfo(mContext,qqToken);
            mUserInfo.getUserInfo(new IUiListener() {
                @Override
                public void onComplete(Object response) {
                    //Toast.makeText(mContext, response.toString(), Toast.LENGTH_SHORT).show();
                    userInfoQQ=gson.fromJson(response.toString(),UserInfoQQ.class);
                    Message message = new Message();
                    message.arg1 = 1;
                    mHandlerofQQ.sendMessage(message);
                    Log.d(TAG,"登录成功"+response.toString());
                }

                @Override
                public void onError(UiError uiError) {
                    Log.e(TAG,"登录失败"+uiError.toString());
                }

                @Override
                public void onCancel() {
                    Log.e(TAG,"登录取消");

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCancel() {
        Toast.makeText(mContext, "授权取消", Toast.LENGTH_SHORT).show();

    }

    public UserInfoQQ getUserInfoQQ() {
        return userInfoQQ;
    }
}
