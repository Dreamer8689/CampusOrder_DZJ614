package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.dreamer.neusoft.campusorder_dzj614.Adapter.ShopCart;
import com.dreamer.neusoft.campusorder_dzj614.Adapter.SublimtOrderAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.FoodBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.isSuccessBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddOrderActivity extends Activity implements View.OnClickListener {
    private ShopCart shopCart;
    private Intent intent;
    private CookApi cookapi;
    private OrderService orderService;
    private List<OrderBean> orderBeanList;
    private SharedPreferences user;
    private int userid, size, i = 0;
    public RecyclerView recyclerView;
    public Handler mHandler;
    public TextView username, phone, address, add_cart_total_tv, add_cart_total_num;
    private TextView send_time, add_shop_phone, add_shop_name;
    private TimePickerView pvTime;
    private Button add_pay_btn;
    private LinearLayout calltoshop_layout, sent_time_layout;
    private String shop_name, shop_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        initView();
        initData();
        Mylistener();
        SublimtResult();

    }

    private void SublimtResult() {


        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1) {
                    i++;
                }
                if (i == size) {
                    Intent intent = new Intent(AddOrderActivity.this, ContentActivity.class);
                    intent.putExtra("type", 3);
                    Toast.makeText(AddOrderActivity.this, "购买成功", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }

            }
        };
    }

    private void initData() {
        user = getSharedPreferences("User", MODE_PRIVATE);
        userid = Integer.valueOf(user.getString("userId", "")).intValue();
        username.setText(user.getString("username", "小飞侠"));
        phone.setText(user.getString("phone", "133XXXX0609"));
        address.setText(user.getString("address", "大连东软信息学院16号楼"));
        add_cart_total_tv.setText("￥" + shopCart.getShoppingTotalPrice());
        add_cart_total_num.setText(shopCart.getShoppingAccount() + "");
        recyclerView.setAdapter(new SublimtOrderAdapter(AddOrderActivity.this, shopCart));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        send_time.setText(str);
        add_shop_name.setText(shop_name);
        add_shop_phone.setText(shop_phone);

    }

    private void Mylistener() {
        sent_time_layout.setOnClickListener(this);
        add_pay_btn.setOnClickListener(this);
        calltoshop_layout.setOnClickListener(this);
    }

    private void initView() {

        intent = getIntent();
        shopCart = (ShopCart) intent.getSerializableExtra("ShopCart");
        shop_name = (String) intent.getStringExtra("shop_name");
        shop_phone = (String) intent.getStringExtra("shop_phone");
        recyclerView = (RecyclerView) findViewById(R.id.Orderable_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(AddOrderActivity.this));
        username = (TextView) findViewById(R.id.order_username);
        phone = (TextView) findViewById(R.id.order_phone);
        address = (TextView) findViewById(R.id.order_address);
        add_cart_total_tv = (TextView) findViewById(R.id.add_cart_total_tv);
        add_cart_total_num = (TextView) findViewById(R.id.add_cart_total_num);
        send_time = (TextView) findViewById(R.id.send_time);
        add_pay_btn = (Button) findViewById(R.id.add_pay_btn);
        calltoshop_layout = (LinearLayout) findViewById(R.id.calltoshop_layout);
        sent_time_layout = (LinearLayout) findViewById(R.id.sent_time_layout);
        add_shop_phone = (TextView) findViewById(R.id.add_shop_phone);
        add_shop_name = (TextView) findViewById(R.id.add_shop_name);
        cookapi = new CookApi(3);
        orderService = cookapi.getOrderService();
        initTimePicker();
    }

    private void initTimePicker() {

        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2000, 1, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2200, 2, 28);
        //时间选择器
        pvTime = new TimePickerView.Builder(AddOrderActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null

                /*btn_Time.setText(getTime(date));*/

                send_time.setText(getTime(date));
            }
        })
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "点", "分", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sent_time_layout:
                pvTime.show(v);
                break;
            case R.id.add_pay_btn:
                toPay();
                break;
            case R.id.calltoshop_layout:
                toCall();
                break;

        }
    }

    private void toCall() {
      String  phone=add_shop_phone.getText().toString();
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }

    private void toPay() {

      int food_id,num,price;
        double sum;
        String suggesttime;
        suggesttime=send_time.getText().toString();
        size =shopCart.getShoppingSingleMap().size();
        for(Map.Entry<FoodBean,Integer> item:shopCart.getShoppingSingleMap().entrySet()){

            food_id=item.getKey().getFood_id();
            num=item.getValue();
            price=item.getKey().getPrice();
            sum=(double) num*price;
            Call<isSuccessBean> Call=orderService.toBuy(userid,food_id,num,sum,suggesttime);
            Call.enqueue(new Callback<isSuccessBean>() {
                @Override
                public void onResponse(Call<isSuccessBean> call, Response<isSuccessBean> response) {
                    if (response.isSuccessful()) {
                        if(response.body().getSuccess().equals("1")){
                            Message message = new Message();
                            message.arg1 = 1;
                            mHandler.sendMessage(message);
                        }
                    }else{
                        Toast.makeText(AddOrderActivity.this, "数据返回失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<isSuccessBean> call, Throwable t) {
                    Toast.makeText(AddOrderActivity.this, "网络请求失败", Toast.LENGTH_SHORT).show();
                }
            });
        }


    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
}
