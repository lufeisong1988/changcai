package com.changcai.buyer.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.indicator.commonnavigator.CubicBezierInterpolator;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by liuxingwei on 2017/6/3.
 */

public class RotateDotsProgressView extends RelativeLayout {

    private View _leftDots;
    private View _centerDots;
    private View _rightDots;
    private View _moveDots;

    private float _mTranslationX;
    private View _parent;
    ObjectAnimator _mRotation;
    ObjectAnimator _mTranslationXObjectAnimator;
    ObjectAnimator _objectAnimator_x_;
    ObjectAnimator _mRotation_x_;
    private AttributeSet attributeSetTemp;
    public boolean cancel;
    private boolean isAnimation;
    PublishSubject<Float> publishSubject;
    private int backgroundDrawableID;
    private int normalBackgroundDrawableID;
    private int gap;
    private int size;
    private CubicBezierInterpolator middleStopInterpolator = new CubicBezierInterpolator(.12, .98, .83, .13);
    AnimatorSet animatorSet = new AnimatorSet();

    public void refreshDone(boolean cancel) {
        this.cancel = cancel;
        cancelAllAnimations();
    }

    public RotateDotsProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public RotateDotsProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RotateDotsProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RotateDotsProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        layoutInflater.inflate(R.layout.dots_progress_view, this, true);
        _leftDots = findViewById(R.id.view_left_dots);
        _rightDots = findViewById(R.id.view_right_dots);
        _centerDots = findViewById(R.id.view_center_dots);
        _moveDots = findViewById(R.id.view_move_dots);
        _parent = findViewById(R.id.dpv_parent);
        if (attributeSet != null) {
            attributeSetTemp = attributeSet;
            TypedArray typedArray = context.obtainStyledAttributes(attributeSetTemp, R.styleable.RotateDotsProgressView);
            if (typedArray != null) {
                if (backgroundDrawableID == 0) {
                    backgroundDrawableID = typedArray.getResourceId(R.styleable.RotateDotsProgressView_dots_selected_color, 0);
                }
                if (normalBackgroundDrawableID == 0) {
                    normalBackgroundDrawableID = typedArray.getResourceId(R.styleable.RotateDotsProgressView_dots_normal_color, 0);
                }
                if (backgroundDrawableID != 0) {
                    _moveDots.setBackgroundResource(backgroundDrawableID);
                }
                if (normalBackgroundDrawableID != 0) {
                    _leftDots.setBackgroundResource(normalBackgroundDrawableID);
                    _centerDots.setBackgroundResource(normalBackgroundDrawableID);
                    _rightDots.setBackgroundResource(normalBackgroundDrawableID);
                }

                if (gap == 0) {
                    gap = typedArray.getDimensionPixelOffset(R.styleable.RotateDotsProgressView_dots_gap, context.getResources().getDimensionPixelOffset(R.dimen.dim14));
                }

                if (size == 0) {
                    size = typedArray.getDimensionPixelOffset(R.styleable.RotateDotsProgressView_dots_radius, context.getResources().getDimensionPixelOffset(R.dimen.dim16));
                }
                LayoutParams layoutParamsLeft = (LayoutParams) _leftDots.getLayoutParams();
                LayoutParams layoutParamsRight = (LayoutParams) _rightDots.getLayoutParams();
                LayoutParams layoutParamsCenter = (LayoutParams) _centerDots.getLayoutParams();
                if (gap != 0) {
                    layoutParamsLeft.setMargins(0, 0, gap, 0);
                    layoutParamsRight.setMargins(gap, 0, 0, 0);
                    if (size != 0) {
                        layoutParamsLeft.width = size;
                        layoutParamsLeft.height = size;
                        layoutParamsRight.width = size;
                        layoutParamsRight.height = size;
                    }
                    _moveDots.setLayoutParams(layoutParamsLeft);
                    _leftDots.setLayoutParams(layoutParamsLeft);
                    _rightDots.setLayoutParams(layoutParamsRight);
                }
                if (size != 0) {
                    layoutParamsCenter.width = size;
                    layoutParamsCenter.height = size;

                    _centerDots.setLayoutParams(layoutParamsCenter);
                }

            }
            typedArray.recycle();
        }
//        addView(contentView);
        setWillNotDraw(false);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _mTranslationX = _rightDots.getLeft() - _leftDots.getLeft();
        if (isAnimation)
            publishSubject.onNext(_mTranslationX);
    }

    public synchronized void showAnimation(boolean isAnimation) {
        if (this.isAnimation) return;
        this.isAnimation = isAnimation;
        if (publishSubject == null) {
            publishSubject = PublishSubject.create();
            publishSubject.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Float>() {
                        @Override
                        public void call(Float s) {
                            if (s.intValue() != 0) {
                                showAnimations();
                            }
                        }
                    });
        }
        publishSubject.onNext(_mTranslationX);
    }


    /**
     * 属性动画
     * 分解动画
     * 第一平移动画。translationX为相对于父控件的X轴的偏移量
     * 第二部旋转整个_parentView旋转后坐标系被被旋转
     * 第三部
     */
    private void showAnimations() {
        if (animatorSet.isRunning()) return;
        _mRotation = ObjectAnimator.ofFloat(_parent, "rotation", 0, -180);
        _mTranslationXObjectAnimator = ObjectAnimator.ofFloat(_moveDots, "translationX", 0, _mTranslationX);
        _mTranslationXObjectAnimator.setInterpolator(middleStopInterpolator);
        _objectAnimator_x_ = ObjectAnimator.ofFloat(_moveDots, "translationX", _mTranslationX, 0);
        _objectAnimator_x_.setInterpolator(middleStopInterpolator);
        _mRotation_x_ = ObjectAnimator.ofFloat(_parent, "rotation", 180, 0);
        animatorSet.removeAllListeners();
        animatorSet.playSequentially(_mTranslationXObjectAnimator, _mRotation, _objectAnimator_x_, _mRotation_x_);
        animatorSet.setDuration(500);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (!isAnimation)return;
                if (animatorSet.isRunning())return;
                    animatorSet.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }
        });
        animatorSet.start();
    }


    public void cancelAllAnimations() {
        removeAllAnimationListeners();
        removeAllViews();
        if (publishSubject != null) {
            publishSubject.onCompleted();
            publishSubject = null;
            this.cancel = false;
            this.isAnimation = false;
            init(getContext(), attributeSetTemp);
        }
    }

    private void removeAllAnimationListeners() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animatorSet.pause();
        }
        animatorSet.cancel();
        if (_leftDots.getAnimation() != null) {
            _leftDots.clearAnimation();
        }
        if (_parent.getAnimation() != null) {
            _parent.clearAnimation();
        }
        isAnimation = false;
    }

    /**
     * 在listView的footer中会被移除
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeAllAnimationListeners();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }
}
