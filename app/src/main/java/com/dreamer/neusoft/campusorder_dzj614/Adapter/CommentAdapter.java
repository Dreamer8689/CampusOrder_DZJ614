package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.CommentActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isSuccessBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CommentService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;
import com.mylhyl.superdialog.SuperDialog;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by DZJ-PC on 2017/5/13.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<OrderBean> mDataList;
    private LayoutInflater mLayoutInflater;
    public CookApi cookApi,cookApi1;
    private CommentService commentService;
    private Intent intent;
    public FoodService foodService;
    private SharedPreferences user;
    private static String  ImgUrL;
    private  static FoodBean foodBean ;
    public CommentAdapter(Context mContext, List<OrderBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;

        cookApi=new CookApi(2);
        foodService=cookApi.getFoodService();
        cookApi1=new CookApi(5);
        commentService=cookApi1.getCommentService();
        mLayoutInflater = LayoutInflater.from(mContext);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.comment_cardview_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderBean entity =(OrderBean) mDataList.get(position);
        if (null == entity)
            return;
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.comment_adress.setText("订餐地址:"+entity.getShopaddress());
        viewHolder.comment_commenttime.setText("评论时间:"+entity.getComment_time());
        viewHolder.comment_foodtotal.setText("总价:￥"+entity.getSum());
        viewHolder.comment_foodprice.setText("单价:￥"+entity.getPrice());
        viewHolder.comment_foodnum.setText("数量:"+entity.getNum());
        viewHolder.comment_content.setText("评论:"+entity.getContent());
        viewHolder.comment_foodname.setText(entity.getFoodname()+"("+entity.getShopname()+")");

        getShopFoodImg(entity.getFood_id(),viewHolder);


        viewHolder.comment_btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onTop(viewHolder.getAdapterPosition());

                }
            }
        });
        viewHolder.comment_btnUnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    new SuperDialog.Builder((FragmentActivity) mContext).setRadius(10)
                            .setTitle("修改评论")
                            .setInput("请填写评论")
                            .setNegativeButton("取消", null)
                            .setPositiveInputButton("确定", new SuperDialog.OnClickPositiveInputListener() {
                                @Override
                                public void onClick(String text, View v) {
                                    toChangeComment(entity.getOrder_id(),text);

                                }
                            }).build();

                }
            }
        });
        viewHolder.comment_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    new SuperDialog.Builder((FragmentActivity) mContext).setTitle("删除评论").setMessage("确认删除评论")
                            .setBackgroundColor(Color.WHITE)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new SuperDialog.OnClickPositiveListener() {
                                @Override
                                public void onClick(View v) {
                                  toDeleteComment(entity.getOrder_id());
                                    mOnSwipeListener.onDel(viewHolder.getAdapterPosition());
                                }
                            }).build();
                }
            }
        });

    }

    private void toDeleteComment(int order_id) {
        Call<isSuccessBean> Call=commentService.toDeleteComment(order_id);
        Call.enqueue(new Callback<isSuccessBean>() {
            @Override
            public void onResponse(Call<isSuccessBean> call, Response<isSuccessBean> response) {
                if(response.isSuccessful()){
                      if(response.body().getSuccess().equals("1")){
                          Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
                      }else{
                          Toast.makeText(mContext, "删除失败", Toast.LENGTH_SHORT).show();
                      }
                }else{
                    Toast.makeText(mContext, "数据返回失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isSuccessBean> call, Throwable t) {
                Toast.makeText(mContext, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void toChangeComment(int order_id, String text) {
        Call<isSuccessBean> call=commentService.toUpdateComment(order_id,text);
        call.enqueue(new Callback<isSuccessBean>() {
            @Override
            public void onResponse(Call<isSuccessBean> call, Response<isSuccessBean> response) {
                if (response.isSuccessful()) {
                    if(response.body().getSuccess().equals("1")){
                        Intent intent=new Intent(mContext, CommentActivity.class);
                        mContext.startActivity(intent);
                        Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(mContext, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "数据返回失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isSuccessBean> call, Throwable t) {
                Toast.makeText(mContext, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public interface onSwipeListener {
        void onDel(int pos);

        void onTop(int pos);
    }

    private onSwipeListener mOnSwipeListener;

    public onSwipeListener getOnDelListener() {
        return mOnSwipeListener;
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }



    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView cook_img;
        TextView comment_foodname;
        TextView comment_foodnum,comment_foodprice;
        TextView comment_foodtotal;
        TextView comment_adress,comment_commenttime,comment_content;
        Button comment_btnTop,comment_btnUnRead,comment_btnDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            comment_foodname = (TextView) itemView.findViewById(R.id.comment_foodname);
            cook_img = (ImageView) itemView.findViewById(R.id.comment_img);
            comment_foodnum = (TextView) itemView.findViewById(R.id.comment_foodnum);
            comment_foodprice = (TextView) itemView.findViewById(R.id.comment_foodprice);
            comment_foodtotal=(TextView)itemView.findViewById(R.id.comment_foodtotal);
            comment_adress = (TextView) itemView.findViewById(R.id.comment_adress);
            comment_commenttime=(TextView)itemView.findViewById(R.id.comment_commenttime);
            comment_btnTop=(Button) itemView.findViewById(R.id.comment_btnTop);
            comment_btnUnRead=(Button) itemView.findViewById(R.id.comment_btnUnRead);
            comment_btnDelete=(Button) itemView.findViewById(R.id.comment_btnDelete);
            comment_content=(TextView)itemView.findViewById(R.id.comment_comment);
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
