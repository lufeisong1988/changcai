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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.changcai.buyer.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by lufeisong on 2017/9/3.
 */

public class GraphLineView extends View {
    private Context context;
    private int TOP_CIRCLE_SIZE = 14;//头部
    private int BOTTOM_CIRCLE_SIZE = 20;//底部

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Resources res;
    private DisplayMetrics dm;
    private Canvas canvas;

    private boolean isMeasure = true;
    private int bheight = 0;//坐标高度
    private float paddingTop = 100;//距离顶部
    private float paddingBottom = 60;//距离底部
    private int spaceXCount = 5;//x轴能显示的个数
    private float spaceXWidth = 0;//x轴2点之间的距离
    private float paddingLeft= 50;
    private float paddingRight= 50;
    private int maxYValue = 1;//Y轴最大值
    private Point[] points ;
    private int position = -1;


    private int canvasHeight;
    private int canvasWidth;

    private List<String> Xs = new ArrayList<>();//存放x轴坐标集合
    private List<String> Ys = new ArrayList<>();//存放y轴坐标集合

    private ArrayList<Integer> xList = new ArrayList<Integer>();// 记录每个x的值
    public GraphLineView(Context context) {
        this(context,null);
    }

    public GraphLineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    void initView(){
        this.res = context.getResources();
        this.mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        TOP_CIRCLE_SIZE = dip2px(4);
        BOTTOM_CIRCLE_SIZE = dip2px(6);
        spaceXWidth = (dm.widthPixels - dip2px(30) ) / spaceXCount;
        paddingTop = dip2px(34);
        paddingBottom = dip2px(20);
        paddingLeft = dip2px(50);
        paddingRight = dip2px(50);

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

            width = (int)(spaceXWidth * (Xs.size() - 1 + 5) ) ;
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
            height = dip2px(120);
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
                bheight = (int) canvasHeight;
            isMeasure = false;
        }
    }


    @Override
    protected void onDraw(Canvas canvas)
    {
        this.canvas = canvas;

        drawBottom(canvas);
        points = getPoints();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(dip2px(1f));
        mPaint.setStyle(Paint.Style.STROKE);
        drawGraph(canvas);

        drawCurrent(canvas);

    }
    //描绘曲线
    private void drawGraph(Canvas canvas){
        Point startp = new Point();
        Point endp = new Point();
        for (int i = 0; i < points.length - 1; i++)
        {
            startp = points[i];
            endp = points[i + 1];
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
            mPaint.setColor(context.getResources().getColor(R.color.graph_top_line));
            canvas.drawPath(path, mPaint);
        }
    }
    //描绘底部
    private void drawBottom(Canvas canvas){
        Log.d("TAG","drawBottom");
        Paint bottomPaint = new Paint();
        bottomPaint.setTextSize(dip2px(12));
        bottomPaint.setColor(Color.parseColor("#d2d2da"));
        //底部的线
        Log.d("TAG","lineStartX:" + (float) (spaceXWidth * 2.5));
        if(Xs.size() > 0){
            canvas.drawLine((float) (spaceXWidth * 2.5),canvasHeight - paddingBottom, (float) (spaceXWidth * (2.5 + Xs.size() - 1)),canvasHeight - paddingBottom,bottomPaint);
        }
        //底部的文字
        for(int i = 0; i < Xs.size();i++){
            if(i == position){
                bottomPaint.setColor(context.getResources().getColor(R.color.graph_bottom_click_dot));
            }else{
                bottomPaint.setColor(context.getResources().getColor(R.color.graph_bottom_line));
            }
            String text = Xs.get(i);
            float textWidth = mPaint.measureText(text);
            xList.add((int) (spaceXWidth * (2.5 + i)));



//            SpannableStringBuilder spannableString = new SpannableStringBuilder(text);
//            spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF0000")), 4,spannableString.length() - 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            canvas.drawText(text, xList.get(i) - textWidth,canvasHeight - paddingBottom / 3,bottomPaint);
        }
    }
    //描绘动态
    private void drawCurrent(Canvas canvas){
        if(position == -1){
            return;
        }
        Paint topPaint = new Paint();
        topPaint.setTextSize(dip2px(14));
        topPaint.setColor(Color.parseColor("#26272A"));

        String text = "销售额:" + Ys.get(position) + "万元";
        float textWidth = topPaint.measureText(text);
        text = "销售额:";
        float textWidth2 = topPaint.measureText(text);
        canvas.drawText(text,points[position].x - textWidth / 2  ,points[position].y - paddingTop / 2,topPaint);
        text = Ys.get(position) ;
        topPaint.setColor(Color.parseColor("#FF5500"));
        float textWidth3 = topPaint.measureText(text);
        canvas.drawText(text,points[position].x - textWidth / 2 + textWidth2 ,points[position].y - paddingTop / 2,topPaint);
        text = "万元";
        topPaint.setColor(Color.parseColor("#26272A"));

        canvas.drawText(text,points[position].x - textWidth / 2 + textWidth2 + textWidth3 ,points[position].y - paddingTop / 2,topPaint);

        Paint circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(points[position].x, points[position].y, TOP_CIRCLE_SIZE / 2, circlePaint);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(context.getResources().getColor(R.color.graph_top_line));
        circlePaint.setStrokeWidth(dip2px(2f));
        canvas.drawCircle(points[position].x, canvasHeight - paddingBottom, BOTTOM_CIRCLE_SIZE / 2, circlePaint);

    }
    public void setData(List<String> Ys, List<String > Xs, int maxYValue){
        this.Xs = Xs;
        this.Ys = Ys;
        this.maxYValue = maxYValue;
        requestLayout();
    }

    private int dip2px(float dpValue)
    {
        return (int) (dpValue * dm.density + 0.5f);
    }

    private Point[] getPoints()
    {
        Point[] points = new Point[Ys.size()];
        for (int i = 0; i < Ys.size(); i++)
        {
            int ph = (int) ((bheight - (int)paddingBottom - (int)paddingTop) *(1 - (Double.parseDouble(Ys.get(i)) / maxYValue)));
            points[i] = new Point(xList.get(i), (int) (ph + paddingTop));
        }
        return points;
    }

    public float getSpaceXWidth() {
        return spaceXWidth;
    }

    public void smoothscrolltoposition(int position){
        this.position = position;
        invalidate();
    }
}
