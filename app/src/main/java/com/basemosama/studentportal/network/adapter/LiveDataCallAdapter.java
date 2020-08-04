package com.basemosama.studentportal.network.adapter;

import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.network.ApiResponse;
import com.basemosama.studentportal.model.network.ApiResult;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveDataCallAdapter<R> implements CallAdapter<ApiResponse<R>, LiveData<ApiResult<R>>> {
    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResult<R>> adapt(final Call<ApiResponse<R>> call) {
        final ApiResult<R> apiResponseResult = new ApiResult();
        return new LiveData<ApiResult<R>>() {
            @Override
            protected void onActive() {
                super.onActive();
                call.clone().enqueue(new Callback<ApiResponse<R>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<R>> call, Response<ApiResponse<R>> response) {
                        postValue(apiResponseResult.createResult(response));
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<R>> call, Throwable t) {
                        postValue(apiResponseResult.createResult(t));
                    }
                });
            }
        };
    }
}
