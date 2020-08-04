package com.basemosama.studentportal.ui.student.result;

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
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentResultBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Grades;
import com.basemosama.studentportal.model.student.StudentResult;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class ResultFragment extends Fragment {
    private FragmentResultBinding resultBinding;
    private ResultAdapter adapter;
    private ResultViewModel resultViewModel;
    private Context context;
    private IMainActivity iMainActivity;
    private User user;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resultBinding = FragmentResultBinding.inflate(inflater, container, false);
        setUpUI();
        return resultBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getResult();
    }

    private void setUpUI() {
        context = getContext();
        recyclerView = resultBinding.resultRv;
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        resultViewModel = viewModelProvider.get(ResultViewModel.class);
        user = iMainActivity.getMyPreferenceManger().getCurrentUser();
        adapter = new ResultAdapter(getContext(), new ArrayList<Grades>(), user.getGradeId(), user.getDepartmentId());
        recyclerView.setAdapter(adapter);
    }

    private void getResult() {
        resultViewModel.getResults().observe(getViewLifecycleOwner(), new Observer<Resource<StudentResult>>() {
            @Override
            public void onChanged(Resource<StudentResult> studentResultResource) {
                if (studentResultResource == null || studentResultResource.data == null)
                    return;

                List<Grades> grades = studentResultResource.data.getGrades();
                switch (studentResultResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        adapter.updateAdapter(grades);
                        String error = AppUtility.getError(context, grades, studentResultResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        adapter.updateAdapter(grades);
                        break;
                }

            }
        });
    }


}
