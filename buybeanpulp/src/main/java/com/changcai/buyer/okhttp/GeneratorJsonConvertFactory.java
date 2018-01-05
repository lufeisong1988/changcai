package com.changcai.buyer.okhttp;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by liuxingwei on 2017/2/27.
 */

public class GeneratorJsonConvertFactory extends Converter.Factory {

    private final Gson gson;

    private GeneratorJsonConvertFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
    }

    public static GeneratorJsonConvertFactory create() {
        return create(new Gson());
    }

    public static GeneratorJsonConvertFactory create(Gson gson) {
        return new GeneratorJsonConvertFactory(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new JsonResponseBodyConverter<>(gson, adapter);
    }


}
