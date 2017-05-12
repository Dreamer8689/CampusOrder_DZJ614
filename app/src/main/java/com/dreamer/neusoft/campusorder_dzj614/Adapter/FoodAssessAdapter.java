package com.dreamer.neusoft.campusorder_dzj614.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.dreamer.neusoft.campusorder_dzj614.javaBean.OrderBean;

import java.util.List;

/**
 * Created by DZJ-PC on 2017/5/8.
 */

public class FoodAssessAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<OrderBean> QuanList;

    public FoodAssessAdapter(Context context, List QuanList) {
        mContext = context;
        this.QuanList = QuanList;
        mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return QuanList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup container) {
        ViewHolder vh;
        if (view != null) {
            vh = (ViewHolder) view.getTag();
        } else {
            vh = new ViewHolder();
            view = mInflater.inflate(R.layout.food_assess_item, container, false);
            vh.avatarView = (ImageView) view.findViewById(R.id.food_assess_avatarView);
            vh.name = (TextView) view.findViewById(R.id.food_assess_name);
            vh.content = (TextView) view.findViewById(R.id.food_assess_content);
            vh.timeView = (TextView) view.findViewById(R.id.food_assess_timeView);
            vh.price1=(TextView)view.findViewById(R.id.food_assess_price);
            vh.num=(TextView)view.findViewById(R.id.food_assess_num);
            vh.sum=(TextView)view.findViewById(R.id.food_assess_sum);
            view.setTag(vh);
        }
        vh.name.setText(QuanList.get(position).getUsername());
        vh.content.setText(QuanList.get(position).getContent());
        vh.price1.setText("单价"+QuanList.get(position).getPrice()+"元/个");
        vh.num.setText("数量"+QuanList.get(position).getNum()+"个");
        vh.sum.setText("总价"+QuanList.get(position).getSum()+"元");

        vh.timeView.setText(QuanList.get(position).getComment_time());
        return view;
    }

    static class ViewHolder {
        ImageView avatarView;
        TextView name;
        TextView content;
        TextView timeView,price1,num,sum;;
    }
}