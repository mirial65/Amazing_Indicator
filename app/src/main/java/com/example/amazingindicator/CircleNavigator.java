package com.example.amazingindicator;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

import java.util.ArrayList;
import java.util.List;

public class CircleNavigator extends View implements IPageNavigator {
    private static final String TAG = "CircleNavigator";
    //圆半径，水平间隔，画笔的线宽
    public int circleRadius, horizontalSpacing, strokeWidth;
    public Paint mPaint;
    //画笔的颜色，这里是由外部传一个颜色过来
    public int color;
    //圆的数量
    public int circleCount;
    //储存每一个圆心的坐标
    public List<PointF> mList = new ArrayList<>();

    //指示器（可移动的那个实心圆）的当前position
    public int mCurIndex = 0;
    //指示器的当前横坐标的值
    public float mCurIndicatorX;

    //是否跟随手指移动，这里我也是由外部来控制是否跟随
    public boolean isFollowHand = true;
    //插值器，在onPageScrolled中要用到
    public Interpolator mInterpolator = new LinearInterpolator();
    public CircleNavigator(Context context) {
        this(context, null);
    }

    public CircleNavigator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        circleRadius = UIUtil.dip_px(3, context);
        horizontalSpacing = UIUtil.dip_px(8, context);
        strokeWidth = UIUtil.dip_px(1, context);
        mPaint = new Paint();
        Log.d(TAG, "init: ");
    }

    //onMeasure什么时候会被调用 ?
    //onMeasure方法的作用是测量控件的大小，当我们创建一个View(执行构造方法)的时候不需要测量控件的大小，只有将这个view放入一个容器（父控件）中的时候才需要测量，
    // 而这个测量方法就是父控件唤起调用的。当控件的父控件要放置该控件的时候，父控件会调用子控件的onMeasure方法询问子控件：“你有多大的尺寸，我要给你多大的地方才能容纳你？”，
    // 然后传入两个参数（widthMeasureSpec和heightMeasureSpec），这两个参数就是父控件告诉子控件可获得的空间以及关于这个空间的约束条件，子控件拿着这些条件就能正确的测量自身的宽高了。

    //onMeasure方法执行了四次，一般只测两次的，应该大概是addView的原因，我猜想：
    //测量了四次，我猜测应该是
    //先测了爸爸的， 然后又测了自己的，然后又重复了一次。感觉只有这样说的通了。
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //Log.d(TAG, "onMeasure: ");
        setMeasuredDimension(getMeasureWidth(widthMeasureSpec), getMeasureHeight(heightMeasureSpec));
    }

    /**
     * 在这里你可以把得到的宽高用log打出来，就知道是怎么测量了四次
     * @param widthMeasureSpec
     * @return
     */
    private int getMeasureWidth(int widthMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int realWidth = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                realWidth = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                realWidth = 2 * circleRadius * circleCount + (circleCount - 1) * horizontalSpacing + 2 * strokeWidth + getPaddingLeft()
                        + getPaddingRight();
                break;
            default:
                break;
        }
        return realWidth;
    }

    private int getMeasureHeight(int heightMeasureSpec) {

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int realHeight = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                realHeight = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                realHeight = 2 * circleRadius + 2 * strokeWidth + getPaddingBottom() + getPaddingTop();
                break;
            default:
                break;
        }
        return realHeight;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        Log.d(TAG, "onLayout: ");
        preparePoints();
    }

    /**
     * 准备好圆心的坐标
     */
    private void preparePoints() {
        int width = getPaddingRight() + strokeWidth + circleRadius;
        int spacing = 2 * circleRadius + horizontalSpacing;
        int height = (int) (getHeight() / 2 + 0.5f);
        for (int i = 0; i < circleCount; i++) {
            PointF pointF = new PointF(width, height);
            mList.add(pointF);
            width += spacing;
        }
        //在onDraw方法之前，要得到第一次的mCurIndicatorX，因为这个数初始化是为零的.
        //但这个index不一定是0， 因为如果   旋转屏幕     的话，要保存的应该是当前index。
        mCurIndicatorX = mList.get(mCurIndex).x;
        Log.d(TAG, "preparePoints: " + mList.size());
    }

    /**
     * 他会先执行完四次onMeasure方法，----》  onLayout方法    ----》再执行onDraw方法
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "onDraw: ");
        mPaint.setColor(color);
        drawCircles(canvas);
        drawIndicator(canvas);
    }

    private void drawCircles(Canvas canvas) {
        for (int i = 0; i < circleCount; i++) {
            PointF pointF = mList.get(i);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(pointF.x, pointF.y, circleRadius, mPaint);
        }
    }

    private void drawIndicator(Canvas canvas) {
        int height = (int) (getHeight() / 2 + 0.5f);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mCurIndicatorX, height, circleRadius, mPaint);
        Log.d(TAG, "drawIndicator: " + mCurIndicatorX);
    }

    private float mDownX, mDownY;

    /**
     * 这个方法并不需要我能够滑动指示器，所以就没有Action_move,而是说我能够触摸这个指示器就行了，所以我就能添加点击事件。
     * 产生的作用： 指示器----->  viewPager
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = x;
                mDownY = y;
                return true;
            case MotionEvent.ACTION_UP:
                //感觉多此一举，这个必定成立
                if (mListener != null) {
                    if (x - mDownX == 0 && y - mDownY == 0) {
                        float maxValue = Float.MAX_VALUE;
                        int index = 0;
                        for (int i = 0; i < circleCount; i++) {
                            int initPosition = (int) mList.get(i).x;
                            if (Math.abs(initPosition - x) < maxValue) {
                                maxValue = Math.abs(initPosition - x);
                                index = i;
                            }
                        }
                        mListener.onClick(index);
                    }
                }
                return true;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 会产生指示器的滑动效果，从viewPager-----》指示器
     * @param position
     * @param positionOffset
     * @param positionOffsetPixels
     */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d(TAG, "onPageScrolled: ");
        if (isFollowHand) {
            //因为log中：
            //D/CircleNavigator: onPageScrolled:
            //D/CircleNavigator: onLayout:
            if (mList.isEmpty()) {
                return;
            }
            int curPosition = Math.min(circleCount - 1, position);
            int nextPosition = Math.min(circleCount - 1, position + 1);


            float curWidth =  mList.get(curPosition).x ;
            float nextWidth = mList.get(nextPosition).x;
            Log.d(TAG, "onPageScrolled: " + curPosition + "---" + nextPosition);

            //positionOffset： [0，1）中的值指示与位置页面的偏移量。
            mCurIndicatorX = curWidth + (nextWidth - curWidth) * mInterpolator.getInterpolation(positionOffset);
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        Log.d(TAG, "onPageSelected: ");
        mCurIndex = position;
        if (!isFollowHand) {
            mCurIndicatorX = mList.get(position).x;
            invalidate();
        }
    }

    public void setIsFollowHand(boolean following) {
        isFollowHand = following;
    }

    public void setCircleCount(int circleCount) {
        this.circleCount = circleCount;
    }

    public void setColor(int color) {
        this.color = color;
    }
    @Override
    public void onPageScrollStateChanged(int state) {
        Log.d(TAG, "onPageScrollStateChanged: ");
    }

    interface OnClickListener2 {
        void onClick(int position);
    }

    private OnClickListener2 mListener;

    public void setOnClickListener2(OnClickListener2 listener) {
        mListener = listener;
    }
}
