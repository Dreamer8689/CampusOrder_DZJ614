package com.dreamer.neusoft.campusorder_dzj614.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dreamer.neusoft.campusorder_dzj614.Adapter.OrderAdapter;
import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;
import com.dreamer.neusoft.campusorder_dzj614.model.Api.CookApi;
import com.dreamer.neusoft.campusorder_dzj614.model.Service.OrderService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {
    private CookApi cookapi;
    private OrderService orderService;
    private List<OrderBean> orderBeanList;
    private SharedPreferences user;
    private int userid;
    public RecyclerView recyclerView;
    private View view;
    public Handler mHandler;
    private OrderAdapter  orderAdapter;
    private LinearLayoutManager mLayoutManager;
    public OrderFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_order, container, false);
        initView();
        initData();
        initEvent();
        return view;

    }

    private void initEvent() {
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1){
                 MyEvent();
                }
            }
        };
    }

    private void MyEvent() {
        orderAdapter=new OrderAdapter(getActivity(),orderBeanList);
        orderAdapter.setOnDelListener(new OrderAdapter.onSwipeListener() {
            @Override
            public void onDel(int pos) {
                if (pos >= 0 && pos < orderBeanList.size()) {
                    Toast.makeText(getActivity(), "删除:" + pos, Toast.LENGTH_SHORT).show();
                    orderBeanList.remove(pos);
                    orderAdapter.notifyItemRemoved(pos);//推荐用这个
                    //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                    //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                    //mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTop(int pos) {
                if (pos > 0 && pos < orderBeanList.size()) {
                    OrderBean orderbean = orderBeanList.get(pos);
                    orderBeanList.remove(orderbean);
                    orderAdapter.notifyItemInserted(0);
                    orderBeanList.add(0, orderbean);
                    orderAdapter.notifyItemRemoved(pos + 1);
                    if (mLayoutManager.findFirstVisibleItemPosition() == 0) {
                        recyclerView.scrollToPosition(0);
                    }

                }
            }
        });
        recyclerView.setAdapter(orderAdapter);


    }

    private void initData() {
        user=getActivity().getSharedPreferences("User", MODE_PRIVATE);
        userid=Integer.valueOf(user.getString("userId","")).intValue();
        getOrder();
    }

    private void getOrder() {
    Call<List<OrderBean>> Call=orderService.getOrderList(userid);
        Call.enqueue(new Callback<List<OrderBean>>() {
            @Override
            public void onResponse(Call<List<OrderBean>> call, Response<List<OrderBean>> response) {
                if(response.isSuccessful()){
                    orderBeanList=response.body();
                    Message message = new Message();
                    message.arg1 = 1;
                    mHandler.sendMessage(message);

                }else{
                    Toast.makeText(getActivity(), "返回数据失败", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<OrderBean>> call, Throwable t) {
             Toast.makeText(getActivity(), "网络请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void initView() {
        cookapi=new CookApi(3);
        orderService=cookapi.getOrderService();
        recyclerView = (RecyclerView) view.findViewById(R.id.order_RecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLayoutManager=new LinearLayoutManager(getActivity());
    }

}
