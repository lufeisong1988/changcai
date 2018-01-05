package com.changcai.buyer.interface_api;

import com.changcai.buyer.common.Urls;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by liuxingwei on 2016/11/28.
 *
 * restful api
 */

public interface UploadFileService {

    @Multipart
    @POST(Urls.UPLOAD_FILE)
    Observable<BaseApiModel<String>> upload(@Part MultipartBody.Part file);



    @Multipart
    @POST(Urls.UPLOAD_FILE)
    Observable<BaseApiModel<String>> uploadParamsWithFilePart(@Part("body") RequestBody body, @Part MultipartBody.Part file);
}
