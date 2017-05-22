package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.util.Log;

import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by DZJ-PC on 2017/5/13.
 */

public class ShopCart implements Serializable {


    private int food_id;
    private int shoppingAccount;//商品总数
    private double shoppingTotalPrice;//商品总价钱
    private Map<FoodBean,Integer>  shoppingSingle;//单个物品的总价价钱

    public ShopCart(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle = new HashMap<>();
    }
    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int shop_id) {
        this.food_id = shop_id;
    }
    public int getShoppingAccount() {
        return shoppingAccount;
    }

    public double getShoppingTotalPrice() {
        return shoppingTotalPrice;
    }

    public Map<FoodBean, Integer> getShoppingSingleMap() {
        return shoppingSingle;
    }

    public boolean addShoppingSingle(FoodBean dish){
//        int remain = dish.getDishRemain();
//        if(remain<=0)
//            return false;
//        dish.setDishRemain(--remain);
        int num = 0;
        food_id=dish.getFood_id();
        if(shoppingSingle.containsKey(dish)){
            num = shoppingSingle.get(dish);

        }
        num+=1;
        shoppingSingle.put(dish,num);
        Log.e("TAG", "addShoppingSingle: "+shoppingSingle.get(dish));

        shoppingTotalPrice += dish.getDishPrice();
        shoppingAccount++;
        return true;
    }

    public boolean subShoppingSingle(FoodBean dish){
        int num = 0;
        if(shoppingSingle.containsKey(dish)){
            num = shoppingSingle.get(dish);
        }
        if(num<=0) return false;
        num--;
        int remain = dish.getDishRemain();
        dish.setDishRemain(++remain);
        shoppingSingle.put(dish,num);
        if (num ==0) shoppingSingle.remove(dish);

        shoppingTotalPrice -= dish.getDishPrice();
        shoppingAccount--;
        return true;
    }

    public int getDishAccount() {
        return shoppingSingle.size();
    }

    public void clear(){
        this.shoppingAccount = 0;
        this.shoppingTotalPrice = 0;
        this.shoppingSingle.clear();
    }
}
