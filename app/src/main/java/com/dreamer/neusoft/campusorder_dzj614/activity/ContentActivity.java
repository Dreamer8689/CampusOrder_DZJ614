package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.fragment.HomeFragment;
import com.dreamer.neusoft.campusorder_dzj614.fragment.MeFragment;
import com.dreamer.neusoft.campusorder_dzj614.fragment.OrderFragment;
import com.dreamer.neusoft.campusorder_dzj614.fragment.SearchFragment;

import java.util.ArrayList;
import java.util.List;

public class ContentActivity extends FragmentActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    //适配器
    private FragmentPagerAdapter mAdapter;
    //装载Fragment的集合
    private List<Fragment> mFragments;
    private LinearLayout HomeLayout;
    private LinearLayout SearchLayout;
    private LinearLayout OrderLayout;
    private LinearLayout MeLayout;

    private ImageButton mImgHome;
    private ImageButton mImgSearch;
    private ImageButton mImgOrder;
    private ImageButton mImgMe;
    private   Intent intent;
    private int type;

    private TextView hometv,searchtv,ordertv,metv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_content);
      intent=getIntent();
        type=intent.getIntExtra("type",0);

        initView();
        initEvent();
        initDatas();
        if(type!=0){
            transform(type);
        }
    }

    private void transform(int type) {
        resetImgs();
        //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
        switch (type) {
            case 1:
                selectTab(0);
                break;
            case 2:
                selectTab(1);
                break;
            case 3:
                selectTab(2);
                break;
            case 4:
                selectTab(3);
                break;

        }
    }

    private void initView() {
        mViewPager=(ViewPager)findViewById(R.id.id_viewpager);
        HomeLayout = (LinearLayout) findViewById(R.id.id_tab_home);
        SearchLayout = (LinearLayout) findViewById(R.id.id_tab_search);
        OrderLayout = (LinearLayout) findViewById(R.id.id_tab_order);
        MeLayout = (LinearLayout) findViewById(R.id.id_tab_me);

        mImgHome = (ImageButton) findViewById(R.id.id_tab_home_img);
        mImgSearch = (ImageButton) findViewById(R.id.id_tab_search_img);
        mImgOrder = (ImageButton) findViewById(R.id.id_tab_order_img);
        mImgMe = (ImageButton) findViewById(R.id.id_tab_me_img);

        hometv=(TextView)findViewById(R.id.hometv);
        searchtv=(TextView)findViewById(R.id.searchtv);
        ordertv=(TextView)findViewById(R.id.ordertv);
        metv=(TextView)findViewById(R.id.metv);
    }

    private void initEvent() {
        HomeLayout.setOnClickListener(this);
        SearchLayout.setOnClickListener(this);
        OrderLayout.setOnClickListener(this);
        MeLayout.setOnClickListener(this);
    }



    private void selectTab(int i) {
        //根据点击的Tab设置对应的ImageButton为绿色
        switch (i) {
            case 0:
                mImgHome.setImageResource(R.drawable.home_on);
                hometv.setTextColor(Color.rgb(18, 150, 219));
                break;
            case 1:
                mImgSearch.setImageResource(R.drawable.search_on);
                searchtv.setTextColor(Color.rgb(18, 150, 219));
                break;
            case 2:
                mImgOrder.setImageResource(R.drawable.order_on);
                ordertv.setTextColor(Color.rgb(18, 150, 219));
                break;
            case 3:
                mImgMe.setImageResource(R.drawable.me_on);
                metv.setTextColor(Color.rgb(18, 150, 219));
                break;

        }
        //设置当前点击的Tab所对应的页面
        mViewPager.setCurrentItem(i);
    }

    public  void   initDatas(){
        mFragments = new ArrayList<Fragment>();
        mFragments.add(new HomeFragment());
        mFragments.add(new SearchFragment());

        mFragments.add(new OrderFragment());
        mFragments.add(new MeFragment());



        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

        };

        mViewPager.setAdapter(mAdapter);
        //设置ViewPager的切换监听
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                mViewPager.setCurrentItem(position);
                resetImgs();
                 selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void resetImgs()
    {
        mImgHome.setImageResource(R.drawable.home_off);
        mImgSearch.setImageResource(R.drawable.search_off);
        mImgOrder.setImageResource(R.drawable.order_off);
        mImgMe.setImageResource(R.drawable.me_off);
        hometv.setTextColor(Color.BLACK);
        searchtv.setTextColor(Color.BLACK);
        ordertv.setTextColor(Color.BLACK);
        metv.setTextColor(Color.BLACK);
    }

    @Override
    public void onClick(View v) {
        resetImgs();
            //根据点击的Tab切换不同的页面及设置对应的ImageButton为绿色
            switch (v.getId()) {
                case R.id.id_tab_home:
                    selectTab(0);
                    break;
                case R.id.id_tab_search:
                    selectTab(1);
                    break;
                case R.id.id_tab_order:
                    selectTab(2);
                    break;
                case R.id.id_tab_me:
                    selectTab(3);
                    break;

            }
    }
}
