package com.basemosama.studentportal.ui.professor.add_assignment;

import android.app.Application;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;
import com.basemosama.studentportal.utility.CombinedLiveData;

import java.util.List;

public class AddAssignmentViewModel extends AndroidViewModel {
    public MutableLiveData<Resource<Assignment>> addAssignmentResultMutableLiveData;

    public MutableLiveData<String> title = new MutableLiveData<>();
    public MutableLiveData<String> path = new MutableLiveData<>();

    private MutableLiveData<Integer> gradeIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> gradePositionMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> departmentPositionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> departmentIdMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> subjectPositionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> subjectIdMutableLiveData = new MutableLiveData<>();
    private ProfessorRepository repository;

    private LiveData<Resource<List<Department>>> departmentsLiveData;
    private LiveData<Resource<List<ProfessorGrade>>> gradesLiveData;

    private MutableLiveData<AddAssignmentFormState> assignmentFormStateMutableLiveData = new MutableLiveData<>();
    private LiveData<Resource<List<Subject>>> subjectsLiveData;
    private MutableLiveData<Subject> subjectMutableLiveData = new MutableLiveData<>();

    private CombinedLiveData<Integer, Integer> combinedLiveData;

    public AddAssignmentViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        gradesLiveData = repository.getGrades();
        departmentsLiveData = repository.getDepartments();
        combinedLiveData = new CombinedLiveData<>(departmentIdMutableLiveData, gradeIdMutableLiveData);
        updateSubjects();
        addAssignmentResultMutableLiveData = repository.getAddAssignmentResultLiveData();
    }

    public LiveData<Resource<List<ProfessorGrade>>> getGrades() {
        return gradesLiveData;
    }

    public LiveData<Resource<List<Department>>> getDepartments() {
        return departmentsLiveData;
    }


    public LiveData<Resource<List<Subject>>> getSubjects() {
        return subjectsLiveData;
    }


    private void updateSubjects() {
        subjectsLiveData = Transformations.switchMap(combinedLiveData, new Function<Pair<Integer, Integer>, LiveData<Resource<List<Subject>>>>() {
            @Override
            public LiveData<Resource<List<Subject>>> apply(Pair<Integer, Integer> input) {
                Integer departmentId = input.first;
                Integer gradeId = input.second;
                if (departmentId != null && gradeId != null) {
                    subjectIdMutableLiveData.setValue(-1);
                    subjectPositionMutableLiveData.setValue(-1);
                    return repository.getSubjectsForSpecificGrade(departmentId, gradeId);
                }
                return null;
            }
        });
    }

    public void uploadNewAssignment(View view) {
        setUpAssignmentFormState();
        if (assignmentFormStateMutableLiveData.getValue() != null && assignmentFormStateMutableLiveData.getValue().isDataValid()) {
            repository.addAssignment(title.getValue(), path.getValue(),
                    departmentIdMutableLiveData.getValue(), gradeIdMutableLiveData.getValue(), subjectIdMutableLiveData.getValue(), subjectMutableLiveData.getValue());
        }
    }


    private void setUpAssignmentFormState() {
        if (TextUtils.isEmpty(title.getValue())) {
            assignmentFormStateMutableLiveData.setValue(new AddAssignmentFormState(R.string.invalid_assignment_title, null, null, null, null));
        } else if (TextUtils.isEmpty(path.getValue())) {
            assignmentFormStateMutableLiveData.setValue(new AddAssignmentFormState(null, R.string.invalid_assignment_path, null, null, null));
        } else if (departmentIdMutableLiveData.getValue() == null) {
            assignmentFormStateMutableLiveData.setValue(new AddAssignmentFormState(null, null, R.string.invalid_assignment_department, null, null));
        } else if (gradeIdMutableLiveData.getValue() == null) {
            assignmentFormStateMutableLiveData.setValue(new AddAssignmentFormState(null, null, null, R.string.invalid_assignment_grade, null));
        } else if (subjectIdMutableLiveData.getValue() == null) {
            assignmentFormStateMutableLiveData.setValue(new AddAssignmentFormState(null, null, null, null, R.string.invalid_assignment_subject));
        } else {
            assignmentFormStateMutableLiveData.setValue(new AddAssignmentFormState(true));
        }
    }

    public MutableLiveData<AddAssignmentFormState> getAssignmentFormState() {
        return assignmentFormStateMutableLiveData;
    }

    public MutableLiveData<Resource<Assignment>> getResult() {
        return addAssignmentResultMutableLiveData;
    }


    public Integer getGradeId() {
        return gradeIdMutableLiveData.getValue();
    }

    public void setGradeId(Integer gradeId) {
        gradeIdMutableLiveData.setValue(gradeId);
    }

    public Integer getGradePosition() {
        return gradePositionMutableLiveData.getValue();
    }

    public void setGradePosition(Integer gradePosition) {
        gradePositionMutableLiveData.setValue(gradePosition);
    }

    public Integer getDepartmentId() {
        return departmentIdMutableLiveData.getValue();
    }

    public void setDepartmentId(Integer departmentId) {
        departmentIdMutableLiveData.setValue(departmentId);
    }

    public Integer getDepartmentPosition() {
        return departmentPositionMutableLiveData.getValue();
    }

    public void setDepartmentPosition(Integer departmentPosition) {
        departmentPositionMutableLiveData.setValue(departmentPosition);
    }

    public Integer getSubjectId() {
        return subjectIdMutableLiveData.getValue();
    }

    public void setSubjectId(Integer subjectId) {
        subjectIdMutableLiveData.setValue(subjectId);
    }

    public Integer getSubjectPosition() {
        return subjectPositionMutableLiveData.getValue();
    }

    public void setSubjectPosition(Integer subjectPosition) {
        subjectPositionMutableLiveData.setValue(subjectPosition);
    }

    public void setCurrentSubject(Subject subject) {
        subjectMutableLiveData.setValue(subject);
    }
}
