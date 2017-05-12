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
import com.dreamer.neusoft.campusorder_dzj614.activity.ShopDetailActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.ShopBean;
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
 * Created by DZJ-PC on 2017/3/20.
 */

public class FoodListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<ShopBean> mDataList;
    private LayoutInflater mLayoutInflater;
    public CookApi cookApi;
    public  int flag ;
    public CollectionService collectionService;
    private SharedPreferences user;
    private int userid;

    public FoodListAdapter(Context mContext, List<ShopBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
        cookApi=new CookApi(4);

        collectionService=cookApi.getCollectionService();
        user=mContext.getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.layoutcardview,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ShopBean entity =(ShopBean) mDataList.get(position);
        if (null == entity)
            return;
      final ViewHolder viewHolder = (ViewHolder) holder;
         viewHolder.bar.setRating((int) entity.getLevel());
        viewHolder.cook_name.setText(entity.getShopname());
        viewHolder.cook_count.setText("简介："+entity.getIntro());
        viewHolder.cook_keywords.setText("订餐电话："+entity.getPhonenum());
        viewHolder.cook_desc.setText("订餐地址："+entity.getAddress());
        if(entity.getPic().equals("")){
            Picasso.with(mContext).load(R.drawable.no_pic).resize(50,50).into(viewHolder.cook_img);
        }
        else{
            Picasso.with(mContext).load(entity.getPic()).resize(50,50).into(viewHolder.cook_img);
        }
        isCollected(entity.getShop_id(),viewHolder,entity);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,ShopDetailActivity.class);

                intent.putExtra("shop_id",entity.getShop_id());
                mContext.startActivity(intent);
            }
        });
        viewHolder.collection_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionShop(entity.getShop_id(),viewHolder,entity);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cook_img,collection_img;
        TextView cook_keywords;
        TextView cook_name;
        TextView cook_count;
        TextView cook_desc;
         RatingBar bar;
        // TextView news_info;

        public ViewHolder(View itemView) {
            super(itemView);
            bar=(RatingBar)itemView.findViewById(R.id.ratingBar);
            cook_name = (TextView) itemView.findViewById(R.id.cook_name);
            cook_img = (ImageView) itemView.findViewById(R.id.cook_img);
            cook_keywords = (TextView) itemView.findViewById(R.id.cook_keywords);
            cook_count= (TextView) itemView.findViewById(R.id.cook_count);
            cook_desc=(TextView)itemView.findViewById(R.id.cook_desc);
            collection_img=(ImageView)itemView.findViewById(R.id.cook_collection_img);
        }
    }

    public void isCollected(int shop_id, final ViewHolder viewHolder, final ShopBean entity){

        Call<isCollectedBean> call =collectionService.isCollection(userid,shop_id,0);
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




    public  void CollectionShop(int shop_id, final ViewHolder viewHolder, final ShopBean entity){
        Call<isCollectedBean> call=collectionService.toCollectionShop(userid,shop_id);
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
