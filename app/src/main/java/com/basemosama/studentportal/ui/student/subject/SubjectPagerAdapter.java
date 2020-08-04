package com.basemosama.studentportal.ui.student.subject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.basemosama.studentportal.model.student.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectPagerAdapter extends FragmentPagerAdapter {
    private List<SubjectFragment> fragmentList = new ArrayList<>();
    private List<Subject> subjectList = new ArrayList<>();

    public SubjectPagerAdapter(@NonNull FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return subjectList.get(position).getName();
    }

    @Override
    public int getCount() {
        return fragmentList != null ? fragmentList.size() : 0;
    }

    public void updateFragments(List<SubjectFragment> fragments, List<Subject> subjects) {
        fragmentList.clear();
        subjectList.clear();
        fragmentList.addAll(fragments);
        subjectList.addAll(subjects);
        notifyDataSetChanged();
    }
}
