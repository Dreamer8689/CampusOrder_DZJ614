package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.ShopFoodCommentsAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CommentService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopAssessFragment extends Fragment {
    public int shop_id;
    public int n=0;
    public  List<FoodBean> food;
    public FoodService foodService;
    public  ArrayList<OrderBean> Order=new ArrayList<OrderBean>();
    public CookApi cookApi,cookApi1;
    public CommentService commentService;
    public RecyclerView recyclerView;
    public  View view;
    public Handler mHandler;
    private SharedPreferences Shop;
    private SharedPreferences.Editor editor;

    public ShopAssessFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shop_assess, container, false);
    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();

        recyclerView = (RecyclerView) view.findViewById(R.id.shop_assess_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getFoodGson();
        //getAssessGson();

    }

    private void getFoodGson() {

        Call<List<FoodBean>> call=foodService.getFoodByShop(String.valueOf(shop_id));
        call.enqueue(new Callback<List<FoodBean>>() {
            @Override
            public void onResponse(Call<List<FoodBean>> call, Response<List<FoodBean>> response) {
                if (response.isSuccessful()) {
                    food=response.body();

                    getAssessGson(food);
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
    private void getAssessGson(final List<FoodBean> food) {

        for (int i = 0; i < food.size(); i++) {
           if(i==food.size()-1) {

               Message message = new Message();
               message.arg1 = 1;
               mHandler.sendMessage(message);
           }
            Call<List<OrderBean>> call=commentService.
                    getFoodOrder(String.valueOf(food.get(i).getFood_id()));

            call.enqueue(new Callback<List<OrderBean>>() {
                @Override
                public void onResponse(Call<List<OrderBean>> call, Response<List<OrderBean>> response) {
                    if (response.isSuccessful()) {
                        if(!response.body().isEmpty()){
                            List<OrderBean>  tmp=response.body();
                            Order.addAll(tmp);


                        }

                    }
                }

                @Override
                public void onFailure(Call<List<OrderBean>> call, Throwable t) {

                }
            });

        }





    }

    private void initView() {
        cookApi=new CookApi(5);
        commentService=cookApi.getCommentService();
        cookApi1=new CookApi(2);
        foodService=cookApi1.getFoodService();
    }


    private void initData() {
        Shop = getActivity().getSharedPreferences("shop", MODE_PRIVATE);
        shop_id=Shop.getInt("shop_id",-1);

        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){
                    try {
                        Thread.sleep(2000);
                        recyclerView.setAdapter(new ShopFoodCommentsAdapter(getActivity(),Order));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }
        };

    }

}
