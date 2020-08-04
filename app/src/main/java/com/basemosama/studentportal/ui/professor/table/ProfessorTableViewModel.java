package com.basemosama.studentportal.ui.professor.table;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;

import java.util.List;

public class ProfessorTableViewModel extends AndroidViewModel {
    private ProfessorRepository repository;
    private LiveData<Resource<Table>> tableLiveData;

    private MutableLiveData<Integer> gradeIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> gradePositionMutableLiveData = new MutableLiveData<>();

    private LiveData<Resource<List<ProfessorGrade>>> gradesLiveData;


    public ProfessorTableViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        gradesLiveData = repository.getGrades();
        updateTable();
    }

    private void updateTable() {
        tableLiveData = Transformations.switchMap(gradeIdMutableLiveData, new Function<Integer, LiveData<Resource<Table>>>() {
            @Override
            public LiveData<Resource<Table>> apply(Integer gradeId) {
                if (gradeId != null) {
                    return repository.getTable(gradeId);
                }
                return null;
            }
        });
    }

    public LiveData<Resource<Table>> getTable() {
        return tableLiveData;
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


}
