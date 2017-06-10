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

public class OrderAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

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
    public OrderAdapter(Context mContext, List<OrderBean> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;

        cookApi1=new CookApi(5);
        commentService=cookApi1.getCommentService();
        cookApi=new CookApi(2);
        foodService=cookApi.getFoodService();

        mLayoutInflater = LayoutInflater.from(mContext);
    }


    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v =mLayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final OrderBean entity =(OrderBean) mDataList.get(position);
        if (null == entity)
            return;
        final ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.order_adress.setText("订餐地址:"+entity.getShopaddress());
        viewHolder.order_ordertime.setText("订餐时间:"+entity.getOrdertime());
        viewHolder.order_foodtotal.setText("总价:￥"+entity.getSum());
        viewHolder.order_foodprice.setText("单价:￥"+entity.getPrice());
        viewHolder.order_foodnum.setText("数量:"+entity.getNum());
        viewHolder.order_foodname.setText(entity.getFoodname()+"("+entity.getShopname()+")");
         if(entity.getContent() == null ||"".equals(entity.getContent())){
             viewHolder.content.setText("暂无评论");   }
         else{
             viewHolder.content.setText("评论:"+entity.getContent());

         }
        getShopFoodImg(entity.getFood_id(),viewHolder);


        viewHolder.order_btnUnRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    new SuperDialog.Builder((FragmentActivity) mContext).setRadius(10)
                            .setTitle("添加评论")
                            .setInput("请填写评论")
                            .setNegativeButton("取消", null)
                            .setPositiveInputButton("确定", new SuperDialog.OnClickPositiveInputListener() {
                                @Override
                                public void onClick(String text, View v) {
                                         toComment(entity.getOrder_id(),text);

                                }
                            }).build();

                }
            }
        });

        viewHolder.order_btnTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    mOnSwipeListener.onTop(viewHolder.getAdapterPosition());

                }
            }
        });

        viewHolder.order_btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnSwipeListener) {
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //((CstSwipeDelMenu) holder.itemView).quickClose();
                    new SuperDialog.Builder((FragmentActivity) mContext).setTitle("删除订单").setMessage("确认删除订单")
                            .setBackgroundColor(Color.WHITE)
                            .setNegativeButton("取消", null)
                            .setPositiveButton("确定", new SuperDialog.OnClickPositiveListener() {
                                @Override
                                public void onClick(View v) {

                                    mOnSwipeListener.onDel(viewHolder.getAdapterPosition());
                                }
                            }).build();
                }
            }
        });

    }

    private void toComment(int order_id, String text) {
        Call<isSuccessBean> call=commentService.toAddComment(order_id,text);
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
        TextView order_foodname;
        TextView order_foodnum,order_foodprice;
        TextView order_foodtotal;
        TextView order_adress,order_ordertime,content;
        Button order_btnTop,order_btnUnRead,order_btnDelete;


        public ViewHolder(View itemView) {
            super(itemView);
            content=(TextView)itemView.findViewById(R.id.order_comment);
            order_foodname = (TextView) itemView.findViewById(R.id.order_foodname);
            cook_img = (ImageView) itemView.findViewById(R.id.order_img);
            order_foodnum = (TextView) itemView.findViewById(R.id.order_foodnum);
            order_foodprice = (TextView) itemView.findViewById(R.id.order_foodprice);
            order_foodtotal=(TextView)itemView.findViewById(R.id.order_foodtotal);
            order_adress = (TextView) itemView.findViewById(R.id.order_adress);
            order_ordertime = (TextView) itemView.findViewById(R.id.order_ordertime);
            order_btnTop=(Button) itemView.findViewById(R.id.order_btnTop);
            order_btnUnRead=(Button) itemView.findViewById(R.id.order_btnUnRead);
            order_btnDelete=(Button) itemView.findViewById(R.id.order_btnDelete);

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
