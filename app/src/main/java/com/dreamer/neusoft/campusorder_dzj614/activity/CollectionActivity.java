package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.fragment.FoodCollectionFragment;
import com.dreamer.neusoft.campusorder_dzj614.fragment.ShopCollectionFragment;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.ShopBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CollectionService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.ShopService;

import java.util.ArrayList;
import java.util.List;

public class CollectionActivity extends FragmentActivity implements View.OnClickListener {
    private Intent intent;
    private int shop_id;
    private ShopBean shopBean;
    private ImageButton back,more;



    public CookApi cookApi;
    public ShopService shopService;

    public CollectionService collectionService;
    private MyAdapter myAdapter;
    private TabLayout main_tab;
    private ViewPager main_viewpager;
    private List<String> mTitleList = new ArrayList<>(2);
    private List<Fragment> fragments = new ArrayList<>(2);
    private FoodCollectionFragment foodCollectionFragment;
    private ShopCollectionFragment shopCollectionFragment;

    private SharedPreferences shop;
    private SharedPreferences.Editor editor;
    private SharedPreferences user;
    private int userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        initView();
        initData();

        initEvent();

        myLister();

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


    }

    private void initEvent() {
        myAdapter = new MyAdapter(getSupportFragmentManager());
        myAdapter.notifyDataSetChanged();
        main_viewpager.setAdapter(myAdapter);
        main_viewpager.setOffscreenPageLimit(1);
        main_tab.setTabMode(TabLayout.MODE_FIXED);
        main_tab.setupWithViewPager(main_viewpager);
        main_viewpager.setCurrentItem(0);

        back.setOnClickListener(this);
    }

    private void initData() {

        mTitleList.add("店铺收藏");
        mTitleList.add("菜单收藏");

        if (shopCollectionFragment == null) {
            shopCollectionFragment = new ShopCollectionFragment();
            fragments.add(shopCollectionFragment);
        }

        if (foodCollectionFragment == null) {
            foodCollectionFragment = new FoodCollectionFragment();
            fragments.add(foodCollectionFragment);
        }

    }

    private void initView() {

        cookApi=new CookApi(4);
        collectionService=cookApi.getCollectionService();

        main_viewpager=(ViewPager)findViewById(R.id.main_viewPager);
        main_tab=(TabLayout)findViewById(R.id.shop_detail_tab);

        back=(ImageButton)findViewById(R.id.collection_back);

        more=(ImageButton)findViewById(R.id.collection_more);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.collection_back:
                intent=new Intent(this,ContentActivity.class);
                intent.putExtra("type",4);
                startActivity(intent);
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
