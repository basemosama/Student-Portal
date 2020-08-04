package com.basemosama.studentportal.ui.student.assignments;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

import java.util.List;

public class AssignmentsViewModel extends AndroidViewModel {
    private StudentRepository repository;

    public AssignmentsViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
    }

    public LiveData<Resource<List<Assignment>>> getAssignments(int type, int subjectId) {
        switch (type) {
            case 1:
                return repository.getSubjectAssignments(subjectId);
            default:
                return repository.getAssignmentsLiveData();
        }
    }
}
