package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.CommentAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CommentService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends Activity {
    private CookApi cookapi;
    private CommentService commentService;
    private List<OrderBean> orderBeanList;
    private SharedPreferences user;
    private int userid;
    public RecyclerView recyclerView;

    public Handler mHandler;
    private CommentAdapter commentAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initView();
        initEvent();
        initData();
    }

    private void initDatas() {
    }

    private void initEvent() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){
                    MyEvent();
                }
            }
        };
    }

    private void MyEvent() {
        commentAdapter=new CommentAdapter(this,orderBeanList);
        commentAdapter.setOnDelListener(new CommentAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < orderBeanList.size()) {
                    Toast.makeText(CommentActivity.this, "删除:" + pos, Toast.LENGTH_SHORT).show();
                    orderBeanList.remove(pos);
                    commentAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < orderBeanList.size()) {
                    OrderBean orderbean = orderBeanList.get(pos);
                    orderBeanList.remove(orderbean);
                    commentAdapter.notifyItemInserted(0);
                    orderBeanList.add(0, orderbean);
                    commentAdapter.notifyItemRemoved(pos + 1);
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        recyclerView.scrollToPosition(0);
                    }

                }
            }
        });
        recyclerView.setAdapter(commentAdapter);


    }

    private void initData() {
        user=getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();
        getComment();
    }

    private void getComment() {
        Call<List<OrderBean>> Call=commentService.getCommentList(userid);
        Call.enqueue(new Callback<List<OrderBean>>() {
            @Override
            public void onResponse(Call<List<OrderBean>> call, Response<List<OrderBean>> response) {
                if(response.isSuccessful()){
                    orderBeanList=response.body();
                    Toast.makeText(CommentActivity.this, "orderBeanList.size():" + orderBeanList.size(), Toast.LENGTH_SHORT).show();

                    Message message = new Message();
                    message.arg1 = 1;
                    mHandler.sendMessage(message);

                }else{
                    Toast.makeText(CommentActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderBean>> call, Throwable t) {
                Toast.makeText(CommentActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        cookapi=new CookApi(5);
        commentService=cookapi.getCommentService();
        recyclerView = (RecyclerView) findViewById(R.id.comment_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(CommentActivity.this));
        mLayoutManager=new LinearLayoutManager(CommentActivity.this);
    }

}
