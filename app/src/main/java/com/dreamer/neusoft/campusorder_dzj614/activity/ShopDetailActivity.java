package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.fragment.ShopAssessFragment;
import com.dreamer.neusoft.campusorder_dzj614.fragment.ShopCookDetailFragment;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.ShopBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isCollectedBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CollectionService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.ShopService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopDetailActivity extends FragmentActivity implements View.OnClickListener {

    private Intent intent;
    private int shop_id;
    private ShopBean shopBean;
     private ImageButton back,cart,more;
    private TextView  shopName,shopJianjie,shopAddress,shopphone;

private ImageView shopImg,shopCollectImg;
    public CookApi cookApi;
    public ShopService shopService;

    public CollectionService collectionService;
    private MyAdapter myAdapter;
    private TabLayout main_tab;
    private ViewPager main_viewpager;
    private List<String> mTitleList = new ArrayList<>(2);
    private List<Fragment> fragments = new ArrayList<>(2);
    private ShopAssessFragment shopAssessFragment;
    private ShopCookDetailFragment shopCokDetailFragment;
    public Handler mHandler;
    private SharedPreferences shop;
    private SharedPreferences.Editor editor;
    private SharedPreferences user;
    private int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopdetail_layout);
        initView();
        initData();
        //isCollected();
        getShopDetailData();
        initEvent();

        myLister();

    }

    public void isCollected(){

     toSharePreference();

        Call<isCollectedBean> call =collectionService.isCollection(userid,shop_id,0);
        call.enqueue(new Callback<isCollectedBean>() {
            @Override
            public void onResponse(Call<isCollectedBean> call, Response<isCollectedBean> response) {
                if(response.isSuccessful()){
                    String Collected=response.body().getCollected();
                    if(Collected.equals("1")){
                        shopBean.setIsCollected(1);
                        Picasso.with(ShopDetailActivity.this).load(R.drawable.heart_on).into(shopCollectImg);

                    }else{
                        shopBean.setIsCollected(0);
                        Picasso.with(ShopDetailActivity.this).load(R.drawable.heart_off).into(shopCollectImg);

                    }

                }else{
                    Toast.makeText(ShopDetailActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isCollectedBean> call, Throwable t) {
                Toast.makeText(ShopDetailActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toSharePreference() {

        shop=getSharedPreferences("shop", Context.MODE_PRIVATE);
        editor=shop.edit();
        editor.putInt("shop_id",shop_id);
        editor.putString("shop_name",shopBean.getShopname().toString());
        editor.putString("shop_phone",shopBean.getPhonenum().toString());
        editor.commit();
    }

    public  void CollectionShop(){
        Call<isCollectedBean> call=collectionService.toCollectionShop(userid,shop_id);
        call.enqueue(new Callback<isCollectedBean>() {
            @Override
            public void onResponse(Call<isCollectedBean> call, Response<isCollectedBean> response) {
                if(response.isSuccessful()){
                    String sccess=response.body().getSuccess();
                    if(sccess.equals("1")){
                        if( shopBean.getIsCollected()==1){
                            shopBean.setIsCollected(0);
                            Toast.makeText(ShopDetailActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                            Picasso.with(ShopDetailActivity.this).load(R.drawable.heart_off).into(shopCollectImg);
                        }else{
                            shopBean.setIsCollected(1);
                            Toast.makeText(ShopDetailActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            Picasso.with(ShopDetailActivity.this).load(R.drawable.heart_on).into(shopCollectImg);
                        }

                    }
                    else{
                        Toast.makeText(ShopDetailActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<isCollectedBean> call, Throwable t) {
                Toast.makeText(ShopDetailActivity.this, "网路请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void getShopDetailData() {
        Call<ShopBean> call=shopService.getShopDetail(String.valueOf(shop_id));
            call.enqueue(new Callback<ShopBean>() {
                @Override
                public void onResponse(Call<ShopBean> call, Response<ShopBean> response) {
                    if(response.isSuccessful()){

                        shopBean=response.body();
                        shopName.setText(shopBean.getShopname());
                        shopJianjie.setText("简介："+shopBean.getIntro());
                        shopAddress.setText("店铺地址："+shopBean.getAddress());
                        shopphone.setText("订餐电话："+shopBean.getPhonenum());

                        Picasso.with(ShopDetailActivity.this).load(shopBean.getPic()).resize(100,100).into(shopImg);
                        Message message = new Message();
                        message.arg1 = 1;
                        mHandler.sendMessage(message);
                    }else{
                        Toast.makeText(ShopDetailActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<ShopBean> call, Throwable t) {

                }
            });


    }

    private void myLister() {
       main_tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               int position = tab.getPosition();

           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });

        shopCollectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionShop();
            }
        });
    }

    private void initEvent() {
        myAdapter = new MyAdapter(getSupportFragmentManager());
        myAdapter.notifyDataSetChanged();
        main_viewpager.setAdapter(myAdapter);
        main_viewpager.setOffscreenPageLimit(1);
        main_tab.setTabMode(TabLayout.MODE_FIXED);
        main_tab.setupWithViewPager(main_viewpager);
        main_viewpager.setCurrentItem(0);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){
                    isCollected();

                }
            }
        };
    }

    private void initData() {

        mTitleList.add("店铺菜单");
        mTitleList.add("菜单评价");

        if (shopCokDetailFragment == null) {
            shopCokDetailFragment = new ShopCookDetailFragment();
            fragments.add(shopCokDetailFragment);
        }

        if (shopAssessFragment == null) {
            shopAssessFragment = new ShopAssessFragment();
            fragments.add(shopAssessFragment);
        }


    }

    private void initView() {
        intent=getIntent();
        shop_id=intent.getIntExtra("shop_id",-1);

        user=getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();

        cookApi=new CookApi(4);
        collectionService=cookApi.getCollectionService();

        main_viewpager=(ViewPager)findViewById(R.id.main_viewPager);
        main_tab=(TabLayout)findViewById(R.id.shop_detail_tab);

        back=(ImageButton)findViewById(R.id.shop_detail_back);
        cart=(ImageButton)findViewById(R.id.shop_detail_cart);
        more=(ImageButton)findViewById(R.id.shop_detail_more);

        shopName=(TextView)findViewById(R.id.shop_detail_name);
        shopJianjie=(TextView)findViewById(R.id.shop_detail_jianjie);
        shopAddress=(TextView)findViewById(R.id.shop_detail_address);
        shopphone=(TextView)findViewById(R.id.shop_detail_phone);
        shopImg=(ImageView)findViewById(R.id.shop_detail_img);
        shopCollectImg=(ImageView)findViewById(R.id.shop_collection_img);
        cookApi=new CookApi(1);
        shopService=cookApi.getShopService();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shop_collection_img:
               // CollectionShop();
                break;
        }
    }
  public   class MyAdapter extends FragmentPagerAdapter {

        public MyAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
          return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return mTitleList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }
}
