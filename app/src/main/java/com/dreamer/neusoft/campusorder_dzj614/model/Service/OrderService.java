package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isSuccessBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public interface OrderService {

    @GET(Constant.ApiServiceUrl.GETALLORDER)  //获取当前用户所有订单信息
    Call<List<OrderBean>> getOrderList(@Query("user_id") int userid);

    @GET(Constant.ApiServiceUrl.INSERTORDERSERVICE)   //购买
    Call<isSuccessBean> toBuy(@Query("user_id") int userId,
                              @Query("food_id") int foodId,
                              @Query("num")int num,
                              @Query("sum") double sum,
                              @Query("suggesttime") String suggesttime);
}
