package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.FoodDetailAdapter;
import com.dreamer.neusoft.campusorder_dzj614.Adapter.ShopCart;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.activity.AddOrderActivity;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.FoodService;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.OrderService;
import com.dreamer.neusoft.campusorder_dzj614.view.FakeAddImageView;
import com.dreamer.neusoft.campusorder_dzj614.weight.PointFTypeEvaluator;
import com.dreamer.neusoft.campusorder_dzj614.weight.ShopCartDialog;
import com.dreamer.neusoft.campusorder_dzj614.weight.ShopCartImp;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.dreamer.neusoft.campusorder_dzj614.weight.ShopCartDialog.pay_mhandle;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCookDetailFragment extends Fragment implements ShopCartImp,ShopCartDialog.ShopCartDialogImp,View.OnClickListener{
    public CookApi cookApi,cookApi1;
    public FoodService foodService;
    public OrderService orderService;
    public RecyclerView recyclerView;
    public  View view;
    private TextView headerView;
    public Integer shop_id;
    public  List<FoodBean> food;
     public  Integer size;
    private SharedPreferences Shop,User;
    private SharedPreferences.Editor editor;
    private  int user_id;
    private LinearLayout headerLayout;//右侧菜单栏最上面的菜单
    private FrameLayout shopingCartLayout;
    private ShopCart shopCart;
    private ImageView shoppingCartView;
    private TextView totalPriceTextView;
    private TextView totalPriceNumTextView;
    private  LinearLayout bottomLayout;
    private RelativeLayout mainLayout;
    private  FoodDetailAdapter foodDetailAdapter;
    private  Button  pay_btn;
    private String suggesttime,shop_name,shop_phone;
    public ShopCookDetailFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view= inflater.inflate(R.layout.fragment_shop_cook_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initData();


        getFoodGson();
        Mylistener();

    }

    private void Mylistener() {
        shopingCartLayout.setOnClickListener(this);
        pay_btn.setOnClickListener(this);
    }

    private void showCart(View view) {
        if(shopCart!=null && shopCart.getShoppingAccount()>0){
            ShopCartDialog dialog = new ShopCartDialog(getActivity(),shopCart,R.style.cartdialog);
            Window window = dialog.getWindow();
            dialog.setShopCartDialogImp(ShopCookDetailFragment.this);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            dialog.show();
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.BOTTOM;
            params.dimAmount =0.5f;
            window.setAttributes(params);
        }
    }

    private void getFoodGson() {
        Call<List<FoodBean>> call=foodService.getFoodByShop(String.valueOf(shop_id));
        call.enqueue(new Callback<List<FoodBean>>() {
            @Override
            public void onResponse(Call<List<FoodBean>> call, Response<List<FoodBean>> response) {
                if (response.isSuccessful()) {
                    food=response.body();

                    foodDetailAdapter=new FoodDetailAdapter(getActivity(),food,shopCart);
                    foodDetailAdapter.setShopCartImp(ShopCookDetailFragment.this);
                    recyclerView.setAdapter(foodDetailAdapter);

                }else{
                    Toast.makeText(getActivity(), "获取数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<FoodBean>> call, Throwable t) {
                Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        shopCart = new ShopCart();
        cookApi=new CookApi(2);
        foodService=cookApi.getFoodService();
        cookApi1=new CookApi(3);
        orderService=cookApi1.getOrderService();
        headerLayout = (LinearLayout)view.findViewById(R.id.right_menu_item);
        headerView = (TextView)view.findViewById(R.id.right_menu_tv);
        shopingCartLayout = (FrameLayout)view.findViewById(R.id.shopcook_cart_layout);
        totalPriceTextView = (TextView)view.findViewById(R.id.shopcook_cart_total_tv);
        totalPriceNumTextView = (TextView)view.findViewById(R.id.shopcook_cart_total_num);
        bottomLayout = (LinearLayout)view.findViewById(R.id.shopcook_cart_bottom);
        shoppingCartView = (ImageView)view.findViewById(R.id.shopcook_cart);
        mainLayout = (RelativeLayout)view.findViewById(R.id.fragment_shop_cook_detail);
        pay_btn=(Button)view.findViewById(R.id.pay_btn);
        recyclerView = (RecyclerView) view.findViewById(R.id.shopCookDetail_recycleview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        pay_mhandle =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){
                     pay_btn.setVisibility(View.GONE);
                }
            }
        };


    }

    private void initData() {
            Shop = getActivity().getSharedPreferences("shop", MODE_PRIVATE);
            shop_id=Shop.getInt("shop_id",-1);
        shop_name=Shop.getString("shop_name","苏香园");
        shop_phone=Shop.getString("shop_phone","13342276666");
        User = getActivity().getSharedPreferences("User", MODE_PRIVATE);
        user_id=Integer.valueOf(User.getString("userId",""));
          suggesttime="11:00-11:30";

    }


    @Override
    public void add(View view, int postion) {
        int[] addLocation = new int[2];
        int[] cartLocation = new int[2];
        int[] recycleLocation = new int[2];
        view.getLocationInWindow(addLocation);
        shoppingCartView.getLocationInWindow(cartLocation);
        recyclerView.getLocationInWindow(recycleLocation);

        PointF startP = new PointF();
        PointF endP = new PointF();
        PointF controlP = new PointF();

        startP.x = addLocation[0];
        startP.y = addLocation[1]-recycleLocation[1];
        endP.x = cartLocation[0];
        endP.y = cartLocation[1]-recycleLocation[1];
        controlP.x = endP.x;
        controlP.y = startP.y;

        final FakeAddImageView fakeAddImageView = new FakeAddImageView(getActivity());
        mainLayout.addView(fakeAddImageView);
        fakeAddImageView.setImageResource(R.drawable.ic_add_circle_blue_700_36dp);
        fakeAddImageView.getLayoutParams().width = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.getLayoutParams().height = getResources().getDimensionPixelSize(R.dimen.item_dish_circle_size);
        fakeAddImageView.setVisibility(View.VISIBLE);
        ObjectAnimator addAnimator = ObjectAnimator.ofObject(fakeAddImageView, "mPointF",
                new PointFTypeEvaluator(controlP), startP, endP);
        addAnimator.setInterpolator(new AccelerateInterpolator());
        addAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                fakeAddImageView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                fakeAddImageView.setVisibility(View.GONE);
                mainLayout.removeView(fakeAddImageView);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        ObjectAnimator scaleAnimatorX = new ObjectAnimator().ofFloat(shoppingCartView,"scaleX", 0.6f, 1.0f);
        ObjectAnimator scaleAnimatorY = new ObjectAnimator().ofFloat(shoppingCartView,"scaleY", 0.6f, 1.0f);
        scaleAnimatorX.setInterpolator(new AccelerateInterpolator());
        scaleAnimatorY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleAnimatorX).with(scaleAnimatorY).after(addAnimator);
        animatorSet.setDuration(800);
        animatorSet.start();

        showTotalPrice();

    }

    @Override
    public void remove(View view, int postion) {
        showTotalPrice();
    }

    @Override
    public void dialogDismiss() {
        showTotalPrice();

    }

    private void showTotalPrice(){
        if(shopCart!=null && shopCart.getShoppingTotalPrice()>0){
            totalPriceTextView.setVisibility(View.VISIBLE);
            totalPriceTextView.setText("￥ "+shopCart.getShoppingTotalPrice());
            totalPriceNumTextView.setVisibility(View.VISIBLE);
            pay_btn.setVisibility(View.VISIBLE);
            totalPriceNumTextView.setText(""+shopCart.getShoppingAccount());

        }else {
            totalPriceTextView.setVisibility(View.GONE);
            totalPriceNumTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.shopcook_cart_layout:
                showCart(v);
            break;
            case R.id.pay_btn:
                toPay();
                break;
        }
    }

    private void toPay() {

        Intent intent=new Intent(getActivity(), AddOrderActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("ShopCart",(Serializable) shopCart);
        bundle.putString("shop_name",shop_name);
        bundle.putString("shop_phone",shop_phone);
        intent.putExtras(bundle);
        startActivity(intent);

    }


}
