package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate;

import android.text.TextUtils;

import com.changcai.buyer.R;
import com.changcai.buyer.bean.DetailsIdent;
import com.changcai.buyer.bean.LocalPhotoFilePathAndUploadId;
import com.changcai.buyer.bean.TempInfoInputForIdCertify;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiServiceGenerator;
import com.changcai.buyer.interface_api.BaseApiModel;
import com.changcai.buyer.interface_api.DoIdentityService;
import com.changcai.buyer.interface_api.GetIdentityDetailsService;
import com.changcai.buyer.interface_api.NetworkResultFunc1;
import com.changcai.buyer.interface_api.UploadFileService;
import com.changcai.buyer.okhttp.ProgressRequestListener;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.ImageUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.Gson;
import com.juggist.commonlibrary.rx.RefreshOrderEvent;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by liuxingwei on 2016/11/26.
 */

public class PersonAuthenticatePresenter implements PersonAuthenticateContract.Presenter {

    private String TAG = PersonAuthenticatePresenter.class.getSimpleName();
    PersonAuthenticateContract.View view;

    private static Subscription subscription;
    private static Subscription commitSubscription;
    private static Subscription detailsSubscription;
    private Map<String, String> uploadResultId;
    private Map<String, String> localFilePath;
    private TempInfoInputForIdCertify tempInfoInputForIdCertify;
    private FormBody.Builder bodyBuilder ;

    public PersonAuthenticatePresenter(PersonAuthenticateContract.View view) {
        this.view = view;
        view.setPresenter(this);


    }

    @Override
    public void start() {
        uploadResultId = new HashMap<>();
        tempInfoInputForIdCertify = SPUtil.getObjectFromShare(Constants.KEY_PERSON_CERTIFY);
        if (null == tempInfoInputForIdCertify) {
            tempInfoInputForIdCertify = new TempInfoInputForIdCertify();
        }
        localFilePath = new HashMap<>();
    }

    @Override
    public void detach() {
        view = null;
        RxUtil.remove(subscription);
        RxUtil.remove(commitSubscription);
        RxUtil.remove(detailsSubscription);
    }

