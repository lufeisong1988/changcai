package com.changcai.buyer.view.indicator;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.indicator.commonnavigator.CubicBezierInterpolator;
import com.changcai.buyer.view.indicator.commonnavigator.Point;


/**
 * Created by liuxingwei on 2017/6/6.
 */

public class VerticalProgressDotsView extends RelativeLayout {
    private View _TopDotsView;
    private View _CenterDotsView;
    private View _BottomDotsView;
    private View _MoveDotsView;

    public boolean cancel;

    private ValueAnimator valueAnimatorTop;
    private ValueAnimator valueAnimatorBottom;
    private ObjectAnimator valueAnimatorBottomToTop;

    ObjectAnimator _mRotation;
    ObjectAnimator _mTranslationXObjectAnimator;
    ObjectAnimator _objectAnimator_x_;
    ObjectAnimator _mRotation_x_;


    private View _parent;
    float r;
    AnimatorSet animatorSet = new AnimatorSet();

    public VerticalProgressDotsView(Context context) {
        super(context);
        init(context);
    }

    public VerticalProgressDotsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VerticalProgressDotsView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.vertical_dots_progress, this, true);
        _CenterDotsView = findViewById(R.id.center);
        _BottomDotsView = findViewById(R.id.bottom);
        _TopDotsView = findViewById(R.id.top);
        _MoveDotsView = findViewById(R.id.move_dots);
        _parent = findViewById(R.id.parent);
        setWillNotDraw(false);
        cancel = false;
    }

    public void refreshComplete(boolean cancel) {
        this.cancel = cancel;
        removeAllAnimationListeners();
        resetLoadingView();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        r = _CenterDotsView.getTop() - _TopDotsView.getBottom() + _CenterDotsView.getWidth();
    }

    /**
     * 拆解动画底部的黑色圆点移动到顶部
     */
    public void bottomToTop() {
        if (animatorSet.isRunning()) return;
        if (valueAnimatorBottomToTop != null && valueAnimatorBottomToTop.isRunning()) return;
        valueAnimatorBottomToTop = ObjectAnimator.ofFloat(_MoveDotsView, _MoveDotsView.Y, _MoveDotsView.getY(), _TopDotsView.getY());
        valueAnimatorBottomToTop.setDuration(500);
        valueAnimatorBottomToTop.start();
        valueAnimatorBottomToTop.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                startLoadingProgress();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void showAnimation() {
        if (animatorSet.isRunning()) return;
        CubicBezierInterpolator middleStopInterpolator = new CubicBezierInterpolator(.12, .98, .83, .13);
        _mRotation = ObjectAnimator.ofFloat(_parent, "rotation", 0, -180);
        _mTranslationXObjectAnimator = ObjectAnimator.ofFloat(_MoveDotsView, _MoveDotsView.X, _TopDotsView.getX(), _BottomDotsView.getX());
        _mTranslationXObjectAnimator.setInterpolator(middleStopInterpolator);
        _objectAnimator_x_ = ObjectAnimator.ofFloat(_MoveDotsView, _MoveDotsView.X, _BottomDotsView.getX(), _TopDotsView.getX());
        _objectAnimator_x_.setInterpolator(middleStopInterpolator);
        _mRotation_x_ = ObjectAnimator.ofFloat(_parent, "rotation", 180, 0);
        animatorSet.removeAllListeners();
        animatorSet.setDuration(500);
        animatorSet.playSequentially(_mTranslationXObjectAnimator, _mRotation, _objectAnimator_x_, _mRotation_x_);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                animatorSet.start();
            }
        });
        animatorSet.start();
    }


    private void removeAllAnimationListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animatorSet.pause();
        }
        animatorSet.cancel();
        if (_mRotation != null) {
            _mRotation.cancel();
            _mRotation.removeAllListeners();
        }
        if (_mTranslationXObjectAnimator != null) {
            _mTranslationXObjectAnimator.cancel();
            _mTranslationXObjectAnimator.removeAllListeners();
        }
        if (_mRotation_x_ != null) {
            _mRotation_x_.cancel();
            _mRotation_x_.removeAllListeners();
        }
        if (_objectAnimator_x_ != null) {
            _objectAnimator_x_.cancel();
            _objectAnimator_x_.removeAllListeners();
        }
        if (_parent.getAnimation() != null) {
            _parent.clearAnimation();
        }
        if (_MoveDotsView.getAnimation() != null) {
            _MoveDotsView.clearAnimation();
        }
        if (_BottomDotsView.getAnimation()!=null){
            _BottomDotsView.clearAnimation();
        }
        if (_CenterDotsView.getAnimation()!=null){
            _CenterDotsView.clearAnimation();
        }
        if (_TopDotsView.getAnimation()!=null){
            _TopDotsView.clearAnimation();
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllAnimationListeners();
    }

    /**
     * _TopDotsView _MoveDotsView _BottomDotsView anticlockwise direction rotate 90 degree
     */
    public void startLoadingProgress() {
        if (animatorSet.isRunning())return;
        Point startPoint = new Point();
        startPoint.x = _TopDotsView.getLeft();
        startPoint.y = _TopDotsView.getTop();
        Point endPoint = new Point();
        endPoint.x = _CenterDotsView.getLeft() - r;
        endPoint.y = _CenterDotsView.getTop();
        valueAnimatorTop = ValueAnimator.ofObject(new CircleTypeInterpolator(90, r, CircleTypeInterpolator.Direction.DOWN), startPoint, endPoint);
        valueAnimatorTop.setDuration(500);
        valueAnimatorTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                _TopDotsView.setX(point.getX());
                _TopDotsView.setY(point.getY());
                _MoveDotsView.setX(point.getX());
                _MoveDotsView.setY(point.getY());
            }
        });
        valueAnimatorTop.start();
        Point startPoint2 = new Point();
        startPoint2.x = _BottomDotsView.getLeft();
        startPoint2.y = _BottomDotsView.getTop();
        Log.d("_TopDotsView", "x&y---------" + _TopDotsView.getLeft() + " " + _TopDotsView.getTop());
        Point endPoint2 = new Point();
        endPoint2.x = _CenterDotsView.getLeft() + r;
        endPoint2.y = _CenterDotsView.getTop();
        valueAnimatorBottom = ValueAnimator.ofObject(new CircleTypeInterpolator(90, r, CircleTypeInterpolator.Direction.UP), startPoint2, endPoint2);
        valueAnimatorBottom.setDuration(500);
        valueAnimatorBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Point point = (Point) animation.getAnimatedValue();
                _BottomDotsView.setX(point.getX());
                _BottomDotsView.setY(point.getY());
            }
        });

        valueAnimatorBottom.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                showAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimatorBottom.start();
    }

    private void resetLoadingView() {
        removeAllViews();
        init(getContext());
    }


    private void setBottomDotsViewBackgroundBlack() {
        _BottomDotsView.setBackgroundResource(R.drawable.black_dots);
    }

    private void setCenterDotsViewBackgroundBlack() {
        _CenterDotsView.setBackgroundResource(R.drawable.black_dots);
    }

    private void setTopDotsViewBackgroundBlack() {
        _TopDotsView.setBackgroundResource(R.drawable.black_dots);
    }

    private void setBottomDotsViewBackgroundWhite() {
        _BottomDotsView.setBackgroundResource(R.drawable.white_dots);
    }

    private void setCenterDotsViewBackgroundWhite() {
        _CenterDotsView.setBackgroundResource(R.drawable.white_dots);
    }

    private void setTopDotsViewBackgroundWhite() {
        _TopDotsView.setBackgroundResource(R.drawable.white_dots);
    }

    /**
     * 底部单独一个着色
     */
    public void setBottomState() {
        setBottomDotsViewBackgroundBlack();
        setCenterDotsViewBackgroundWhite();
        setTopDotsViewBackgroundWhite();
    }

    public void setAllDotsBlack() {
        setBottomDotsViewBackgroundBlack();
        setCenterDotsViewBackgroundBlack();
        setTopDotsViewBackgroundBlack();
    }

    public void setAllDotsWhite() {
        setTopDotsViewBackgroundWhite();
        setCenterDotsViewBackgroundWhite();
        setBottomDotsViewBackgroundWhite();
    }

    public void setCenterState() {
        setBottomDotsViewBackgroundWhite();
        setCenterDotsViewBackgroundBlack();
        setTopDotsViewBackgroundWhite();
    }

}
