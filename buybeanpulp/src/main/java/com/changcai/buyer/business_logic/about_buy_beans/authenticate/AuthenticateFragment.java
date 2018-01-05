package com.changcai.buyer.business_logic.about_buy_beans.authenticate;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.assign_platform.AssignPlatformActivity;
import com.changcai.buyer.business_logic.about_buy_beans.bind_bank.BindBankActivity;
import com.changcai.buyer.business_logic.about_buy_beans.company_authenticate.CompanyAuthenticateActivity;
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.PersonAuthenticateActivity;
import com.changcai.buyer.business_logic.about_buy_beans.person_introduce.PersonIntroduceActivity;
import com.changcai.buyer.business_logic.about_buy_beans.reset_paypassword.ResetPayPasswordActivity;
import com.changcai.buyer.business_logic.about_buy_beans.set_paypassword.SetPayPasswordActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.util.SPUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;


/**
 * Created by liuxingwei on 2016/11/17.
 */

public class AuthenticateFragment extends BaseFragment implements AuthenticateContract.View {


    @BindView(R.id.iv_authenticate_step_first)
    ImageView ivAuthenticateStepFirst;
    @BindView(R.id.tv_authenticate_first_info)
    TextView tvAuthenticateFirstInfo;
    @BindView(R.id.tv_check_first)
    TextView tvCheckFirst;
    @BindView(R.id.iv_more_icon)
    ImageView ivMoreIcon;
    @BindView(R.id.ll_first_step)
    LinearLayout llFirstStep;
    @BindView(R.id.tv_authenticate_first_details)
    TextView tvAuthenticateFirstDetails;
    @BindView(R.id.ll_first_step_info)
    LinearLayout llFirstStepInfo;
    @BindView(R.id.iv_authenticate_step_second)
    ImageView ivAuthenticateStepSecond;
    @BindView(R.id.tv_step_second_title)
    TextView tvStepSecondTitle;
    @BindView(R.id.tv_second_step_info)
    TextView tvSecondStepInfo;

