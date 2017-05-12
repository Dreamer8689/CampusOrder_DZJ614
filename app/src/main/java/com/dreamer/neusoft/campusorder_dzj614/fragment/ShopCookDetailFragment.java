package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.FoodDetailAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCookDetailFragment extends Fragment {
    public CookApi cookApi;
    public FoodService foodService;
    public RecyclerView recyclerView;
    public  View view;
    public Integer shop_id;
    public  List<FoodBean> food;
     public  Integer size;
    private SharedPreferences Shop;
    private SharedPreferences.Editor editor;
    public ShopCookDetailFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shop_cook_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();

        recyclerView = (RecyclerView) view.findViewById(R.id.shopCookDetail_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getFoodGson();

    }

    private void getFoodGson() {
        Call<List<FoodBean>> call=foodService.getFoodByShop(String.valueOf(shop_id));
        call.enqueue(new Callback<List<FoodBean>>() {
            @Override
            public void onResponse(Call<List<FoodBean>> call, Response<List<FoodBean>> response) {
                if (response.isSuccessful()) {
                    food=response.body();
                    size=food.size();
                    recyclerView.setAdapter(new FoodDetailAdapter(getActivity(),food));
                }else{
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodBean>> call, Throwable t) {
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        cookApi=new CookApi(2);
        foodService=cookApi.getFoodService();


    }

    private void initData() {
            Shop = getActivity().getSharedPreferences("shop", MODE_PRIVATE);
            shop_id=Shop.getInt("shop_id",-1);

    }


}
