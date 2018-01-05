package com.changcai.buyer.ui.strategy;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.AuthsBean;
import com.changcai.buyer.bean.StraddleModel;
import com.changcai.buyer.bean.StrategyInitModel;
import com.changcai.buyer.ui.introduction.MemberShipIntroductionActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.CustomFontTextView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by liuxingwei on 2017/7/31.
 */

public class LevelJudgementView extends RelativeLayout {

    @BindView(R.id.iv_level)
    ImageView ivLevel;
    @BindView(R.id.tv_level_details)
    TextView tvLevelDetails;
    @BindView(R.id.tv_upgrade)
    CustomFontTextView tvUpgrade;
    @BindView(R.id.tv_explain)
    CustomFontTextView tvExplain;

    protected SpannableString spannableString;
    protected Target target = new Target() {

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            CenterImageSpan imageSpan = new CenterImageSpan(getContext(), bitmap);
            spannableString.setSpan(imageSpan, 4, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvLevelDetails.setText(spannableString);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {

        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }
    };

    public LevelJudgementView(Context context) {
        super(context);
        init();
    }

    public LevelJudgementView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LevelJudgementView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public LevelJudgementView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    public void init() {
        RelativeLayout.LayoutParams mCurrentLayoutParams;
        if (null == this.getLayoutParams()) {
            mCurrentLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
            this.setLayoutParams(mCurrentLayoutParams);
        }
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        layoutInflater.inflate(R.layout.level_judge, this, true);
        ButterKnife.bind(this);
    }


    public void setTvExplainText(String explainText) {
        tvExplain.setText(explainText);
    }


    public void setTvLevelDetails(@Nullable String upgrade, @Nullable String imageUrl) {
        spannableString = new SpannableString("升级到 ".concat("1").concat(TextUtils.isEmpty(upgrade) ? "" : upgrade).concat(" 即可查看内容"));
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(getContext()).load(imageUrl).into(target);
        } else {
            tvLevelDetails.setText(spannableString);
        }
    }

    public void setIvLevel(@Nullable String url) {
        if (!TextUtils.isEmpty(url)) {
            PicassoImageLoader.getInstance().displayNetImage((Activity) getContext(), url, ivLevel);
        }
    }

    public void setIvLevel(@DrawableRes int resourceId){
        ivLevel.setImageResource(resourceId);
    }
    @OnClick({R.id.tv_upgrade})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_upgrade:
                if (getContext() instanceof Activity) {
                    getContext().startActivity(new Intent(getContext(), MemberShipIntroductionActivity.class));
                }
                break;
        }
    }

    public void test() {
        setTvExplainText("提供豆粕现货采购、销售、点价、转月、对冲等全方位专业操作策略建议。不错过重要行情机会。");
        SpannableString test = new SpannableString("升级到34黄金会员 即可查看内容");
        CenterImageSpan im = new CenterImageSpan(getContext(), BitmapFactory.decodeResource(getContext().getResources(), R.drawable.vip3_hj_app));
        test.setSpan(im, 3, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvLevelDetails.setText(test);
        ivLevel.setImageResource(R.drawable.about_maidoupo);
    }

    public void showPermission(@Nullable Bundle bundle,String type) {
        if (bundle !=null && bundle.containsKey("StrategyInitModel")) {
            StrategyInitModel strategyInitModel = (StrategyInitModel) bundle.getSerializable("StrategyInitModel");
            if (strategyInitModel == null || strategyInitModel.getAuths() == null) {
                return;
            }
            for (AuthsBean authsBean : strategyInitModel.getAuths()) {
                if (authsBean.getMenu() != null && authsBean.getMenu().equalsIgnoreCase(type)) {
                    setTvExplainText(authsBean.getMinGradeDescription());
                    setTvLevelDetails(authsBean.getMinGradeName(),authsBean.getMinGradePic());
                    setIvLevel(R.drawable.permisison_need);
                }
            }
        }
    }
}
