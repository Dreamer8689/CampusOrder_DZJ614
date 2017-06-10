package com.dreamer.neusoft.campusorder_dzj614.weight;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.ShopCart;
import com.dreamer.neusoft.campusorder_dzj614.R;


/**
 * Created by cheng on 16-12-22.
 */
public class ShopCartDialog extends Dialog implements View.OnClickListener,ShopCartImp{

    private LinearLayout linearLayout,bottomLayout,clearLayout;
    private FrameLayout shopingcartLayout;
    private ShopCart shopCart;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private RecyclerView recyclerView;
    private PopupDishAdapter dishAdapter;
    private ShopCartDialogImp shopCartDialogImp;
    private Button cart_pay_btn;
    private Context context;
    private  int themeResId;
    public static Handler pay_mhandle;
    public ShopCartDialog(Context context, ShopCart shopCart, int themeResId) {
        super(context,themeResId);

        this.shopCart = shopCart;
        this.context=context;
        this.themeResId=themeResId;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopdish_cartpopup_view);
        cart_pay_btn=(Button)findViewById(R.id.cart_pay_btn);
        linearLayout = (LinearLayout) findViewById(R.id.shop_cart_detail_linearlayout);
        clearLayout = (LinearLayout)findViewById(R.id.clear_layout);
        shopingcartLayout = (FrameLayout)findViewById(R.id.shop_cart_detail_layout);
        bottomLayout = (LinearLayout)findViewById(R.id.shop_cart_detail_bottom);
        totalPriceTextView = (TextView)findViewById(R.id.shop_cart_detail_total_tv);
        totalPriceNumTextView = (TextView)findViewById(R.id.shop_cart_detail_total_num);
        recyclerView = (RecyclerView)findViewById(R.id.shop_cart_detail_recycleview);
        shopingcartLayout.setOnClickListener(ShopCartDialog.this);
        bottomLayout.setOnClickListener(ShopCartDialog.this);
        clearLayout.setOnClickListener(ShopCartDialog.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        dishAdapter = new PopupDishAdapter(context,shopCart);
        recyclerView.setAdapter(dishAdapter);
        dishAdapter.setShopCartImp(ShopCartDialog.this);
        showTotalPrice();
    }

    @Override
    public void show() {
         super.show();
        animationShow(1000);
    }

    @Override
    public void dismiss() {
        animationHide(1000);
    }

    private void showTotalPrice(){
        if(shopCart!=null && shopCart.getShoppingTotalPrice()>0){
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText("￥ "+shopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText(""+shopCart.getShoppingAccount());

        }else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
        }
    }

    private void animationShow(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY",1000, 0).setDuration(mDuration)
        );
        animatorSet.start();
    }

    private void animationHide(int mDuration) {
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(linearLayout, "translationY",0,1000).setDuration(mDuration)
        );
        animatorSet.start();

        if(shopCartDialogImp!=null){
            shopCartDialogImp.dialogDismiss();
        }

        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                ShopCartDialog.super.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.shop_cart_detail_bottom:
            case R.id.shop_cart_detail_layout:
                this.dismiss();
                break;
            case R.id.clear_layout:
                clear();
                break;
        }
    }

    @Override
    public void add(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
        if(shopCart.getShoppingAccount()==0){
            this.dismiss();
            Message message = new Message();
            message.arg1 = 1;
            pay_mhandle.sendMessage(message);
        }
    }

    public ShopCartDialogImp getShopCartDialogImp() {
        return shopCartDialogImp;
    }

    public void setShopCartDialogImp(ShopCartDialogImp shopCartDialogImp) {
        this.shopCartDialogImp = shopCartDialogImp;
    }

    public interface ShopCartDialogImp{
        public void dialogDismiss();
    }

    public void clear(){
        shopCart.clear();
        showTotalPrice();

        if(shopCart.getShoppingAccount()==0){
            cart_pay_btn.setVisibility(View.GONE);
            Message message = new Message();
            message.arg1 = 1;
            pay_mhandle.sendMessage(message);
            this.dismiss();
        }
    }
}
