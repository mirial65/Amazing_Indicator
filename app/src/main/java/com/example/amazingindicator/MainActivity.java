package com.example.amazingindicator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioGroup;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "ECLAIR", "GINGERBREAD", "HONEYCOMB",
            "ICE_CREAM_SANDWICH", "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT"};
    private List<String> mList = Arrays.asList(CHANNELS);
    private ViewPager mViewPager;
    private MagicIndicator mMagicIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        initMagicIndicator();
    }

    private void init() {
        mMagicIndicator = findViewById(R.id.magic_indicator);
        mViewPager = findViewById(R.id.vp_pager);
        MyPagerAdapter adapter = new MyPagerAdapter(mList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
    }

    private void initMagicIndicator() {
        CircleNavigator circleNavigator = new CircleNavigator(this);
        //其实这几个方法都没必要invalidate或其他的刷新，因为我的视图绘制都是在addView之后执行的。
        circleNavigator.setCircleCount(mList.size());
        circleNavigator.setColor(Color.RED);
        circleNavigator.setIsFollowHand(true);
        circleNavigator.setOnClickListener2(new CircleNavigator.OnClickListener2() {
            @Override
            public void onClick(int position) {
                mViewPager.setCurrentItem(position);
            }
        });
        //相当于给这个自定义控件装一个指示器吧
        mMagicIndicator.setNavigator(circleNavigator);
        //Log.d("CircleNavigator", "initMagicIndicator: ");
        //将两者进行绑定
        ViewPagerHelper.bind(mMagicIndicator, mViewPager);
        //Log.d("CircleNavigator", "initMagicIndicator: 2");
    }
}
