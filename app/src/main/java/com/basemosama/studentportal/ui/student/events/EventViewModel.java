package com.basemosama.studentportal.ui.student.events;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.basemosama.studentportal.model.student.Event;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;

import java.util.List;

public class EventViewModel extends AndroidViewModel {
    private StudentRepository repository;
    private LiveData<Resource<List<Event>>> eventsMutableLiveData;
    private MutableLiveData<Event> currentEventMutableLiveData = new MutableLiveData<>();

    public EventViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);
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
