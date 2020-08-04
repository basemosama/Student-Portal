package com.basemosama.studentportal.ui.student.subject;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

import java.util.List;

public class SubjectViewModel extends AndroidViewModel {
    private StudentRepository repository;


    public SubjectViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
    }

    public LiveData<Resource<List<Subject>>> getSubjects() {
        return repository.getSubjectMutableLiveData();
    }
}
