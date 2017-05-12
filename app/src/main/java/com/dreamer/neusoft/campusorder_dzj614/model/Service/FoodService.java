package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public interface FoodService {
    @GET(Constant.ApiServiceUrl.FOODBYSHOPSERVER)   //获取当前店铺的所有菜单信息
    Call<List<FoodBean>> getFoodByShop(@Query("shop_id") String shop_id);


    @GET(Constant.ApiServiceUrl.FOODDETAILSERVICE)  //获取菜谱详情
    Call<FoodBean> getFoodDetail(@Query("food_id") int food_id);


}
