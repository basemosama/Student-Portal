package com.basemosama.studentportal.ui.professor.table;

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

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorTableBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class ProfessorTableFragment extends Fragment implements DrawerListener {
    private Context context;
    private FragmentProfessorTableBinding tableBinding;
    private ProfessorTableViewModel tableViewModel;
    private IMainActivity iMainActivity;
    private PowerSpinnerView gradeSpinner;


    public ProfessorTableFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tableBinding = FragmentProfessorTableBinding.inflate(inflater, container, false);
        setUpUI();
        return tableBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getGrades();
        getTable();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (gradeSpinner.isShowing())
            gradeSpinner.dismiss();
    }

    private void setUpUI() {
        context = getContext();
        gradeSpinner = tableBinding.tableGradeSpinner;

    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        iMainActivity.setDrawerListener(this);
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        tableViewModel = viewModelProvider.get(ProfessorTableViewModel.class);
    }

    private void getTable() {
        tableViewModel.getTable().observe(getViewLifecycleOwner(), new Observer<Resource<Table>>() {
            @Override
            public void onChanged(Resource<Table> tableResource) {
                handleTableResource(tableResource);
            }
        });
    }

    private void getGrades() {
        tableViewModel.getGrades().observe(getViewLifecycleOwner(), new Observer<Resource<List<ProfessorGrade>>>() {
            @Override
            public void onChanged(Resource<List<ProfessorGrade>> assignmentResource) {
                handleGradeResource(assignmentResource);
            }
        });
    }

    private void handleTableResource(Resource<Table> tableResource) {

        if (tableResource == null || tableResource.data == null)
            return;

        tableBinding.professorChooseGradeTextView.setVisibility(View.GONE);
        Table table = tableResource.data;
        switch (tableResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                if (table != null) {
                    tableBinding.professorChooseGradeTextView.setVisibility(View.GONE);
                    tableBinding.setTable(table);
                } else {
                    tableBinding.professorChooseGradeTextView.setVisibility(View.VISIBLE);
                }
                String error = AppUtility.getError(context, table, tableResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                if (table != null) {
                    tableBinding.professorChooseGradeTextView.setVisibility(View.GONE);
                    tableBinding.setTable(table);
                } else {
                    tableBinding.professorChooseGradeTextView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void handleGradeResource(Resource<List<ProfessorGrade>> gradeResource) {
        if (gradeResource == null || gradeResource.data == null)
            return;
        final List<ProfessorGrade> grades = gradeResource.data;
        List<String> gradesNames = new ArrayList<>();
        for (ProfessorGrade grade : grades) {
            gradesNames.add(grade.getName());
        }
        Integer gradePosition = tableViewModel.getGradePosition();

        switch (gradeResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                gradeSpinner.setItems(gradesNames);
                if (gradePosition != null) {
                    gradeSpinner.selectItemByIndex(gradePosition);
                }
                gradeSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String name) {
                        Integer gradeIdInViewModel = tableViewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            tableViewModel.setGradeId(gradeId);
                            tableViewModel.setGradePosition(position);
                        }
                    }
                });
                String error = AppUtility.getError(context, grades, gradeResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                gradeSpinner.setItems(gradesNames);
                if (gradePosition != null) {
                    gradeSpinner.selectItemByIndex(gradePosition);
                }
                gradeSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String name) {
                        Integer gradeIdInViewModel = tableViewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            tableViewModel.setGradeId(gradeId);
                            tableViewModel.setGradePosition(position);
                        }
                    }
                });
                break;
        }

    }

    @Override
    public void onDrawerStateChanged() {
        if (gradeSpinner.isShowing())
            gradeSpinner.dismiss();
    }
}
