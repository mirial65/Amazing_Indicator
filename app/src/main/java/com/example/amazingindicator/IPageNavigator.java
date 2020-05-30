package com.example.amazingindicator;

import androidx.annotation.Px;

public interface IPageNavigator {
    /**
     * ViewPager的三个回调
     */
    void onPageScrolled(int position, float positionOffset, @Px int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);
}
