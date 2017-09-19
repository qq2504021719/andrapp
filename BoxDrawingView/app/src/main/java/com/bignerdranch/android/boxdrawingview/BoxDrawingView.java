package com.bignerdranch.android.boxdrawingview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/19.
 */

public class BoxDrawingView extends View {
    private static final String TAG = "BoxDrawingView";

    private Box mCurrentBox;
    private List<Box> mBoxen = new ArrayList<>();

    private Paint mBoxPaint;
    private Paint mBackgroundPaint;

    public BoxDrawingView(Context context) {
        super(context);
    }

    public BoxDrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 边框色
        mBoxPaint = new Paint();
        mBoxPaint.setColor(0x22ff0000);

        // 背景色
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setColor(0xfff8efe0);
    }

    /**
     * 绘制图形(在onTouchEvent-ACTION_MOVE中我们调用了invalidate,这迫使它重新完成自我绘制,并再次调用onDraw方法)
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawPaint(mBackgroundPaint);

        for(Box box:mBoxen){
            float left = Math.min(box.getOrigin().x,box.getCurrent().x);
            float right = Math.max(box.getOrigin().x,box.getCurrent().x);
            float top = Math.min(box.getOrigin().y,box.getCurrent().y);
            float bottom = Math.max(box.getOrigin().y,box.getCurrent().y);

            canvas.drawRect(left,top,right,bottom,mBoxPaint);
        }
    }

    /**
     * 用户触摸屏幕事件监听
     * @param event
     * @return
     */
    public boolean onTouchEvent(MotionEvent event){
        PointF current = new PointF(event.getX(),event.getY());

        String action = "";

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mCurrentBox = new Box(current);
                mBoxen.add(mCurrentBox);
                action ="手指触摸到屏幕";
                break;
            case MotionEvent.ACTION_MOVE:
                if(mCurrentBox != null){
                    mCurrentBox.setCurrent(current);
                    invalidate(); //强制BoxDrawingView重新绘制自己。这样用户就能实时看到矩形框
                }
                action ="手指在屏幕上移动";
                break;
            case MotionEvent.ACTION_UP:
                mCurrentBox = null;
                action ="手指离开屏幕";
                break;
            case MotionEvent.ACTION_CANCEL:
                mCurrentBox = null;
                action ="父视图拦截了触摸事件";
                break;
        }

        Log.i(TAG,action+" at x="+current.x+", y="+current.y);

        return true;
    }
}
