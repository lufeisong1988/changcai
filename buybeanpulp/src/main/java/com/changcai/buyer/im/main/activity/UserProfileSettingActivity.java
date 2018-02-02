package com.changcai.buyer.im.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.im.contact.constant.UserConstant;
import com.changcai.buyer.im.contact.helper.UserUpdateHelper;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.view.ActionSheetDialog;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.changcai.buyer.view.RoundImageView;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.SimpleCallback;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.common.media.picker.PickImageHelper;
import com.netease.nim.uikit.common.media.picker.activity.PickImageActivity;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.nos.NosService;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;
import com.netease.nimlib.sdk.uinfo.model.NimUserInfo;

import java.io.File;

import butterknife.BindView;

/**
 * Created by lufeisong on 2017/12/25.
 */

public class UserProfileSettingActivity extends CompatTouchBackActivity {
    @BindView(R.id.cl_header)
    ConstraintLayout clHeader;
    @BindView(R.id.cl_user_introducion)
    ConstraintLayout clUserIntroducion;
    @BindView(R.id.iv_header)
    RoundImageView ivHeader;
    @BindView(R.id.tv_introduction)
    TextView tvIntroduction;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.cl_user_nickname)
    ConstraintLayout clUserNickname;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;
    private Drawable defaultDrawable;
    // data
    AbortableFuture<String> uploadAvatarFuture;
    String account;
    private NimUserInfo userInfo;
    // constant
    private static final int PICK_AVATAR_REQUEST = 0x0E;
    private static final int AVATAR_TIME_OUT = 30000;

    public static void start(Context context, String account) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileSettingActivity.class);
        intent.putExtra(Extras.EXTRA_ACCOUNT, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void injectFragmentView() {
        defaultDrawable = this.getResources().getDrawable(R.drawable.icon_default_head);
        parseIntent();
        initListsner();

    }


    @Override
    protected int getTitleTextColor() {
        return getResources().getColor(R.color.black);
    }

    @Override
    protected int getNavigationVisible() {
        return View.VISIBLE;
    }

    @Override
    protected int getToolBarBackgroundColor() {
        return getResources().getColor(R.color.white);
    }

    @Override
    protected int getNavigationIcon() {
        return R.drawable.icon_nav_back;
    }

    @Override
    protected int getTitleText() {
        return R.string.user_profile_title;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_profile_setting;
    }

    void parseIntent() {
        account = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
    }

    void initListsner() {
        clHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final PickImageHelper.PickImageOption option = new PickImageHelper.PickImageOption();
                option.titleResId = R.string.set_head_image;
                option.crop = true;
                option.multiSelect = false;
                option.cropOutputImageWidth = 720;
                option.cropOutputImageHeight = 720;
                new ActionSheetDialog(UserProfileSettingActivity.this)
                        .builder()
                        .setTitle("选择头像")
                        .setCancelable(true)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("我的相册中选择", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        int from = PickImageActivity.FROM_LOCAL;
                                        if (!option.crop) {
                                            PickImageActivity.start(UserProfileSettingActivity.this, PICK_AVATAR_REQUEST, from, option.outputPath, option.multiSelect,
                                                    option.multiSelectMaxCount, true, false, 0, 0);
                                        } else {
                                            PickImageActivity.start(UserProfileSettingActivity.this, PICK_AVATAR_REQUEST, from, option.outputPath, false, 1,
                                                    false, true, option.cropOutputImageWidth, option.cropOutputImageHeight);
                                        }

                                    }
                                })
                        .addSheetItem("拍摄", ActionSheetDialog.SheetItemColor.Blue,
                                new ActionSheetDialog.OnSheetItemClickListener() {
                                    @Override
                                    public void onClick(int which) {
                                        int from = PickImageActivity.FROM_CAMERA;
                                        if (!option.crop) {
                                            PickImageActivity.start(UserProfileSettingActivity.this, PICK_AVATAR_REQUEST, from, option.outputPath, option.multiSelect, 1,
                                                    true, false, 0, 0);
                                        } else {
                                            PickImageActivity.start(UserProfileSettingActivity.this, PICK_AVATAR_REQUEST, from, option.outputPath, false, 1,
                                                    false, true, option.cropOutputImageWidth, option.cropOutputImageHeight);
                                        }
                                    }
                                })
                        .show();
