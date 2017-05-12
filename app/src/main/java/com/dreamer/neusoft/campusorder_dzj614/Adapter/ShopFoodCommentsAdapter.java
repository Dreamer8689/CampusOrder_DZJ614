package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DZJ-PC on 2017/4/30.
 */

public class ShopFoodCommentsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public CookApi cookApi;
    public FoodService foodService;
    private  static FoodBean foodBean ;
    private Context mContext;
    private ArrayList<OrderBean> mDataList;
    private LayoutInflater mLayoutInflater;
   private static String  ImgUrL;
    Integer i=0;
    public ShopFoodCommentsAdapter(Context mContext, ArrayList<OrderBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        cookApi=new CookApi(2);
        foodService=cookApi.getFoodService();
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.shopfoodcomments_cardview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderBean entity =(OrderBean) mDataList.get(position);
        if (null == entity)
            return;
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.cook_name.setText(entity.getFoodname()+"("+entity.getShopname()+")");
        viewHolder.cook_count.setText("数量："+entity.getNum());
        viewHolder.cook_keywords.setText("总价："+entity.getPrice());
        viewHolder.UserName.setText("用户："+entity.getUsername());

        viewHolder.comment.setText("评论："+entity.getContent());
        viewHolder.commenttime.setText("评论时间："+entity.getComment_time());
        viewHolder.adress.setText("地址："+entity.getShopaddress());
        viewHolder.ordertime.setText("订餐时间："+entity.getOrdertime());

        getShopFoodImg(entity.getFood_id(),viewHolder);



        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(mContext,ShopDetailActivity.class);
//
//                intent.putExtra("shop_id",entity.getShop_id());
//                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cook_img;
        TextView cook_keywords;
        TextView cook_name;
        TextView cook_count;
        TextView comment,UserName,commenttime,adress,ordertime;
        RatingBar bar;
        // TextView news_info;

        public ViewHolder(View itemView) {
            super(itemView);
            bar=(RatingBar)itemView.findViewById(R.id.ratingBar);
            cook_name = (TextView) itemView.findViewById(R.id.shop_food_name);
            cook_img = (ImageView) itemView.findViewById(R.id.shop_food_img);
            cook_keywords = (TextView) itemView.findViewById(R.id.shop_food_total);
            cook_count= (TextView) itemView.findViewById(R.id.shop_food_num);
            UserName=(TextView)itemView.findViewById(R.id.shop_food_UserName);

            comment = (TextView) itemView.findViewById(R.id.shop_food_comment);
            commenttime= (TextView) itemView.findViewById(R.id.shop_food_commenttime);
            adress=(TextView)itemView.findViewById(R.id.shop_food_adress);

            ordertime=(TextView)itemView.findViewById(R.id.shop_food_ordertime);

        }
    }

    public void  getShopFoodImg(int food_id, final ViewHolder viewHolder) {


            Call<FoodBean> call=foodService.getFoodDetail(food_id);
            call.enqueue(new Callback<FoodBean>() {
               @Override
               public void onResponse(Call<FoodBean> call, Response<FoodBean> response) {
                   if (response.isSuccessful()) {
                       foodBean=response.body();
                       ImgUrL=foodBean.getPic().toString();

                       if(ImgUrL.isEmpty()){
                           Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(viewHolder.cook_img);
                       }
                       else{
                           Picasso.with(mContext).load(ImgUrL).resize(50,50).into(viewHolder.cook_img);
                       }
                   }
               }

               @Override
               public void onFailure(Call<FoodBean> call, Throwable t) {

               }
           });


    }

}
