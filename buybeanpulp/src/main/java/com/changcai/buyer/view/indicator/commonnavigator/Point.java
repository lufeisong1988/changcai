package com.changcai.buyer.view.indicator.commonnavigator;

/**
 * Created by liuxingwei on 2017/6/7.
 */

public class Point implements Cloneable {

    public float x;
    public float y;
    public Point(float x, float y) {
        this.x = x;        this.y = y;
    }
    public Point() {    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        Point point=null;
        point= (Point) super.clone();
        return point;
    }
}
