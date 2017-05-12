package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.ShopFoodAssessActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isCollectedBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CollectionService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by DZJ-PC on 2017/4/30.
 */

public class FoodDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context mContext;
    private List<FoodBean> mDataList;
    private LayoutInflater mLayoutInflater;
    private int userid;
    public CookApi cookApi;
    public CollectionService collectionService;
    private SharedPreferences user;
    public FoodDetailAdapter(Context mContext, List<FoodBean>  mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
        cookApi=new CookApi(4);

        collectionService=cookApi.getCollectionService();
        user=mContext.getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.shopcook_detail_cardview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FoodBean entity =(FoodBean) mDataList.get(position);
        if (null == entity)
            return;
        final ViewHolder viewHolder = (ViewHolder) holder;
       if(entity.getRecommand()==0){
           Picasso.with(mContext).load(R.drawable.no_pic).resize(32,32).into(viewHolder.cook_rem);
       }
        viewHolder.cook_name.setText(entity.getFoodname());
        viewHolder.cook_price.setText(entity.getPrice()+"元/份");
        viewHolder.cook_info.setText(entity.getIntro());
        if(entity.getPic().equals("")){
            Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(viewHolder.cook_img);
        }
        else{
            Picasso.with(mContext).load(entity.getPic()).resize(50,50).into(viewHolder.cook_img);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ShopFoodAssessActivity.class);
                intent.putExtra("food_id",entity.getFood_id());
                mContext.startActivity(intent);
            }
        });
        viewHolder.collection_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionFood(entity.getFood_id(),viewHolder,entity);
            }
        });

       isCollected(entity.getFood_id(),viewHolder,entity);


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView cook_img,cook_rem,collection_img;
        TextView cook_price;
        TextView cook_name;
        TextView cook_info;

        RatingBar bar;


        public ViewHolder(View itemView) {
            super(itemView);
            cook_rem=(ImageView)itemView.findViewById(R.id.shop_cook_recommentImg);
            cook_name = (TextView) itemView.findViewById(R.id.shop_cook_name);
            cook_img = (ImageView) itemView.findViewById(R.id.shop_cook_img);
            cook_price = (TextView) itemView.findViewById(R.id.shop_cook_price);
            cook_info= (TextView) itemView.findViewById(R.id.shop_cook_info);

            collection_img=(ImageView)itemView.findViewById(R.id.shop_cook_collect);

        }

    }


    public void isCollected(int food_id, final ViewHolder viewHolder, final FoodBean entity){

        Call<isCollectedBean> call =collectionService.isCollection(userid,food_id,1);
        call.enqueue(new Callback<isCollectedBean>() {
            @Override
            public void onResponse(Call<isCollectedBean> call, Response<isCollectedBean> response) {
                if(response.isSuccessful()){
                    String Collected=response.body().getCollected();
                    if(Collected.equals("1")){
                        entity.setIsCollected(1);
                        Picasso.with(mContext).load(R.drawable.heart_on).into(viewHolder.collection_img);

                    }else{
                        entity.setIsCollected(0);
                        Picasso.with(mContext).load(R.drawable.heart_off).into(viewHolder.collection_img);

                    }

                }else{
                    Toast.makeText(mContext, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isCollectedBean> call, Throwable t) {
                Toast.makeText(mContext, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }




    public  void CollectionFood(int food_id, final ViewHolder viewHolder, final FoodBean entity){
        Call<isCollectedBean> call=collectionService.toCollectionFood(userid,food_id);
        call.enqueue(new Callback<isCollectedBean>() {
            @Override
            public void onResponse(Call<isCollectedBean> call, Response<isCollectedBean> response) {
                if(response.isSuccessful()){
                    String sccess=response.body().getSuccess();
                    if(sccess.equals("1")){
                        if( entity.getIsCollected()==1){
                            entity.setIsCollected(0);
                            Toast.makeText(mContext, "取消收藏", Toast.LENGTH_SHORT).show();
                            Picasso.with(mContext).load(R.drawable.heart_off).into(viewHolder.collection_img);
                        }else{
                            entity.setIsCollected(1);
                            Toast.makeText(mContext, "收藏成功", Toast.LENGTH_SHORT).show();
                            Picasso.with(mContext).load(R.drawable.heart_on).into(viewHolder.collection_img);
                        }

                    }
                    else{
                        Toast.makeText(mContext, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<isCollectedBean> call, Throwable t) {
                Toast.makeText(mContext, "网路请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }




}
