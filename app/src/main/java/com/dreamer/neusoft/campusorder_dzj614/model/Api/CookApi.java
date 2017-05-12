package com.dreamer.neusoft.campusorder_dzj614.model.Api;

import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CollectionService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.CommentService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.OrderService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.ShopService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.UserService;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by DZJ-PC on 2017/3/19.
 */

public class CookApi {


    private UserService Service;
    private ShopService shopService;
    private FoodService foodService;
    private OrderService orderService;
    private CollectionService collectionService;
    private CommentService commentService;

    public  CookApi(int i){
        Retrofit  retrofit=new Retrofit.Builder()
                .baseUrl(Constant.ApiBaseUrl.FOODBASEURL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        switch (i){
            case 0:
                Service = retrofit.create(UserService.class);
                break;
            case 1:
                shopService = retrofit.create(ShopService.class);
                break;
            case 2:
                foodService= retrofit.create(FoodService.class);
                break;
            case 3:
                orderService= retrofit.create(OrderService.class);
                break;
            case 4:
                collectionService= retrofit.create(CollectionService.class);
                break;
            case 5:
                commentService= retrofit.create(CommentService.class);
                break;

        }


    }

    public UserService getService(){
        return this.Service;
    }
    public ShopService getShopService(){
        return this.shopService;
    }

    public FoodService getFoodService(){
        return this.foodService;
    }
    public OrderService getOrderService(){
        return this.orderService;
    }

    public CollectionService getCollectionService(){
        return this.collectionService;
    }
    public CommentService getCommentService(){
        return this.commentService;
    }

}
