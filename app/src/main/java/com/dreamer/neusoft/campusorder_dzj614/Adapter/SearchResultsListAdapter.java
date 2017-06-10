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

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.ShopFoodAssessActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by DZJ-PC on 2017/5/25.
 */

public class SearchResultsListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<FoodBean> mDataList;
    private LayoutInflater mLayoutInflater;
    private Intent intent;

    private SharedPreferences user;
    private static String  ImgUrL;
    private  static FoodBean foodBean ;
public  SearchResultsListAdapter(Context mContext, List<FoodBean> mDataList){
    this.mContext = mContext;
    this.mDataList = mDataList;
    mLayoutInflater = LayoutInflater.from(mContext);

}
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.search_results_list_item,parent,false);
        return new SearchResultsListAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final FoodBean entity =(FoodBean) mDataList.get(position);
        if (null == entity)
            return;
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.search_foodname.setText(entity.getFoodname());
        viewHolder.search_foodinfo.setText("简介:"+entity.getIntro());
        viewHolder.search_price.setText("单价:￥"+entity.getPrice());
        if(entity.getPic().equals("")){
            Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(viewHolder.search_img);
        }
        else{
            Picasso.with(mContext).load(entity.getPic()).resize(50,50).into(viewHolder.search_img);
        }


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    intent=new Intent(mContext,ShopFoodAssessActivity.class);
                    intent.putExtra("food_id",entity.getFood_id());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView search_img;
        TextView search_foodname;
        TextView search_foodinfo,search_price;



        public ViewHolder(View itemView) {
            super(itemView);
            search_foodname = (TextView) itemView.findViewById(R.id.search_foodname);
            search_img = (ImageView) itemView.findViewById(R.id.search_img);
            search_foodinfo = (TextView) itemView.findViewById(R.id.search_foodinfo);
            search_price = (TextView) itemView.findViewById(R.id.search_price);

        }
    }
}
