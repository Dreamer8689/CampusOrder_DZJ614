package com.dreamer.neusoft.campusorder_dzj614.constant;

/**
 * Created by DZJ-PC on 2017/3/20.
 */

public final class Constant {
    public final  static  class imageurl{


    }

    public final  static  class ApiBaseUrl {


        public final  static  String FOODBASEURL ="http://60.205.189.39/";//请求连接。


    }

    public final  static  class ApiServiceUrl {


        public  final static  String LOGINSERVICE="userLogin.do";      //登陆
        public  final  static  String REGSERVICE="userRegister.do";    //注册
        public  final  static  String ALLSHOPSERVICE="getAllShops.do ";  //获取所有店铺
        public  final  static  String  INSERTORDERSERVICE="insertOrder.do";//购买
        public  final  static  String FOODBYSHOPSERVER ="getFoodByShop.do ";  //获取店铺所有商品
        public  final  static  String SHOPDETAILSERVICE="getShopById.do ";//获取店铺详情
        public  final  static  String FOODDETAILSERVICE="getFoodById.do";//获取菜谱详情
        public  final  static  String FOODORDERSERVICE="getAllUserFoodOrder.do";//获取菜谱评价列表
        public  final  static  String USERCOLLECTION="getAllUserCollection.do";//获取当前用户的所有收藏信息
        public  final  static  String  COLLECTIONSHOP="userCollectShop.do";//收藏/取消收藏店铺
        public  final  static  String  COLLECTIONFOOD="userCollectFood.do";//收藏/取消收藏菜谱
        public  final  static  String  ISCOLLECTION="isCollected.do" ;//判断是否收藏
        public  final  static  String  SEARCHSERVICE="getFoodBySearch.do ";//搜索菜谱/口味
        public  final  static  String  CHANGEUSERINFO="updateUserById.do";//修改用户信息
        public  final  static  String  GETALLORDER="getAllUserOrder.do";//获取当前用户所有订单信息
        public  final  static  String  GETALLCOMMENT="getAllUserComment.do ";//获取当前用户所有评论信息
        public  final  static  String  INSERTCOMMENT="insertComment.do ";//增加评论信息
        public  final  static  String  UPDATECOMMENT="updateComment.do ";//修改评论信息
        public  final  static  String  DELETECOMMENT="deleteComment.do";//删除评论信息
        public  final  static  String  GETUSERINFO="getUserById.do";//获取用户信息
    }

}
