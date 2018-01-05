package com.changcai.buyer.im.main.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.changcai.buyer.CompatTouchBackActivity;
import com.changcai.buyer.R;
import com.changcai.buyer.im.contact.constant.UserConstant;
import com.changcai.buyer.im.contact.helper.UserUpdateHelper;
import com.changcai.buyer.view.RotateDotsProgressView;
import com.netease.nimlib.sdk.RequestCallbackWrapper;
import com.netease.nimlib.sdk.ResponseCode;
import com.netease.nimlib.sdk.uinfo.constant.UserInfoFieldEnum;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by lufeisong on 2017/12/25.
 */

public class UserProfileIntroductionActivity extends CompatTouchBackActivity {
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.progress)
    RotateDotsProgressView progress;

    private int key;
    private String data;
    private Map<Integer, UserInfoFieldEnum> fieldMap;

    private static final String EXTRA_KEY = "EXTRA_KEY";
    private static final String EXTRA_DATA = "EXTRA_DATA";
    public static final int REQUEST_CODE = 1000;
    public static final void startActivity(Context context, int key, String data) {
        Intent intent = new Intent();
        intent.setClass(context, UserProfileIntroductionActivity.class);
        intent.putExtra(EXTRA_KEY, key);
        intent.putExtra(EXTRA_DATA, data);
        ((Activity) context).startActivityForResult(intent, REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        showKeyboard(false);
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        showKeyboard(false);
        super.onBackPressed();
    }
    @Override
    protected void injectFragmentView() {
        parseIntent();
        initListener();
        initData();
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
        return R.string.user_profile_introduction;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_user_profile_introduction;
    }

    @Override
    public void toolBarRightBtnListener() {
        super.toolBarRightBtnListener();
        if (fieldMap == null) {
            fieldMap = new HashMap<>();
            fieldMap.put(UserConstant.KEY_SIGNATURE, UserInfoFieldEnum.SIGNATURE);
        }
        showLoading();
        UserUpdateHelper.update(fieldMap.get(key), etContent.getText().toString(), callback);
    }
    private void parseIntent() {
        key = getIntent().getIntExtra(EXTRA_KEY, -1);
        data = getIntent().getStringExtra(EXTRA_DATA);
    }
    void initListener(){
        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int length = s.toString().length();
                if(length == 0){
                    etContent.setTextColor(getResources().getColor(R.color.font_gray));
                }else{
                    etContent.setTextColor(getResources().getColor(R.color.font_black));
                }
                if(length > 200){
                    int index = etContent.getSelectionStart();
                    Editable editable = etContent.getText();
                    editable.delete(index - 1, index);
                    tvNum.setText("200/200");
                }else{
                    tvNum.setText(length + "/200");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    void initData(){
        tv_right.setText("完成");
        tv_right.setVisibility(View.VISIBLE);
        if(data != null){
            etContent.setText(data);
        }
    }

    RequestCallbackWrapper callback = new RequestCallbackWrapper() {
        @Override
        public void onResult(int code, Object result, Throwable exception) {
            dismissLoading();
            if (code == ResponseCode.RES_SUCCESS) {
                onUpdateCompleted();
            } else if (code == ResponseCode.RES_ETIMEOUT) {
                Toast.makeText(UserProfileIntroductionActivity.this, R.string.user_info_update_failed, Toast.LENGTH_SHORT).show();
            }
        }
    };
    private void onUpdateCompleted() {
        showKeyboard(false);
        Toast.makeText(UserProfileIntroductionActivity.this, R.string.user_info_update_success, Toast.LENGTH_SHORT).show();
        finish();
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
