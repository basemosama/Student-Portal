package com.basemosama.studentportal.ui.professor.add_source;

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
import com.basemosama.studentportal.databinding.FragmentAddSourceBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.ui.professor.add_assignment.AddAssignmentFormState;
import com.basemosama.studentportal.utility.AppUtility;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.powerspinner.DefaultSpinnerAdapter;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class AddSourceFragment extends Fragment implements DrawerListener {
    private FragmentAddSourceBinding addSourceBinding;
    private PowerSpinnerView departmentSpinner, gradeSpinner, subjectSpinner;
    private EditText titleEditText, pathEditText;
    private AddSourceViewModel addSourceViewModel;
    private Context context;
    private IMainActivity iMainActivity;
    private DefaultSpinnerAdapter adapter;

    public AddSourceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addSourceBinding = FragmentAddSourceBinding.inflate(inflater, container, false);
        setUpUI();
        return addSourceBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getDepartments();
        getGrades();
        getSubjects();
        getAssignmentFormState();
        getResult();
    }

    @Override
    public void onStop() {
        super.onStop();
        closeSpinners();
    }

    private void setUpUI() {
        context = getContext();
        departmentSpinner = addSourceBinding.sourceDepartmentSpinner;
        gradeSpinner = addSourceBinding.sourceGradeSpinner;
        subjectSpinner = addSourceBinding.sourceSubjectSpinner;
        titleEditText = addSourceBinding.addSourceTitleEditText;
        pathEditText = addSourceBinding.addSourcePathEditText;
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

        addSourceViewModel = viewModelProvider.get(AddSourceViewModel.class);
        addSourceBinding.setViewModel(addSourceViewModel);
        addSourceBinding.setLifecycleOwner(getViewLifecycleOwner());
        departmentSpinner.setLifecycleOwner(getViewLifecycleOwner());
        gradeSpinner.setLifecycleOwner(getViewLifecycleOwner());
        subjectSpinner.setLifecycleOwner(getViewLifecycleOwner());

    }

    private void getDepartments() {
        addSourceViewModel.getDepartments().observe(getViewLifecycleOwner(), new Observer<Resource<List<Department>>>() {
            @Override
            public void onChanged(Resource<List<Department>> listResource) {
                handleDepartmentResource(listResource);
            }
        });
    }

    private void getGrades() {
        addSourceViewModel.getGrades().observe(getViewLifecycleOwner(), new Observer<Resource<List<ProfessorGrade>>>() {
            @Override
            public void onChanged(Resource<List<ProfessorGrade>> listResource) {
                handleGradeResource(listResource);
            }
        });
    }

    private void getSubjects() {
        addSourceViewModel.getSubjects().observe(getViewLifecycleOwner(), new Observer<Resource<List<Subject>>>() {
            @Override
            public void onChanged(Resource<List<Subject>> listResource) {
                handleSubjectResource(listResource);
            }
        });
    }

    private void getAssignmentFormState() {
        addSourceViewModel.getSourceFormState().observe(getViewLifecycleOwner(), new Observer<AddAssignmentFormState>() {
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

    private void getResult() {
        addSourceViewModel.getResult().observe(getViewLifecycleOwner(), new Observer<Resource<Source>>() {
            @Override
            public void onChanged(Resource<Source> sourceResource) {
                if (sourceResource == null)
                    return;

                switch (sourceResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        String error = AppUtility.getError(context, sourceResource.data, sourceResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        Toast.makeText(context, R.string.successful_adding_source_message, Toast.LENGTH_SHORT).show();
                        break;
                }
                addSourceViewModel.getResult().setValue(null);
            }
        });

    }

    //Helper Method to handle grade and department result
    private void handleGradeResource(Resource<List<ProfessorGrade>> gradeResource) {
        if (gradeResource == null || gradeResource.data == null)
            return;

        final List<ProfessorGrade> grades = gradeResource.data;
        List<String> gradesNames = new ArrayList<>();
        for (ProfessorGrade grade : grades) {
            gradesNames.add(grade.getName());
        }
        Integer gradePosition = addSourceViewModel.getGradePosition();

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
                        Integer gradeIdInViewModel = addSourceViewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            addSourceViewModel.setGradeId(gradeId);
                            addSourceViewModel.setGradePosition(position);
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
                        Integer gradeIdInViewModel = addSourceViewModel.getGradeId();
                        int gradeId = grades.get(position).getId();
                        if (gradeIdInViewModel == null || gradeIdInViewModel != gradeId) {
                            addSourceViewModel.setGradeId(gradeId);
                            addSourceViewModel.setGradePosition(position);
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
        Integer departmentPosition = addSourceViewModel.getDepartmentPosition();

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
                        Integer departmentIdInViewModel = addSourceViewModel.getDepartmentId();
                        if (departmentIdInViewModel == null || departmentIdInViewModel != departmentId) {
                            addSourceViewModel.setDepartmentId(departmentId);
                            addSourceViewModel.setDepartmentPosition(position);
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
                        Integer departmentIdInViewModel = addSourceViewModel.getDepartmentId();
                        if (departmentIdInViewModel == null || departmentIdInViewModel != departmentId) {
                            addSourceViewModel.setDepartmentId(departmentId);
                            addSourceViewModel.setDepartmentPosition(position);
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
        Integer subjectPosition = addSourceViewModel.getSubjectPosition();

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
                            Integer subjectIdInViewModel = addSourceViewModel.getSubjectId();
                            if (subjectIdInViewModel == null || subjectIdInViewModel != subjectId) {
                                addSourceViewModel.setSubjectId(subjectId);
                                addSourceViewModel.setSubjectPosition(position);
                                addSourceViewModel.setCurrentSubject(subjects.get(position));
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
                            Integer subjectIdInViewModel = addSourceViewModel.getSubjectId();
                            if (subjectIdInViewModel == null || subjectIdInViewModel != subjectId) {
                                addSourceViewModel.setSubjectId(subjectId);
                                addSourceViewModel.setSubjectPosition(position);
                                addSourceViewModel.setCurrentSubject(subjects.get(position));
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

