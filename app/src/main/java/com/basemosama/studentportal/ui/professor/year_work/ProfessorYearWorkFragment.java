package com.basemosama.studentportal.ui.professor.year_work;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorYearWorkBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;


public class ProfessorYearWorkFragment extends Fragment implements StudentNamesAdapter.StudentClickListener, DrawerListener {
    private FragmentProfessorYearWorkBinding yearWorkBinding;
    private StudentNamesAdapter adapter;
    private Context context;
    private ProfessorYearWorkViewModel viewModel;
    private PowerSpinnerView departmentSpinner, gradeSpinner;
    private IMainActivity iMainActivity;

    public ProfessorYearWorkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        yearWorkBinding = FragmentProfessorYearWorkBinding.inflate(inflater, container, false);
        setUpUI();
        return yearWorkBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getDepartments();
        getGrades();
        getStudents();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeSpinners();
    }

    private void setUpUI() {
        context = getContext();
        RecyclerView studentNamesRv = yearWorkBinding.professorStudentNamesRv;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new StudentNamesAdapter(context, new ArrayList<User>(), this);
        studentNamesRv.setLayoutManager(linearLayoutManager);
        studentNamesRv.setAdapter(adapter);
        departmentSpinner = yearWorkBinding.yearWorkDepartmentSpinner;
        gradeSpinner = yearWorkBinding.yearWorkGradeSpinner;
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

        viewModel = viewModelProvider.get(ProfessorYearWorkViewModel.class);
        yearWorkBinding.setViewModel(viewModel);
        yearWorkBinding.setLifecycleOwner(getViewLifecycleOwner());
        departmentSpinner.setLifecycleOwner(getViewLifecycleOwner());
        gradeSpinner.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void getDepartments() {
        viewModel.getDepartments().observe(getViewLifecycleOwner(), new Observer<Resource<List<Department>>>() {
            @Override
            public void onChanged(Resource<List<Department>> listResource) {
                handleDepartmentResource(listResource);
            }
        });
    }

    private void getGrades() {
        viewModel.getGrades().observe(getViewLifecycleOwner(), new Observer<Resource<List<ProfessorGrade>>>() {
            @Override
            public void onChanged(Resource<List<ProfessorGrade>> listResource) {
                handleGradeResource(listResource);
            }
        });
    }

    private void getStudents() {
        viewModel.getStudents().observe(getViewLifecycleOwner(), new Observer<Resource<List<User>>>() {
            @Override
            public void onChanged(Resource<List<User>> listResource) {
                handleStudentResource(listResource);
            }
        });
    }

    @Override
    public void OnStudentClickListener(User student, View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.STUDENT_MARKS_BUNDLE_KEY, student);
        Navigation.findNavController(view).navigate(R.id.action_nav_result_to_add_marks_fragment, bundle);
    }


    /**
     * Helper Method to handle grade and department result
     * and setup grade and department spinners
     **/
    //Helper Method to handle grade and department result
    private void handleGradeResource(Resource<List<ProfessorGrade>> gradeResource) {
        if (gradeResource == null || gradeResource.data == null)
            return;
        final List<ProfessorGrade> grades = gradeResource.data;
        List<String> gradesNames = new ArrayList<>();
        for (ProfessorGrade grade : grades) {
            gradesNames.add(grade.getName());
        }
        Integer gradePosition = viewModel.getGradePosition();
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
                        Integer gradeIdInViewModel = viewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            viewModel.setGradeId(gradeId);
                            viewModel.setGradePosition(position);
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
                        Integer gradeIdInViewModel = viewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            viewModel.setGradeId(gradeId);
                            viewModel.setGradePosition(position);
                        }
                    }
                });

                break;
        }
    }

    private void handleDepartmentResource(Resource<List<Department>> departmentResource) {
        if (departmentResource == null || departmentResource.data == null)
            return;
        final List<Department> departments = departmentResource.data;
        List<String> departmentNames = new ArrayList<>();
        for (Department department : departments) {
            departmentNames.add(department.getName());
        }
        Integer departmentPosition = viewModel.getDepartmentId();
        switch (departmentResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                departmentSpinner.setItems(departmentNames);
                if (departmentPosition != null) {
                    departmentSpinner.selectItemByIndex(departmentPosition);

                }
                departmentSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String name) {
                        int departmentId = departments.get(position).getId();
                        Integer departmentIdInViewModel = viewModel.getDepartmentId();
                        if (departmentIdInViewModel == null || departmentIdInViewModel != departmentId) {
                            viewModel.setDepartmentId(departmentId);
                            viewModel.setDepartmentPosition(position);
                        }
                    }
                });
                String error = AppUtility.getError(context, departments, departmentResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                departmentSpinner.setItems(departmentNames);
                if (departmentPosition != null) {
                    departmentSpinner.selectItemByIndex(departmentPosition);
                }
                departmentSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String name) {
                        int departmentId = departments.get(position).getId();
                        Integer departmentIdInViewModel = viewModel.getDepartmentId();
                        if (departmentIdInViewModel == null || departmentIdInViewModel != departmentId) {
                            viewModel.setDepartmentId(departmentId);
                            viewModel.setDepartmentPosition(position);
                        }
                    }
                });
                break;
        }
    }

    private void handleStudentResource(Resource<List<User>> listResource) {
        if (listResource == null || listResource.data == null)
            return;
        List<User> students = listResource.data;
        viewModel.isStudentsLoadedMutableLiveData.setValue(true);
        switch (listResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                adapter.updateAdapter(students);
                String error = AppUtility.getError(context, students, listResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                adapter.updateAdapter(students);
                break;
        }
    }

    @Override
    public void onDrawerStateChanged() {
        if (departmentSpinner.isShowing())
            departmentSpinner.dismiss();
        if (gradeSpinner.isShowing())
            gradeSpinner.dismiss();
    }

    private void closeSpinners() {
        if (departmentSpinner.isShowing())
            departmentSpinner.dismiss();
        if (gradeSpinner.isShowing())
            gradeSpinner.dismiss();

    }
}
