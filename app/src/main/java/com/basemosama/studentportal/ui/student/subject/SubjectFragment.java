package com.basemosama.studentportal.ui.student.subject;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentSubjectBinding;
import com.basemosama.studentportal.ui.student.assignments.AssignmentsFragment;
import com.basemosama.studentportal.ui.student.marks.MarksFragment;
import com.basemosama.studentportal.ui.student.source.SourceFragment;
import com.basemosama.studentportal.utility.Constants;


public class SubjectFragment extends Fragment {
    private FragmentManager fragmentManager;
    private FragmentSubjectBinding subjectBinding;
    private FragmentContainerView assignmentFragmentContainerView, sourceFragmentContainerView, marksFragmentContainerView;
    private LinearLayout assignmentLayout, sourceLayout, marksLayout;
    private ImageView assignmentArrow, sourceArrow, marksArrow;
    private int subjectId;

    public static SubjectFragment newInstance(String subjectName, int subjectId) {
        SubjectFragment subjectFragment = new SubjectFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.SUBJECT_NAME_KEY, subjectName);
        bundle.putInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, subjectId);
        subjectFragment.setArguments(bundle);
        return subjectFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        subjectBinding = FragmentSubjectBinding.inflate(inflater, container, false);
        setUpUI();
        setUpFragmentsVisibility();
        setUpFragments();
        return subjectBinding.getRoot();
    }


    private void setUpUI() {
        assignmentLayout = subjectBinding.subjectAssignmentLayout;
        sourceLayout = subjectBinding.subjectSourceLayout;
        marksLayout = subjectBinding.subjectMarksLayout;

        assignmentFragmentContainerView = subjectBinding.subjectAssignmentFragment;
        sourceFragmentContainerView = subjectBinding.subjectSourceFragment;
        marksFragmentContainerView = subjectBinding.subjectMarksFragment;

        assignmentArrow = subjectBinding.assignmentArrow;
        sourceArrow = subjectBinding.sourcesArrow;
        marksArrow = subjectBinding.marksArrow;

    }

    private AssignmentsFragment getAssignmentFragment(int type) {
        AssignmentsFragment assignmentsFragment = new AssignmentsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ASSIGNMENT_TYPE_KEY, type);
        bundle.putInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, subjectId);
        assignmentsFragment.setArguments(bundle);
        return assignmentsFragment;
    }

    private SourceFragment getSourceFragment() {
        SourceFragment sourceFragment = new SourceFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, subjectId);
        sourceFragment.setArguments(bundle);
        return sourceFragment;
    }

    private MarksFragment getMarksFragment() {
        MarksFragment marksFragment = new MarksFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, subjectId);
        marksFragment.setArguments(bundle);
        return marksFragment;
    }

    private void setUpFragments() {
        fragmentManager = getChildFragmentManager();

        Bundle bundle = getArguments();
        if (bundle != null) {
            subjectId = bundle.getInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, 1);
        }
        AssignmentsFragment assignmentsFragment = getAssignmentFragment(1);
        SourceFragment sourcesFragment = getSourceFragment();
        MarksFragment marksFragment = getMarksFragment();
        fragmentManager.beginTransaction()
                .add(R.id.subject_assignment_fragment, assignmentsFragment)
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.subject_source_fragment, sourcesFragment)
                .commit();
        fragmentManager.beginTransaction()
                .add(R.id.subject_marks_fragment, marksFragment)
                .commit();
    }

    private void setUpFragmentsVisibility() {
        assignmentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = String.valueOf(assignmentArrow.getTag());
                if (TextUtils.equals(tag, Constants.CLOSED_ARROW_TAG)) {
                    assignmentFragmentContainerView.setVisibility(View.VISIBLE);
                    assignmentArrow.setImageResource(R.drawable.arrow_up);
                    assignmentArrow.setTag(Constants.OPENED_ARROW_TAG);
                } else {
                    assignmentFragmentContainerView.setVisibility(View.GONE);
                    assignmentArrow.setImageResource(R.drawable.arrow_down);
                    assignmentArrow.setTag(Constants.CLOSED_ARROW_TAG);
                }
            }
        });
        sourceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = String.valueOf(sourceArrow.getTag());
                if (TextUtils.equals(tag, Constants.CLOSED_ARROW_TAG)) {
                    sourceFragmentContainerView.setVisibility(View.VISIBLE);
                    sourceArrow.setImageResource(R.drawable.arrow_up);
                    sourceArrow.setTag(Constants.OPENED_ARROW_TAG);
                } else {
                    sourceFragmentContainerView.setVisibility(View.GONE);
                    sourceArrow.setImageResource(R.drawable.arrow_down);
                    sourceArrow.setTag(Constants.CLOSED_ARROW_TAG);
                }
            }
        });

        marksLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tag = String.valueOf(marksArrow.getTag());
                if (TextUtils.equals(tag, Constants.CLOSED_ARROW_TAG)) {
                    marksFragmentContainerView.setVisibility(View.VISIBLE);
                    marksArrow.setImageResource(R.drawable.arrow_up);
                    marksArrow.setTag(Constants.OPENED_ARROW_TAG);
                } else {
                    marksFragmentContainerView.setVisibility(View.GONE);
                    marksArrow.setImageResource(R.drawable.arrow_down);
                    marksArrow.setTag(Constants.CLOSED_ARROW_TAG);
                }
            }
        });
    }

}
