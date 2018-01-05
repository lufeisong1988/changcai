package com.changcai.buyer.business_logic.about_buy_beans.company_authenticate;

import android.content.Context;

import com.changcai.buyer.BasePresenter;
import com.changcai.buyer.BaseView;
import com.changcai.buyer.bean.LocalPhotoFilePathAndUploadId;
import com.changcai.buyer.okhttp.ProgressRequestListener;

import java.util.Map;

/**
 * Created by liuxingwei on 2016/11/26.
 */

public interface CompanyAuthenticateContract {

    interface View extends BaseView<Presenter> {


        Context getActivityContext();

        void showTakePictureDialog();

        boolean isActive();

        void showCommitSuccessDialog();

        void refreshUI(String name, String idNo,
                       String iv1Selected, String companyName,
                       String uniformCode, String type, Map<String, String> filePath);

        void resetImageViewForFailOrClear(String index);
        void filedUploadDone(String index);


    }

    interface Presenter extends BasePresenter {

        void uploadFile(final ProgressRequestListener progressRequestListener, String filePath, final String index);

        void commitCertify(String name, String idNumber, String codeType, String companyName, String companyCode);

        void saveLocalData(String name, String idNumber, String codeType, String companyName, String companyCode, Map<String, LocalPhotoFilePathAndUploadId> idMap);

        Map<String, String> getTempFileData();
        void  setTempFileData(Map<String,String> value);

        Map<String,String> getLocalFileData();
        void setLocalFileData(Map<String,String> value);




        void loadIdentityDetails();
    }
}
