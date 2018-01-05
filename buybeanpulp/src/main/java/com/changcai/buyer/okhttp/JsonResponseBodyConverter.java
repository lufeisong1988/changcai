package com.changcai.buyer.okhttp;

import android.text.TextUtils;

import com.changcai.buyer.bean.ResponseStatus;
import com.changcai.buyer.common.Constants;
import com.changcai.buyer.interface_api.ApiCodeErrorException;
import com.changcai.buyer.interface_api.ErrorCode;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import com.google.gson.stream.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;


/**
 * Created by liuxingwei on 2017/2/27.
 *
 * 自定义json解析器 解析服务器返回值统一判断
 */
public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final TypeAdapter<T> adapter;

    private static final Charset UTF8 = Charset.forName("UTF-8");

    public JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    /**
     *
     * @param value
     * @return
     * @throws IOException
     */
    @Override
    public T convert(ResponseBody value) throws IOException {
        if (value.contentLength() == 0) {
            throw new ApiCodeErrorException(ErrorCode.NET_ERROR);
        }
        String response = value.string();
        ResponseStatus responseStatus = gson.fromJson(response, ResponseStatus.class);
        //errorCode !=0 认为是异常
        if (!responseStatus.getErrorCode().contentEquals(Constants.REQUEST_SUCCESS_S)) {
            value.close();
            throw new ApiCodeErrorException(TextUtils.isEmpty(responseStatus.getErrorCode()) ? Constants.REQUEST_FAIL : responseStatus.getErrorCode(), TextUtils.isEmpty(responseStatus.getErrorDesc()) ? Constants.SERVICE_BUSY : responseStatus.getErrorDesc());
        }
        MediaType contentType = value.contentType();
        Charset charset = contentType != null ? contentType.charset(UTF8) : UTF8;
        InputStream inputStream = new ByteArrayInputStream(response.getBytes());
        Reader reader = new InputStreamReader(inputStream, charset);
        JsonReader jsonReader = gson.newJsonReader(reader);
        try {
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }
    }
}
