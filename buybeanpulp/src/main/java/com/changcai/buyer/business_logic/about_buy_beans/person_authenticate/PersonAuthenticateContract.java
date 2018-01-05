package com.changcai.buyer.business_logic.about_buy_beans.person_authenticate;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.LocalPhotoFilePathAndUploadId;
import com.changcai.buyer.okhttp.ProgressRequestBody;
import com.changcai.buyer.okhttp.ProgressRequestListener;

import java.util.Map;

/**
 * Created by liuxingwei on 2016/11/26.
 */

public interface PersonAuthenticateContract {

    interface View extends BaseView<Presenter> {
        void showTakePictureDialog();
        boolean isActive();
        void showCommitSuccessDialog();
        Context getActivityContext();
        void refreshUI(String name,String idNumber,Map<String,String> fileMap,String fromLocal);
        void resetImageViewForFail(String index);
        void filedUploadDone(String index);

    }

    interface Presenter extends BasePresenter {


        void uploadFile(ProgressRequestListener progressRequestListener,String filePath,String index);

        void commitCertify(String name,String idNumber);

        void saveLocalData(String name, String idNumber,Map<String,LocalPhotoFilePathAndUploadId> idMap);

        Map<String, String> getTempFileData();
        void setTempFileData(Map<String,String> var);
        Map<String,String> getLocalFileData();
       void   setLocalFileData(Map<String,String> var);

        void loadIdentityDetails();
    }
}