    @Override
    public void uploadFile(final ProgressRequestListener progressRequestListener, final String filePath, final String index) {
        LogUtil.d(TAG, filePath);
        RxUtil.remove(subscription);
        subscription = Observable.just(filePath)
                .subscribeOn(Schedulers.computation())//计算线程
                .map(new Func1<String, String>() {
                    //压缩文件
                    @Override
                    public String call(String s) {
                        File fileDir = new File(Constants.IMAGE_DIR);
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        return ImageUtil.compressImage(s, Constants.IMAGE_DIR + ImageUtil.getPhotoFileName());//压缩路径
                    }
                })
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<BaseApiModel<String>>>() {
                    //上传压缩后的文件
                    @Override
                    public Observable<BaseApiModel<String>> call(String s) {
                        UploadFileService uploadFileService = ApiServiceGenerator.createReqeustService(UploadFileService.class, progressRequestListener);
                        File file = new File(s);
                        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
                        if (bodyBuilder ==  null){
                            bodyBuilder = new FormBody.Builder();
                            Gson gson = new Gson();
                            Map<String, String> requestMap = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
                            if (!requestMap.containsKey(Constants.KEY_TOKEN_ID)){
                                requestMap.put(Constants.KEY_TOKEN_ID,SPUtil.getString(Constants.KEY_TOKEN_ID));
                            }
                            String requestString = DesUtil.encrypt(gson.toJson(requestMap), "abcdefgh");
                            bodyBuilder.add("requestJSON",requestString);
                        }
                        return uploadFileService.uploadParamsWithFilePart(bodyBuilder.build(),body);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BaseApiModel<String>>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.d(TAG, "upload-file---success");
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.d("TAG","上传图片error:" + e.toString());
                        view.resetImageViewForFail(index);
                        if (view.isActive())
                            view.showErrorDialog(view.getActivityContext().getString(R.string.please_reload));
                    }

                    @Override
                    public void onNext(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode() .equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                            uploadResultId.put(String.valueOf(index), stringBaseApiModel.getResultObject().toString());
                            LogUtil.d(TAG, stringBaseApiModel.getResultObject().toString());
                            localFilePath.put(String.valueOf(index), filePath);
                            LogUtil.d(TAG, index + " ------  " + filePath);
                            view.filedUploadDone(index);
                        } else {
                            if (view.isActive())
                                view.showErrorDialog(stringBaseApiModel.getErrorDesc());
                        }
                    }
                });
        RxUtil.addSubscription(subscription);
    }

    @Override
    public void commitCertify(String name, String idNumber) {

        LogUtil.d(TAG, uploadResultId.size() + "已上传照片");
        if (uploadResultId.size() < 3) {
            view.showErrorDialog(view.getActivityContext().getString(R.string.please_commit_correct_photo));
        } else {

            DoIdentityService doIdentityService = ApiServiceGenerator.createService(DoIdentityService.class);


            Map<String, String> fieldMap = new HashMap<>();
            fieldMap.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
            //认证类型，个人或者企业 person、enter
            fieldMap.put("identityType", "person");
            fieldMap.put("userName", name);
            fieldMap.put("identityCardNo", idNumber);
            fieldMap.put("file1Id", uploadResultId.get("1"));
            fieldMap.put("file2Id", uploadResultId.get("2"));
            fieldMap.put("file3Id", uploadResultId.get("3"));
            RxUtil.remove(commitSubscription);
            commitSubscription = doIdentityService
                    .doIdentity(fieldMap)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<BaseApiModel<String>>() {
                        @Override
                        public void onCompleted() {
                            LogUtil.d(TAG, "commitSubscription--commit-authenticate---success");
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (view.isActive())
                                view.showErrorDialog(e.getMessage());
                        }

                        @Override
                        public void onNext(BaseApiModel<String> stringBaseApiModel) {

                            if (stringBaseApiModel.getErrorCode().equalsIgnoreCase( Constants.REQUEST_SUCCESS_S)) {
                                view.showCommitSuccessDialog();
                                RefreshOrderEvent.publish(true);
                            } else {
                                view.showErrorDialog(stringBaseApiModel.getErrorDesc());
                            }
                        }
                    });

            RxUtil.addSubscription(commitSubscription);
        }
    }

    @Override
    public void saveLocalData(String name, String idNumber, Map<String, LocalPhotoFilePathAndUploadId> idMap) {
        TempInfoInputForIdCertify tempInfoInputForIdCertify = new TempInfoInputForIdCertify();
        tempInfoInputForIdCertify.setInputTempName(name);
        tempInfoInputForIdCertify.setInputTempIdNo(idNumber);
        tempInfoInputForIdCertify.setIdMap(idMap);
        SPUtil.saveObjectToShare(Constants.KEY_PERSON_CERTIFY, tempInfoInputForIdCertify);
    }

    @Override
    public Map<String, String> getTempFileData() {
        return uploadResultId;
    }

    @Override
    public void setTempFileData(Map<String, String> var) {
        if (null != var && !var.isEmpty())
            uploadResultId.putAll(var);
    }

    @Override
    public Map<String, String> getLocalFileData() {
        return localFilePath;
    }

    @Override
    public void setLocalFileData(Map<String, String> var) {
        if (null != var && !var.isEmpty())
            localFilePath.putAll(var);
    }

    @Override
    public void loadIdentityDetails() {
        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetIdentityDetailsService getIdentityDetailsService = ApiServiceGenerator.createService(GetIdentityDetailsService.class);
        RxUtil.remove(detailsSubscription);
        detailsSubscription = getIdentityDetailsService.getIdenttityDetails(fieldMap)
                .subscribeOn(Schedulers.io())
                .delay(500, TimeUnit.MILLISECONDS)
                .map(new NetworkResultFunc1<DetailsIdent>())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<DetailsIdent>() {
                    @Override
                    public void call(DetailsIdent detailsIdent) {
                        if (view.isActive()) {

                            Map<String, String> map = new TreeMap<>();
                            if (!TextUtils.isEmpty(detailsIdent.getFile1())) {
                                map.put("1", detailsIdent.getFile1());
                            }
                            if (!TextUtils.isEmpty(detailsIdent.getFile2())) {
                                map.put("2", detailsIdent.getFile2());
                            }
                            if (!TextUtils.isEmpty(detailsIdent.getFile3())) {
                                map.put("3", detailsIdent.getFile3());
                            }
                            view.refreshUI(detailsIdent.getUserName()
                                    , detailsIdent.getIdentityCardNo(),
                                    map, "remote");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        LogUtil.d(TAG, throwable.toString());
                        view.showErrorDialog(throwable.getLocalizedMessage());
                    }
                });
    }
}
