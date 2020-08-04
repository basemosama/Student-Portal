package com.basemosama.studentportal.model.network;

import android.text.TextUtils;

import com.basemosama.studentportal.utility.Constants;

import retrofit2.Response;

/*** A generic class that holds a result success w/ data or an error exception.*/
public class ApiResult<T> {
    public ApiResult<T> createResult(Throwable error) {
        return new Error(!error.getMessage().equals("") ? error.getMessage() : Constants.MAIN_ERROR_MESSAGE);
    }

    public ApiResult<T> createResult(Response<ApiResponse<T>> response) {
        ApiResponse<T> apiItemResponse = response.body();
        if (response.isSuccessful() && apiItemResponse != null) {
            if (apiItemResponse.getStatus() == 1 && apiItemResponse.getData() != null) {
                T item = apiItemResponse.getData();
                return new ApiResult.Success<>(item);
            } else {
                String error = apiItemResponse.getMessage();
                if (TextUtils.isEmpty(error)) {
                    error = Constants.MAIN_ERROR_MESSAGE;
                }
                return new ApiResult.Error(error);
            }
        } else {
            return new ApiResult.Error(Constants.MAIN_ERROR_MESSAGE);
        }
    }

    @Override
    public String toString() {
        if (this instanceof ApiResult.Success) {
            ApiResult.Success success = (ApiResult.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof ApiResult.Error) {
            ApiResult.Error error = (ApiResult.Error) this;
            return "Error[exception=" + error.getError() + "]";
        }
        return "";
    }

    // Success sub-class
    public final static class Success<T> extends ApiResult {
        private T data;

        public Success(T data) {
            this.data = data;
        }

        public T getData() {
            return this.data;
        }
    }

    public final static class Empty extends ApiResult {
    }

    // Error sub-class
    public final static class Error extends ApiResult {
        private String error;

        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return this.error;
        }
    }

}
