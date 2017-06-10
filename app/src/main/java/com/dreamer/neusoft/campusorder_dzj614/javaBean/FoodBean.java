package com.dreamer.neusoft.campusorder_dzj614.javaBean;

import java.io.Serializable;

/**
 * Created by DZJ-PC on 2017/4/10.
 */

public class FoodBean implements Serializable {

    /**
     * food_id : 1
     * foodname : 酸菜鱼
     * intro : 地道的川菜川菜取材广泛，调味多变，菜式多样，口味清鲜醇浓并重，以善用麻辣调味著称，并以其别具一格的烹调方法和浓郁的地方风味，融会了东南西北各方的特点，博采众家之长，善于吸收，善于创新，享誉中外。
     * pic :
     * price : 23
     * shop_id : 1
     * type_id : 1
     * recommand : 1
     */



    private int dishAmount;
    private int dishRemain;
    private int food_id;
    private String foodname;
    private String intro;
    private String pic;
    private int price;
    private int shop_id;
    private int type_id;
    private int recommand;
    private int isCollected;

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getShop_id() {
        return shop_id;
    }

    public void setShop_id(int shop_id) {
        this.shop_id = shop_id;
    }

    public int getType_id() {
        return type_id;
    }

    public void setType_id(int type_id) {
        this.type_id = type_id;
    }

    public int getRecommand() {
        return recommand;
    }

    public void setRecommand(int recommand) {
        this.recommand = recommand;
    }

    public int getIsCollected() {
        return isCollected;
    }

    public void setIsCollected(int isCollected) {
        this.isCollected = isCollected;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public FoodBean(String foodname, int price, int dishAmount) {
        this.foodname = foodname;
        this.price = price;
        this.dishAmount = dishAmount;
        this.dishRemain = dishAmount;
    }


    public String getDishName() {
        return foodname;
    }

    public void setDishName(String foodname) {
        this.foodname = foodname;
    }

    public double getDishPrice() {
        return price;
    }

    public void setDishPrice(int price) {
        this.price = price;
    }

    public int getDishAmount() {
        return dishAmount;
    }

    public void setDishAmount(int dishAmount) {
        this.dishAmount = dishAmount;
    }

    public int getDishRemain() {
        return dishRemain;
    }

    public void setDishRemain(int dishRemain) {
        this.dishRemain = dishRemain;
    }

    public int hashCode() {
        int code = this.foodname.hashCode() + (int) this.price;
        return code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;

        return obj instanceof FoodBean &&
                this.foodname.equals(((FoodBean) obj).foodname) &&
                this.price == ((FoodBean) obj).price &&
                this.dishAmount == ((FoodBean) obj).dishAmount &&
                this.dishRemain == ((FoodBean) obj).dishRemain;
    }
}
