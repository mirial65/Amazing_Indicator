package com.example.amazingindicator;

import android.content.Context;

public class UIUtil {
    public static int  dip_px(int dpValue, Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5f);
    }
}
