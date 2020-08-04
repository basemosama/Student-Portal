package com.basemosama.studentportal.ui.student.marks;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Marks;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

public class MarksViewModel extends AndroidViewModel {

    private StudentRepository repository;

    public MarksViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
    }

    public LiveData<Resource<Marks>> getMarks(int subjectId) {
        return repository.getMarks(subjectId);
    }
}
