package com.eryue.ui;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by enjoy on 2018/3/3.
 */

public class CircleView extends View {

    private Paint paint;//画笔
    private RectF oval; // RectF对象（圆环）
    private int currentDegree = 0;//当前度数（除360可求百分比）
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();//颜色渐变插值器
    private int height;//控件高
    private int width;//控件宽
    private int circleWidth;//圆环宽

    private final static int strokeWidth = 40;//画笔大小

    private boolean isGradual = true;//是否显示渐变色

    public CircleView(Context context) {
        super(context);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private void init(){
        //初始化画笔
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.RED);
        oval = new RectF();
        paint.setStrokeWidth(strokeWidth); // 线宽
        paint.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        //计算最小宽度
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);

        if(width >= height)
        {
            circleWidth = height;
        }
        else
        {
            circleWidth = width;
        }

        setMeasuredDimension(circleWidth,circleWidth);
        oval.left = strokeWidth / 2; // 左边
        oval.top = strokeWidth / 2; // 上边
        oval.right = circleWidth - strokeWidth / 2; // 右边
        oval.bottom = circleWidth - strokeWidth / 2; // 下边
//自动旋转
//      handler.postDelayed(runnable, 500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        // canvas.drawArc(oval,-90 + currentDegree, 1.3f , false, paint); //绘制圆弧
        for (int i = 0; i < currentDegree; i++) {
            if (!isGradual) {
                if (i < 180) {
                    paint.setColor(Color.BLUE);//右半边颜色
                } else {
                    paint.setColor(Color.GREEN);//所半边颜色
                }
            } else {
                Integer color = (Integer) argbEvaluator.evaluate(i / 360f, Color.BLUE, Color.GREEN);//颜色插值器（level 11以上才可以用）
                paint.setColor(color);
            }
            if (i % 2 == 0) {
                canvas.drawArc(oval, -90 + i, 1.35f, false, paint); // 绘制圆弧 1.35f是每个色块宽度
            }
        }


        //画:金额¥100.00
        //画:今日预估收入
        //
        //绘制白色的底部背景
//          if(currentDegree < 360)
//          {
//              paint.setColor(Color.WHITE);//右半边颜色
//              for(int j = currentDegree; j < 360; j++)
//              {
//                  if (j % 2 == 0)
//                  {
//                  canvas.drawArc(oval, -90 + j, 1.35f, false, paint); // 绘制圆弧 1.35f是每个色块宽度
//                  }
//              }
//          }
    }


    /**
     * 根据百分比设置颜色范围
     * @param pDegree
     */
    public void setCurrentDegree(float pDegree)
    {
        this.currentDegree = (int)(360f * pDegree);
    }

    /**
     * 颜色是否渐变
     * @param gradual
     */
    public void setGradual(boolean gradual)
    {
        this.isGradual = gradual;
    }

}
