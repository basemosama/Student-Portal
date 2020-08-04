package com.basemosama.studentportal.ui.student.subject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.viewpager.widget.ViewPager;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentSubjectContainerBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.google.android.material.tabs.TabLayout;
import com.h6ah4i.android.tablayouthelper.TabLayoutHelper;

import java.util.ArrayList;
import java.util.List;


public class SubjectContainerFragment extends Fragment {
    private FragmentSubjectContainerBinding subjectContainerBinding;
    private SubjectViewModel subjectViewModel;
    private SubjectPagerAdapter subjectPagerAdapter;
    private Context context;
    private TabLayout tabLayout;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        subjectContainerBinding = FragmentSubjectContainerBinding.inflate(inflater, container, false);
        setUpUI();
        return subjectContainerBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getSubjects();
    }

    private void setUpUI() {
        context = getContext();
        tabLayout = subjectContainerBinding.tabs;
        subjectPagerAdapter = new SubjectPagerAdapter(getChildFragmentManager());
        ViewPager viewPager = subjectContainerBinding.viewPager;
        viewPager.setAdapter(subjectPagerAdapter);
        TabLayoutHelper tabLayoutHelper = new TabLayoutHelper(tabLayout, viewPager);
        tabLayoutHelper.setAutoAdjustTabModeEnabled(true);

    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);
        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));
        subjectViewModel = viewModelProvider.get(SubjectViewModel.class);
    }

    private void getSubjects() {
        subjectViewModel.getSubjects().observe(getViewLifecycleOwner(), new Observer<Resource<List<Subject>>>() {
            @Override
            public void onChanged(Resource<List<Subject>> listResource) {
                if (listResource == null || listResource.data == null)
                    return;

                List<Subject> subjects = listResource.data;
                switch (listResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        updateTabs(subjects);
                        String error = AppUtility.getError(context, subjects, listResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        updateTabs(subjects);
                        break;
                }

            }
        });
    }


    private void updateTabs(List<Subject> subjects) {
        List<SubjectFragment> subjectFragmentList = new ArrayList<>();
        for (Subject subject : subjects) {
            subjectFragmentList.add(
                    SubjectFragment.newInstance(
                            subject.getName(), subject.getId()));
        }
        subjectPagerAdapter.updateFragments(subjectFragmentList, subjects);
    }
}


