package com.changcai.buyer.business_logic.about_buy_beans.company_authenticate;

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
import com.changcai.buyer.rx.RefreshOrderEvent;
import com.changcai.buyer.util.DesUtil;
import com.changcai.buyer.util.ImageUtil;
import com.changcai.buyer.util.LogUtil;
import com.changcai.buyer.util.RxUtil;
import com.changcai.buyer.util.SPUtil;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

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

public class CompanyAuthenticatePresenter implements CompanyAuthenticateContract.Presenter {
    private String TAG = CompanyAuthenticatePresenter.class.getSimpleName();
    CompanyAuthenticateContract.View view;

    private static Subscription subscription;
    private static Subscription commitSubscription;
    private static Subscription detailsSubscription;
    private Map<String, String> uploadResultId;
    private Map<String, String> localFilePath;
    private TempInfoInputForIdCertify tempInfoInputForIdCertify;
    private FormBody.Builder bodyBuilder;

    public CompanyAuthenticatePresenter(CompanyAuthenticateContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }


    @Override
    public void start() {
        uploadResultId = new TreeMap<>();
        tempInfoInputForIdCertify = SPUtil.getObjectFromShare(Constants.KEY_LOCAL_INFO);
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
                .subscribeOn(Schedulers.computation())
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
                        if (bodyBuilder == null) {
                            bodyBuilder = new FormBody.Builder();
                            Gson gson = new Gson();
                            Map<String, String> requestMap = SPUtil.getObjectFromShare(Constants.REQUEST_BASE_PARAMETERS);
                            if (!requestMap.containsKey(Constants.KEY_TOKEN_ID)) {
                                requestMap.put(Constants.KEY_TOKEN_ID, SPUtil.getString(Constants.KEY_TOKEN_ID));
                            }
                            String requestString = DesUtil.encrypt(gson.toJson(requestMap), "abcdefgh");
                            bodyBuilder.add("requestJSON", requestString);
                        }
                        return uploadFileService.uploadParamsWithFilePart(bodyBuilder.build(), body);
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
                        view.resetImageViewForFailOrClear(index);
                        if (view.isActive())
                            view.showErrorDialog(view.getActivityContext().getString(R.string.please_reload));
                    }

