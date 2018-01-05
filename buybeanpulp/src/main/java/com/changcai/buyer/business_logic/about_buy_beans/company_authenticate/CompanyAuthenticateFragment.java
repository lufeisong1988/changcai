package com.changcai.buyer.business_logic.about_buy_beans.company_authenticate;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.BaseFragment;
import com.changcai.buyer.R;
import com.changcai.buyer.bean.LocalPhotoFilePathAndUploadId;
import com.changcai.buyer.bean.TempInfoInputForIdCertify;
import com.changcai.buyer.bean.UserInfo;
import com.changcai.buyer.business_logic.about_buy_beans.assign_platform.AssignPlatformActivity;
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.PersonAuthenticateFragment;
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.big_photo.BigPhotoActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.okhttp.ProgressRequestListener;
import com.changcai.buyer.permission.AfterPermissionGranted;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.rx.BackEvent;
import com.changcai.buyer.util.CameraUtil;
import com.changcai.buyer.util.IDCardUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.SectorProgressView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/11/26.
 */

public class CompanyAuthenticateFragment extends BaseFragment implements CompanyAuthenticateContract.View {

    private static final int CHECK_BIG_PHOTO1 = 9001;
    private static final int CHECK_BIG_PHOTO2 = 9002;
    private static final int CHECK_BIG_PHOTO3 = 9003;
    private static final int CHECK_BIG_PHOTO4 = 9004;
    private static final int CHECK_BIG_PHOTO5 = 9005;
    CompanyAuthenticateContract.Presenter presenter;
    @BindView(R.id.et_legal_people_name)
    EditText etLegalPeopleName;
    @BindView(R.id.et_legal_people_id_card)
    EditText etLegalPeopleIdCard;
    @BindView(R.id.iv_code_selected1)
    ImageView ivCodeSelected1;
    @BindView(R.id.ll_unification_code)
    LinearLayout llUnificationCode;
    @BindView(R.id.iv_code_selected2)
    ImageView ivCodeSelected2;
    @BindView(R.id.ll_organization_code)
    LinearLayout llOrganizationCode;
    @BindView(R.id.et_company_name)
    EditText etCompanyName;
    @BindView(R.id.et_uniform_standard_credit_code)
    EditText etUniformStandardCreditCode;
    @BindView(R.id.iv_front_image)
    ImageView ivFrontImage;
    @BindView(R.id.progress_front)
    SectorProgressView progressFront;
    @BindView(R.id.ll_progress_front)
    LinearLayout llProgressFront;
    @BindView(R.id.ll_id_front)
    ImageView llIdFront;
    @BindView(R.id.iv_background_image)
    ImageView ivBackgroundImage;
    @BindView(R.id.progress_back)
    SectorProgressView progressBack;
    @BindView(R.id.ll_progress_back)
    LinearLayout llProgressBack;
    @BindView(R.id.ll_id_background)
    ImageView llIdBackground;
    @BindView(R.id.iv_license)
    ImageView ivLicense;
    @BindView(R.id.progress_license)
    SectorProgressView progressLicense;
    @BindView(R.id.ll_progress_license)
    LinearLayout llProgressLicense;
    @BindView(R.id.ll_industry_license)
    ImageView llIndustryLicense;
    @BindView(R.id.iv_institutional_framework_image)
    ImageView ivInstitutionalFrameworkImage;
    @BindView(R.id.progress_code)
    SectorProgressView progressCode;
    @BindView(R.id.ll_progress_code)
    LinearLayout llProgressCode;
    @BindView(R.id.ll_institutional_framework)
    ImageView llInstitutionalFramework;
    @BindView(R.id.iv_tax_license)
    ImageView ivTaxLicense;
    @BindView(R.id.progress_front_tax_license)
    SectorProgressView progressFrontTaxLicense;
    @BindView(R.id.ll_progress_tax_license)
    LinearLayout llProgressTaxLicense;
    @BindView(R.id.ll_tax_license)
    ImageView llTaxLicense;
    @BindView(R.id.ll_view_cell_three)
    LinearLayout llViewCellThree;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.tv_company_legal_person)
    TextView tvCompanyLegalPerson;
    @BindView(R.id.tv_company_type)
    TextView tvCompanyType;
    @BindView(R.id.tv_company_data)
    TextView tvCompanyData;
    @BindView(R.id.tv_correlation_credentials)
    TextView tvCorrelationCredentials;
    @BindView(R.id.tv_credit_title)
    TextView tvCreditTitle;
    @BindView(R.id.tv_institutional_title)
    TextView tvInstitutionalTitle;
    @BindView(R.id.iv_agree_sign)
    ImageView ivAgreeSign;
    @BindView(R.id.tv_net_sign)
    TextView tvNetSign;
    @BindView(R.id.tv_sign_text)
    TextView tvSignText;


    private Dialog dialog;
    private DialogItemOnClick dialogItemOnClick;
    private int courseIndex;
    private UploadListener uploadListenerFront;
    private UploadListener uploadListenerBack;

    private UploadListener industryLicenseListener;
    private UploadListener codeProgressListener;
    private UploadListener taxLicenseProgressListener;
    protected List<UploadListener> uploadListeners;
    protected Unbinder unbinder;

    private Subscription backSubscription;
    private String status;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_id_authenticate, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAgreeSign.setSelected(true);
        llOrganizationCode.setSelected(true);
        tvInstitutionalTitle.setSelected(true);
        ivCodeSelected2.setSelected(true);

        ivCodeSelected1.setSelected(false);
        llUnificationCode.setSelected(false);
        tvCreditTitle.setSelected(false);
        etUniformStandardCreditCode.setHint(R.string.organization_code2);
        uploadListeners = new ArrayList<>();

        taxLicenseProgressListener = new UploadListener();
        industryLicenseListener = new UploadListener();
        codeProgressListener = new UploadListener();
        uploadListenerFront = new UploadListener();
        uploadListenerBack = new UploadListener();

        uploadListeners.add(taxLicenseProgressListener);
        uploadListeners.add(industryLicenseListener);
        uploadListeners.add(codeProgressListener);
        uploadListeners.add(uploadListenerFront);
        uploadListeners.add(uploadListenerBack);
        presenter.start();
        if (!isSuccess()) {

            final TempInfoInputForIdCertify tem = SPUtil.getObjectFromShare(Constants.KEY_LOCAL_INFO);
            if (tem == null) {
                return;
            }
            Map<String, LocalPhotoFilePathAndUploadId> map = tem.getIdMap();
            if (map != null) {

                Observable.just(map)
                        .map(new Func1<Map<String, LocalPhotoFilePathAndUploadId>, Map<String, Map<String, String>>>() {
                            @Override
                            public Map<String, Map<String, String>> call(Map<String, LocalPhotoFilePathAndUploadId> stringLocalPhotoFilePathAndUploadIdMap) {
                                Map<String, Map<String, String>> allIdAndFilePath = new TreeMap<>();

                                //本地路径
                                Map<String, String> map1 = new TreeMap<>();
                                //本地上传后的ID
                                Map<String, String> map2 = new TreeMap<>();

                                for (Map.Entry<String, LocalPhotoFilePathAndUploadId> entry : stringLocalPhotoFilePathAndUploadIdMap.entrySet()) {
                                    if (!entry.getKey().isEmpty()) {
                                        map1.put(entry.getKey(), entry.getValue().getLocalFilePath());
                                        map2.put(entry.getKey(), entry.getValue().getId());
                                    }
                                }
                                allIdAndFilePath.put("map1", map1);
                                allIdAndFilePath.put("map2", map2);
                                return allIdAndFilePath;
                            }
                        }).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<Map<String, Map<String, String>>>() {
                            @Override
                            public void call(Map<String, Map<String, String>> stringStringMap) {
                                presenter.setLocalFileData(stringStringMap.get("map1"));
                                presenter.setTempFileData(stringStringMap.get("map2"));
                                refreshUI(tem.getInputTempName()
                                        , tem.getInputTempIdNo()
                                        , tem.getIv1Select()
                                        , tem.getCompanyName()
                                        , tem.getCode()
                                        , "local", stringStringMap.get("map1"));
                            }
                        });
            }

        }

    }

    private boolean isSuccessOrProcess() {
        if (TextUtils.isEmpty(status))
            status = ((UserInfo) SPUtil.getObjectFromShare(Constants.KEY_USER_INFO)).getEnterStatus();
        if (status.equalsIgnoreCase("SUCCESS") || status.contentEquals("PROCESS")) {
            return true;
        }
        return false;
    }

    private boolean isSuccess(){
        if (TextUtils.isEmpty(status))
            status = ((UserInfo) SPUtil.getObjectFromShare(Constants.KEY_USER_INFO)).getEnterStatus();
        if (status.equalsIgnoreCase("SUCCESS")) {
            return true;
        }
        return false;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (isSuccessOrProcess()) {
            presenter.loadIdentityDetails();
        }

        RxUtil.remove(backSubscription);
        backSubscription = BackEvent.getObservable().subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    if (!isSuccessOrProcess()) {
                        if (etLegalPeopleName == null) {
                            return;
                        }
                        if (!TextUtils.isEmpty(etLegalPeopleName.getText().toString())
                                || !TextUtils.isEmpty(etLegalPeopleIdCard.getText().toString())
                                || !TextUtils.isEmpty(etCompanyName.getText().toString())
                                || !TextUtils.isEmpty(etUniformStandardCreditCode.getText().toString())
                                || (null != presenter.getTempFileData() && !presenter.getTempFileData().isEmpty())) {
                            if (isActive()) {
                                showMakeSureBackDialog();
                            }
                        } else {
                            if (activity != null) {
                                activity.finish();
                            }
                        }
                    } else {
                        if (activity != null) {
                            activity.finish();
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detach();
        RxUtil.remove(backSubscription);
    }


    public void refreshUI(String name, String idNo,
                          String iv1Selected, String companyName,
                          String uniformCode, String type, Map<String, String> filePath) {

        if (!TextUtils.isEmpty(name)) {
            etLegalPeopleName.setText(name);
        }
        if (!TextUtils.isEmpty(idNo)) {
            etLegalPeopleIdCard.setText(idNo);
        }
        if (!TextUtils.isEmpty(iv1Selected) && Boolean.parseBoolean(iv1Selected)) {
            ivCodeSelected1.setSelected(true);
        } else {
            ivCodeSelected2.setSelected(false);
        }

        if (!TextUtils.isEmpty(companyName)) {
            etCompanyName.setText(companyName);
        }
        if (!TextUtils.isEmpty(uniformCode)) {
            etUniformStandardCreditCode.setText(uniformCode);
        }
        if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("local")) {
            if (filePath.containsKey("1")) {
                PicassoImageLoader.getInstance().displayImage(activity, filePath.get("1"), ivFrontImage);
                llIdFront.setVisibility(View.GONE);
            }
            if (filePath.containsKey("2")) {
                PicassoImageLoader.getInstance().displayImage(activity, filePath.get("2"), ivBackgroundImage);
                llIdBackground.setVisibility(View.GONE);

            }
            if (filePath.containsKey("3")) {
                PicassoImageLoader.getInstance().displayImage(activity, filePath.get("3"), ivLicense);
                llIndustryLicense.setVisibility(View.GONE);
            }
            if (filePath.containsKey("4")) {
                PicassoImageLoader.getInstance().displayImage(activity, filePath.get("4"), ivInstitutionalFrameworkImage);
                llInstitutionalFramework.setVisibility(View.GONE);
            }
            if (filePath.containsKey("5")) {
                PicassoImageLoader.getInstance().displayImage(activity, filePath.get("5"), ivTaxLicense);
                llTaxLicense.setVisibility(View.GONE);
            }
        } else {
            disableFocus();
            setLayoutGone();
            if (!TextUtils.isEmpty(iv1Selected) && Boolean.parseBoolean(iv1Selected)) {
                llUnificationCode.setVisibility(View.VISIBLE);
                ivCodeSelected1.setVisibility(View.GONE);
                llOrganizationCode.setVisibility(View.GONE);
                llViewCellThree.setVisibility(View.GONE);
                //三张
            } else {
                llOrganizationCode.setVisibility(View.VISIBLE);
                ivCodeSelected2.setVisibility(View.GONE);
                llUnificationCode.setVisibility(View.GONE);
                llViewCellThree.setVisibility(View.VISIBLE);
                //五张
            }
            Drawable defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);
            if (ivCodeSelected1.isSelected()) {
                if (filePath.containsKey("1")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("1"), ivFrontImage, defaultDrawable);
                }
                if (filePath.containsKey("2")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("2"), ivBackgroundImage, defaultDrawable);
                }
                if (filePath.containsKey("4")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("4"), ivLicense, defaultDrawable);
                }
            } else {
                if (filePath.containsKey("1")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("1"), ivFrontImage, defaultDrawable);
                }
                if (filePath.containsKey("2")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("2"), ivBackgroundImage, defaultDrawable);
                }
                if (filePath.containsKey("4")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("4"), ivLicense, defaultDrawable);
                }
                if (filePath.containsKey("5")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("5"), ivInstitutionalFrameworkImage, defaultDrawable);
                }
                if (filePath.containsKey("6")) {
                    PicassoImageLoader.getInstance().displayNetImage(activity, filePath.get("6"), ivTaxLicense, defaultDrawable);
                }
            }

        }
    }

    private void setLayoutGone() {
        llIdFront.setVisibility(View.GONE);
        llIdBackground.setVisibility(View.GONE);
        llInstitutionalFramework.setVisibility(View.GONE);
        llIndustryLicense.setVisibility(View.GONE);
        llTaxLicense.setVisibility(View.GONE);
    }

    @Override
    public void resetImageViewForFailOrClear(String index) {
        switch (index) {
            case "1":
                ivFrontImage.setImageDrawable(null);
                ivFrontImage.destroyDrawingCache();
                progressFront.setVisibility(View.GONE);
                llIdFront.setVisibility(View.VISIBLE);
                break;
            case "2":
                ivBackgroundImage.setImageDrawable(null);
                ivBackgroundImage.destroyDrawingCache();
                progressBack.setVisibility(View.GONE);
                llIdBackground.setVisibility(View.VISIBLE);
                break;
            case "3":
                ivLicense.setImageDrawable(null);
                ivLicense.destroyDrawingCache();
                progressLicense.setVisibility(View.GONE);
                llIndustryLicense.setVisibility(View.VISIBLE);
                break;
            case "4":

                ivInstitutionalFrameworkImage.setImageDrawable(null);
                ivInstitutionalFrameworkImage.destroyDrawingCache();
                progressCode.setVisibility(View.GONE);
                llInstitutionalFramework.setVisibility(View.VISIBLE);
                break;
            case "5":
                ivTaxLicense.setImageDrawable(null);
                ivTaxLicense.destroyDrawingCache();
                llProgressTaxLicense.setVisibility(View.GONE);
                llTaxLicense.setVisibility(View.VISIBLE);
                break;
        }
    }


    @SuppressWarnings("deprecation")
    private void disableFocus() {
        etLegalPeopleName.setClickable(false);
        etLegalPeopleName.setFocusable(false);
        etLegalPeopleIdCard.setClickable(false);
        etLegalPeopleIdCard.setFocusable(false);
        ivCodeSelected1.setClickable(false);
        llUnificationCode.setClickable(false);
        ivCodeSelected2.setClickable(false);
        llOrganizationCode.setClickable(false);
        etCompanyName.setClickable(false);
        etCompanyName.setFocusable(false);
        etUniformStandardCreditCode.setClickable(false);
        etUniformStandardCreditCode.setFocusable(false);
        ivFrontImage.setClickable(false);
        llIdFront.setClickable(false);
        ivBackgroundImage.setClickable(false);
        llIdBackground.setClickable(false);
        ivLicense.setClickable(false);
        llIndustryLicense.setClickable(false);
        ivInstitutionalFrameworkImage.setClickable(false);
        llInstitutionalFramework.setClickable(false);
        ivTaxLicense.setClickable(false);
        llTaxLicense.setClickable(false);
        tvPost.setClickable(false);
        llViewCellThree.setClickable(false);
        progressFront.setClickable(false);
        llProgressFront.setClickable(false);
        progressBack.setClickable(false);
        llProgressBack.setClickable(false);
        progressLicense.setClickable(false);
        llProgressLicense.setClickable(false);
        progressCode.setClickable(false);
        llProgressCode.setClickable(false);
        progressFrontTaxLicense.setClickable(false);
        llProgressTaxLicense.setClickable(false);
        ivAgreeSign.setClickable(false);
        ivAgreeSign.setSelected(false);
        tvPost.setVisibility(View.GONE);

        tvSignText.setText("已签署");

        tvCompanyLegalPerson.setText(R.string.legal_person_information);
        tvCompanyLegalPerson.setTextColor(getResources().getColor(R.color.global_text_gray));

        tvCompanyType.setText(R.string.company_type);
        tvCompanyType.setTextColor(getResources().getColor(R.color.global_text_gray));


        tvCompanyData.setText(R.string.company_info2);
        tvCompanyData.setTextColor(getResources().getColor(R.color.global_text_gray));

        tvCorrelationCredentials.setText(R.string.relevant_credentials2);
        tvCorrelationCredentials.setTextColor(getResources().getColor(R.color.global_text_gray));

        tvCreditTitle.setSelected(false);
        llOrganizationCode.setSelected(false);
        tvInstitutionalTitle.setSelected(false);
        llUnificationCode.setSelected(false);
    }

    private void showMakeSureBackDialog() {
        if (activity.isFinishing()) {
            return;
        }
        ConfirmDialog.createConfirmDialogCustomButtonString(activity, getString(R.string.quit_input), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                Map<String, LocalPhotoFilePathAndUploadId> idMap = new TreeMap<>();
                if (presenter.getTempFileData() != null && !presenter.getTempFileData().isEmpty()) {


                    for (Map.Entry<String, String> entry : presenter.getTempFileData().entrySet()) {
                        LocalPhotoFilePathAndUploadId localPhotoFilePathAndUploadId = new LocalPhotoFilePathAndUploadId();
                        localPhotoFilePathAndUploadId.setLocalFilePath(presenter.getLocalFileData().get(entry.getKey()));
                        localPhotoFilePathAndUploadId.setId(entry.getValue());
                        idMap.put(entry.getKey(), localPhotoFilePathAndUploadId);

                    }

                    presenter.saveLocalData(etLegalPeopleName.getText().toString(),
                            etLegalPeopleIdCard.getText().toString(),
                            ivCodeSelected1.isSelected() ? "true" : "false",
                            etCompanyName.getText().toString(), etUniformStandardCreditCode.getText().toString(), idMap);
                }
                if (activity != null) {
                    activity.finish();
                }
            }
        }, new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

            }
        }, false);
    }

    public CompanyAuthenticateFragment() {
    }

    public static CompanyAuthenticateFragment getInstance() {
        return new CompanyAuthenticateFragment();
    }

    @Override
    public void filedUploadDone(String index) {

        switch (index) {
            case "1":
                progressFront.loadCompleted(SectorProgressView.ViewType.IMAGE);
                llProgressFront.setVisibility(View.GONE);
                break;
            case "2":
                progressBack.loadCompleted(SectorProgressView.ViewType.IMAGE);
                llProgressBack.setVisibility(View.GONE);
                break;
            case "3":
                progressLicense.loadCompleted(SectorProgressView.ViewType.IMAGE);
                llProgressLicense.setVisibility(View.GONE);
                break;
            case "4":
                progressCode.loadCompleted(SectorProgressView.ViewType.IMAGE);
                llProgressCode.setVisibility(View.GONE);
                break;
            case "5":
                progressFrontTaxLicense.loadCompleted(SectorProgressView.ViewType.IMAGE);
                llProgressTaxLicense.setVisibility(View.GONE);
                break;
        }
    }


    private class UploadListener implements ProgressRequestListener {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {

            if (this == uploadListenerFront) {
                progressFront.setProgress((double) bytesWritten / (double) contentLength);
//                if (done) {
//                    progressFront.loadCompleted(SectorProgressView.ViewType.IMAGE);
//                    llProgressFront.setVisibility(View.GONE);
//                }
            }
            if (this == uploadListenerBack) {
                progressBack.setProgress((double) bytesWritten / (double) contentLength);
//                if (done) {
//                    progressBack.loadCompleted(SectorProgressView.ViewType.IMAGE);
//                    llProgressBack.setVisibility(View.GONE);
//                }
            }
            if (this == industryLicenseListener) {
                progressLicense.setProgress((double) bytesWritten / (double) contentLength);
//                if (done) {
//                    progressLicense.loadCompleted(SectorProgressView.ViewType.IMAGE);
//                    llProgressLicense.setVisibility(View.GONE);
//                }
            }
            if (this == codeProgressListener) {
                progressCode.setProgress((double) bytesWritten / (double) contentLength);
//                if (done) {
//                    progressCode.loadCompleted(SectorProgressView.ViewType.IMAGE);
//                    llProgressCode.setVisibility(View.GONE);
//                }
            }
            if (this == taxLicenseProgressListener) {
                progressFrontTaxLicense.setProgress((double) bytesWritten / (double) contentLength);
//                if (done) {
//                    progressFrontTaxLicense.loadCompleted(SectorProgressView.ViewType.IMAGE);
//                    llProgressTaxLicense.setVisibility(View.GONE);
//                }

            }
        }
    }

    @Override
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public void showTakePictureDialog() {
        if (!isActive()) {
            return;
        }
        View view = activity.getLayoutInflater().inflate(R.layout.take_picture_choose_dialog, null, false);
        TextView tvCamera = ButterKnife.findById(view, R.id.camera);
        TextView tvGallery = ButterKnife.findById(view, R.id.gallery);
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
        tvCamera.setOnClickListener(dialogItemOnClick);
        tvGallery.setOnClickListener(dialogItemOnClick);
        tvCancel.setOnClickListener(dialogItemOnClick);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @OnClick({R.id.tv_net_sign, R.id.iv_agree_sign, R.id.ll_unification_code, R.id.ll_organization_code, R.id.iv_front_image, R.id.ll_id_front, R.id.iv_background_image, R.id.ll_id_background, R.id.iv_license, R.id.ll_industry_license, R.id.iv_institutional_framework_image, R.id.ll_institutional_framework, R.id.iv_tax_license, R.id.ll_tax_license, R.id.tv_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_unification_code:
                if (!ivCodeSelected1.isSelected()) {
                    ivCodeSelected1.setSelected(true);
                    llUnificationCode.setSelected(true);
                    tvCreditTitle.setSelected(true);
                    llOrganizationCode.setSelected(false);
                    tvInstitutionalTitle.setSelected(false);
                    ivCodeSelected2.setSelected(false);
                    llViewCellThree.setVisibility(View.GONE);
                    etUniformStandardCreditCode.setHint(R.string.input_society_credit_code);
                }
                break;
            case R.id.ll_organization_code:
                if (!ivCodeSelected2.isSelected()) {
                    llOrganizationCode.setSelected(true);
                    tvInstitutionalTitle.setSelected(true);
                    ivCodeSelected2.setSelected(true);

                    ivCodeSelected1.setSelected(false);
                    llUnificationCode.setSelected(false);
                    tvCreditTitle.setSelected(false);
                    etUniformStandardCreditCode.setHint(R.string.organization_code2);
                    llViewCellThree.setVisibility(View.VISIBLE);


                }
                break;
            case R.id.iv_front_image:
                if (llProgressFront.getVisibility() == View.GONE && llIdFront.getVisibility() == View.GONE && ivFrontImage.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 1);
                    bundle.putString("path", presenter.getLocalFileData().get("1") == null ? "" : presenter.getLocalFileData().get("1"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO1);
                }

                break;
            case R.id.ll_id_front:
                courseIndex = 1;
                showTakePictureDialog();
                break;
            case R.id.iv_background_image:
                if (llProgressBack.getVisibility() == View.GONE && llIdBackground.getVisibility() == View.GONE && ivBackgroundImage.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 2);
                    bundle.putString("path", presenter.getLocalFileData().get("2") == null ? "" : presenter.getLocalFileData().get("2"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO2);
                }
                break;
            case R.id.ll_id_background:
                courseIndex = 2;
                showTakePictureDialog();
                break;
            case R.id.iv_license:
                if (llProgressLicense.getVisibility() == View.GONE && llIndustryLicense.getVisibility() == View.GONE && ivLicense.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 4);
                    bundle.putString("path", presenter.getLocalFileData().get("3") == null ? "" : presenter.getLocalFileData().get("3"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO3);
                }
                break;
            case R.id.ll_industry_license:
                courseIndex = 3;
                showTakePictureDialog();
                break;
            case R.id.iv_institutional_framework_image:
                if (llInstitutionalFramework.getVisibility() == View.GONE && llProgressCode.getVisibility() == View.GONE && ivInstitutionalFrameworkImage.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 5);
                    bundle.putString("path", presenter.getLocalFileData().get("4") == null ? "" : presenter.getLocalFileData().get("4"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO4);
                }
                break;
            case R.id.ll_institutional_framework:
                courseIndex = 4;
                showTakePictureDialog();
                break;
            case R.id.iv_tax_license:
                if (llProgressLicense.getVisibility() == View.GONE && llTaxLicense.getVisibility() == View.GONE && ivTaxLicense.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 6);
                    bundle.putString("path", presenter.getLocalFileData().get("5") == null ? "" : presenter.getLocalFileData().get("5"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO5);
                }
                break;
            case R.id.ll_tax_license:
                courseIndex = 5;
                showTakePictureDialog();
                break;
            case R.id.tv_post:
                if (TextUtils.isEmpty(etLegalPeopleName.getText().toString())) {
                    showErrorDialog(getString(R.string.please_input_legal_people_name));
                    return;
                }

                if (!IDCardUtil.isIDCard(etLegalPeopleIdCard.getText().toString())) {
                    showErrorDialog(getString(R.string.please_input_legal_right_people_id_card));
                    return;
                }

                if (TextUtils.isEmpty(etCompanyName.getText().toString())){
                    showErrorDialog("请输入企业名称");
                    return;
                }
                if (TextUtils.isEmpty(etUniformStandardCreditCode.getText().toString())) {
                    if (ivCodeSelected1.isSelected()) {
                        showErrorDialog(getString(R.string.please_input_ogzion_code));
                    } else {
                        showErrorDialog(getString(R.string.please_input_uniform_code));
                    }
                    return;
                }

                if (ivCodeSelected1.isSelected()) {
                    if (etUniformStandardCreditCode.getText().length() < 18) {
                        showErrorDialog(getString(R.string.please_input_correct_uniform_code));
                        return;
                    }
                    if (!StringUtil.uniformCode(etUniformStandardCreditCode.getText().toString())) {
                        showErrorDialog(getString(R.string.please_input_correct_uniform_code));
                        return;
                    }
                } else {
                    if (!StringUtil.isValidEntpCode(etUniformStandardCreditCode.getText().toString())) {
                        showErrorDialog(getString(R.string.organization_correct_code));
                        return;
                    }

                }

                if (llProgressBack.getVisibility() == View.VISIBLE || llProgressFront.getVisibility() == View.VISIBLE || llProgressCode.getVisibility() == View.VISIBLE || llProgressTaxLicense.getVisibility() == View.VISIBLE) {
                    showErrorDialog(getString(R.string.uploading));
                    return;
                }
                presenter.commitCertify(etLegalPeopleName.getText().toString(),
                        etLegalPeopleIdCard.getText().toString(),
                        ivCodeSelected1.isSelected() ? "uniformSocialCreditCode" : "zzjgdm",
                        etCompanyName.getText().toString(), etUniformStandardCreditCode.getText().toString());
                break;
            case R.id.iv_agree_sign:
                ivAgreeSign.setSelected(ivAgreeSign.isSelected() ? false : true);
                tvPost.setEnabled(ivAgreeSign.isSelected() ? true : false);
                break;
            case R.id.tv_net_sign:
                gotoActivity(AssignPlatformActivity.class);
                break;

        }
    }

    private class DialogItemOnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            int id = v.getId();
            switch (id) {
                case R.id.camera:
                    PermissionGen.needPermission(CompanyAuthenticateFragment.this, 100,
                            new String[] {
                                    Manifest.permission.CAMERA
                            }
                    );
//                    CameraUtil.getInstance().takePhotoFromFragment(CompanyAuthenticateFragment.this);
                    break;
                case R.id.gallery:
                    if (requestGalleryPermission()) {
                        CameraUtil.getInstance().goAlbumViewFragment(CompanyAuthenticateFragment.this);
                    }
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
    public void setPresenter(CompanyAuthenticateContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(activity, message);
    }

    /**
     * runtime permission
     */
    @AfterPermissionGranted(Constants.PERMISSIONS_CODE_GALLERY)
    private boolean requestGalleryPermission() {
        if (RuntimePermission.hasPermissions(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            return true;
        } else {
            // Ask for one permission
            RuntimePermission.requestPermissions(getActivity(), getString(R.string.permissions_tips_gallery),
                    Constants.PERMISSIONS_CODE_GALLERY, Manifest.permission.READ_EXTERNAL_STORAGE);
            return false;
        }
    }

    @Override
    public void showCommitSuccessDialog() {
        ConfirmDialog.createConfirmDialog(activity, getString(R.string.commit_success), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == activity.RESULT_OK) {
            if (requestCode == CameraUtil.REQUEST_CODE_TAKE_IMAGE_FROM_ALBUM) {
                if (null == data) {
                    showErrorDialog(getString(R.string.get_picture_failed));
                    return;
                }
                switch (courseIndex) {
                    case 1:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivFrontImage);
                        //显示加载进度
                        llProgressFront.setVisibility(View.VISIBLE);
                        //隐藏默认背景
                        llIdFront.setVisibility(View.GONE);
                        // 上传图片
                        presenter.uploadFile(uploadListenerFront, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(1));
                        break;
                    case 2:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivBackgroundImage);
                        llProgressBack.setVisibility(View.VISIBLE);
                        llIdBackground.setVisibility(View.GONE);
                        presenter.uploadFile(uploadListenerBack, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(2));
                        break;
                    case 3:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivLicense);
                        llIndustryLicense.setVisibility(View.GONE);
                        llProgressLicense.setVisibility(View.VISIBLE);
                        presenter.uploadFile(industryLicenseListener, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(3));
                        break;
                    case 4:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivInstitutionalFrameworkImage);
                        llProgressCode.setVisibility(View.VISIBLE);
                        llInstitutionalFramework.setVisibility(View.GONE);
                        presenter.uploadFile(codeProgressListener, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(4));
                        break;
                    case 5:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivTaxLicense);
                        llProgressTaxLicense.setVisibility(View.VISIBLE);
                        llTaxLicense.setVisibility(View.GONE);
                        presenter.uploadFile(taxLicenseProgressListener, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(5));
                        break;
                }
            } else if (requestCode == CameraUtil.REQUEST_CODE_TAKE_IMAGE_FROM_CAMERA) {
                if (CameraUtil.getInstance().getOutImageUri() == null) {
                    showErrorDialog(getString(R.string.get_picture_failed));
                    return;
                }
                switch (courseIndex) {
                    case 1:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivFrontImage);

                        //显示加载进度
                        llProgressFront.setVisibility(View.VISIBLE);
                        //隐藏默认背景
                        llIdFront.setVisibility(View.GONE);
                        // 上传图片
                        presenter.uploadFile(uploadListenerFront, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(1));

                        break;
                    case 2:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivBackgroundImage);
                        llProgressBack.setVisibility(View.VISIBLE);
                        llIdBackground.setVisibility(View.GONE);
                        presenter.uploadFile(uploadListenerBack, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(2));

                        break;
                    case 3:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivLicense);
                        llIndustryLicense.setVisibility(View.GONE);
                        llProgressLicense.setVisibility(View.VISIBLE);
                        presenter.uploadFile(industryLicenseListener, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(3));
                        break;
                    case 4:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivInstitutionalFrameworkImage);
                        llProgressCode.setVisibility(View.VISIBLE);
                        llInstitutionalFramework.setVisibility(View.GONE);
                        presenter.uploadFile(codeProgressListener, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(4));
                        break;
                    case 5:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivTaxLicense);
                        llProgressTaxLicense.setVisibility(View.VISIBLE);
                        llTaxLicense.setVisibility(View.GONE);
                        presenter.uploadFile(taxLicenseProgressListener, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(5));
                        break;
                }
            } else if (requestCode == CHECK_BIG_PHOTO1) {
                resetImageViewForFailOrClear("1");
                presenter.getLocalFileData().remove("1");
                presenter.getTempFileData().remove("1");
                showTakePictureDialog();
            } else if (requestCode == CHECK_BIG_PHOTO2) {
                resetImageViewForFailOrClear("2");
                presenter.getLocalFileData().remove("2");
                presenter.getTempFileData().remove("2");
                showTakePictureDialog();

            } else if (requestCode == CHECK_BIG_PHOTO3) {
                resetImageViewForFailOrClear("3");
                presenter.getLocalFileData().remove("3");
                presenter.getTempFileData().remove("3");

                showTakePictureDialog();

            } else if (requestCode == CHECK_BIG_PHOTO4) {
                resetImageViewForFailOrClear("4");
                presenter.getLocalFileData().remove("4");
                presenter.getTempFileData().remove("4");

                showTakePictureDialog();

            } else if (requestCode == CHECK_BIG_PHOTO5) {
                resetImageViewForFailOrClear("5");
                presenter.getLocalFileData().remove("5");
                presenter.getTempFileData().remove("5");
                showTakePictureDialog();

            }
        }
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed(){
        Log.d("TAG","doSomethingSucceed");
        CameraUtil.getInstance().takePhotoFromFragment(CompanyAuthenticateFragment.this);

    }
    @PermissionFail(requestCode = 100)
    public void doSomethingFail(){
        Log.d("TAG","doSomethingFail");
    }
}

