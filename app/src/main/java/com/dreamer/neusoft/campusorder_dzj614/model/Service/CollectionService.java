package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.CollectionBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isCollectedBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public interface CollectionService {

    @GET(Constant.ApiServiceUrl.USERCOLLECTION)  //获取当前用户的所有收藏信息
    Call<List<CollectionBean>> getUserCollection(@Query("user_id") int userid,
                                                @Query("flag") int flag);

    @GET(Constant.ApiServiceUrl.COLLECTIONSHOP)  //收藏/取消收藏店铺接口
    Call<isCollectedBean> toCollectionShop(@Query("user_id") int userid,
                                  @Query("shop_id") int shopid);



    @GET(Constant.ApiServiceUrl.COLLECTIONFOOD)  //收藏/取消收藏店铺接口
    Call<isCollectedBean> toCollectionFood(@Query("user_id") int userid,
                                           @Query("food_id") int foodid);

    @GET(Constant.ApiServiceUrl.ISCOLLECTION)  //是否收藏菜品接口
    Call<isCollectedBean> isCollection(@Query("user_id") int userid,
                                       @Query("shop_food_id") int ShopFoodId,
                                       @Query("flag") int flag);



}
