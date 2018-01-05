package com.changcai.buyer.view.indicator;

import android.animation.TypeEvaluator;

import com.changcai.buyer.view.indicator.commonnavigator.Point;

/**
 * Created by liuxingwei on 2017/6/7.
 */

public class CircleTypeInterpolator implements TypeEvaluator {
    private float radius;

    public static final int DIRECTION_UP = 0;
    public static final int DIRECTION_DOWN = 1;
    public int direction;

    private CircleTypeInterpolator.Direction _mDirection;
    /**
     * default angle = 180
     */
    private float angle = 180;

    public enum Direction {
        UP, DOWN,DOWN_RIGHT,UP_LEFT
    }

    public void setDirection(CircleTypeInterpolator.Direction direction) {
        _mDirection = direction;
        if (_mDirection.compareTo(Direction.UP) == 0) {
            this.direction = DIRECTION_UP;
        } else if (_mDirection.compareTo(Direction.DOWN) == 0) {
            this.direction = DIRECTION_DOWN;
        }
    }

    public CircleTypeInterpolator(float radius) {
        this.radius = radius;
    }

    public CircleTypeInterpolator(float radius, int direction) {
        this.radius = radius;
        this.direction = direction;
    }


    public CircleTypeInterpolator(float angle, float radius, CircleTypeInterpolator.Direction direction) {
        this.radius = radius;
        this.angle = angle;
        setDirection(direction);
    }

    @Override
    public Object evaluate(float fraction, Object startValue, Object endValue) {
        Point point = new Point();
        Point startPoint = (Point) startValue;
        Point endPoint = (Point) endValue;
        float angle = fraction * this.angle;

        /**
         * 沿着Y轴坐标向下运动
         *沿着x轴向左运动
         * targetPoint = startPoint =
         */
        if (_mDirection.compareTo(Direction.DOWN)==0) {
            float longX = (float) ((radius * Math.sin(Math.toRadians(angle))));
            float longY = (float) (radius - radius * Math.cos(Math.toRadians(angle)));
            point.setX(startPoint.getX() - longX);
            point.setY(startPoint.getY() + longY);
        }
        /**
         * 沿着Y轴坐标向上运动
         *沿着x轴向左运动
         * targetPoint = startPoint =
         */
        else if (_mDirection.compareTo(Direction.UP)==0) {
            float longX  = (float) ((radius * Math.sin(Math.toRadians(angle))));
            float longY = (float) (radius - radius * Math.cos(Math.toRadians(angle)));
            point.setX(startPoint.getX() + longX);
            point.setY(startPoint.getY() - longY);
        }

        else  if (_mDirection.compareTo(Direction.DOWN_RIGHT)==0){
            float  longY = (float) ((radius * Math.sin(Math.toRadians(angle))));
            float  longX= (float) (radius - radius * Math.cos(Math.toRadians(angle)));
            point.setX(startPoint.getX() + longX);
            point.setY(startPoint.getY() + longY);
        }

        else if (_mDirection.compareTo(Direction.UP_LEFT)==0){
            float  longY = (float) ((radius * Math.sin(Math.toRadians(angle))));
            float  longX  = (float) (radius - radius * Math.cos(Math.toRadians(angle)));
            point.setX(startPoint.getX() - longX);
            point.setY(startPoint.getY() - longY);
        }
        //旋转的角度
        // default angle 180
//        float longY = (float) ((radius * Math.sin(Math.toRadians(angle))));
//
//        if (direction == DIRECTION_UP) {
//            point.setY(startPoint.getY() - longY);
//        } else {
//            point.setY(startPoint.getY() + longY);
//        }
//        float longX = (float) (radius - radius * Math.cos(Math.toRadians(angle)));
//        if (direction == DIRECTION_UP) {
//            point.setX(startPoint.getX() - longX);
//        } else {
//            point.setX(startPoint.getX() + longX);
//
//        }

        return point;
    }
}
