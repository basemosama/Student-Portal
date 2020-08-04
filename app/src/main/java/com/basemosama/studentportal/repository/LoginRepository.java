package com.basemosama.studentportal.repository;


import android.app.Application;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.model.login.LoggedInUser;
import com.basemosama.studentportal.model.login.LoginInfo;
import com.basemosama.studentportal.model.network.ApiResponse;
import com.basemosama.studentportal.model.network.ApiResult;
import com.basemosama.studentportal.network.ServiceGenerator;
import com.basemosama.studentportal.network.client.StudentClient;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

public class LoginRepository {
    private static LoginRepository repository;
    private StudentClient client;
    private MediatorLiveData<Resource<LoggedInUser>> loginResultLiveData = new MediatorLiveData<>();
    private Context context;

    private LoginRepository(Context context) {
        this.context = context;
        client = ServiceGenerator.createService(StudentClient.class);
    }

    public static LoginRepository getInstance(Application application) {
        if (repository == null) {
            repository = new LoginRepository(application.getApplicationContext());
        }
        return repository;
    }

    public static void clear() {
        repository = null;
    }

    public void login(LoginInfo loginInfo) {
        loginResultLiveData.setValue(Resource.loading((LoggedInUser) null));
        final LiveData<ApiResult<ApiResponse<LoggedInUser>>> apiResultLiveData = client.login(loginInfo.getType(), loginInfo.getUsername(), loginInfo.getPassword());
        loginResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<LoggedInUser>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<LoggedInUser>> apiResult) {
                loginResultLiveData.removeSource(apiResultLiveData);

                if (apiResult instanceof ApiResult.Success) {
                    LoggedInUser user = (LoggedInUser) ((ApiResult.Success) apiResult).getData();
                    loginResultLiveData.setValue(Resource.success((user)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = Constants.MAIN_ERROR_MESSAGE;
                    if (!AppUtility.isConnectedToInternet(context)) {
                        error = context.getString(R.string.main_no_internet_error);
                    }
                    loginResultLiveData.setValue(Resource.error(error, (LoggedInUser) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    if (!AppUtility.isConnectedToInternet(context)) {
                        error = context.getString(R.string.main_no_internet_error);
                    }
                    loginResultLiveData.setValue(Resource.error(error, (LoggedInUser) null));
                }
            }
        });
    }

    public LiveData<Resource<LoggedInUser>> getLoginResult() {
        return loginResultLiveData;
    }


}