    @BindView(R.id.ll_second_step)
    LinearLayout llSecondStep;
    @BindView(R.id.iv_authenticate_step_third)
    ImageView ivAuthenticateStepThird;
    @BindView(R.id.tv_step_third_title)
    TextView tvStepThirdTitle;
    @BindView(R.id.iv_more_icon_3)
    ImageView ivMoreIcon3;
    @BindView(R.id.iv_authenticate_step_fourth)
    ImageView ivAuthenticateStepFourth;
    @BindView(R.id.tv_step_fourth_title)
    TextView tvStepFourthTitle;
    @BindView(R.id.tv_third_fourth_info)
    TextView tvThirdFourthInfo;
    @BindView(R.id.iv_more_icon_4)
    ImageView ivMoreIcon4;
    @BindView(R.id.iv_authenticate_step_fifth)
    ImageView ivAuthenticateStepFifth;
    @BindView(R.id.tv_step_fifth_title)
    TextView tvStepFifthTitle;
    @BindView(R.id.tv_third_fifth_info)
    TextView tvThirdFifthInfo;
    @BindView(R.id.iv_more_icon_5)
    ImageView ivMoreIcon5;
    @BindView(R.id.ll_input_user_introduce)
    LinearLayout llInputUserIntroduce;
    @BindView(R.id.ll_fourth)
    LinearLayout llFourth;
    @BindView(R.id.ll_contract)
    LinearLayout llContract;
    @BindView(R.id.ll_third)
    LinearLayout llThird;
    @BindView(R.id.tv_check_three)
    TextView tvCheckThree;
    @BindView(R.id.tv_check_four)
    TextView tvCheckFour;
    @BindView(R.id.tv_ping_an)
    TextView tvPingAn;
    @BindView(R.id.tv_nong_hang)
    TextView tvNongHang;
    @BindView(R.id.tv_check_five)
    TextView tvCheckFive;
    @BindView(R.id.tvServicePhone)
    TextView tvServicePhone;
    @BindView(R.id.ll_second)
    LinearLayout llSecond;
    @BindView(R.id.ll_web_view)
    LinearLayout llWebView;
    @BindView(R.id.ll_text)
    LinearLayout llText;
    @BindView(R.id.tv_third_step_info)
    TextView tvThirdStepInfo;
    @BindView(R.id.iv_more_icon2)
    ImageView ivMoreIcon2;
    private AuthenticateContract.Presenter presenter;
    private DialogItemOnClick dialogItemOnClick;
    private Dialog dialog;

//    private Subscription backSubsciption;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d("AuthenticateFragment", "onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("AuthenticateFragment", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_authenticate_main, container, false);
        ButterKnife.bind(this, rootView);
        Log.d("AuthenticateFragment", "onCreateView");
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadIdentityDetails();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("AuthenticateFragment", "onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.start();


        Log.d("AuthenticateFragment", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshUI();
        Log.d("AuthenticateFragment", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
//        RxUtil.remove(backSubsciption);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("AuthenticateFragment", "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detach();
//        RxUtil.remove(backSubsciption);
        Log.d("AuthenticateFragment", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("AuthenticateFragment", "onDestroy");
    }


    public AuthenticateFragment() {
    }

    public static AuthenticateFragment newInstance() {
        return new AuthenticateFragment();
    }


    private class DialogItemOnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            int id = v.getId();
            switch (id) {
                case R.id.person:
                    startActivity(new Intent(activity, PersonAuthenticateActivity.class));
                    break;
                case R.id.company:
                    gotoActivity(CompanyAuthenticateActivity.class);
                    break;
                case R.id.cancel:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
            }
        }
    }

    @Override
    public void showStepFirstIconLevelListDrawable(int level) {
        ivAuthenticateStepFirst.setImageLevel(level);
    }

    @Override
    public void showStepSecondIconLevelListDrawable(int level) {

        ivAuthenticateStepSecond.setImageLevel(level);
    }

    @Override
    public void showStepThirdIconLevelListDrawable(int level) {
        ivAuthenticateStepThird.setImageLevel(level);
    }


    @Override
    public void showStepFourthIconLevelListDrawable(int level) {
        ivAuthenticateStepFourth.setImageLevel(level);
    }

    @Override
    public void showStepFifthIconLevelListDrawable(int level) {
        ivAuthenticateStepFifth.setImageLevel(level);
    }

    @Override
    public void showAuthenticateDialog() {
        if (activity.isFinishing() || !isActive()) {
            return;
        }
        View view = activity.getLayoutInflater().inflate(R.layout.authenticate_id_choose_dialog, null, false);
        TextView tvPerson = ButterKnife.findById(view, R.id.person);
        TextView tvCompany = ButterKnife.findById(view, R.id.company);
        TextView tvCancel = ButterKnife.findById(view, R.id.cancel);

        if (null == dialog) {
            dialog = new Dialog(activity, R.style.whiteFrameWindowStyle);
        }
        dialog.setContentView(view);
        Window window = dialog.getWindow();
        window.setWindowAnimations(R.style.choosed_menu_animstyle);
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        wl.gravity = Gravity.BOTTOM;
        dialog.onWindowAttributesChanged(wl);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        if (null == dialogItemOnClick) {
            dialogItemOnClick = new DialogItemOnClick();
        }
        tvPerson.setOnClickListener(dialogItemOnClick);
        tvCompany.setOnClickListener(dialogItemOnClick);
        tvCancel.setOnClickListener(dialogItemOnClick);
    }

    @Override
    public boolean isActive() {
        return this.isAdded();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void refreshUI() {
        UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);
        tvServicePhone.setText(getString(R.string.service_phone4) + SPUtil.getString(Constants.KEY_CONTACT_PHONE));
        if (null != userInfo) {
            if (userInfo.getEnterStatus().equalsIgnoreCase("INIT")) {
                showStepFirstIconLevelListDrawable(0);
                llFirstStep.setBackgroundColor(getResources().getColor(R.color.global_blue));
                tvAuthenticateFirstInfo.setTextColor(getResources().getColor(R.color.white));
                ivMoreIcon.setVisibility(View.VISIBLE);
                llFirstStepInfo.setVisibility(View.VISIBLE);
                llText.setVisibility(View.GONE);
                llWebView.setVisibility(View.VISIBLE);
            } else if (userInfo.getEnterStatus().equalsIgnoreCase("FAIL")) {
                showStepFirstIconLevelListDrawable(1);
                tvAuthenticateFirstInfo.setText(R.string.certify_authenticate_text_fail);
                tvAuthenticateFirstInfo.setTextColor(getResources().getColor(R.color.red_orange));
                ivMoreIcon.setVisibility(View.VISIBLE);
                llFirstStepInfo.setVisibility(View.GONE);
                llFirstStep.setBackgroundColor(getResources().getColor(R.color.white));
                llWebView.setVisibility(View.GONE);
            } else if (userInfo.getEnterStatus().equalsIgnoreCase("PROCESS")) {
                llFirstStep.setBackgroundColor(getResources().getColor(R.color.white));
                showStepFirstIconLevelListDrawable(50);
                llFirstStepInfo.setVisibility(View.VISIBLE);
                tvAuthenticateFirstDetails.setTextColor(getResources().getColor(R.color.orange_details_info));
                if ("PERSONAL".equalsIgnoreCase(userInfo.getEnterType())) {
                    tvAuthenticateFirstDetails.setText(R.string.string_authenticate_details_waiting);
                } else {
                    tvAuthenticateFirstDetails.setText(R.string.string_authenticate_details_waiting_company);
                }
                tvAuthenticateFirstInfo.setText(R.string.string_processing);
                tvAuthenticateFirstInfo.setTextColor(getResources().getColor(R.color.black));
                ivMoreIcon.setVisibility(View.INVISIBLE);
                llWebView.setVisibility(View.GONE);
                llText.setVisibility(View.VISIBLE);
            } else if (userInfo.getEnterStatus().equalsIgnoreCase("SUCCESS")) {
                llFirstStep.setBackgroundColor(getResources().getColor(R.color.white));
                ivMoreIcon.setVisibility(View.VISIBLE);
                showStepFirstIconLevelListDrawable(100);
                tvAuthenticateFirstInfo.setText(String.format(getString(R.string.admin_success), userInfo.getEnterType().equalsIgnoreCase("PERSONAL") ? getString(R.string.person1) : getString(R.string.company2)));
                tvAuthenticateFirstInfo.setTextColor(getResources().getColor(R.color.black));
                tvCheckFirst.setVisibility(View.VISIBLE);
                llWebView.setVisibility(View.GONE);
                //银行签约
                tvSecondStepInfo.setVisibility(View.INVISIBLE);
                if ("SUCCESS".equalsIgnoreCase(userInfo.getBankSignStatus())) {
                    showStepSecondIconLevelListDrawable(100);
                    llSecond.setBackgroundColor(getResources().getColor(R.color.white));
                    tvStepSecondTitle.setText(R.string.agree_bank);
                    tvStepSecondTitle.setTextColor(getResources().getColor(R.color.black));
                    llSecondStep.setVisibility(View.GONE);
                    ivMoreIcon2.setVisibility(View.GONE);
//                    if (userInfo.getPayChannels().size() > 1) {
//                        llContract.setVisibility(View.VISIBLE);
//                    } else {
//                        llContract.setVisibility(View.VISIBLE);
//                        if (userInfo.getPayChannels().isEmpty()) {
//                            llContract.setVisibility(View.GONE);
//                        } else {
//                            if (userInfo.getPayChannels().get(0).getPayChannel().equalsIgnoreCase("ABC")) {
//                                tvNongHang.setVisibility(View.VISIBLE);
//                                tvPingAn.setVisibility(View.GONE);
//                            } else {
//                                tvNongHang.setVisibility(View.GONE);
//                                tvPingAn.setVisibility(View.VISIBLE);
//                            }
//                        }
//
//                    }

                } else {
                    if (userInfo.getEnterType().equalsIgnoreCase("PERSONAL")) {
                        ivMoreIcon2.setVisibility(View.VISIBLE);
                        llSecondStep.setVisibility(View.GONE);
                    } else {
                        ivMoreIcon2.setVisibility(View.GONE);
                        llSecondStep.setVisibility(View.VISIBLE);
                    }
                    llSecond.setBackgroundColor(getResources().getColor(R.color.global_blue));
                    showStepSecondIconLevelListDrawable(1);
                    llContract.setVisibility(View.GONE);
                    tvStepSecondTitle.setText(R.string.sign_bank_account);
                    tvStepSecondTitle.setTextColor(getResources().getColor(R.color.white));
                }
//支付密码设置
                if (Boolean.parseBoolean(userInfo.getPayPassword())) {
                    showStepThirdIconLevelListDrawable(100);
                    tvThirdStepInfo.setVisibility(View.GONE);
                    tvStepThirdTitle.setText(R.string.already_set_pay_pass);
                    tvCheckThree.setVisibility(View.VISIBLE);
                    llThird.setBackgroundColor(getResources().getColor(R.color.white));
                    ivMoreIcon3.setVisibility(View.VISIBLE);
                    tvStepThirdTitle.setTextColor(getResources().getColor(R.color.black));
                } else {
                    if ("SUCCESS".equalsIgnoreCase(userInfo.getBankSignStatus())) {
                        showStepThirdIconLevelListDrawable(5);
                        tvStepThirdTitle.setText(R.string.set_pay_password);
                        tvThirdStepInfo.setVisibility(View.VISIBLE);
                        tvThirdStepInfo.setText("");
                        tvCheckThree.setVisibility(View.GONE);
                        ivMoreIcon3.setVisibility(View.VISIBLE);
                        tvStepThirdTitle.setTextColor(getResources().getColor(R.color.white));
                        llThird.setBackgroundColor(getResources().getColor(R.color.global_blue));
                    } else {
                        tvThirdStepInfo.setVisibility(View.VISIBLE);
                        tvThirdStepInfo.setText(R.string.string_authenticate_third_step_info);
                        tvStepThirdTitle.setText(R.string.set_pay_password);
                        tvCheckThree.setVisibility(View.GONE);
                        ivMoreIcon3.setVisibility(View.INVISIBLE);
                        llThird.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                }
                //签约状态
                if (Boolean.parseBoolean(userInfo.getIsContracted())) {
                    showStepFourthIconLevelListDrawable(100);
                    tvThirdFourthInfo.setVisibility(View.GONE);
                    tvStepFourthTitle.setText(R.string.already_agree_contract);
                    tvStepFourthTitle.setTextColor(getResources().getColor(R.color.black));
                    tvCheckFour.setVisibility(View.VISIBLE);
                    llFourth.setBackgroundColor(getResources().getColor(R.color.white));
                    ivMoreIcon4.setVisibility(View.VISIBLE);
                } else {
                    if (Boolean.parseBoolean(userInfo.getPayPassword())) {
                        showStepFourthIconLevelListDrawable(5);
                        tvStepFourthTitle.setText(R.string.assign_platform_agreement);
                        tvThirdFourthInfo.setVisibility(View.INVISIBLE);
                        tvCheckFour.setVisibility(View.GONE);
                        tvStepFourthTitle.setTextColor(getResources().getColor(R.color.white));
                        llFourth.setBackgroundColor(getResources().getColor(R.color.global_blue));
                        ivMoreIcon4.setVisibility(View.VISIBLE);
                    } else {
                        showStepFourthIconLevelListDrawable(0);//第三部未完成
                        tvThirdFourthInfo.setVisibility(View.VISIBLE);
                        tvStepFourthTitle.setText(R.string.assign_platform_agreement);
                        tvCheckFour.setVisibility(View.GONE);
                        llFourth.setBackgroundColor(getResources().getColor(R.color.white));
                        ivMoreIcon4.setVisibility(View.INVISIBLE);
                    }
                }
                //用户介绍
                if (TextUtils.isEmpty(userInfo.getBuyerInformation()) && TextUtils.isEmpty(userInfo.getSellerInformation())) {
                    if (Boolean.parseBoolean(userInfo.getIsContracted())) {
                        showStepFifthIconLevelListDrawable(1);
                        tvThirdFifthInfo.setVisibility(View.INVISIBLE);
                        tvStepFifthTitle.setText(R.string.input_user_introduction);
                        ivMoreIcon5.setVisibility(View.VISIBLE);
                        tvStepFifthTitle.setTextColor(getResources().getColor(R.color.white));
                        llInputUserIntroduce.setBackgroundColor(getResources().getColor(R.color.global_blue));
                    } else {
                        showStepFifthIconLevelListDrawable(0);//第四部未完成
                        tvThirdFifthInfo.setVisibility(View.VISIBLE);
                        tvStepFifthTitle.setText(R.string.input_user_introduction);
                        ivMoreIcon5.setVisibility(View.INVISIBLE);
                        tvThirdFifthInfo.setTextColor(getResources().getColor(R.color.global_text_gray3));
                        llInputUserIntroduce.setBackgroundColor(getResources().getColor(R.color.white));
                    }
                } else {
                    showStepFifthIconLevelListDrawable(100);
                    tvStepFifthTitle.setText(R.string.already_input_introduce);
                    tvStepFifthTitle.setTextColor(getResources().getColor(R.color.black));
                    tvThirdFifthInfo.setVisibility(View.GONE);
                    tvCheckFive.setVisibility(View.VISIBLE);
                    ivMoreIcon5.setVisibility(View.VISIBLE);
                    llInputUserIntroduce.setBackgroundColor(getResources().getColor(R.color.white));
                }

            }

        }

    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    @Override
    public void setPresenter(@NonNull AuthenticateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {

    }


    @Optional
    @OnClick({R.id.iv_more_icon2, R.id.ll_second, R.id.tv_third_step_info, R.id.tv_check_three, R.id.ll_fourth, R.id.ll_input_user_introduce, R.id.person, R.id.company, R.id.cancel, R.id.iv_authenticate_step_first, R.id.tv_authenticate_first_info, R.id.tv_check_first, R.id.iv_more_icon, R.id.ll_first_step, R.id.tv_authenticate_first_details, R.id.ll_first_step_info, R.id.iv_authenticate_step_second, R.id.tv_step_second_title, R.id.tv_second_step_info, R.id.ll_second_step, R.id.iv_authenticate_step_third, R.id.tv_step_third_title, R.id.iv_more_icon_3, R.id.iv_authenticate_step_fourth, R.id.tv_step_fourth_title, R.id.tv_third_fourth_info, R.id.iv_more_icon_4, R.id.iv_authenticate_step_fifth, R.id.tv_step_fifth_title, R.id.tv_third_fifth_info, R.id.iv_more_icon_5})
    public void onClick(View view) {
        UserInfo userInfo = SPUtil.getObjectFromShare(Constants.KEY_USER_INFO);

        switch (view.getId()) {
            case R.id.iv_more_icon:
            case R.id.ll_first_step:
            case R.id.tv_authenticate_first_info:
            case R.id.iv_authenticate_step_first:
            case R.id.tv_check_first:
                //认证成功直接查看
                if (userInfo.getEnterStatus().equalsIgnoreCase("SUCCESS")) {
                    if (userInfo.getEnterType().equalsIgnoreCase("PERSONAL")) {
                        gotoActivity(PersonAuthenticateActivity.class);
                    } else {
                        gotoActivity(CompanyAuthenticateActivity.class);
                    }

                    return;
                }
                //认证中不能查看
                if (!userInfo.getEnterStatus().equalsIgnoreCase("PROCESS")) {
                    showAuthenticateDialog();
                }


                break;
            case R.id.tv_authenticate_first_details:
                break;
            case R.id.ll_first_step_info:
                break;
            case R.id.iv_authenticate_step_second:
            case R.id.tv_step_second_title:
            case R.id.tv_second_step_info:
            case R.id.ll_second:
            case R.id.iv_more_icon2:

                if (userInfo.getEnterType().equalsIgnoreCase("PERSONAL") && userInfo.getEnterStatus().equalsIgnoreCase("SUCCESS") && !userInfo.getBankSignStatus().equalsIgnoreCase("SUCCESS")) {
                    gotoActivity(BindBankActivity.class);
                }
                break;
            case R.id.ll_second_step:


                break;
            case R.id.iv_authenticate_step_third:
            case R.id.tv_step_third_title:
            case R.id.iv_more_icon_3:
            case R.id.tv_third_step_info:
            case R.id.ll_third:
            case R.id.tv_check_three:

                if ("true".equalsIgnoreCase(userInfo.getPayPassword())) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isReset", true);//重置密码
                    gotoActivity(ResetPayPasswordActivity.class, bundle);
                } else {
                    if ("SUCCESS".equalsIgnoreCase(userInfo.getBankSignStatus())) {
                        gotoActivity(SetPayPasswordActivity.class);
                    }
                }
                break;

            case R.id.ll_input_user_introduce:
            case R.id.iv_authenticate_step_fifth:
            case R.id.tv_step_fifth_title:
            case R.id.tv_third_fifth_info:
            case R.id.iv_more_icon_5:
                if (!userInfo.getEnterStatus().equalsIgnoreCase("SUCCESS")) {
                    return;
                }
                if (Boolean.parseBoolean(userInfo.getIsContracted())) {
                    gotoActivity(PersonIntroduceActivity.class);
                }
                break;
            case R.id.ll_fourth:
            case R.id.iv_authenticate_step_fourth:
            case R.id.tv_step_fourth_title:
            case R.id.iv_more_icon_4:
            case R.id.tv_third_fourth_info:
                if ("SUCCESS".equalsIgnoreCase(userInfo.getBankSignStatus())) {
                    if (Boolean.parseBoolean(userInfo.getPayPassword())) {
                        Bundle bundle = new Bundle();
                        if (Boolean.parseBoolean(userInfo.getIsContracted())) {
                            bundle.putBoolean("button_gone", true);
                        }
                        gotoActivity(AssignPlatformActivity.class, bundle);
                    }
                }
                break;
        }
    }


}
