package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public interface OrderService {

    @GET(Constant.ApiServiceUrl.GETALLORDER)  //获取当前用户所有订单信息
    Call<OrderBean> getOrderList(@Query("user_id") String userid);

    @GET(Constant.ApiServiceUrl.INSERTORDERSERVICE)   //购买
    Call<String> toBuy(@Query("user_id") String userId,
                       @Query("food_id") String foodId,
                       @Query("num")String num,
                       @Query("sum") String sum,
                       @Query("suggesttime") String suggesttime);
}
