package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.ShopFoodAssessActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.weight.ShopCartImp;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by DZJ-PC on 2017/4/30.
 */

public class FoodDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private Context mContext;
    private List<FoodBean> mDataList;
    private LayoutInflater mLayoutInflater;



    private int mItemCount;
    private ShopCart shopCart;
    private ShopCartImp shopCartImp;
    public FoodDetailAdapter(Context mContext, List<FoodBean>  mDataList,ShopCart shopCart) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        this.shopCart = shopCart;
        mLayoutInflater = LayoutInflater.from(mContext);

    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.shopcook_detail_cardview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final FoodBean entity =(FoodBean) mDataList.get(position);
        if (null == entity)
            return;
        final ViewHolder viewHolder = (ViewHolder) holder;
       if(entity.getRecommand()==0){
           Picasso.with(mContext).load(R.drawable.no_pic).resize(32,32).into(viewHolder.cook_rem);
       }
        viewHolder.cook_name.setText(entity.getFoodname());
        viewHolder.cook_price.setText("￥"+entity.getPrice()+"元/份");
        viewHolder.cook_info.setText(entity.getIntro());
        if(entity.getPic().equals("")){
            Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(viewHolder.cook_img);
        }
        else{
            Picasso.with(mContext).load(entity.getPic()).resize(50,50).into(viewHolder.cook_img);
        }


        int count = 0;
        if(shopCart.getShoppingSingleMap().containsKey(entity)){
            count = shopCart.getShoppingSingleMap().get(entity);
        }
        if(count<=0){
            viewHolder.right_dish_remove_iv.setVisibility(View.GONE);
            viewHolder.right_dish_account_tv.setVisibility(View.GONE);
        }else {
            viewHolder.right_dish_remove_iv.setVisibility(View.VISIBLE);
            viewHolder.right_dish_account_tv.setVisibility(View.VISIBLE);

            viewHolder.right_dish_account_tv.setText(count+"");
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext, ShopFoodAssessActivity.class);
                intent.putExtra("food_id",entity.getFood_id());
                mContext.startActivity(intent);
            }
        });


        viewHolder.right_dish_add_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(shopCart.addShoppingSingle(entity)) {
                    notifyItemChanged(position);
                    if(shopCartImp!=null)
                        shopCartImp.add(v,position);

                }

            }
        });
        viewHolder.right_dish_remove_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(shopCart.subShoppingSingle(entity)){
                    notifyItemChanged(position);
                    if(shopCartImp!=null)
                        shopCartImp.remove(view,position);
                }

            }
        });


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

        private ImageView right_dish_remove_iv;
        private ImageView right_dish_add_iv;
        private TextView right_dish_account_tv;



        public ViewHolder(View itemView) {
            super(itemView);
            cook_rem=(ImageView)itemView.findViewById(R.id.shop_cook_recommentImg);
            cook_name = (TextView) itemView.findViewById(R.id.shop_cook_name);
            cook_img = (ImageView) itemView.findViewById(R.id.shop_cook_img);
            cook_price = (TextView) itemView.findViewById(R.id.shop_cook_price);
            cook_info= (TextView) itemView.findViewById(R.id.shop_cook_info);

            collection_img=(ImageView)itemView.findViewById(R.id.shop_cook_collect);

            right_dish_remove_iv = (ImageView)itemView.findViewById(R.id.shop_cook_remove);
            right_dish_add_iv = (ImageView)itemView.findViewById(R.id.shop_cook_add);
            right_dish_account_tv = (TextView) itemView.findViewById(R.id.shop_cook_account);
        }

    }



    public ShopCartImp getShopCartImp() {
        return shopCartImp;
    }

    public void setShopCartImp(ShopCartImp shopCartImp) {
        this.shopCartImp = shopCartImp;
    }



}
