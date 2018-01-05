package com.changcai.buyer.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lufeisong on 2017/8/28.
 */

/**********************************************************
 * @文件名称：LineGraphicView.java
 * @文件作者：rzq
 * @创建时间：2015年5月27日 下午3:05:19
 * @文件描述：自定义简单曲线图
 * @修改历史：2015年5月27日创建初始版本
 **********************************************************/
public class LineGraphicView extends View
{
    /**
     * 公共部分
     */
    private Canvas canvas;
    private static final int CIRCLE_SIZE = 10;
    private int position = -1;

    private static enum Linestyle
    {
        Line, Curve
    }

    private Context mContext;
    private Paint mPaint;
    private Resources res;
    private DisplayMetrics dm;
    private float spaceWidth = 0;//x轴2点之间的距离

    /**
     * data
     */
    private Linestyle mStyle = Linestyle.Curve;

    private int canvasHeight;
    private int canvasWidth;
    private int bheight = 0;
    private int blwidh = 0;
    private boolean isMeasure = true;
    /**
     * Y轴最大值
     */
    private int maxValue = 1;
    /**
     * Y轴间距值
     */
    private int averageValue = 0;
    private int marginTop = 20;
    private int marginBottom = 40;

    /**
     * 曲线上总点数
     */
    private Point[] mPoints;
    /**
     * 纵坐标值
     */
    private List<String> yRawData = new ArrayList<>();
    /**
     * 横坐标值
     */
    private List<String> xRawDatas = new ArrayList<>();
    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
    private int spacingHeight = 0;

    public LineGraphicView(Context context)
    {
        this(context, null);
    }