//            PickImageHelper.pickImage(UserProfileSettingActivity.this, PICK_AVATAR_REQUEST, option);
            }
        });
        clUserIntroducion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String introductionStr = tvIntroduction.getText().toString();
                if (introductionStr.equals(getResources().getString(R.string.user_profile_introduction_tip))) {
                    UserProfileIntroductionActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_SIGNATURE,
                            "");
                } else {
                    UserProfileIntroductionActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_SIGNATURE,
                            introductionStr);
                }

            }
        });
        clUserNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nicknameStr = tvNickname.getText().toString();
                if (nicknameStr.equals(getResources().getString(R.string.user_profile_introduction_tip))) {
                    UserProfileNicknameActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_NICKNAME,
                            "");
                } else {
                    UserProfileNicknameActivity.startActivity(UserProfileSettingActivity.this, UserConstant.KEY_NICKNAME,
                            nicknameStr);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == PICK_AVATAR_REQUEST) {
            String path = data.getStringExtra(Extras.EXTRA_FILE_PATH);
            updateAvatar(path);
        }
    }

    /**
     * 更新头像
     */
    private void updateAvatar(final String path) {
        if (TextUtils.isEmpty(path)) {
            return;
        }

        File file = new File(path);
        if (file == null) {
            return;
        }

        showLoading();
        new Handler().postDelayed(outimeTask, AVATAR_TIME_OUT);
        uploadAvatarFuture = NIMClient.getService(NosService.class).upload(file, PickImageAction.MIME_JPEG);
        uploadAvatarFuture.setCallback(new RequestCallbackWrapper<String>() {
            @Override
            public void onResult(int code, String url, Throwable exception) {
                if (code == ResponseCode.RES_SUCCESS && !TextUtils.isEmpty(url)) {

                    UserUpdateHelper.update(UserInfoFieldEnum.AVATAR, url, new RequestCallbackWrapper<Void>() {
                        @Override
                        public void onResult(int code, Void result, Throwable exception) {
                            if (code == ResponseCode.RES_SUCCESS) {
                                Toast.makeText(UserProfileSettingActivity.this, R.string.head_update_success, Toast.LENGTH_SHORT).show();
                                onUpdateDone();
                            } else {
                                Toast.makeText(UserProfileSettingActivity.this, R.string.head_update_failed, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); // 更新资料
                } else {
                    Toast.makeText(UserProfileSettingActivity.this, R.string.user_info_update_failed, Toast
                            .LENGTH_SHORT).show();
                    onUpdateDone();
                }
            }
        });
    }

    private void cancelUpload(int resId) {
        if (uploadAvatarFuture != null) {
            uploadAvatarFuture.abort();
            Toast.makeText(UserProfileSettingActivity.this, resId, Toast.LENGTH_SHORT).show();
            onUpdateDone();
        }
    }

    private Runnable outimeTask = new Runnable() {
        @Override
        public void run() {
            cancelUpload(R.string.user_info_update_failed);
        }
    };

    private void onUpdateDone() {
        uploadAvatarFuture = null;
        dismissLoading();
        getUserInfo();
    }

    private void getUserInfo() {
        userInfo = (NimUserInfo) NimUIKit.getUserInfoProvider().getUserInfo(account);
        if (userInfo == null) {
            NimUIKit.getUserInfoProvider().getUserInfoAsync(account, new SimpleCallback<NimUserInfo>() {

                @Override
                public void onResult(boolean success, NimUserInfo result, int code) {
                    if (success) {
                        userInfo = result;
                        updateUI();
                    } else {
                        Toast.makeText(UserProfileSettingActivity.this, "getUserInfoFromRemote failed:" + code, Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            updateUI();
        }
    }

    private void updateUI() {
        if (userInfo.getAvatar() != null && !userInfo.getAvatar().equals("")) {
            PicassoImageLoader.getInstance().displayNetImage(UserProfileSettingActivity.this, userInfo.getAvatar(), ivHeader, defaultDrawable);
        }
        if (userInfo.getSignature() != null && !userInfo.getSignature().equals("")) {
            tvIntroduction.setText(userInfo.getSignature());
        } else {
            tvIntroduction.setText(R.string.user_profile_introduction_tip);
        }
        if (userInfo.getName() != null && !userInfo.getName().equals("")) {
            tvNickname.setText(userInfo.getName());
        } else {
            tvNickname.setText(R.string.user_profile_introduction_tip);
        }
    }
    //viewModel

    public void showLoading() {
        progress.setVisibility(View.VISIBLE);
        progress.showAnimation(true);
    }


    public void dismissLoading() {
        progress.setVisibility(View.GONE);
        progress.refreshDone(true);
    }
}
