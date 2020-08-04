package com.basemosama.studentportal.ui.student.source;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

import java.util.List;

public class SourceViewModel extends AndroidViewModel {
    private StudentRepository repository;

    public SourceViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
    }

    public LiveData<Resource<List<Source>>> getSources(int subjectId) {
        return repository.getSubjectSources(subjectId);
    }
}
