package com.basemosama.studentportal.ui.login;

import android.app.Application;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.model.login.LoggedInUser;
import com.basemosama.studentportal.model.login.LoginInfo;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.LoginRepository;
import com.basemosama.studentportal.utility.Constants;

public class LoginViewModel extends AndroidViewModel {

    public MutableLiveData<String> username = new MutableLiveData<>();
    public MutableLiveData<String> password = new MutableLiveData<>();
    private MutableLiveData<String> type = new MutableLiveData<>(Constants.STUDENT_PATH);

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();

    private LiveData<Resource<LoggedInUser>> loginResultLiveData;

    private LoginRepository loginRepository;


    public LoginViewModel(@NonNull Application application) {
        super(application);
        loginRepository = LoginRepository.getInstance(application);
        loginResultLiveData = loginRepository.getLoginResult();
    }

    public void onLoginClicked(View view) {
        login();
    }

    public void setType(String typeValue) {
        type.setValue(typeValue);
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        return !username.trim().isEmpty();
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 6;
    }


    public void login() {
        LoginInfo loginInfo = new LoginInfo(username.getValue(), password.getValue(), type.getValue());
        loginDataChanged(loginInfo.getUsername(), loginInfo.getPassword());
        if (loginFormState.getValue().checkIfDataValid()) {
            loginRepository.login(loginInfo);
        }
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }


    public LiveData<Resource<LoggedInUser>> getLoginResult() {
        return loginResultLiveData;
    }


}


