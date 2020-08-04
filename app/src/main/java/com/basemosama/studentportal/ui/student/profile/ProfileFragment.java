package com.basemosama.studentportal.ui.student.profile;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basemosama.studentportal.databinding.FragmentProfileBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.User;

public class ProfileFragment extends Fragment {
    User user;
    private FragmentProfileBinding profileBinding;
    private Context context;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        return profileBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpUI();
    }

    private void setUpUI() {
        context = getContext();
        iMainActivity = (IMainActivity) getActivity();
        user = iMainActivity.getMyPreferenceManger().getCurrentUser();
        String description = getStudentDescription();
        profileBinding.setDescription(description);
        profileBinding.setUser(user);

    }

    private String getStudentDescription() {
        String description = "";
        if (user != null) {
            int gradeId = user.getGradeId();
            String grade = "";
            if (gradeId == 1) {
                grade = "st";
            } else if (gradeId == 2) {
                grade = "nd";
            } else if (gradeId > 2) {
                grade = "th";
            }
            String department = "Electronics and Communication";
            String faculty = "Engineering";

            description = gradeId + grade + " " + department + " " + faculty;
        }
        return description;
    }
}
