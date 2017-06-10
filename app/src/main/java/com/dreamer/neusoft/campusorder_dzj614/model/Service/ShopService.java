package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.ShopBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public interface ShopService {
    @GET(Constant.ApiServiceUrl.ALLSHOPSERVICE)  //获取所有的店铺
    Call<List<ShopBean>> getShop();


    @GET(Constant.ApiServiceUrl.SHOPDETAILSERVICE)   //获取当前店铺详情
    Call<ShopBean> getShopDetail(@Query("shop_id") String shop_id);


    @GET(Constant.ApiServiceUrl.SEARCHSERVICE)  //搜索菜品接口
    Call<List<FoodBean>> toSearch(@Query("search") String search);
}
