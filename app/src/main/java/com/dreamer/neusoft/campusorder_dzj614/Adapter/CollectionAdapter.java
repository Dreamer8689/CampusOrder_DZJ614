package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.ShopDetailActivity;
import com.dreamer.neusoft.campusorder_dzj614.activity.ShopFoodAssessActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.CollectionBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.ShopBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.ShopService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DZJ-PC on 2017/5/10.
 */

public class CollectionAdapter  extends  RecyclerView.Adapter<RecyclerView.ViewHolder> {
       private Context mContext;
        private List<CollectionBean> mDataList;
        private LayoutInflater mLayoutInflater;
        public CookApi cookApi,cookApi1;
    private  Intent intent;
        public ShopService shopService;
        public FoodService foodService;
        private SharedPreferences user;
         public ShopBean shopBean;

        public CollectionAdapter(Context mContext, List<CollectionBean> mDataList) {
            this.mContext = mContext;
            this.mDataList = mDataList;

            cookApi=new CookApi(1);
            shopService=cookApi.getShopService();
            cookApi1=new CookApi(2);
            foodService=cookApi1.getFoodService();

          mLayoutInflater = LayoutInflater.from(mContext);
        }


        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.collection_cardview_item,parent,false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            final CollectionBean entity =(CollectionBean) mDataList.get(position);
            if (null == entity)
                return;
            final ViewHolder viewHolder = (ViewHolder) holder;

            if(entity.getPic().equals("")){
                Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(viewHolder.cook_img);
            }
            else{
                Picasso.with(mContext).load(entity.getPic()).resize(50,50).into(viewHolder.cook_img);
            }


            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(entity.getFlag()==0){
                        intent=new Intent(mContext,ShopDetailActivity.class);

                        intent.putExtra("shop_id",entity.getShop_id());
                    }else{

                        intent=new Intent(mContext,ShopFoodAssessActivity.class);

                        intent.putExtra("food_id",entity.getFood_id());
                    }


                    mContext.startActivity(intent);
                }
            });
            if(entity.getFlag()==0){
                getShopDetailData(viewHolder,entity.getShop_id());
            }else{
                getFoodDetailData(viewHolder,entity.getFood_id());
            }


        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }


public class ViewHolder extends RecyclerView.ViewHolder{

    ImageView cook_img;
    TextView collection_comment;
    TextView cook_name,collection_price;
    TextView collection_address;



    public ViewHolder(View itemView) {
        super(itemView);
        cook_name = (TextView) itemView.findViewById(R.id.collection_name);
        cook_img = (ImageView) itemView.findViewById(R.id.shopAssess_collection_img);
        collection_comment = (TextView) itemView.findViewById(R.id.collection_comment);
        collection_address = (TextView) itemView.findViewById(R.id.collection_address);
        collection_price=(TextView)itemView.findViewById(R.id.collection_price);

    }
}

    public void getShopDetailData(final ViewHolder viewHolder, int shop_id) {
        Call<ShopBean> call=shopService.getShopDetail(String.valueOf(shop_id));
        call.enqueue(new Callback<ShopBean>() {
            @Override
            public void onResponse(Call<ShopBean> call, Response<ShopBean> response) {
                if(response.isSuccessful()){

                    shopBean=response.body();

                    viewHolder.collection_address.setText("订餐地址："+shopBean.getAddress());
                    viewHolder.cook_name.setText(shopBean.getShopname());
                    viewHolder.collection_price.setText("订餐电话"+shopBean.getPhonenum());
                    viewHolder.collection_comment.setText("店铺简介："+shopBean.getIntro());

                }else{
                    Toast.makeText(mContext, "返回数据失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ShopBean> call, Throwable t) {
                Toast.makeText(mContext, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public void getFoodDetailData(final ViewHolder viewHolder, int food_id) {
        Call<FoodBean> call=foodService.getFoodDetail(food_id);
        call.enqueue(new Callback<FoodBean>() {
            @Override
            public void onResponse(Call<FoodBean> call, Response<FoodBean> response) {
                if(response.isSuccessful()){

                    FoodBean foodBean=response.body();
                    viewHolder.collection_address.setText(" ");
                    viewHolder.cook_name.setText("菜名："+foodBean.getFoodname());
                    viewHolder.collection_price.setText("单价"+foodBean.getPrice()+"元/份");
                    viewHolder.collection_comment.setText("菜品描述："+foodBean.getIntro());

                }else{
                    Toast.makeText(mContext, "返回数据失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<FoodBean> call, Throwable t) {
                Toast.makeText(mContext, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

}
