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

public interface CommentService {

    @GET(Constant.ApiServiceUrl.FOODORDERSERVICE)  //获取菜谱评价列表
    Call<List<OrderBean>> getFoodOrder(@Query("food_id") String food_id);


    @GET(Constant.ApiServiceUrl.GETALLCOMMENT)  //获取当前用户所有评论信息
    Call<List<OrderBean>> getCommentList(@Query("user_id") int userid);

    @GET(Constant.ApiServiceUrl.INSERTCOMMENT)  //获取当前用户所有评论信息
    Call<isSuccessBean> toAddComment(@Query("order_id") int orderID,
                                     @Query("content") String Comment);

    @GET(Constant.ApiServiceUrl.UPDATECOMMENT)  //更新当前用户评论信息
    Call<isSuccessBean> toUpdateComment(@Query("order_id") int orderID,
                                 @Query("content") String Comment);

    @GET(Constant.ApiServiceUrl.DELETECOMMENT)  //删除当前用户评论信息
    Call<isSuccessBean> toDeleteComment(@Query("order_id") int orderID);
}
