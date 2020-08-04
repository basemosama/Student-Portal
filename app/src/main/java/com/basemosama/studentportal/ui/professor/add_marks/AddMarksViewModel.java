package com.basemosama.studentportal.ui.professor.add_marks;

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
import com.basemosama.studentportal.model.professor.Prediction;
import com.basemosama.studentportal.model.student.Marks;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;
import com.basemosama.studentportal.utility.CombinedLiveData;

import java.util.List;

public class AddMarksViewModel extends AndroidViewModel {

    public MutableLiveData<String> attendanceMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> workMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> midtermMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> semesterMutableLiveData = new MutableLiveData<>();
    private ProfessorRepository repository;
    private MutableLiveData<Integer> subjectIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> subjectPositionMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> studentIdMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<AddMarksFormState> marksFormStateMutableLiveData = new MutableLiveData<>();

    private LiveData<Resource<Marks>> studentMarksLiveData;
    private CombinedLiveData<Integer, Integer> combinedLiveData;

    public AddMarksViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        combinedLiveData = new CombinedLiveData<>(studentIdMutableLiveData, subjectIdMutableLiveData);
        updateStudentMarks();
    }


    public LiveData<Resource<List<Subject>>> getSubjects(int departmentId, int gradeId) {
        return repository.getSubjectsForSpecificGrade(departmentId, gradeId);
    }

    private void updateStudentMarks() {
        studentMarksLiveData = Transformations.switchMap(combinedLiveData, new Function<Pair<Integer, Integer>, LiveData<Resource<Marks>>>() {
            @Override
            public LiveData<Resource<Marks>> apply(Pair<Integer, Integer> input) {
                Integer studentId = input.first;
                Integer subjectId = input.second;
                return studentId != null && subjectId != null ? repository.getMarks(subjectId, studentId) : null;
            }
        });
    }

    public void uploadMarks(View view) {
        checkIfDataIsValid();
        if (marksFormStateMutableLiveData.getValue() != null && marksFormStateMutableLiveData.getValue().isDataValid()) {
            repository.addMarks(studentIdMutableLiveData.getValue(),
                    subjectIdMutableLiveData.getValue(), getDoubleValueFromMutableLiveData(attendanceMutableLiveData),
                    getDoubleValueFromMutableLiveData(workMutableLiveData), getDoubleValueFromMutableLiveData(midtermMutableLiveData),
                    getDoubleValueFromMutableLiveData(semesterMutableLiveData));
        }
    }

    public void predictResult(View view) {
        checkIfDataIsValid();
        if (marksFormStateMutableLiveData.getValue() != null && marksFormStateMutableLiveData.getValue().isDataValid()) {
            repository.predictResult(getDoubleValueFromMutableLiveData(attendanceMutableLiveData).intValue()
                    , getDoubleValueFromMutableLiveData(workMutableLiveData).intValue()
                    , getDoubleValueFromMutableLiveData(midtermMutableLiveData).intValue());
        }
    }

    private void checkIfDataIsValid() {
        if (getDoubleValueFromMutableLiveData(attendanceMutableLiveData) == null) {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(R.string.invalid_marks_attendance, null, null, null, null));
        } else if (getDoubleValueFromMutableLiveData(workMutableLiveData) == null) {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(null, R.string.invalid_marks_work, null, null, null));
        } else if (getDoubleValueFromMutableLiveData(midtermMutableLiveData) == null) {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(null, null, R.string.invalid_marks_midterm, null, null));
        } else if (getDoubleValueFromMutableLiveData(semesterMutableLiveData) == null) {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(null, null, null, R.string.invalid_marks_semester, null));
        } else if (subjectIdMutableLiveData.getValue() == null) {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(null, null, null, null, R.string.invalid_assignment_subject));
        } else if (studentIdMutableLiveData.getValue() == null) {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(null, null, null, null, R.string.main_error_message));
        } else {
            marksFormStateMutableLiveData.setValue(new AddMarksFormState(true));
        }
    }

    private Double getDoubleValueFromMutableLiveData(MutableLiveData<String> stringMutableLiveData) {
        return stringMutableLiveData.getValue() != null && !TextUtils.isEmpty(stringMutableLiveData.getValue()) ? Double.parseDouble(stringMutableLiveData.getValue()) : null;
    }


    public MutableLiveData<Resource<Marks>> getAddMarksResult() {
        return repository.getAddMarksResultLiveData();
    }

    public MutableLiveData<Resource<Prediction>> getPrediction() {
        return repository.getPredictionMutableLiveData();
    }


    public LiveData<Resource<Marks>> getStudentMarksLiveData() {
        return studentMarksLiveData;
    }

    public LiveData<AddMarksFormState> getMarksFormStateMutableLiveData() {
        return marksFormStateMutableLiveData;
    }

    public MutableLiveData<Integer> getSubjectIdMutableLiveData() {
        return subjectIdMutableLiveData;
    }

    public MutableLiveData<Integer> getSubjectPositionMutableLiveData() {
        return subjectPositionMutableLiveData;
    }

    public MutableLiveData<Integer> getStudentIdMutableLiveData() {
        return studentIdMutableLiveData;
    }


    public void setStudentId(Integer studentId) {
        studentIdMutableLiveData.setValue(studentId);
    }

    public void setAttendance(double attendance) {
        attendanceMutableLiveData.setValue(String.valueOf(attendance));
    }

    public void setWork(double work) {
        workMutableLiveData.setValue(String.valueOf(work));
    }

    public void setMidterm(double midterm) {
        midtermMutableLiveData.setValue(String.valueOf(midterm));
    }

    public void setSemester(double semester) {
        semesterMutableLiveData.setValue(String.valueOf(semester));
    }

    public void setAttendance(String attendance) {
        attendanceMutableLiveData.setValue(attendance);
    }

    public void setWork(String work) {
        workMutableLiveData.setValue(work);
    }

    public void setMidterm(String midterm) {
        midtermMutableLiveData.setValue(midterm);
    }

    public void setSemester(String semester) {
        semesterMutableLiveData.setValue(semester);
    }


    public void setSubjectId(Integer id) {
        subjectIdMutableLiveData.setValue(id);
    }

    public void setSubjectPosition(Integer position) {
        subjectPositionMutableLiveData.setValue(position);
    }

}

