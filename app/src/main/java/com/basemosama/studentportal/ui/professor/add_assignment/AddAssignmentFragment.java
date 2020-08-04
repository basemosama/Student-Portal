package com.basemosama.studentportal.ui.professor.add_assignment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.basemosama.studentportal.databinding.FragmentAddAssignmentBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.powerspinner.DefaultSpinnerAdapter;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class AddAssignmentFragment extends Fragment implements DrawerListener {
    private FragmentAddAssignmentBinding addAssignmentBinding;
    private PowerSpinnerView departmentSpinner, gradeSpinner, subjectSpinner;
    private EditText titleEditText, pathEditText;
    private AddAssignmentViewModel addAssignmentViewModel;
    private Context context;
    private IMainActivity iMainActivity;
    private DefaultSpinnerAdapter adapter;

    public AddAssignmentFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addAssignmentBinding = FragmentAddAssignmentBinding.inflate(inflater, container, false);
        setUpUI();
        return addAssignmentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getDepartments();
        getGrades();
        getSubjects();
        getAssignmentFormState();
        getAddingAssignmentResult();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeSpinners();
    }

    private void setUpUI() {
        context = getContext();
        departmentSpinner = addAssignmentBinding.departmentSpinner;
        gradeSpinner = addAssignmentBinding.gradeSpinner;
        subjectSpinner = addAssignmentBinding.subjectSpinner;
        titleEditText = addAssignmentBinding.addAssignmentTitleEditText;
        pathEditText = addAssignmentBinding.addAssignmentPathEditText;
        adapter = new DefaultSpinnerAdapter(subjectSpinner);
        subjectSpinner.setSpinnerAdapter(adapter);
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
        addAssignmentViewModel = viewModelProvider.get(AddAssignmentViewModel.class);
        addAssignmentBinding.setViewModel(addAssignmentViewModel);
        addAssignmentBinding.setLifecycleOwner(getViewLifecycleOwner());
        departmentSpinner.setLifecycleOwner(getViewLifecycleOwner());
        gradeSpinner.setLifecycleOwner(getViewLifecycleOwner());
        subjectSpinner.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void getDepartments() {
        addAssignmentViewModel.getDepartments().observe(getViewLifecycleOwner(), new Observer<Resource<List<Department>>>() {
            @Override
            public void onChanged(Resource<List<Department>> assignmentResource) {
                handleDepartmentResource(assignmentResource);

            }
        });
    }

    private void getGrades() {
        addAssignmentViewModel.getGrades().observe(getViewLifecycleOwner(), new Observer<Resource<List<ProfessorGrade>>>() {
            @Override
            public void onChanged(Resource<List<ProfessorGrade>> assignmentResource) {
                handleGradeResource(assignmentResource);
            }
        });
    }

    private void getSubjects() {
        addAssignmentViewModel.getSubjects().observe(getViewLifecycleOwner(), new Observer<Resource<List<Subject>>>() {
            @Override
            public void onChanged(Resource<List<Subject>> assignmentResource) {
                handleSubjectResource(assignmentResource);
            }
        });
    }

    private void getAssignmentFormState() {
        addAssignmentViewModel.getAssignmentFormState().observe(getViewLifecycleOwner(), new Observer<AddAssignmentFormState>() {
            @Override
            public void onChanged(AddAssignmentFormState assignmentFormState) {
                if (assignmentFormState.getTitleError() != null) {
                    titleEditText.setError(getString(assignmentFormState.getTitleError()));
                } else if (assignmentFormState.getPathError() != null) {
                    pathEditText.setError(getString(assignmentFormState.getPathError()));
                } else if (assignmentFormState.getDepartmentError() != null) {
                    if (getView() != null)
                        Snackbar.make(getView(), assignmentFormState.getDepartmentError(), BaseTransientBottomBar.LENGTH_SHORT).show();
                } else if (assignmentFormState.getGradeError() != null) {
                    if (getView() != null)
                        Snackbar.make(getView(), assignmentFormState.getGradeError(), BaseTransientBottomBar.LENGTH_SHORT).show();
                } else if (assignmentFormState.getSubjectError() != null) {
                    if (getView() != null)
                        Snackbar.make(getView(), assignmentFormState.getSubjectError(), BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getAddingAssignmentResult() {
        addAssignmentViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<Resource<Assignment>>() {
            @Override
            public void onChanged(Resource<Assignment> assignmentResource) {
                if (assignmentResource == null)
                    return;

                switch (assignmentResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        String error = AppUtility.getError(context, assignmentResource.data, assignmentResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        Toast.makeText(context, R.string.successful_adding_assignment_message, Toast.LENGTH_SHORT).show();
                        break;
                }
                addAssignmentViewModel.getResult().setValue(null);
            }
        });
    }


    private void handleGradeResource(Resource<List<ProfessorGrade>> gradeResource) {
        if (gradeResource == null || gradeResource.data == null)
            return;
        final List<ProfessorGrade> grades = gradeResource.data;
        List<String> gradesNames = new ArrayList<>();

        for (ProfessorGrade grade : grades) {
            gradesNames.add(grade.getName());
        }
        Integer gradePosition = addAssignmentViewModel.getGradePosition();
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
                        Integer gradeIdInViewModel = addAssignmentViewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            addAssignmentViewModel.setGradeId(gradeId);
                            addAssignmentViewModel.setGradePosition(position);
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
                        Integer gradeIdInViewModel = addAssignmentViewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            addAssignmentViewModel.setGradeId(gradeId);
                            addAssignmentViewModel.setGradePosition(position);
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
        Integer departmentPosition = addAssignmentViewModel.getDepartmentPosition();

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
                        Integer departmentIdInViewModel = addAssignmentViewModel.getDepartmentId();
                        if (departmentIdInViewModel == null || departmentIdInViewModel != departmentId) {
                            addAssignmentViewModel.setDepartmentId(departmentId);
                            addAssignmentViewModel.setDepartmentPosition(position);
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
                        Integer departmentIdInViewModel = addAssignmentViewModel.getDepartmentId();
                        if (departmentIdInViewModel == null || departmentIdInViewModel != departmentId) {
                            addAssignmentViewModel.setDepartmentId(departmentId);
                            addAssignmentViewModel.setDepartmentPosition(position);
                        }
                    }
                });

                break;
        }
    }


    private void handleSubjectResource(Resource<List<Subject>> subjectsResource) {
        if (subjectsResource == null || subjectsResource.data == null)
            return;
        final List<Subject> subjects = subjectsResource.data;
        List<String> subjectsNames = new ArrayList<>();

        for (Subject subject : subjects) {
            subjectsNames.add(subject.getName());
        }
        Integer subjectPosition = addAssignmentViewModel.getSubjectPosition();


        switch (subjectsResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                if (subjectsNames != null && subjectsNames.size() > 0) {
                    subjectSpinner.setItems(subjectsNames);
                    if (subjectPosition != null) {
                        if (subjectPosition < 0) {
                            subjectSpinner.clearSelectedItem();
                        } else {
                            subjectSpinner.selectItemByIndex(subjectPosition);
                        }
                    }
                    subjectSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(int position, String name) {
                            int subjectId = subjects.get(position).getId();
                            Integer subjectIdInViewModel = addAssignmentViewModel.getSubjectId();
                            if (subjectIdInViewModel == null || subjectIdInViewModel != subjectId) {
                                addAssignmentViewModel.setSubjectId(subjectId);
                                addAssignmentViewModel.setSubjectPosition(position);
                                addAssignmentViewModel.setCurrentSubject(subjects.get(position));
                            }
                        }
                    });
                }
                String error = AppUtility.getError(context, subjects, subjectsResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                if (subjectsNames != null && subjectsNames.size() > 0) {
                    adapter.setItems(subjectsNames);
                    if (subjectPosition != null) {
                        if (subjectPosition < 0) {
                            subjectSpinner.clearSelectedItem();
                            subjectSpinner.setSpinnerAdapter(adapter);

                        } else {
                            subjectSpinner.selectItemByIndex(subjectPosition);
                        }
                    }
                    subjectSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener<String>() {
                        @Override
                        public void onItemSelected(int position, String name) {
                            int subjectId = subjects.get(position).getId();
                            Integer subjectIdInViewModel = addAssignmentViewModel.getSubjectId();
                            if (subjectIdInViewModel == null || subjectIdInViewModel != subjectId) {
                                addAssignmentViewModel.setSubjectId(subjectId);
                                addAssignmentViewModel.setSubjectPosition(position);
                                addAssignmentViewModel.setCurrentSubject(subjects.get(position));
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onDrawerStateChanged() {
        closeSpinners();
    }

    private void closeSpinners() {
        if (departmentSpinner.isShowing())
            departmentSpinner.dismiss();
        if (gradeSpinner.isShowing())
            gradeSpinner.dismiss();
        if (subjectSpinner.isShowing())
            subjectSpinner.dismiss();

    }
}


