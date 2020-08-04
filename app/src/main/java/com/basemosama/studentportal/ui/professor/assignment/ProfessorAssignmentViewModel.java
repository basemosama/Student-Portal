package com.basemosama.studentportal.ui.professor.assignment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;

import java.util.List;

public class ProfessorAssignmentViewModel extends AndroidViewModel {
    private ProfessorRepository repository;
    private LiveData<Resource<List<Assignment>>> assignmentLiveData;

    public ProfessorAssignmentViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        assignmentLiveData = repository.getAssignments();
    }

    public LiveData<Resource<List<Assignment>>> getAssignments() {
        return assignmentLiveData;
    }
}