    public LineGraphicView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    private void initView()
    {
        this.res = mContext.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        spaceWidth = dm.widthPixels / 4;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int width = 0;
        int height = 0;
        //获得宽度MODE
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        //获得宽度的值
        if (modeW == MeasureSpec.AT_MOST) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        }
        if (modeW == MeasureSpec.EXACTLY) {
            width = widthMeasureSpec;
        }
        if (modeW == MeasureSpec.UNSPECIFIED) {

            width = (int)(spaceWidth * (xRawDatas.size() - 1 + 4));
        }
        //获得高度MODE
        int modeH = MeasureSpec.getMode(height);
        //获得高度的值
        if (modeH == MeasureSpec.AT_MOST) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        }
        if (modeH == MeasureSpec.EXACTLY) {
            height = heightMeasureSpec;
        }
        if (modeH == MeasureSpec.UNSPECIFIED) {
            //ScrollView和HorizontalScrollView
            height = 300;
        }
        //设置宽度和高度
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        if (isMeasure)
        {
            this.canvasHeight = getHeight();
            this.canvasWidth = getWidth();
            if (bheight == 0)
                bheight = (int) (canvasHeight - marginBottom);
            blwidh = dip2px(30);
            isMeasure = false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        this.canvas = canvas;
        mPaint.setColor(Color.YELLOW);
//        drawAllXLine(canvas);
        // 画直线（纵向）
        drawAllYLine(canvas);
        drawBottomLine(canvas);
        // 点的操作设置
        mPoints = getPoints();

        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(dip2px(1f));
        mPaint.setStyle(Paint.Style.STROKE);
        if (mStyle == Linestyle.Curve)
        {
            drawScrollLine(canvas);
        }
        else
        {
            drawLine(canvas);
        }

        mPaint.setStyle(Paint.Style.FILL);
//        for (int i = 0; i < mPoints.length; i++)
//        {
//            canvas.drawCircle(mPoints[i].x, mPoints[i].y, CIRCLE_SIZE / 2, mPaint);
//        }
    }

    /**
     *  画所有横向表格，包括X轴
     */
    private void drawAllXLine(Canvas canvas)
    {
        for (int i = 0; i < spacingHeight + 1; i++)
        {
            canvas.drawLine(blwidh, bheight - (bheight / spacingHeight) * i + marginTop, (canvasWidth - blwidh),
                    bheight - (bheight / spacingHeight) * i + marginTop, mPaint);// Y坐标
            drawText(String.valueOf(averageValue * i), blwidh / 2, bheight - (bheight / spacingHeight) * i + marginTop,
                    canvas);
        }
    }

    /**
     * 画所有纵向表格，包括Y轴
     */
    private void drawAllYLine(Canvas canvas)
    {
        for (int i = 0; i < yRawData.size(); i++)
        {
            xList.add((int)(spaceWidth  * (i + 2)));
//            canvas.drawLine(blwidh + (canvasWidth - blwidh) / yRawData.size() * i + 2 * spaceWidth, marginTop, blwidh
//                    + (canvasWidth - blwidh) / yRawData.size() * i + 2 * spaceWidth, bheight + marginTop, mPaint);
            int exchange = dip2px(13);
            int height = bheight +  exchange;
            float textWidth = mPaint.measureText(xRawDatas.get(i));
            drawText(xRawDatas.get(i), (int)(xList.get(i) - dip2px(textWidth) / 2), height,
                    canvas);// X坐标
        }
    }

    private void drawScrollLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            int wt = (startp.x + endp.x) / 2;
            Point p3 = new Point();
            Point p4 = new Point();
            p3.y = startp.y;
            p3.x = wt;
            p4.y = endp.y;
            p4.x = wt;

            Path path = new Path();
            path.moveTo(startp.x, startp.y);
            path.cubicTo(p3.x, p3.y, p4.x, p4.y, endp.x, endp.y);
            canvas.drawPath(path, mPaint);

        }
        if(position == -1)
            return;
        smoothscrolltopositionInvidate(canvas,mPoints[position],position);
    }
    private void smoothscrolltopositionInvidate(Canvas canvas,Point startp,int i){
        //描点
        canvas.drawCircle(startp.x,startp.y,10,mPaint);
        //描绘点上的详情
        Paint titlePaint = new Paint();
        titlePaint.setColor(Color.BLUE);
        titlePaint.setTextSize(30);
        String text = "销售额:" + xRawDatas.get(i) + "万元";
        float textWidth = titlePaint.measureText(text);
        canvas.drawText(text,startp.x - textWidth / 2,startp.y - 30,titlePaint);

        //描绘底部线上的点
        int ph = bheight - (int) (bheight * (0 / maxValue));
        canvas.drawCircle(startp.x,ph + marginTop,10,mPaint);
    }
    private void drawBottomLine(Canvas canvas){
        mPaint.setColor(Color.GREEN);
        int ph = bheight - (int) (bheight * (0 / maxValue));
        canvas.drawLine(2 * spaceWidth,ph + marginTop,xRawDatas.size() * spaceWidth,ph + marginTop,mPaint);
    }
    private void drawLine(Canvas canvas)
    {
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < mPoints.length - 1; i++)
        {
            startp = mPoints[i];
            endp = mPoints[i + 1];
            canvas.drawLine(startp.x, startp.y, endp.x, endp.y, mPaint);
        }
    }

    private void drawText(String text, int x, int y, Canvas canvas)
    {
        Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setTextSize(dip2px(12));
        p.setColor(Color.BLUE);
        p.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(text, x, y, p);
    }

    private Point[] getPoints()
    {
        Point[] points = new Point[yRawData.size()];
        for (int i = 0; i < yRawData.size(); i++)
        {
            int ph = bheight - (int) (bheight * (Double.parseDouble(yRawData.get(i)) / maxValue));

            points[i] = new Point(xList.get(i), ph + marginTop);
        }
        return points;
    }

    public void setData(List<String> yRawData, List<String> xRawData, int maxValue, int averageValue)
    {
        this.maxValue = maxValue;
        this.averageValue = averageValue;
        this.mPoints = new Point[yRawData.size()];
        this.xRawDatas = xRawData;
        this.yRawData = yRawData;
        this.spacingHeight = maxValue / averageValue;
        requestLayout();
    }

    public void setTotalvalue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setPjvalue(int averageValue)
    {
        this.averageValue = averageValue;
    }

    public void setMargint(int marginTop)
    {
        this.marginTop = marginTop;
    }

    public void setMarginb(int marginBottom)
    {
        this.marginBottom = marginBottom;
    }

    public void setMstyle(Linestyle mStyle)
    {
        this.mStyle = mStyle;
    }

    public void setBheight(int bheight)
    {
        this.bheight = bheight;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }
    public static void main(String[] args){
        int i = 1;
        int j = 1;
        System.out.print(i + j);
    }
    public void smoothscrolltoposition(int position){
        this.position = position;
        invalidate();
    }

    public float getSpaceWidth() {
        return spaceWidth;
    }
}

