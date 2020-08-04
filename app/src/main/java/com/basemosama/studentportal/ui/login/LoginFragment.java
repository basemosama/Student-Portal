package com.basemosama.studentportal.ui.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentLoginBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.login.LoggedInUser;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.MyPreferenceManger;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding loginBinding;
    private LoginViewModel loginViewModel;
    private Context context;
    private IMainActivity iMainActivity;
    private MyPreferenceManger myPreferenceManger;

    public LoginFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        return loginBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        setUpUI();
        getLoginResult();
    }

    private void setUpUI() {
        context = getContext();
        myPreferenceManger = new MyPreferenceManger(context);
        setUpLoginFields();
    }

    private void setUpLoginFields() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                loginBinding.contentLayout.passwordTil.setEndIconVisible(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        loginBinding.contentLayout.passwordEditText.addTextChangedListener(textWatcher);
        loginBinding.contentLayout.passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login();
                }
                return false;
            }
        });

        loginViewModel.getLoginFormState().observe(getViewLifecycleOwner(), new Observer<LoginFormState>() {
            @Override
            public void onChanged(LoginFormState loginFormState) {
                if (loginFormState.getUsernameError() != null) {
                    loginBinding.contentLayout.usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    loginBinding.contentLayout.passwordEditText.setError(getString(loginFormState.getPasswordError()));
                    loginBinding.contentLayout.passwordTil.setEndIconVisible(false);
                }
            }
        });

    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();

        NavController navController = NavHostFragment.findNavController(this);
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.login_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));
        loginViewModel = viewModelProvider.get(LoginViewModel.class);
        loginBinding.setLoginViewModel(loginViewModel);
        loginBinding.setLifecycleOwner(getViewLifecycleOwner());

    }

    private void getLoginResult() {
        loginViewModel.getLoginResult().observe(getViewLifecycleOwner(), new Observer<Resource<LoggedInUser>>() {
            @Override
            public void onChanged(Resource<LoggedInUser> loggedInUserResource) {
                handleLoginResource(loggedInUserResource);
            }
        });

    }

    private void handleLoginResource(Resource<LoggedInUser> loggedInUserResource) {
        if (loggedInUserResource == null)
            return;

        switch (loggedInUserResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                String error = loggedInUserResource.message;
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                LoggedInUser user = loggedInUserResource.data;
                saveTokenAndNavigateToMainActivity(user);
                break;
        }
    }

    private void saveTokenAndNavigateToMainActivity(LoggedInUser user) {
        if (user != null && !TextUtils.isEmpty(user.getApi_token())) {
            boolean isStudent = true;
            if (user.getStudent() != null && user.getProfessor() == null) {
                isStudent = true;
                myPreferenceManger.saveStudent(user);
            } else if (user.getProfessor() != null && user.getStudent() == null) {
                isStudent = false;
                myPreferenceManger.saveProfessor(user);
            }
            if (isStudent) {
                Navigation.findNavController(loginBinding.getRoot()).navigate(R.id.action_login_fragment_to_student_navigation);
            } else {
                Navigation.findNavController(loginBinding.getRoot()).navigate(R.id.action_login_fragment_to_professor_navigation);
            }

        }
    }

}



