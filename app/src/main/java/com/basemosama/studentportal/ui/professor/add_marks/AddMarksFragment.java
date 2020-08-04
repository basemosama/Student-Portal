package com.basemosama.studentportal.ui.professor.add_marks;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.basemosama.studentportal.databinding.FragmentAddMarksBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.Prediction;
import com.basemosama.studentportal.model.student.Marks;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class AddMarksFragment extends Fragment implements DrawerListener {
    private FragmentAddMarksBinding addMarksBinding;
    private Context context;
    private AddMarksViewModel viewModel;
    private User student;
    private PowerSpinnerView subjectSpinner;
    private IMainActivity iMainActivity;

    public AddMarksFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addMarksBinding = FragmentAddMarksBinding.inflate(inflater, container, false);
        setUpUI();
        return addMarksBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        observeValidityOfData();
        getStudent();
        getSubjects();
        getAddMarksResult();
        getMarks();
        getPrediction();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (subjectSpinner.isShowing())
            subjectSpinner.dismiss();

    }

    private void setUpUI() {
        context = getContext();
        subjectSpinner = addMarksBinding.marksSubjectSpinner;
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

        viewModel = viewModelProvider.get(AddMarksViewModel.class);
        addMarksBinding.setViewModel(viewModel);
        addMarksBinding.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void getStudent() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Constants.STUDENT_MARKS_BUNDLE_KEY)) {
            student = bundle.getParcelable(Constants.STUDENT_MARKS_BUNDLE_KEY);
            addMarksBinding.setStudentName(student.getName());
            if (viewModel.getStudentIdMutableLiveData().getValue() == null) {
                viewModel.setStudentId(student.getId());
            } else if (viewModel.getStudentIdMutableLiveData().getValue() != student.getId()) {

                viewModel.setWork("");
                viewModel.setMidterm("");
                viewModel.setSemester("");
                viewModel.setAttendance("");
                viewModel.getPrediction().setValue(null);
                viewModel.setSubjectId(null);
                viewModel.setSubjectPosition(null);
                viewModel.setStudentId(student.getId());
            }
        }

    }

    private void getSubjects() {
        viewModel.getSubjects(student.getDepartmentId(), student.getGradeId()).observe(getViewLifecycleOwner(), new Observer<Resource<List<Subject>>>() {
            @Override
            public void onChanged(Resource<List<Subject>> listResource) {
                handleSubjectResource(listResource);
            }
        });
    }


    private void getAddMarksResult() {
        viewModel.getAddMarksResult().observe(getViewLifecycleOwner(), new Observer<Resource<Marks>>() {
            @Override
            public void onChanged(Resource<Marks> marksResource) {
                handleAddMarksResource(marksResource);
            }
        });
    }


    private void getMarks() {
        viewModel.getStudentMarksLiveData().observe(getViewLifecycleOwner(), new Observer<Resource<Marks>>() {
            @Override
            public void onChanged(Resource<Marks> marksResource) {
                handleMarksResource(marksResource);
            }
        });
    }

    private void getPrediction() {
        viewModel.getPrediction().observe(getViewLifecycleOwner(), new Observer<Resource<Prediction>>() {
            @Override
            public void onChanged(Resource<Prediction> predictionResource) {
                handlePredictionResource(predictionResource);
            }
        });
    }

    private void observeValidityOfData() {
        viewModel.getMarksFormStateMutableLiveData().observe(getViewLifecycleOwner(), new Observer<AddMarksFormState>() {
            @Override
            public void onChanged(AddMarksFormState addMarksFormState) {
                if (addMarksFormState.getAttendanceError() != null) {
                    addMarksBinding.addMarksAttendanceEditText.setError(getString(addMarksFormState.getAttendanceError()));
                } else if (addMarksFormState.getWorkError() != null) {
                    addMarksBinding.addMarksWorkEditText.setError(getString(addMarksFormState.getWorkError()));
                } else if (addMarksFormState.getMidtermError() != null) {
                    addMarksBinding.addMarksMidtermEditText.setError(getString(addMarksFormState.getMidtermError()));
                } else if (addMarksFormState.getSemesterError() != null) {
                    addMarksBinding.addMarksSemesterEditText.setError(getString(addMarksFormState.getSemesterError()));
                } else if (addMarksFormState.getSubjectError() != null) {
                    if (getView() != null)
                        Snackbar.make(getView(), addMarksFormState.getSubjectError(), BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void handleSubjectResource(Resource<List<Subject>> subjectsResource) {
        if (subjectsResource == null || subjectsResource.data == null)
            return;
        final List<Subject> subjects = subjectsResource.data;
        List<String> subjectsNames = new ArrayList<>();

        for (Subject subject : subjects) {
            subjectsNames.add(subject.getName());
        }
        Integer subjectPosition = viewModel.getSubjectPositionMutableLiveData().getValue();


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
                            Integer subjectIdInViewModel = viewModel.getSubjectIdMutableLiveData().getValue();
                            if (subjectIdInViewModel == null || subjectIdInViewModel != subjectId) {
                                viewModel.setSubjectId(subjectId);
                                viewModel.setSubjectPosition(position);
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
                            Integer subjectIdInViewModel = viewModel.getSubjectIdMutableLiveData().getValue();
                            if (subjectIdInViewModel == null || subjectIdInViewModel != subjectId) {
                                viewModel.setSubjectId(subjectId);
                                viewModel.setSubjectPosition(position);
                            }
                        }
                    });
                }
                break;
        }

    }

    private void handleAddMarksResource(Resource<Marks> marksResource) {
        if (marksResource == null)
            return;

        switch (marksResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                String error = AppUtility.getError(context, marksResource.data, context.getString(R.string.error_adding_marks));
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;


            case SUCCESS:
                iMainActivity.hideLoading();
                Toast.makeText(context, R.string.successful_adding_marks_message, Toast.LENGTH_SHORT).show();
                break;
        }
        viewModel.getAddMarksResult().setValue(null);
    }

    private void handleMarksResource(Resource<Marks> marksResource) {
        if (marksResource == null)
            return;
        Marks marks = marksResource.data;
        switch (marksResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                String error = AppUtility.getError(context, marksResource.data, marksResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                viewModel.setAttendance("");
                viewModel.setWork("");
                viewModel.setMidterm("");
                viewModel.setSemester("");


                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                if (marks != null) {
                    viewModel.setAttendance(marks.getAttendance());
                    viewModel.setWork(marks.getWork());
                    viewModel.setMidterm(marks.getMidterm());
                    viewModel.setSemester(marks.getSemester());
                } else {
                    viewModel.setAttendance("");
                    viewModel.setWork("");
                    viewModel.setMidterm("");
                    viewModel.setSemester("");
                }

        }
    }

    private void handlePredictionResource(Resource<Prediction> predictionResource) {
        if (predictionResource == null)
            return;

        switch (predictionResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;
            case ERROR:
                iMainActivity.hideLoading();
                String error = AppUtility.getError(context, predictionResource.data, getString(R.string.error_predict_result));
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;


            case SUCCESS:
                iMainActivity.hideLoading();
                String prediction = predictionResource.data.getGrade();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(prediction);
                builder.setTitle(R.string.prediction_dialog_title);
                builder.setPositiveButton(R.string.ok_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.create();
                builder.show();
                break;
        }
        viewModel.getPrediction().setValue(null);
    }

    @Override
    public void onDrawerStateChanged() {
        if (subjectSpinner.isShowing())
            subjectSpinner.dismiss();
    }
}
