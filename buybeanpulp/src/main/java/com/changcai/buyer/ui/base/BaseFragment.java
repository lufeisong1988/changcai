package com.changcai.buyer.ui.base;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.R;
import com.changcai.buyer.view.immersion.ImmersionBar;

/**
 * @author zhoujun
 * @version 1.0
 * @description
 * @date 2014年8月4日 下午9:46:07
 */
public class BaseFragment extends Fragment {
    protected TextView btnLeft, tvTitle, btnRight;
    protected ImageView iv_btn_right;
    protected FragmentActivity activity;

    protected View titleView;
    protected ImageView btnBack;
    protected  ImageView ivCall;
    protected ImmersionBar immersionBar;
    private int CALL_PHONE = 991;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        initTitle();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initSystemStatusBar();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (FragmentActivity) activity;
    }
    protected void initSystemStatusBar(){
        immersionBar = ImmersionBar.with(this);
        immersionBar.navigationBarWithKitkatEnable(false).init();
    }
    /**
     * 初始化title
     */
    protected void initTitle() {
        btnLeft = (TextView) getActivity().findViewById(R.id.btnLeft);
        tvTitle = (TextView) getActivity().findViewById(R.id.tvTitle);
        btnRight = (TextView) getActivity().findViewById(R.id.btnRight);
        iv_btn_right = (ImageView) getActivity().findViewById(R.id.iv_btn_right);
    }





    /**
     * 跳转activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 跳转activity ，绑定数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(getContext(), pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && immersionBar != null)
            immersionBar.init();
    }
    protected void showShortToast(String pMsg) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        Toast.makeText(getActivity(), pMsg, Toast.LENGTH_SHORT).show();
    }
    /**
     * 返回上一个界面
     */
    protected void back() {
//		showHideFragment();
        if(getActivity() != null)
            getActivity().getSupportFragmentManager().popBackStackImmediate();
    }

    /**
     * 结束activity；
     */
    protected void finish() {
        getActivity().finish();
    }


    /**
     * 显示拨打电话的dialog
     */
    protected void showChooseDialog(final String phone) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.layout_quoto_phone, null);
        final Dialog dialog = new Dialog(getActivity(), R.style.whiteFrameWindowStyle);
        dialog.setContentView(view);

        TextView tvPhone = (TextView) view.findViewById(R.id.phone);
        tvPhone.setText("拨打交易热线" + phone);
        tvPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, CALL_PHONE);
                    return;
                }
                try {
                    callPhone(phone);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        TextView tvCancel = (TextView) view.findViewById(R.id.cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
    }

    private void callPhone(String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
        startActivity(intent);
    }



































    //TODO 废弃的代码
    /**
     * lufeisong(修改) 隐藏掉未使用的代码
     * 2017-10-09
     */

    protected void initActionBar(View view){
        btnLeft = (TextView) view.findViewById(R.id.btnLeft);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        btnRight = (TextView) view.findViewById(R.id.btnRight);
        iv_btn_right = (ImageView) view.findViewById(R.id.iv_btn_right);
        ivCall = (ImageView) view.findViewById(R.id.iv_service_phone);
        titleView = view.findViewById(R.id.viewTop);
        btnBack = (ImageView) view.findViewById(R.id.btnBack);
    }

    /**
     * 隐藏输入法
     */
    protected void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            //这里可能会报空指针错误
        }
    }

    /**
     * 推荐给好友（系统自带的分享方式）
     *
     * @param url
     * @param shareTitle
     */
    public void recommandToYourFriend(String url, String shareTitle) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, shareTitle + "   " + url);

        Intent itn = Intent.createChooser(intent, "分享");
        startActivity(itn);
    }

    protected void showNext(Fragment fragment, int id) {
        showNext(fragment, id, null, true);
    }

    protected void showNext(Fragment fragment, int id, Bundle b) {
        showNext(fragment, id, b, true);
    }

    protected void showNext(Fragment fragment, int id, Bundle b, boolean isAddBackStack) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.replace(id, fragment);
        if (b != null) {
            fragment.setArguments(b);
        }
        if (isAddBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commitAllowingStateLoss();
    }


    /**
     * 打开activity
     *
     * @param pAction activity动作
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 打开activity
     *
     * @param pAction activity动作
     * @param pBundle 数据
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }
    protected void showShortToast(int pResId) {
        showShortToast(getString(pResId));
    }

    protected void showShortToastInCenter(int pResId) {
        showShortToastInCenter(getString(pResId));
    }


    protected void showShortToastInCenter(String pMsg) {
        if (getActivity() == null) {
            return;
        }
        Toast toast = Toast.makeText(getActivity(), pMsg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
