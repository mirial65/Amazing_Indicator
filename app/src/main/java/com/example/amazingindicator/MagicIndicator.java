package com.example.amazingindicator;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;

/**
 * 对框架MagicIndicator的部分 解读
 */
public class MagicIndicator extends FrameLayout {
    public IPageNavigator mNavigator;
    public MagicIndicator(@NonNull Context context) {
        this(context, null);
    }

    public MagicIndicator(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels) {
        if (mNavigator != null) {
            //Log.d("CircleNavigator", "onPageScrolled: 3");
            //这个mNavigator是可能不为空的，当调用完setNavigator方法后，此时就是mNavigator的实现类在操作了
            mNavigator.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    public void onPageSelected(int position) {
        if (mNavigator != null) {
            mNavigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state) {
        if (mNavigator != null) {
            mNavigator.onPageScrollStateChanged(state);
        }
    }

    public void setNavigator(IPageNavigator navigator) {
        //先删除再添加
        removeAllViews();
        mNavigator = navigator;
        if (mNavigator instanceof View) {
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            //Log.d("CircleNavigator", "setNavigator: ");
            //添加子view，并设置了它的大小
            //从这里可以看出来是在MagicIndicator上添加了一个自定义控件（指示器）
            addView((View) mNavigator, lp);
        }
    }
}
