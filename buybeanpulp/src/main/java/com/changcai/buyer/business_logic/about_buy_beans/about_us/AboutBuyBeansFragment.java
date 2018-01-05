package com.changcai.buyer.business_logic.about_buy_beans.about_us;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.business_logic.about_buy_beans.app_function_introduce.AppFunctionIntroduceActivity;
import com.changcai.buyer.util.AndroidUtil;
import com.changcai.buyer.view.ConfirmDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.changcai.buyer.util.StringUtil.fromHtml;

/**
 * Created by liuxingwei on 2017/1/4.
 */

public class AboutBuyBeansFragment extends BaseFragment implements AboutBuyBeansContract.View {

    public static final int EXTERNAL_STORAGE_REQ_CODE = 9527;
    AboutBuyBeansContract.Presenter presenter;
    Unbinder unbinder;
    @BindView(R.id.tv_app_version)
    TextView tvAppVersion;
    @BindView(R.id.tv_function_introduce)
    TextView tvFunctionIntroduce;

    protected Dialog updateDialog;
    @BindView(R.id.tv_check_version)
    TextView tvCheckVersion;
    private DialogItemOnClick dialogItemOnClick;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_about_beans, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvAppVersion.setText("买豆粕V" + AndroidUtil.getAppVersionName(activity));

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
        unbinder.unbind();
    }

    @Override
    public void showCheckResultToast(boolean isNewVersion) {

        if (!isNewVersion) {
            showErrorDialog(getString(R.string.already_new_version));
        }
    }

    private class DialogItemOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.tv_afterwards:
                    if (updateDialog.isShowing())
                        updateDialog.dismiss();
                    break;
                case R.id.tv_right_now:
                    if (updateDialog.isShowing()) {
                        updateDialog.dismiss();
                    }
                    startAPKDOWNLOAD();
                    break;
            }
        }
    }

    private void startAPKDOWNLOAD() {
        if (updateDialog.isShowing()) {
            updateDialog.dismiss();
        }
        Bundle b = new Bundle();
        b.putString("url", "http://a.app.qq.com/o/simple.jsp?pkgname=com.changcai.buyer");
        AndroidUtil.startBrowser(getActivity(), b, true);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }


    @Override
    public void showNewUpdateVersion(String content) {

        if (!isActive()) {
            return;
        }
        Drawable redPot = ContextCompat.getDrawable(activity, R.drawable.red_round_shape);
        tvCheckVersion.setCompoundDrawablesWithIntrinsicBounds(null, null, redPot, null);
        View view = activity.getLayoutInflater().inflate(R.layout.update_version, null, false);
        TextView cancel = ButterKnife.findById(view, R.id.tv_afterwards);
        TextView sure = ButterKnife.findById(view, R.id.tv_right_now);
        TextView log = ButterKnife.findById(view, R.id.tv_update_version_info);
        TextView title = ButterKnife.findById(view, R.id.tv_update_version_title);
        title.setText("有新版本发布--!");
        log.setText(fromHtml(content));
        if (null == updateDialog) {
            updateDialog = new Dialog(activity, R.style.whiteFrameWindowStyle);
        }
        updateDialog.setContentView(view);
        Window window = updateDialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.CENTER;
        updateDialog.onWindowAttributesChanged(wl);
        updateDialog.setCanceledOnTouchOutside(true);
        updateDialog.show();

        if (null == dialogItemOnClick) {
            dialogItemOnClick = new DialogItemOnClick();
        }
        cancel.setOnClickListener(dialogItemOnClick);
        sure.setOnClickListener(dialogItemOnClick);
    }

    @Override
    public void setPresenter(AboutBuyBeansContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(activity, message);
    }

    public boolean requestPermission() {
        //判断当前Activity是否已经获得了该权限
        if (ContextCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            //如果App的权限申请曾经被用户拒绝过，就需要在这里跟用户做出解释
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(activity, getString(R.string.permissions_write_and_read), Toast.LENGTH_SHORT).show();
            } else {
                //进行权限请求
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        EXTERNAL_STORAGE_REQ_CODE);
            }

            return false;
        }

        return true;
    }

    @OnClick({R.id.tv_app_version, R.id.tv_function_introduce, R.id.tv_check_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_check_version:
                presenter.checkNewVersion();
                break;
            case R.id.tv_function_introduce:
                gotoActivity(AppFunctionIntroduceActivity.class);
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_REQ_CODE: {
                // 如果请求被拒绝，那么通常grantResults数组为空
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    startAPKDOWNLOAD();
                } else {
                    //申请失败，可以继续向用户解释。
                }
                return;
            }
        }
    }
}
