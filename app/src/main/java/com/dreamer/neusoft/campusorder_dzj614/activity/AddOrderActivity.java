package com.dreamer.neusoft.campusorder_dzj614.activity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import c.b.BP;
import c.b.PListener;
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
    private MaterialSpinner spinner;
    ProgressDialog dialog;
    String APPID = "019fd09145535e3d77191927bd111b3a";
    String name,discript;
    double price;
    private static final int REQUESTPERMISSION = 101;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);
        BP.init(APPID);
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
        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(AddOrderActivity.this, item, Toast.LENGTH_SHORT).show();
            }
        });

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
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setItems("支付宝支付", "微信支付");


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
        TheirOfPay();
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

    private void TheirOfPay() {

           if(spinner.getSelectedIndex()==0){
              toPay123(true);
           }else{
             toPay123(false);
           }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }




    /**
     * 检查某包名应用是否已经安装
     *
     * @param packageName 包名
     * @param browserUrl  如果没有应用市场，去官网下载
     * @return
     */

    private boolean checkPackageInstalled(String packageName, String browserUrl) {
        try {
            // 检查是否有支付宝客户端
            AddOrderActivity.this.getPackageManager().getPackageInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            // 没有安装支付宝，跳转到应用市场
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                AddOrderActivity.this. startActivity(intent);
            } catch (Exception ee) {// 连应用市场都没有，用浏览器去支付宝官网下载
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(browserUrl));
                    AddOrderActivity.this.startActivity(intent);
                } catch (Exception eee) {
                    Toast.makeText(AddOrderActivity.this,
                            "您的手机上没有没有应用市场也没有浏览器，我也是醉了，你去想办法安装支付宝/微信吧",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
        return false;
    }


    private void installApk(String s) {
        if (ContextCompat.checkSelfPermission(AddOrderActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //申请权限
            ActivityCompat.requestPermissions((Activity) AddOrderActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
        } else {
            installBmobPayPlugin(s);
        }
    }


    /**
     * 调用支付
     *
     * @param alipayOrWechatPay 支付类型，true为支付宝支付,false为微信支付
     */
    public void toPay123(final boolean alipayOrWechatPay) {
        if (alipayOrWechatPay) {
            if (!checkPackageInstalled("com.eg.android.AlipayGphone",
                    "https://www.alipay.com")) { // 支付宝支付要求用户已经安装支付宝客户端
                Toast.makeText(AddOrderActivity.this, "请安装支付宝客户端", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        } else {
            if (checkPackageInstalled("com.tencent.mm", "http://weixin.qq.com")) {// 需要用微信支付时，要安装微信客户端，然后需要插件
                // 有微信客户端，看看有无微信支付插件，170602更新了插件，这里可检查可不检查
                if (!BP.isAppUpToDate(AddOrderActivity.this, "cn.bmob.knowledge", 8)){
                    Toast.makeText(
                            AddOrderActivity.this,
                            "监测到本机的支付插件不是最新版,最好进行更新,请先更新插件(无流量消耗)",
                            Toast.LENGTH_SHORT).show();
                    installApk("bp.db");
                    return;
                }
            } else {// 没有安装微信
                Toast.makeText(AddOrderActivity.this, "请安装微信客户端", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        showDialog("正在获取订单...\nSDK版本号:" + BP.getPaySdkVersion());


        try {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName cn = new ComponentName("com.bmob.app.sport",
                    "com.bmob.app.sport.wxapi.BmobActivity");
            intent.setComponent(cn);
            Toast.makeText(this, "com.bmob.app.sport.wxapi.BmobActivity", Toast.LENGTH_SHORT).show();
            AddOrderActivity.this.startActivity(intent);
        } catch (Throwable e) {
            e.printStackTrace();
        }

        for(Map.Entry<FoodBean,Integer> item:shopCart.getShoppingSingleMap().entrySet()) {

            name=item.getKey().getDishName();
            discript=item.getKey().getIntro();

        }
        price=shopCart.getShoppingTotalPrice();

        BP.pay(name,discript, price, alipayOrWechatPay, new PListener() {

            // 因为网络等原因,支付结果未知(小概率事件),出于保险起见稍后手动查询
            @Override
            public void unknow() {
                Toast.makeText(AddOrderActivity.this, "支付结果未知,请稍后手动查询", Toast.LENGTH_SHORT)
                        .show();
                hideDialog();
            }

            // 支付成功,如果金额较大请手动查询确认
            @Override
            public void succeed() {
                Toast.makeText(AddOrderActivity.this, "支付成功!", Toast.LENGTH_SHORT).show();
                hideDialog();
            }

            // 无论成功与否,返回订单号
            @Override
            public void orderId(String orderId) {
                // 此处应该保存订单号,比如保存进数据库等,以便以后查询


                showDialog("获取订单成功!请等待跳转到支付页面~");
            }

            // 支付失败,原因可能是用户中断支付操作,也可能是网络原因
            @Override
            public void fail(int code, String reason) {

                // 当code为-2,意味着用户中断了操作
                // code为-3意味着没有安装BmobPlugin插件
               // Toast.makeText(AddOrderActivity.this, code+reason, Toast.LENGTH_SHORT).show();
               System.out.print( code+reason);
                Log.i("111111111111",code+reason);
                if (code == -3) {
                    Toast.makeText(
                            AddOrderActivity.this,
                            "监测到你尚未安装支付插件,无法进行支付,请先安装插件(已打包在本地,无流量消耗),安装结束后重新支付",
                            Toast.LENGTH_SHORT).show();
//                    installBmobPayPlugin("bp.db");
                    installApk("bp.db");
                } else {
                    Toast.makeText(AddOrderActivity.this, "支付中断!", Toast.LENGTH_SHORT)
                            .show();
                }

                hideDialog();
            }
        });

    }






    void showDialog(String message) {
        try {
            if (dialog == null) {
                dialog = new ProgressDialog(AddOrderActivity.this);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    void hideDialog() {
        if (dialog != null && dialog.isShowing())
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
    }

    /**
     * 安装assets里的apk文件
     *
     * @param fileName
     */
    void installBmobPayPlugin(String fileName) {
        try {
            InputStream is = getAssets().open(fileName);
            File file = new File(Environment.getExternalStorageDirectory()
                    + File.separator + fileName + ".apk");
            if (file.exists())
                file.delete();
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(Uri.parse("file://" + file),
                    "application/vnd.android.package-archive");
            this.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    installBmobPayPlugin("bp.db");
                } else {
                    //提示没有权限，安装不了
                    Toast.makeText(AddOrderActivity.this,"您拒绝了权限，这样无法安装支付插件",Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
