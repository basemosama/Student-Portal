package com.basemosama.studentportal.network.adapter;

import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.network.ApiResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class LiveDataCallAdapterFactory extends CallAdapter.Factory {

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {

        if (CallAdapter.Factory.getRawType(returnType) != LiveData.class)
            return null;

        Type observableType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) returnType);
        Type rawObservableType = CallAdapter.Factory.getRawType(observableType);

        if (rawObservableType != ApiResult.class)
            throw new IllegalArgumentException("Type Must Be ApiResult");

        if (!(observableType instanceof ParameterizedType))
            throw new IllegalArgumentException("Resource Must Be Parameterized");


        Type bodyType = CallAdapter.Factory.getParameterUpperBound(0, (ParameterizedType) observableType);
        return new LiveDataCallAdapter<Type>(bodyType);
    }
}
