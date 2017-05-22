package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.weight.ShopCartImp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by DZJ-PC on 2017/5/19.
 */

public class SublimtOrderAdapter  extends RecyclerView.Adapter{

    private static String TAG = "SublimtOrderAdapter";
    private ShopCart shopCart;
    private Context mContext;
    private int itemCount;
    private ArrayList<FoodBean> dishList;
    private ShopCartImp shopCartImp;

    public  SublimtOrderAdapter(Context context, ShopCart shopCart){
        this.shopCart = shopCart;
        this.mContext = context;
        this.itemCount = shopCart.getDishAccount();
        this.dishList = new ArrayList<>();
        dishList.addAll(shopCart.getShoppingSingleMap().keySet());
        Log.e(TAG, "SublimtOrderAdapter: "+this.itemCount );


    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_dish_item, parent, false);
        DishViewHolder viewHolder = new DishViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DishViewHolder dishholder = (DishViewHolder)holder;
        final FoodBean dish = getDishByPosition(position);

        if(dish!=null) {
            dishholder.add_dish_name.setText(dish.getDishName());
            dishholder.add_dish_price.setText("￥"+dish.getDishPrice());
            int num = shopCart.getShoppingSingleMap().get(dish);
            dishholder.add_dish_num.setText("x"+num);
            dishholder.add_dish_info.setText(dish.getIntro());
            dishholder.add_dish_sumprice.setText("￥"+num*dish.getPrice());
            if(dish.getPic().isEmpty()){
                Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(dishholder.add_dish_img);
            }
            else{
                Picasso.with(mContext).load(dish.getPic()).resize(50,50).into(dishholder.add_dish_img);
            }
        }
    }

    @Override
    public int getItemCount() {
        return this.itemCount;
    }

    public FoodBean getDishByPosition(int position){
        return dishList.get(position);
    }


    private class DishViewHolder extends RecyclerView.ViewHolder{
        private TextView add_dish_name;
        private TextView add_dish_info;
        private ImageView add_dish_img;
        private TextView add_dish_price;
        private TextView add_dish_num;
        private TextView add_dish_sumprice;

        public DishViewHolder(View itemView) {
            super(itemView);
            add_dish_name = (TextView)itemView.findViewById(R.id.add_dish_name);
            add_dish_info = (TextView)itemView.findViewById(R.id.add_dish_info);
            add_dish_img = (ImageView)itemView.findViewById(R.id.add_dish_img);
            add_dish_price = (TextView) itemView.findViewById(R.id.add_dish_price);
            add_dish_num = (TextView)itemView.findViewById(R.id.add_dish_num);
            add_dish_sumprice = (TextView)itemView.findViewById(R.id.add_dish_sumprice);
        }

    }
}
