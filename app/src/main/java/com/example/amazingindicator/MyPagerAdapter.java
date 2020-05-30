package com.example.amazingindicator;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;

public class MyPagerAdapter extends PagerAdapter {
    private List<String > mList;
    public MyPagerAdapter(List<String> list) {
        mList = list;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //todo:
        //在这里可以进行不同的操作，比如说图片
        TextView textView = new TextView(container.getContext());
        textView.setText(mList.get(position));
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.rgb(0, 255, 0));
        textView.setTextSize(20);
        container.addView(textView);
        return textView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
