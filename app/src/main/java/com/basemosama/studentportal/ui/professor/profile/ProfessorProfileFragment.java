package com.basemosama.studentportal.ui.professor.profile;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorProfileBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.OfficeHour;
import com.basemosama.studentportal.model.professor.ProfessorProfileData;
import com.basemosama.studentportal.model.professor.ProfessorProject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class ProfessorProfileFragment extends Fragment {
    private FragmentProfessorProfileBinding profileBinding;
    private ProfessorProfileViewModel viewModel;
    private Context context;
    private ProjectAdapter projectAdapter;
    private OfficeHourAdapter officeHourAdapter;
    private IMainActivity iMainActivity;
    private RecyclerView officeHoursRv;
    private RecyclerView projectsRv;
    private ImageView nextArrow, previousArrow;
    private LinearLayoutManager layoutManager;

    public ProfessorProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileBinding = FragmentProfessorProfileBinding.inflate(inflater, container, false);
        setUpUI();
        return profileBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getProfileData();
    }

    private void setUpUI() {
        context = getContext();
        officeHoursRv = profileBinding.profileOfficeHoursRv;
        projectsRv = profileBinding.profileProjectsRv;
        nextArrow = profileBinding.arrowNext;
        previousArrow = profileBinding.arrowPrevious;
        officeHourAdapter = new OfficeHourAdapter(context, new ArrayList<OfficeHour>());
        projectAdapter = new ProjectAdapter(context, new ArrayList<ProfessorProject>());
        layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        projectsRv.setLayoutManager(layoutManager);
        projectsRv.setAdapter(projectAdapter);
        officeHoursRv.setAdapter(officeHourAdapter);
        profileBinding.arrowNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition() + 1;
                if (lastVisible <= projectAdapter.getItemCount()) {
                    layoutManager.scrollToPosition(lastVisible);
                    previousArrow.setVisibility(View.VISIBLE);
                }
                if (lastVisible < projectAdapter.getItemCount() - 1) {
                    nextArrow.setVisibility(View.VISIBLE);
                } else {
                    nextArrow.setVisibility(View.GONE);
                }
            }
        });
        profileBinding.arrowPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition() - 1;
                if (firstVisible >= 0) {
                    layoutManager.scrollToPosition(firstVisible);
                    nextArrow.setVisibility(View.VISIBLE);
                }
                if (firstVisible <= projectAdapter.getItemCount()) {
                    previousArrow.setVisibility(View.GONE);
                } else {
                    previousArrow.setVisibility(View.VISIBLE);
                }

            }
        });
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        viewModel = viewModelProvider.get(ProfessorProfileViewModel.class);

        viewModel = new ViewModelProvider(requireActivity())
                .get(ProfessorProfileViewModel.class);
    }

    private void getProfileData() {
        viewModel.getProfileData().observe(getViewLifecycleOwner(), new Observer<Resource<ProfessorProfileData>>() {
            @Override
            public void onChanged(Resource<ProfessorProfileData> professorProfileDataResource) {
                handleProfileResource(professorProfileDataResource);
            }
        });

    }

    private void setUpProfile(ProfessorProfileData profileData) {
        List<ProfessorProject> projects = profileData.getProjects();
        profileBinding.setProfessor(profileData);
        profileBinding.setDescription(getProfessorDescription(profileData));
        officeHourAdapter.updateAdapter(profileData.getOfficeHours());
        projectAdapter.updateAdapter(projects);
        final int lastVisible = layoutManager.findLastCompletelyVisibleItemPosition() + 1;
        if (lastVisible >= projectAdapter.getItemCount()) {
            nextArrow.setVisibility(View.GONE);
        }

    }

    private void handleProfileResource(Resource<ProfessorProfileData> profileResource) {

        if (profileResource == null || profileResource.data == null)
            return;

        ProfessorProfileData profileData = profileResource.data;

        switch (profileResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                setUpProfile(profileData);
                String error = AppUtility.getError(context, profileData, profileResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                setUpProfile(profileData);
                break;
        }
    }

    private String getProfessorDescription(ProfessorProfileData ProfileData) {
        String description = "";
        if (ProfileData != null) {
            String prefix = getString(R.string.professor_at_prefix);
            String faculty = "Engineering";
            description = prefix + " " + ProfileData.getDepartment().getName() + " " + faculty;
        }
        return description;
    }


}
