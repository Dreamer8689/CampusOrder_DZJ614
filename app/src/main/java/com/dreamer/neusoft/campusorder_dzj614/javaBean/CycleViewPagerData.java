package com.dreamer.neusoft.campusorder_dzj614.javaBean;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dreamer.neusoft.campusorder_dzj614.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by DZJ-PC on 2017/4/24.
 */

public class CycleViewPagerData {
    private Context mContext;
    public  CycleViewPagerData(Context context){
            this.mContext=context;
    }

    public  List<Info>  getmList(List<Info> mList){
        mList.add(new Info("烘焙大赛", "http://i3.meishichina.com/attachment/magic/2017/04/21/20170421149273900158613.jpg"));
        mList.add(new Info("恋上芒果香", "http://i3.meishichina.com/attachment/magic/2017/04/24/20170424149301514715713.jpg"));
        mList.add(new Info("唤醒你的食欲", "http://i3.meishichina.com/attachment/magic/2017/04/19/20170419149256969673113.jpg"));
        mList.add(new Info("当鲜花遇上美食", "http://i3.meishichina.com/attachment/magic/2017/04/14/20170414149213773184313.jpg"));
        mList.add(new Info("炼奶冰淇淋", "http://i3.meishichina.com/attachment/recipe/2016/08/05/20160805wpqy1hrtu6siazma.jpg"));
        mList.add(new Info("豆瓣鲫鱼", "http://i3.meishichina.com/attachment/recipe/2016/06/03/20160603o3gdmvogivwzr1cv.jpg@!p800"));
        mList.add(new Info("腐皮春卷", "http://i3.meishichina.com/attachment/recipe/2016/06/08/20160608z6xfxpu1uv7yms6c.jpg@!p800"));
        mList.add(new Info("排骨烧胡萝卜莴笋", "http://i3.meishichina.com/attachment/recipe/2016/04/18/201604185e4pwt11s9xa5mez.jpg@!p800"));
        mList.add(new Info("榆钱鲜肉煎饺", "http://i3.meishichina.com/attachment/recipe/2016/04/02/2016040214595633637718041796.jpg@!p800"));

        return mList;
    }

    public static View getImageView(Context context, String url) {
        RelativeLayout rl = new RelativeLayout(context);
        //添加一个ImageView，并加载图片
        ImageView imageView = new ImageView(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(layoutParams);
        //使用Picasso来加载图片
        Picasso.with(context).load(url).into(imageView);
        //在Imageview前添加一个半透明的黑色背景，防止文字和图片混在一起
        ImageView backGround = new ImageView(context);
        backGround.setLayoutParams(layoutParams);
        backGround.setBackgroundResource(R.color.cycle_image_bg);
        rl.addView(imageView);
        rl.addView(backGround);
        return rl;
    }

}
