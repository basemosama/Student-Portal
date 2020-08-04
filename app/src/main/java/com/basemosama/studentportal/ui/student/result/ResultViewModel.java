package com.basemosama.studentportal.ui.student.result;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.StudentResult;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

public class ResultViewModel extends AndroidViewModel {
    private StudentRepository repository;

    public ResultViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
    }


    public LiveData<Resource<StudentResult>> getResults() {
        return repository.getResultMutableLiveData();
    }
}
