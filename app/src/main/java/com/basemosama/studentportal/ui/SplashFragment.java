package com.basemosama.studentportal.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentSplashBinding;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.utility.MyPreferenceManger;

public class SplashFragment extends Fragment {
    private MyPreferenceManger myPreferenceManger;
    private User user;

    public SplashFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        com.basemosama.studentportal.databinding.FragmentSplashBinding splashBinding = FragmentSplashBinding.inflate(inflater, container, false);
        return splashBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View splashView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(splashView, savedInstanceState);
        Context context = getContext();
        myPreferenceManger = new MyPreferenceManger(context);

        boolean isStudent = myPreferenceManger.isCurrentUserStudent();
        String apiKey = myPreferenceManger.getApiKey();
        user = myPreferenceManger.getCurrentUser();
        if (user != null && !TextUtils.isEmpty(apiKey)) {
            if (isStudent) {
                Navigation.findNavController(splashView).navigate(R.id.action_splash_fragment_to_student_navigation);
            } else {
                Navigation.findNavController(splashView).navigate(R.id.action_splash_fragment_to_professor_navigation);
            }
        } else {
            Navigation.findNavController(splashView).navigate(R.id.action_splash_fragment_to_loginFragment);
        }
    }
}

