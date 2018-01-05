package com.changcai.buyer.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.changcai.buyer.R;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.view.indicator.commonnavigator.CubicBezierInterpolator;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.subjects.PublishSubject;

/**
 * Created by liuxingwei on 2017/8/3.
 */

public class NewRotateDotsProgressView extends RelativeLayout{

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
    private View contentView;
    private AttributeSet attributeSetTemp;
    public boolean cancel;
    private boolean isAnimation;
    PublishSubject<Float> publishSubject;
    private int backgroundDrawableID;
    private int normalBackgroundDrawableID;
    private int gap;
    private int size;
    private CubicBezierInterpolator middleStopInterpolator = new CubicBezierInterpolator(.12, .98, .83, .13);
    public void refreshDone(boolean cancel) {
        this.cancel = cancel;
        cancelAllAnimations();
    }
    public NewRotateDotsProgressView(Context context) {
        super(context);
        initProgressView(null);
    }

    public NewRotateDotsProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initProgressView(attrs);
    }

    public NewRotateDotsProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initProgressView(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public NewRotateDotsProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initProgressView(attrs);
    }

    private void initProgressView(AttributeSet attributeSet){
        RelativeLayout.LayoutParams layoutParams;
        if (this.getLayoutParams()==null){
            layoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        }else{
            layoutParams = (LayoutParams) this.getLayoutParams();
        }
        this.setLayoutParams(layoutParams);
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.new_progress_view,this,true);
        _leftDots = findViewById(R.id.view_left_dots);
        _rightDots = findViewById(R.id.view_right_dots);
        _centerDots = findViewById(R.id.view_center_dots);
        _moveDots = findViewById(R.id.view_move_dots);
        _parent = findViewById(R.id.dpv_parent);
        if (attributeSet != null) {
            attributeSetTemp = attributeSet;
            TypedArray typedArray = getContext().obtainStyledAttributes(attributeSetTemp, R.styleable.RotateDotsProgressView);
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
                    gap = typedArray.getDimensionPixelOffset(R.styleable.RotateDotsProgressView_dots_gap, getContext().getResources().getDimensionPixelOffset(R.dimen.dim14));
                }

                if (size == 0) {
                    size = typedArray.getDimensionPixelOffset(R.styleable.RotateDotsProgressView_dots_radius, getContext().getResources().getDimensionPixelOffset(R.dimen.dim16));
                }

                LayoutParams layoutParamsLeft =  _leftDots.getLayoutParams()==null?new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT): (LayoutParams) _leftDots.getLayoutParams();
                LayoutParams layoutParamsRight = _rightDots.getLayoutParams()==null?new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT): (LayoutParams) _rightDots.getLayoutParams();
                LayoutParams layoutParamsCenter = _centerDots.getLayoutParams()==null?new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT): (LayoutParams) _centerDots.getLayoutParams();

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
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        _mTranslationX = _rightDots.getLeft() - _leftDots.getLeft();
        /**
         * ondraw 完成的时候发射一次_mTranslationX
         */
        if (isAnimation)
            publishSubject.onNext(_mTranslationX);
    }

    public synchronized void showAnimation(boolean isAnimation) {
        if (this.isAnimation) return;
        this.isAnimation = isAnimation;
        /**
         * 事件订阅再次发射_mTranslationX
         */
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

        _mRotation = ObjectAnimator.ofFloat(_parent, "rotation", 0, -180);
        _mRotation.setDuration(500);
        _mTranslationXObjectAnimator = ObjectAnimator.ofFloat(_moveDots, "translationX", 0, _mTranslationX);
        _mTranslationXObjectAnimator.setInterpolator(middleStopInterpolator);
        _mTranslationXObjectAnimator.setDuration(500);
        _objectAnimator_x_ = ObjectAnimator.ofFloat(_moveDots, "translationX", _mTranslationX, 0);
        _objectAnimator_x_.setInterpolator(middleStopInterpolator);
        _objectAnimator_x_.setDuration(500);
        _mRotation_x_ = ObjectAnimator.ofFloat(_parent, "rotation", 180, 0);
        _mRotation_x_.setDuration(500);
        _mTranslationXObjectAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.d("animation",String.valueOf(cancel).concat("_mRotation is null:").concat(_mTranslationXObjectAnimator == null?"null":"not null"));
                if (cancel || _mRotation == null) return;
                _mRotation.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        _mTranslationXObjectAnimator.start();
        _mRotation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.d("animation",String.valueOf(cancel).concat("_objectAnimator_x_ is null:").concat(_mTranslationXObjectAnimator == null?"null":"not null"));
                if (cancel || _objectAnimator_x_ == null) return;
                _objectAnimator_x_.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        _objectAnimator_x_.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.d("animation",String.valueOf(cancel).concat("_mRotation_x_ is null:").concat(_mTranslationXObjectAnimator == null?"null":"not null"));

                if (cancel || _mRotation_x_ == null) return;
                _mRotation_x_.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        _mRotation_x_.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                LogUtil.d("animation",String.valueOf(cancel).concat("_mTranslationXObjectAnimator is null:").concat(_mTranslationXObjectAnimator == null?"null":"not null"));
                if (cancel || _mTranslationXObjectAnimator != null) return;
                _mTranslationXObjectAnimator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    private void cancelAllAnimations() {
        if (_mRotation != null) {
            _mRotation.cancel();
        }
        if (_mTranslationXObjectAnimator != null) {
            _mTranslationXObjectAnimator.cancel();
        }
        if (_mRotation_x_ != null) {
            _mRotation_x_.cancel();
        }
        if (_objectAnimator_x_ != null) {
            _objectAnimator_x_.cancel();
        }
        removeAllAnimationListeners();
        removeAllViews();
        if (publishSubject != null) {
            publishSubject.onCompleted();
            publishSubject = null;
            this.cancel = false;
            this.isAnimation = false;
            initProgressView(attributeSetTemp);
        }
    }

    private void removeAllAnimationListeners() {
        if (_mRotation != null) {
            _mRotation.removeAllListeners();
        }
        if (_mTranslationXObjectAnimator != null) {
            _mTranslationXObjectAnimator.removeAllListeners();
        }
        if (_mRotation_x_ != null) {
            _mRotation_x_.removeAllListeners();
        }
        if (_objectAnimator_x_ != null) {
            _objectAnimator_x_.removeAllListeners();
        }
        if (_leftDots.getAnimation() != null) {
            _leftDots.clearAnimation();
        }
        if (_parent.getAnimation() != null) {
            _parent.clearAnimation();
        }
    }

    /**
     * 在listView的footer中会被移除
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogUtil.d("animation","onDetachedFromWindow");
        removeAllAnimationListeners();
    }


//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        LogUtil.d("animation","onAttachedToWindow");
//    }
}