                    @Override
                    public void onNext(BaseApiModel<String> stringBaseApiModel) {
                        if (stringBaseApiModel.getErrorCode().equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
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
//        UploadFileService uploadFileService = ApiServiceGenerator.createReqeustService(UploadFileService.class, progressRequestListener);
//        File file = new File(filePath);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), new ProgressRequestBody(requestFile, progressRequestListener));
//        subscription = uploadFileService
//                .upload(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<BaseApiModel<String>>() {
//                    @Override
//                    public void onCompleted() {
//                        LogUtil.d(TAG, "upload-file---success");
//                        view.filedUploadDone(index);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.resetImageViewForFailOrClear(index);
//                        if (view.isActive())
//                            view.showErrorDialog(view.getActivityContext().getString(R.string.please_reload));
//                    }
//
//                    @Override
//                    public void onNext(BaseApiModel<String> stringBaseApiModel) {
//                        LogUtil.d("uploadFile", stringBaseApiModel.getResultObject().toString());
//                        if (Integer.parseInt(stringBaseApiModel.getErrorCode()) == Constants.REQUEST_SUCCESS) {
//                            uploadResultId.put(String.valueOf(index), stringBaseApiModel.getResultObject().toString());
//                            localFilePath.put(String.valueOf(index), filePath);
//                        } else {
//                            if (view.isActive())
//                                view.showErrorDialog(stringBaseApiModel.getErrorDesc());
//                        }
//
//                    }
//                });
//        RxUtil.addSubscription(subscription);
    }

    @Override
    public void commitCertify(String name, String idNumber, String codeType, String companyName, String companyCode) {
        if (uploadResultId.isEmpty() && "uniformSocialCreditCode".equalsIgnoreCase(codeType)) {
            if (uploadResultId.size() < 3) {
                view.showErrorDialog(view.getActivityContext().getString(R.string.please_commit_correct_photo));
                return;
            }

        } else {
            if (uploadResultId.isEmpty() && uploadResultId.size() < 5) {
                view.showErrorDialog(view.getActivityContext().getString(R.string.please_commit_correct_photo));
                return;
            }
        }

        DoIdentityService doIdentityService = ApiServiceGenerator.createService(DoIdentityService.class);

        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        //认证类型，个人或者企业 person、enter
        fieldMap.put("identityType", "enter");
        fieldMap.put("userName", name);
        fieldMap.put("identityCardNo", idNumber);
        fieldMap.put("file1Id", uploadResultId.get("1"));
        fieldMap.put("file2Id", uploadResultId.get("2"));
        fieldMap.put("enterName", companyName);
        if ("zzjgdm".equalsIgnoreCase(codeType)) {
            fieldMap.put("file4Id", uploadResultId.get("3"));
            fieldMap.put("file5Id", uploadResultId.get("4"));
            fieldMap.put("file6Id", uploadResultId.get("5"));
            fieldMap.put("zzjgdm", companyCode);
        } else {
            fieldMap.put("uniformSocialCreditCode", companyCode);
            fieldMap.put("file4Id", uploadResultId.get("3"));
        }

        commitSubscription = doIdentityService
                .doIdentity(fieldMap)
                .map(new NetworkResultFunc1<String>())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        RefreshOrderEvent.publish(true);
                        if (view.isActive()) {
                            view.showCommitSuccessDialog();
                        }


                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (view.isActive())
                            view.showErrorDialog(throwable.getLocalizedMessage());
                    }
                });

        RxUtil.addSubscription(commitSubscription);

    }

    @Override
    public void saveLocalData(String name, String idNumber, String codeType, String companyName, String companyCode, Map<String, LocalPhotoFilePathAndUploadId> idMap) {
        TempInfoInputForIdCertify tempInfoInputForIdCertify = new TempInfoInputForIdCertify();
        tempInfoInputForIdCertify.setInputTempName(name);
        tempInfoInputForIdCertify.setInputTempIdNo(idNumber);
        tempInfoInputForIdCertify.setCompanyName(companyName);
        tempInfoInputForIdCertify.setCode(companyCode);
        tempInfoInputForIdCertify.setIv1Select(codeType);
        tempInfoInputForIdCertify.setIdMap(idMap);
        SPUtil.saveObjectToShare(Constants.KEY_LOCAL_INFO, tempInfoInputForIdCertify);
    }


    /**
     * 网络图片ID
     *
     * @return
     */
    @Override
    public Map<String, String> getTempFileData() {
        return uploadResultId;
    }

    @Override
    public void setTempFileData(Map<String, String> value)

    {
        if (null != value && !value.isEmpty())
            uploadResultId = value;
    }

    /**
     * 本地文件路径
     *
     * @return
     */
    @Override
    public Map<String, String> getLocalFileData() {
        return localFilePath;
    }

    @Override
    public void setLocalFileData(Map<String, String> value) {
        if (null != value && !value.isEmpty())
            localFilePath = value;
    }

    @Override
    public void loadIdentityDetails() {

        Map<String, String> fieldMap = new HashMap<>();
        fieldMap.put("tokenId", SPUtil.getString(Constants.KEY_TOKEN_ID));
        GetIdentityDetailsService getIdentityDetailsService = ApiServiceGenerator.createService(GetIdentityDetailsService.class);
        detailsSubscription = getIdentityDetailsService.getIdenttityDetails(fieldMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseApiModel<DetailsIdent>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BaseApiModel<DetailsIdent> detailsIdentBaseApiModel) {
                        if (view.isActive()) {
                            if (detailsIdentBaseApiModel.getErrorCode().equalsIgnoreCase(Constants.REQUEST_SUCCESS_S)) {
                                DetailsIdent detailsIdent = detailsIdentBaseApiModel.getResultObject();

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
                                if (!TextUtils.isEmpty(detailsIdent.getFile4())) {
                                    map.put("4", detailsIdent.getFile4());
                                }
                                if (!TextUtils.isEmpty(detailsIdent.getFile5())) {
                                    map.put("5", detailsIdent.getFile5());
                                }
                                if (!TextUtils.isEmpty(detailsIdent.getFile6())) {
                                    map.put("6", detailsIdent.getFile6());
                                }
                                view.refreshUI(detailsIdent.getUserName()
                                        , detailsIdent.getIdentityCardNo()
                                        , TextUtils.isEmpty(detailsIdent.getUniformSocialCreditCode()) ? "false" : "true"
                                        , detailsIdent.getEnterName()
                                        , TextUtils.isEmpty(detailsIdent.getUniformSocialCreditCode()) ? detailsIdent.getZzjgdm() : detailsIdent.getUniformSocialCreditCode(),
                                        "remote", map);
                            } else {
                                view.showErrorDialog(view.getActivityContext().getString(R.string.error));
                            }
                        }
                    }
                });
    }


}
