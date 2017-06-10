package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.FoodListAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.CycleViewPager;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.CycleViewPagerData;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.Info;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.ShopBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.ShopService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    List<Info> mList = new ArrayList<>();
    CycleViewPager mCycleViewPager;
    CycleViewPagerData cycleViewPagerData;


    public RecyclerView recyclerView;
    public  View view;


    public  List<ShopBean> shop;
    public CookApi cookApi;
    public ShopService shopService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        initData();
        initView();
        recyclerView = (RecyclerView) view.findViewById(R.id.Home_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getShopGson();

        return view;
    }

    private void getShopGson() {
        Call<List<ShopBean>> call=shopService.getShop();
        call.enqueue(new Callback<List<ShopBean>>() {
            @Override
            public void onResponse(Call<List<ShopBean>> call, Response<List<ShopBean>> response) {
                if(response.isSuccessful()){
                  shop=response.body();


                    recyclerView.setAdapter(new FoodListAdapter(getActivity(),shop));
                }else{
                   Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ShopBean>> call, Throwable t) {
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initData() {
        cycleViewPagerData=new CycleViewPagerData(getActivity());
        mList=cycleViewPagerData.getmList(mList);

    }

    private void initView() {
        mCycleViewPager = (CycleViewPager) view.findViewById(R.id.cycle_view);
        //设置选中和未选中时的图片
        assert mCycleViewPager != null;
        mCycleViewPager.setIndicators(R.drawable.ad_select, R.drawable.ad_unselect);
        //设置轮播间隔时间，默认为4000
        mCycleViewPager.setDelay(4000);
        mCycleViewPager.setData(mList, mAdCycleViewListener);

        cookApi=new CookApi(1);
           shopService=cookApi.getShopService();
    }


    private CycleViewPager.ImageCycleViewListener mAdCycleViewListener =
            new CycleViewPager.ImageCycleViewListener() {

                @Override
                public void onImageClick(Info info, int position, View imageView) {

                    if (mCycleViewPager.isCycle()) {
                        position = position - 1;
                    }
                    Toast.makeText(getActivity(), info.getTitle() + "选择了--" + position, Toast.LENGTH_LONG).show();
                }
            };


}
