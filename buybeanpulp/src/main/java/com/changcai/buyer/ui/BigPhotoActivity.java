package com.changcai.buyer.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.changcai.buyer.R;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.ui.base.BaseActivity;
import com.changcai.buyer.util.CapturePhotoUtils;
import com.changcai.buyer.util.Logger;
import com.changcai.buyer.util.PicassoImageLoader;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.view.ConfirmDialog;
import com.changcai.buyer.view.MyAlertDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.security.Permission;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.DefaultOnDoubleTapListener;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by liuxingwei on 2016/11/14.
 */
public class BigPhotoActivity extends BaseActivity implements View.OnClickListener {

    private PhotoView photoView;
    private Intent intent;
    private String picUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cms_big_photo);
        intent = getIntent();
        Bundle bundle = intent.getExtras();
        picUrl = bundle.getString("picUrl", "");
        Log.d("bigPicUrl", picUrl);
        initTitle();
        initView();
    }

    private void initView() {
        btnBack.setVisibility(View.GONE);
        titleView.setBackgroundResource(R.color.black);
        btnLeft.setBackgroundResource(R.drawable.icon_nav_clos_light);
        btnLeft.setVisibility(View.VISIBLE);
        iv_btn_right.setImageResource(R.drawable.icon_nav_download_light);
        iv_btn_right.setVisibility(View.VISIBLE);
        photoView = (PhotoView) findViewById(R.id.iv_photo_view);
        btnLeft.setOnClickListener(this);
        RxView.clicks(iv_btn_right).observeOn(AndroidSchedulers.mainThread())
                .throttleFirst(500, TimeUnit.MILLISECONDS).flatMap(new Func1<Void, Observable<Boolean>>() {
            @Override
            public Observable<Boolean> call(Void aVoid) {
                if (PackageManager.PERMISSION_GRANTED == ContextCompat.checkSelfPermission(BigPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    return Observable.just(true);
                }
                return Observable.just(false);
            }
        }).subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                if (aBoolean) {
                    savePic();
                } else {
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(BigPhotoActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        showPermissionDialog();
                    } else {
                        ActivityCompat.requestPermissions(BigPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_WRITE_EX_STORAGE);
                    }
                }
            }
        });
        Drawable defaultDrawable = ContextCompat.getDrawable(this, R.mipmap.no_network_2);
        PicassoImageLoader.getInstance().displayNetImage(this, picUrl, photoView, defaultDrawable);

        photoView.setOnDoubleTapListener(new DefaultOnDoubleTapListener(new PhotoViewAttacher(new ImageView(this))));
        photoView.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {

            }
        });
    }

    private void showPermissionDialog(){
        ConfirmDialog.createConfirmDialog(BigPhotoActivity.this, "可前往应用权限管理开启读写手机存储", "用户已拒绝访问相册", "确认", new ConfirmDialog.OnBtnConfirmListener() {
            @Override
            public void onConfirmListener() {

            }
        });
    }
    private void savePic() {
        Observable.just(photoView.getVisibleRectangleBitmap())
                .flatMap(new Func1<Bitmap, Observable<String>>() {
                    @Override
                    public Observable<String> call(Bitmap bitmap) {
                        return Observable.just(CapturePhotoUtils.insertImage(getContentResolver(), bitmap, "maidoupo", picUrl));
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (TextUtils.isEmpty(s)) {
                            if (BigPhotoActivity.this.isFinishing()) {
                                Logger.d("保存图片失败");
                            } else {
                                MyAlertDialog myAlertDialog = new MyAlertDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", "保存图片失败");
                                bundle.putInt("icon", R.drawable.icon_alt_fail);
                                myAlertDialog.setArguments(bundle);
                                myAlertDialog.show(getSupportFragmentManager(), "savePic");
                            }
                        } else {
                            if (!BigPhotoActivity.this.isFinishing()) {
                                MyAlertDialog myAlertDialog = new MyAlertDialog();
                                Bundle bundle = new Bundle();
                                bundle.putString("title", "保存图片成功");
                                bundle.putInt("icon", R.drawable.icon_alt_done);
                                myAlertDialog.setArguments(bundle);
                                myAlertDialog.show(getSupportFragmentManager(), "savePic");
                            }
                        }
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < grantResults.length; i++) {
            boolean isReject = ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                if (!isReject) {
                    ActivityCompat.requestPermissions(BigPhotoActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constants.REQUEST_PERMISSION_WRITE_EX_STORAGE);
                } else {
                    showPermissionDialog();
                }
                return;
            }
        }
        savePic();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLeft:
                finish();
                break;
        }
    }
}
