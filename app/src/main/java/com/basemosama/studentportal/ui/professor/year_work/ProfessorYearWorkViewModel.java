package com.basemosama.studentportal.ui.professor.year_work;

import android.app.Application;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;
import com.basemosama.studentportal.utility.CombinedLiveData;

import java.util.List;

public class ProfessorYearWorkViewModel extends AndroidViewModel {
    public MutableLiveData<Boolean> isStudentsLoadedMutableLiveData = new MutableLiveData<>(false);
    private ProfessorRepository repository;
    private MutableLiveData<Integer> gradeIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> gradePositionMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Integer> departmentIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> departmentPositionMutableLiveData = new MutableLiveData<>();

    private LiveData<Resource<List<User>>> studentsLiveData = new MediatorLiveData<>();

    private LiveData<Resource<List<Department>>> departmentsLiveData;
    private LiveData<Resource<List<ProfessorGrade>>> gradesLiveData;

    private CombinedLiveData<Integer, Integer> combinedLiveData;

    public ProfessorYearWorkViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        departmentsLiveData = repository.getDepartments();
        gradesLiveData = repository.getGrades();
        combinedLiveData = new CombinedLiveData<>(departmentIdMutableLiveData, gradeIdMutableLiveData);
        updateStudents();
    }


    private void updateStudents() {
        studentsLiveData = Transformations.switchMap(combinedLiveData, new Function<Pair<Integer, Integer>, LiveData<Resource<List<User>>>>() {
            @Override
            public LiveData<Resource<List<User>>> apply(Pair<Integer, Integer> input) {
                Integer departmentId = input.first;
                Integer gradeId = input.second;
                if (departmentId != null && gradeId != null) {
                    return repository.getStudents(departmentId, gradeId);
                }
                return null;
            }
        });
    }


    public LiveData<Resource<List<User>>> getStudents() {
        return studentsLiveData;
    }

    public LiveData<Resource<List<Department>>> getDepartments() {
        return departmentsLiveData;
    }

    public LiveData<Resource<List<ProfessorGrade>>> getGrades() {
        return gradesLiveData;
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


    public void setDepartmentPosition(Integer departmentPosition) {
        departmentPositionMutableLiveData.setValue(departmentPosition);
    }


}
