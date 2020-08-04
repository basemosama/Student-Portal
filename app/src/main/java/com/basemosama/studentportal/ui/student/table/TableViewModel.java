package com.basemosama.studentportal.ui.student.table;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

public class TableViewModel extends AndroidViewModel {
    private StudentRepository repository;

    public TableViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
    }

    public LiveData<Resource<Table>> getTable() {
        return repository.getTableMutableLiveData();
    }
}
