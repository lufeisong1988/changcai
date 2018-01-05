package com.changcai.buyer.view.indicator.commonnavigator;

import android.animation.TimeInterpolator;
import android.graphics.PointF;

/**
 * Created by liuxingwei on 2017/6/7.
 * 三阶贝塞尔曲线动画
 */

public class CubicBezierInterpolator implements TimeInterpolator{

    //A cool decelerated interpolated. It's like the wet version of Alex Bailon dreams
    public static final CubicBezierInterpolator STANDARD_CURVE = new CubicBezierInterpolator(.29, .09, .24, .99);

    //A really UGLY interpolator. Only useful to see if the interpolatod is being applied
    public static final CubicBezierInterpolator FATALE_CURVE = new CubicBezierInterpolator(0, 1.34, 1, 1.81);

    protected PointF start;
    protected PointF end;
    protected PointF a = new PointF();
    protected PointF b = new PointF();
    protected PointF c = new PointF();


    public CubicBezierInterpolator(PointF start, PointF end) throws IllegalArgumentException {
        if (start.x < 0 || start.x > 1) {
            throw new IllegalArgumentException("startX value must be in the range [0, 1]");
        }
        if (end.x < 0 || end.x > 1) {
            throw new IllegalArgumentException("endX value must be in the range [0, 1]");
        }
        this.start = start;
        this.end = end;
    }


    public CubicBezierInterpolator(float startX, float startY, float endX, float endY) {
        this(new PointF(startX, startY), new PointF(endX, endY));
    }


    public CubicBezierInterpolator(double startX, double startY, double endX, double endY) {
        this((float) startX, (float) startY, (float) endX, (float) endY);
    }


    @Override
    public float getInterpolation(float time) {
        return getBezierCoordinateY(getXForTime(time));
    }


    protected float getBezierCoordinateY(float time) {
        c.y = 3 * start.y;
        b.y = 3 * (end.y - start.y) - c.y;
        a.y = 1 - c.y - b.y;
        return time * (c.y + time * (b.y + time * a.y));
    }


    protected float getXForTime(float time) {
        float x = time;
        float z;
        for (int i = 1; i < 14; i++) {
            z = getBezierCoordinateX(x) - time;
            if (Math.abs(z) < 1e-3) {
                break;
            }
            x -= z / getXDerivate(x);
        }
        return x;
    }


    private float getXDerivate(float t) {
        return c.x + t * (2 * b.x + 3 * a.x * t);
    }


    private float getBezierCoordinateX(float time) {
        c.x = 3 * start.x;
        b.x = 3 * (end.x - start.x) - c.x;
        a.x = 1 - c.x - b.x;
        return time * (c.x + time * (b.x + time * a.x));
    }

}
