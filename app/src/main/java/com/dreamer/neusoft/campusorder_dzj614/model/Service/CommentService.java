package com.dreamer.neusoft.campusorder_dzj614.model.Service;

import com.dreamer.neusoft.campusorder_dzj614.constant.Constant;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public interface CommentService {

    @GET(Constant.ApiServiceUrl.FOODORDERSERVICE)  //获取菜谱评价列表
    Call<List<OrderBean>> getFoodOrder(@Query("food_id") String food_id);


    @GET(Constant.ApiServiceUrl.GETALLCOMMENT)  //获取当前用户所有评论信息
    Call<OrderBean> getCommentList(@Query("user_id") String userid);

    @GET(Constant.ApiServiceUrl.INSERTCOMMENT)  //获取当前用户所有评论信息
    Call<String> toAddComment(@Query("order_id") String orderID,
                              @Query("Comment") String Comment);

    @GET(Constant.ApiServiceUrl.UPDATECOMMENT)  //更新当前用户评论信息
    Call<String> toUpdateComment(@Query("order_id") String orderID,
                                 @Query("Comment") String Comment);

    @GET(Constant.ApiServiceUrl.DELETECOMMENT)  //删除当前用户评论信息
    Call<String> toDeleteComment(@Query("order_id") String orderID);
}
