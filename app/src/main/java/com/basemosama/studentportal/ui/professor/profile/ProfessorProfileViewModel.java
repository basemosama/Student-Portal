package com.basemosama.studentportal.ui.professor.profile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.professor.ProfessorProfileData;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;

public class ProfessorProfileViewModel extends AndroidViewModel {
    private ProfessorRepository repository;
    private LiveData<Resource<ProfessorProfileData>> profileDataLiveData;

    public ProfessorProfileViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        profileDataLiveData = repository.getProfileData();
    }

    public LiveData<Resource<ProfessorProfileData>> getProfileData() {
        return profileDataLiveData;
    }
}
