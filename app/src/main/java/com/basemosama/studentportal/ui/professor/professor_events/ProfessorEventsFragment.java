package com.basemosama.studentportal.ui.professor.professor_events;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentEventsBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Event;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.ui.student.events.EventAdapter;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

import java.util.ArrayList;
import java.util.List;


public class ProfessorEventsFragment extends Fragment implements EventAdapter.EventClickListener {

    private FragmentEventsBinding eventsBinding;
    private EventAdapter adapter;
    private ProfessorEventViewModel eventViewModel;
    private Context context;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventsBinding = FragmentEventsBinding.inflate(inflater, container, false);
        setUpUI();
        return eventsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getEvents();
    }

    private void setUpUI() {
        context = getContext();
        adapter = new EventAdapter(context, new ArrayList<Event>(), this);
        eventsBinding.eventRv.setAdapter(adapter);
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        eventViewModel = viewModelProvider.get(ProfessorEventViewModel.class);
    }

    private void getEvents() {
        eventViewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<Resource<List<Event>>>() {
            @Override
            public void onChanged(Resource<List<Event>> eventResource) {

                if (eventResource == null || eventResource.data == null)
                    return;

                switch (eventResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        adapter.updateAdapter(eventResource.data);
                        String error = AppUtility.getError(context, eventResource.data, eventResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        adapter.updateAdapter(eventResource.data);
                        break;
                }

            }
        });
    }

    @Override
    public void onEventClickListener(Event event, View view) {
        eventViewModel.setCurrentEvent(event);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EVENT_TITLE_BUNDLE_KEY, event.getTitle());
        iMainActivity.getNavController().navigate(R.id.action_nav_events_to_prof_event_detail_fragment, bundle);
    }
}
