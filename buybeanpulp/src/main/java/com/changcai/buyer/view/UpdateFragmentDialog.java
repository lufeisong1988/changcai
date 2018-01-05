package com.changcai.buyer.view;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.util.AndroidUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.changcai.buyer.util.StringUtil.fromHtml;

/**
 * Created by liuxingwei on 2017/7/5.
 */

public class UpdateFragmentDialog extends BaseBottomDialog {
    @BindView(R.id.tv_update_version_title)
    TextView tvUpdateVersionTitle;
    @BindView(R.id.tv_update_version_info)
    TextView tvUpdateVersionInfo;
    @BindView(R.id.tv_afterwards)
    TextView tvAfterwards;
    @BindView(R.id.tv_right_now)
    TextView tvRightNow;
    Unbinder unbinder;

    @Override
    public int getLayoutRes() {
        return R.layout.update_version;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();

        params.dimAmount = getDimAmount();
        //此处不要用view的宽高去决定参数，因为view的生命周期方法和fragment什么周期方法不同步 view.width或者view.getmesureHeight都为0
//        params.width = getResources().getDimensionPixelOffset(R.dimen.dim560);
//        params.height = getResources().getDimensionPixelOffset(R.dimen.dim144);
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;

        window.setAttributes(params);
    }

    @Override
    public void bindView(View v) {
        unbinder = ButterKnife.bind(this, v);
        tvUpdateVersionTitle.setText("有新版本发布");
        tvUpdateVersionInfo.setText(fromHtml(getArguments().getString("content","")));
    }

    @Override
    public float getDimAmount() {
        return 0.5f;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.tv_update_version_title, R.id.tv_update_version_info, R.id.tv_afterwards, R.id.tv_right_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_update_version_title:
                break;
            case R.id.tv_update_version_info:
                break;
            case R.id.tv_afterwards:
                dismiss();
                break;
            case R.id.tv_right_now:
                Bundle b = new Bundle();
                b.putString("url", "http://a.app.qq.com/o/simple.jsp?pkgname=com.changcai.buyer");
                AndroidUtil.startBrowser(getActivity(), b, true);
                break;
        }
    }
}
