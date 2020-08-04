package com.basemosama.studentportal.ui.professor.assignment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorAssignmentBinding;
import com.basemosama.studentportal.interfaces.FabClickListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.ui.student.assignments.AssignmentsAdapter;
import com.basemosama.studentportal.utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class ProfessorAssignmentFragment extends Fragment implements AssignmentsAdapter.AssignmentClickListener, FabClickListener {
    private FragmentProfessorAssignmentBinding professorAssignmentBinding;
    private ProfessorAssignmentViewModel assignmentsViewModel;
    private Context context;
    private RecyclerView assignmentRV;
    private AssignmentsAdapter adapter;
    private IMainActivity iMainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        professorAssignmentBinding = FragmentProfessorAssignmentBinding.inflate(inflater, container, false);
        setupUI();
        return professorAssignmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpFAB();
        setUpViewModel();
        getAssignments();
    }

    private void setupUI() {
        context = getContext();
        assignmentRV = professorAssignmentBinding.professorAssignmentRv;
        adapter = new AssignmentsAdapter(context, new ArrayList<Assignment>(), this);
        assignmentRV.setAdapter(adapter);
    }

    private void setUpFAB() {
        iMainActivity = (IMainActivity) getActivity();
        iMainActivity.setFabClickListener(this);
        iMainActivity.showAddFab();
    }

    private void setUpViewModel() {
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        assignmentsViewModel = viewModelProvider.get(ProfessorAssignmentViewModel.class);
    }

    private void getAssignments() {
        assignmentsViewModel.getAssignments().observe(getViewLifecycleOwner(), new Observer<Resource<List<Assignment>>>() {
            @Override
            public void onChanged(Resource<List<Assignment>> listResource) {
                handleAssignmentResource(listResource);
            }
        });
    }

    private void handleAssignmentResource(Resource<List<Assignment>> listResource) {
        if (listResource == null || listResource.data == null)
            return;

        List<Assignment> assignments = listResource.data;
        switch (listResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                adapter.updateAdapter(assignments);
                String error = AppUtility.getError(context, assignments, listResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                adapter.updateAdapter(assignments);
                break;
        }
    }


    @Override
    public void onAssignmentClickListener(Assignment data) {
        String path = data.getPath();
        if (URLUtil.isValidUrl(path)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getPath()));
            startActivity(intent);
        } else {
            Toast.makeText(context, R.string.invalid_assignment_path_error_message, Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onFabClickListener(NavController navController) {
        navController.navigate(R.id.action_nav_assignments_to_add_assignment_fragment);
    }
}
