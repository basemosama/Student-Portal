package com.basemosama.studentportal.ui.professor.professor_events;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.basemosama.studentportal.model.student.Event;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;

import java.util.List;

public class ProfessorEventViewModel extends AndroidViewModel {
    private ProfessorRepository repository;
    private LiveData<Resource<List<Event>>> eventsMutableLiveData;
    private MutableLiveData<Event> currentEventMutableLiveData = new MutableLiveData<>();

    public ProfessorEventViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        eventsMutableLiveData = repository.getEventMutableLiveData();

    }

    public LiveData<Resource<List<Event>>> getEvents() {
        return eventsMutableLiveData;
    }

    public MutableLiveData<Event> getCurrentEvent() {
        return currentEventMutableLiveData;
    }

    public void setCurrentEvent(Event event) {
        currentEventMutableLiveData.setValue(event);
    }
}
