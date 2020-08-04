package com.basemosama.studentportal.ui.professor.source;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;

import java.util.List;

public class ProfessorSourceViewModel extends AndroidViewModel {
    private ProfessorRepository repository;
    private LiveData<Resource<List<Source>>> sourceLiveData;

    public ProfessorSourceViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        sourceLiveData = repository.getSources();
    }

    public LiveData<Resource<List<Source>>> getSources() {
        return sourceLiveData;
    }
}
