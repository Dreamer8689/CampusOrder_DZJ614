package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.FoodAssessAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isCollectedBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CollectionService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CommentService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;
import com.dreamer.neusoft.campusorder_dzj614.view.MyListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopFoodAssessActivity extends Activity {
    public PullToRefreshScrollView pullLoadMoreRecyclerView;
    public MyListView listview;
    public TextView tvDes;
    private int food_id;
    private Intent intent;
    private ImageView foodDetail_img,collection_img;
    private CookApi cookapi,cookapi1;
    private FoodService foodservice;
    private CommentService commentService;
    private FoodBean foodbean;
    public Handler mHandler,mHandler1;
    public List<OrderBean>  OrderBeanList;
    private  TextView name,info,price;

    private int userid;
    public CookApi cookApi;
    public CollectionService collectionService;
    private SharedPreferences user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_food_detail);
        initView();
        initData();

       getFoodData();

        initEvent();

    myLister();


    }

    private void getFoodAssessData() {
        Call<List<OrderBean>> call=commentService.
                getFoodOrder(String.valueOf(foodbean.getFood_id()));
        call.enqueue(new Callback<List<OrderBean>>() {
            @Override
            public void onResponse(Call<List<OrderBean>> call, Response<List<OrderBean>> response) {
                if(response.isSuccessful()){
                    OrderBeanList =response.body();
                    Message message = new Message();
                    message.arg1 = 1;
                    mHandler1.sendMessage(message);

                }else{
                    Toast.makeText(ShopFoodAssessActivity.this, "获取评价失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderBean>> call, Throwable t) {
                Toast.makeText(ShopFoodAssessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void myLister() {
        pullLoadMoreRecyclerView.setMode(PullToRefreshBase.Mode.BOTH);
        pullLoadMoreRecyclerView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                OrderBeanList.clear();
                getFoodAssessData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                OrderBeanList.clear();
                getFoodAssessData();
            }
        });

        collection_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CollectionFood(food_id);
            }
        });

    }

    private void getFoodData() {
        Call<FoodBean> Call=foodservice.getFoodDetail(food_id);
        Call.enqueue(new Callback<FoodBean>() {
            @Override
            public void onResponse(Call<FoodBean> call, Response<FoodBean> response) {
                if(response.isSuccessful()){
                    foodbean=response.body();
                    Message message = new Message();
                    message.arg1 = 1;
                    mHandler.sendMessage(message);

                }
            }

            @Override
            public void onFailure(Call<FoodBean> call, Throwable t) {

            }
        });
    }

    private void initView() {
        listview= (MyListView) findViewById(R.id.FoodDetail_listview);
        pullLoadMoreRecyclerView= (PullToRefreshScrollView) findViewById(R.id.foodDetail_pullLoadMoreRecyclerView);
        foodDetail_img=(ImageView)findViewById(R.id.foodDetail_img);

        cookapi=new CookApi(2);
        foodservice=cookapi.getFoodService();
        cookapi1=new CookApi(5);
        commentService=cookapi1.getCommentService();
        name=(TextView)findViewById(R.id.foodDetail_name);
        info=(TextView)findViewById(R.id.foodDetail_intro);
        price=(TextView)findViewById(R.id.foodDetail_price);
        collection_img=(ImageView)findViewById(R.id.shop_cook_collect);


    }


    private void initEvent() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){

                    Picasso.with(ShopFoodAssessActivity.this)
                            .load(foodbean.getPic())
                            .into(foodDetail_img);

                    getFoodAssessData();
                    name.setText(foodbean.getFoodname());
                    info.setText("介绍："+foodbean.getIntro());
                    price.setText("价格："+foodbean.getPrice());
                }
            }
        };

        mHandler1=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){
                FoodAssessAdapter foodAssessAdapter=
                        new FoodAssessAdapter(ShopFoodAssessActivity.this,OrderBeanList);
                    listview.setAdapter(foodAssessAdapter);
                    isCollected(food_id);



                }
            }
        };




    }

    private void initData() {
        intent=getIntent();
        food_id=intent.getIntExtra("food_id",0);
        cookApi=new CookApi(4);
        collectionService=cookApi.getCollectionService();
        user=getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();



    }


    public void isCollected(int food_id){

        Call<isCollectedBean> call =collectionService.isCollection(userid,food_id,1);
        call.enqueue(new Callback<isCollectedBean>() {
            @Override
            public void onResponse(Call<isCollectedBean> call, Response<isCollectedBean> response) {
                if(response.isSuccessful()){
                    String Collected=response.body().getCollected();
                    if(Collected.equals("1")){
                        foodbean.setIsCollected(1);
                        Picasso.with(ShopFoodAssessActivity.this).load(R.drawable.heart_on).into(collection_img);

                    }else{
                        foodbean.setIsCollected(0);
                        Picasso.with(ShopFoodAssessActivity.this).load(R.drawable.heart_off).into(collection_img);

                    }

                }else{
                    Toast.makeText(ShopFoodAssessActivity.this, "返回数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<isCollectedBean> call, Throwable t) {
                Toast.makeText(ShopFoodAssessActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }




    public  void CollectionFood(int food_id){
        Call<isCollectedBean> call=collectionService.toCollectionFood(userid,food_id);
        call.enqueue(new Callback<isCollectedBean>() {
            @Override
            public void onResponse(Call<isCollectedBean> call, Response<isCollectedBean> response) {
                if(response.isSuccessful()){
                    String sccess=response.body().getSuccess();
                    if(sccess.equals("1")){
                        if( foodbean.getIsCollected()==1){
                            foodbean.setIsCollected(0);
                            Toast.makeText(ShopFoodAssessActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                            Picasso.with(ShopFoodAssessActivity.this).load(R.drawable.heart_off).into(collection_img);
                        }else{
                            foodbean.setIsCollected(1);
                            Toast.makeText(ShopFoodAssessActivity.this, "收藏成功", Toast.LENGTH_SHORT).show();
                            Picasso.with(ShopFoodAssessActivity.this).load(R.drawable.heart_on).into(collection_img);
                        }

                    }
                    else{
                        Toast.makeText(ShopFoodAssessActivity.this, "收藏失败", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<isCollectedBean> call, Throwable t) {
                Toast.makeText(ShopFoodAssessActivity.this, "网路请求失败", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
