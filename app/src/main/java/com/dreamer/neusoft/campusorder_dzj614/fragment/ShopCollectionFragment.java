package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.CollectionAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.CollectionBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CollectionService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCollectionFragment extends Fragment {
      private CookApi cookapi;
    private CollectionService collectionService;
   private List<CollectionBean> collectionBean;
    private SharedPreferences user;
    private int userid;
    public RecyclerView recyclerView;
    public ShopCollectionFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View view= inflater.inflate(R.layout.fragment_shop_collection, container, false);
        initView();
        initData();
        recyclerView = (RecyclerView) view.findViewById(R.id.shopCollection_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void initView() {
        cookapi=new CookApi(4);
        collectionService=cookapi.getCollectionService();



    }

    private void initData() {
        user=getActivity().getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();
        getShopCollection();
    }

    private void getShopCollection() {
        Call<List<CollectionBean>> call=collectionService.getUserCollection(userid,0);
         call.enqueue(new Callback<List<CollectionBean>>() {
             @Override
             public void onResponse(Call<List<CollectionBean>> call, Response<List<CollectionBean>> response) {
                 if(response.isSuccessful()){
                    List<CollectionBean>  CollectionShop=response.body();
                     recyclerView.setAdapter(new CollectionAdapter(getActivity(),CollectionShop));
                 }
                 else{
                     Toast.makeText(getActivity(), "返回数据失败", Toast.LENGTH_SHORT).show();
                 }
             }

             @Override
             public void onFailure(Call<List<CollectionBean>> call, Throwable t) {
                 Toast.makeText(getActivity(), "请求数据失败", Toast.LENGTH_SHORT).show();
             }
         });


    }


}
