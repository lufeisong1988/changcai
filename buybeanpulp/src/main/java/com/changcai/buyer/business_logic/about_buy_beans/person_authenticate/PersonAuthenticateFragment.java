package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate;

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
import com.changcai.buyer.business_logic.about_buy_beans.person_authenticate.big_photo.BigPhotoActivity;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.okhttp.ProgressRequestListener;
import com.changcai.buyer.permission.AfterPermissionGranted;
import com.changcai.buyer.permission.RuntimePermission;
import com.changcai.buyer.util.CameraUtil;
import com.changcai.buyer.util.IDCardUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.changcai.buyer.util.StringUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.SectorProgressView;
import com.juggist.commonlibrary.rx.BackEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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

public class PersonAuthenticateFragment extends BaseFragment implements PersonAuthenticateContract.View {
    @BindView(R.id.tv_person_data)
    TextView tvPersonData;
    @BindView(R.id.tv_correlation_credentials)
    TextView tvCorrelationCredentials;
    @BindView(R.id.iv_agree_sign)
    ImageView ivAgreeSign;
    @BindView(R.id.tv_net_sign)
    TextView tvNetSign;
    private String TAG = PersonAuthenticateFragment.class.getSimpleName();
    private static final int CHECK_BIG_PHOTO1 = 9001;
    private static final int CHECK_BIG_PHOTO2 = 9002;
    private static final int CHECK_BIG_PHOTO3 = 9003;
    PersonAuthenticateContract.Presenter personAuthenticatePresenter;
    @BindView(R.id.et_legal_people_name)
    EditText etLegalPeopleName;
    @BindView(R.id.et_legal_people_id_card)
    EditText etLegalPeopleIdCard;
    @BindView(R.id.iv_front_image)
    ImageView ivFrontImage;
    @BindView(R.id.ll_id_front)
    ImageView llIdFront;
    @BindView(R.id.iv_background_image)
    ImageView ivBackgroundImage;
    @BindView(R.id.ll_id_background)
    ImageView llIdBackground;
    @BindView(R.id.iv_handle_front_image)
    ImageView ivHandleFrontImage;
    @BindView(R.id.ll_handle_front_image)
    ImageView llHandleFrontImage;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.tv_sign_text)
    TextView tvSignText;

    Dialog dialog;
    @BindView(R.id.progress_front)
    SectorProgressView progressFront;
    @BindView(R.id.ll_progress_front)
    LinearLayout llProgressFront;
    @BindView(R.id.progress_back)
    SectorProgressView progressBack;
    @BindView(R.id.ll_progress_back)
    LinearLayout llProgressBack;
    @BindView(R.id.iv_progress_handle_front)
    SectorProgressView ivProgressHandleFront;
    @BindView(R.id.ll_progress_handle_front)
    LinearLayout llProgressHandleFront;
    private DialogItemOnClick dialogItemOnClick;
    private int courseIndex;
    private List<UploadListener> uploadListeners;
    private UploadListener uploadListenerFront;
    private UploadListener uploadListenerBack;
    private UploadListener uploadListenerHandleFront;
    private Subscription backSubscription;

    private String status;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personality_authentication, container, false);
        baseUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ivAgreeSign.setSelected(true);
        uploadListeners = new ArrayList<>();
        uploadListenerFront = new UploadListener();
        uploadListenerBack = new UploadListener();
        uploadListenerHandleFront = new UploadListener();
        uploadListeners.add(uploadListenerFront);
        uploadListeners.add(uploadListenerBack);
        uploadListeners.add(uploadListenerHandleFront);
        personAuthenticatePresenter.start();
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
                                || (null != personAuthenticatePresenter.getTempFileData() && !personAuthenticatePresenter.getTempFileData().isEmpty())) {
                            showMakeSureBackDialog();
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
        if (!isSuccess()) {
            final TempInfoInputForIdCertify tem = SPUtil.getObjectFromShare(Constants.KEY_PERSON_CERTIFY);
            if (tem == null) {
                return;
            }

            Map<String, LocalPhotoFilePathAndUploadId> map = tem.getIdMap();
            if (map == null) {
                return;
            }


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
                            personAuthenticatePresenter.setLocalFileData(stringStringMap.get("map1"));
                            personAuthenticatePresenter.setTempFileData(stringStringMap.get("map2"));
                            refreshUI(tem.getInputTempName()
                                    , tem.getInputTempIdNo(), stringStringMap.get("map1"), "local");
                        }
                    });
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        LogUtil.d(TAG, "onViewStateRestored");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isSuccessOrProcess()) {
            personAuthenticatePresenter.loadIdentityDetails();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        uploadListeners.clear();
        RxUtil.remove(backSubscription);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(TAG, "onDestroy");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        personAuthenticatePresenter.detach();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        LogUtil.d(TAG, "onSaveInstanceState");
    }


    @Override
    public void setRetainInstance(boolean retain) {
        super.setRetainInstance(retain);
        LogUtil.d(TAG, "setRetainInstance");

    }


    private void showMakeSureBackDialog() {
        if (activity.isFinishing()) {
            return;
        }
        ConfirmDialog.createConfirmDialogCustomButtonString(activity, getString(R.string.quit_input), new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {
                Map<String, LocalPhotoFilePathAndUploadId> idMap = new TreeMap<>();
                if (personAuthenticatePresenter.getTempFileData() != null || !personAuthenticatePresenter.getTempFileData().isEmpty()) {

                    for (Map.Entry<String, String> entry : personAuthenticatePresenter.getTempFileData().entrySet()) {
                        LocalPhotoFilePathAndUploadId localPhotoFilePathAndUploadId = new LocalPhotoFilePathAndUploadId();
                        localPhotoFilePathAndUploadId.setLocalFilePath(personAuthenticatePresenter.getLocalFileData().get(entry.getKey()));
                        localPhotoFilePathAndUploadId.setId(entry.getValue());

                        idMap.put(entry.getKey(), localPhotoFilePathAndUploadId);
                    }
                    personAuthenticatePresenter.saveLocalData(etLegalPeopleName.getText().toString(),
                            etLegalPeopleIdCard.getText().toString(), idMap);
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

    public PersonAuthenticateFragment() {
    }

    @Override
    public void filedUploadDone(String index) {
        switch (index) {
            case "1":
                progressFront.loadCompleted(SectorProgressView.ViewType.IMAGE);
                setProgressFrontLineaLayoutGone();
                break;
            case "2":
                progressBack.loadCompleted(SectorProgressView.ViewType.IMAGE);
                setLlProgressBackGone();
                break;
            case "3":
                ivProgressHandleFront.loadCompleted(SectorProgressView.ViewType.IMAGE);
                setLlProgressHandleFrontGone();
                break;
        }
    }


    private class UploadListener implements ProgressRequestListener {
        @Override
        public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {

            if (this == uploadListenerFront) {
                progressFront.setProgress((double) bytesWritten / (double) contentLength);
            }
            if (this == uploadListenerBack) {
                progressBack.setProgress((double) bytesWritten / (double) contentLength);
            }
            if (this == uploadListenerHandleFront) {
                ivProgressHandleFront.setProgress((double) bytesWritten / (double) contentLength);
            }
        }

    }


    public static PersonAuthenticateFragment newInstance() {
        return new PersonAuthenticateFragment();
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
    public Context getActivityContext() {
        return activity;
    }

    @Override
    public void refreshUI(String name, String idNumber, Map<String, String> fileMap, String fromLocal) {
        if (!TextUtils.isEmpty(name)) {
            etLegalPeopleName.setText(name);
        }
        if (!TextUtils.isEmpty(idNumber)) {
            etLegalPeopleIdCard.setText(idNumber);
        }

        if (!TextUtils.isEmpty(fromLocal) && fromLocal.equalsIgnoreCase("local")) {
            if (fileMap.containsKey("1")) {
                PicassoImageLoader.getInstance().displayImage(activity, fileMap.get("1"), ivFrontImage);
                llIdFront.setVisibility(View.GONE);
            }
            if (fileMap.containsKey("2")) {
                PicassoImageLoader.getInstance().displayImage(activity, fileMap.get("2"), ivBackgroundImage);
                llIdBackground.setVisibility(View.GONE);
            }
            if (fileMap.containsKey("3")) {
                PicassoImageLoader.getInstance().displayImage(activity, fileMap.get("3"), ivHandleFrontImage);
                llHandleFrontImage.setVisibility(View.GONE);
            }
        } else {
            disableFocus();
            setViewGone();
            Drawable defaultDrawable = ContextCompat.getDrawable(activity, R.mipmap.no_network_2);

            if (fileMap.containsKey("1")) {
                PicassoImageLoader.getInstance().displayNetImage(activity, fileMap.get("1"), ivFrontImage, defaultDrawable);
//                PicassoImageLoader.getInstance().displayImageDimen(activity,fileMap.get("1"),ivFrontImage,defaultDrawable,R.dimen.dim524,R.dimen.dim295);
            }
            if (fileMap.containsKey("2")) {
                PicassoImageLoader.getInstance().displayNetImage(activity, fileMap.get("2"), ivBackgroundImage, defaultDrawable);
            }
            if (fileMap.containsKey("3")) {
                PicassoImageLoader.getInstance().displayNetImage(activity, fileMap.get("3"), ivHandleFrontImage, defaultDrawable);
            }
        }
    }

    @Override
    public void resetImageViewForFail(String index) {
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
                ivHandleFrontImage.setImageDrawable(null);
                ivHandleFrontImage.destroyDrawingCache();
                ivProgressHandleFront.setVisibility(View.GONE);
                llHandleFrontImage.setVisibility(View.VISIBLE);
                break;
        }
    }


    public void setViewGone() {
        llIdFront.setVisibility(View.GONE);
        llIdBackground.setVisibility(View.GONE);
        llHandleFrontImage.setVisibility(View.GONE);
        tvPost.setVisibility(View.GONE);
    }

    @SuppressWarnings("deprecation")
    private void disableFocus() {
        etLegalPeopleName.setFocusable(false);
        etLegalPeopleIdCard.setClickable(false);
        etLegalPeopleIdCard.setFocusable(false);
        etLegalPeopleIdCard.setClickable(false);
        ivFrontImage.setClickable(false);
        llIdFront.setClickable(false);
        ivBackgroundImage.setClickable(false);
        llIdBackground.setClickable(false);
        ivHandleFrontImage.setClickable(false);
        llHandleFrontImage.setClickable(false);
        tvPost.setClickable(false);

        progressFront.setClickable(false);
        llProgressFront.setClickable(false);
        progressBack.setClickable(false);
        llProgressBack.setClickable(false);
        ivProgressHandleFront.setClickable(false);
        llProgressHandleFront.setClickable(false);

        ivAgreeSign.setClickable(false);
        ivAgreeSign.setSelected(false);
        tvSignText.setText("已签署");


        tvPersonData.setText(R.string.personality_information2);
        tvPersonData.setTextColor(getResources().getColor(R.color.global_text_gray));

        tvCorrelationCredentials.setText(R.string.relevant_credentials2);
        tvCorrelationCredentials.setTextColor(getResources().getColor(R.color.global_text_gray));
    }

    @Override
    public void setPresenter(PersonAuthenticateContract.Presenter presenter) {
        this.personAuthenticatePresenter = presenter;
    }


    private boolean isSuccessOrProcess() {
        if (TextUtils.isEmpty(status))
            status = ((UserInfo) SPUtil.getObjectFromShare(Constants.KEY_USER_INFO)).getEnterStatus();
        if (status.equalsIgnoreCase("SUCCESS") || status.contentEquals("PROCESS")) {
            return true;
        }
        return false;
    }

    private boolean isSuccess() {
        if (TextUtils.isEmpty(status))
            status = ((UserInfo) SPUtil.getObjectFromShare(Constants.KEY_USER_INFO)).getEnterStatus();
        if (status.equalsIgnoreCase("SUCCESS")) {
            return true;
        }
        return false;
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
                    PermissionGen.needPermission(PersonAuthenticateFragment.this, 100,
                            new String[] {
                                    Manifest.permission.CAMERA
                            }
                    );

                    break;
                case R.id.gallery:
                    intentToGallery();
                    break;
                case R.id.cancel:
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    break;
            }
        }
    }

    public void intentToGallery() {
        if (requestGalleryPermission()) {
            CameraUtil.getInstance().goAlbumViewFragment(PersonAuthenticateFragment.this);
        }
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
                        setProgressFrontLineaLayoutVisible();
                        //隐藏默认背景
                        setFrontIdLayoutGone();
                        // 上传图片
                        personAuthenticatePresenter.uploadFile(uploadListenerFront, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(1));
                        break;
                    case 2:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivBackgroundImage);
                        setLlProgressBackVisible();
                        setBackIdLayoutGone();
                        personAuthenticatePresenter.uploadFile(uploadListenerBack, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(2));
                        break;
                    case 3:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, data.getData()), ivHandleFrontImage);
                        setLlProgressHandleFrontVisible();
                        setHandleFrontLayoutGone();
                        personAuthenticatePresenter.uploadFile(uploadListenerHandleFront, CameraUtil.getSelectPicPath(activity, data.getData()), String.valueOf(3));
                        break;
                }
            } else if (requestCode == CameraUtil.REQUEST_CODE_TAKE_IMAGE_FROM_CAMERA) {

                if (CameraUtil.getInstance().getOutImageUri() == null) {
                    showErrorDialog(getString(R.string.get_picture_failed));
                    return;
                }
                switch (courseIndex) {
                    case 1:
                        PicassoImageLoader.getInstance().displayImage(activity,
                                CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()),
                                ivFrontImage);
                        LogUtil.d(TAG, "camera- file" + CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()));
                        //显示加载进度
                        setProgressFrontLineaLayoutVisible();
                        //隐藏默认背景
                        setFrontIdLayoutGone();
                        // 上传图片
                        personAuthenticatePresenter.uploadFile(uploadListenerFront, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(1));


                        break;
                    case 2:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivBackgroundImage);
                        LogUtil.d(TAG, "camera- file" + CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()));
                        setLlProgressBackVisible();
                        setBackIdLayoutGone();
                        personAuthenticatePresenter.uploadFile(uploadListenerBack, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(2));

                        break;
                    case 3:
                        PicassoImageLoader.getInstance().displayImage(activity, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), ivHandleFrontImage);
                        LogUtil.d(TAG, "camera- file" + CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()));
                        setLlProgressHandleFrontVisible();
                        setHandleFrontLayoutGone();
                        personAuthenticatePresenter.uploadFile(uploadListenerHandleFront, CameraUtil.getSelectPicPath(activity, CameraUtil.getInstance().getOutImageUri()), String.valueOf(3));

                        break;
                }
            } else if (requestCode == CHECK_BIG_PHOTO1) {
                resetImageViewForFail("1");
                personAuthenticatePresenter.getLocalFileData().remove("1");
                personAuthenticatePresenter.getTempFileData().remove("1");
                showTakePictureDialog();
            } else if (requestCode == CHECK_BIG_PHOTO2) {
                resetImageViewForFail("2");
                personAuthenticatePresenter.getLocalFileData().remove("2");
                personAuthenticatePresenter.getTempFileData().remove("2");
                showTakePictureDialog();
            } else if (requestCode == CHECK_BIG_PHOTO3) {
                resetImageViewForFail("3");
                personAuthenticatePresenter.getLocalFileData().remove("3");
                personAuthenticatePresenter.getTempFileData().remove("3");
                showTakePictureDialog();
            }
        }
    }

    @Override
    public void setInitialSavedState(SavedState state) {
        super.setInitialSavedState(state);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        LogUtil.d(TAG, "onLowMemory");
    }

    @Override
    public void showErrorDialog(String message) {
        ConfirmDialog.createConfirmDialog(activity, message);
    }

    @OnClick({R.id.iv_agree_sign, R.id.tv_net_sign, R.id.iv_front_image, R.id.ll_id_front, R.id.iv_background_image, R.id.ll_id_background, R.id.iv_handle_front_image, R.id.ll_handle_front_image, R.id.tv_post})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_front_image:
                courseIndex = 1;
                if (llProgressFront.getVisibility() == View.GONE && llIdFront.getVisibility() == View.GONE && ivFrontImage.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 1);
                    bundle.putString("path", personAuthenticatePresenter.getLocalFileData().get("1") == null ? "" : personAuthenticatePresenter.getLocalFileData().get("1"));
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
                courseIndex = 2;
                if (llProgressBack.getVisibility() == View.GONE && llIdBackground.getVisibility() == View.GONE && ivBackgroundImage.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 2);
                    bundle.putString("path", personAuthenticatePresenter.getLocalFileData().get("2") == null ? "" : personAuthenticatePresenter.getLocalFileData().get("2"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO2);
                }
                break;
            case R.id.ll_id_background:
                courseIndex = 2;
                showTakePictureDialog();
                break;
            case R.id.iv_handle_front_image:
                courseIndex = 3;
                if (llProgressHandleFront.getVisibility() == View.GONE && llHandleFrontImage.getVisibility() == View.GONE && ivHandleFrontImage.getDrawable() != null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt("title", 3);
                    bundle.putString("path", personAuthenticatePresenter.getLocalFileData().get("3") == null ? "" : personAuthenticatePresenter.getLocalFileData().get("3"));
                    Intent intent = new Intent(activity, BigPhotoActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, CHECK_BIG_PHOTO3);
                }
                break;
            case R.id.ll_handle_front_image:
                courseIndex = 3;
                showTakePictureDialog();
                break;
            case R.id.tv_post:
                String name = etLegalPeopleName.getText().toString();
                String idNo = etLegalPeopleIdCard.getText().toString();
                if (TextUtils.isEmpty(name)) {
                    showErrorDialog(getString(R.string.please_input_people_name));
                    return;
                }
                if (!StringUtil.ChineseName(name)) {
                    showErrorDialog(getString(R.string.chanese_name));
                    return;
                }

                if (TextUtils.isEmpty(idNo)) {
                    showErrorDialog(getString(R.string.please_input_no2));
                    return;
                }
                if (!IDCardUtil.isIDCard(idNo)) {
                    showErrorDialog(getString(R.string.please_input_no2_be_correct));
                    return;
                }

                if (llProgressBack.getVisibility() == View.VISIBLE || llProgressFront.getVisibility() == View.VISIBLE || llProgressHandleFront.getVisibility() == View.VISIBLE) {
                    showErrorDialog(getString(R.string.uploading));
                    return;
                }
                personAuthenticatePresenter.commitCertify(etLegalPeopleName.getText().toString(), etLegalPeopleIdCard.getText().toString());
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

    /**
     * front id photo default background gone
     */
    private void setFrontIdLayoutGone() {
        llIdFront.setVisibility(View.GONE);
    }

    /**
     * back id photo default background gone
     */
    private void setBackIdLayoutGone() {
        llIdBackground.setVisibility(View.GONE);
    }

    /**
     * handle front id photo default background gone
     */
    private void setHandleFrontLayoutGone() {
        llHandleFrontImage.setVisibility(View.GONE);
    }

    /**
     * 身份证正面照
     */
    private void setProgressFrontLineaLayoutVisible() {
        llProgressFront.setVisibility(View.VISIBLE);
    }

    /**
     * 身份证正面照隐藏
     */
    private void setProgressFrontLineaLayoutGone() {
        llProgressFront.setVisibility(View.GONE);
    }

    /**
     * 显示身份证背面上传进度
     */
    private void setLlProgressBackVisible() {
        llProgressBack.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏身份证背面上传进度
     */
    private void setLlProgressBackGone() {
        llProgressBack.setVisibility(View.GONE);
    }

    /**
     * 手持身份证正面进度显示
     */
    private void setLlProgressHandleFrontVisible() {
        llProgressHandleFront.setVisibility(View.VISIBLE);
    }


    /**
     * 隐藏手持身份证正面进度
     */
    private void setLlProgressHandleFrontGone() {
        llProgressHandleFront.setVisibility(View.GONE);
    }

    @PermissionSuccess(requestCode = 100)
    public void doSomethingSucceed(){
        Log.d("TAG","doSomethingSucceed");
        CameraUtil.getInstance().takePhotoFromFragment(PersonAuthenticateFragment.this);

    }
    @PermissionFail(requestCode = 100)
    public void doSomethingFail(){
        Log.d("TAG","doSomethingFail");
    }
}
