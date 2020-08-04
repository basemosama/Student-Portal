package com.basemosama.studentportal.ui.student.events;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentEventDetailBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Event;

public class EventDetailFragment extends Fragment {
    private FragmentEventDetailBinding eventDetailBinding;
    private EventViewModel viewModel;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        eventDetailBinding = FragmentEventDetailBinding.inflate(inflater, container, false);
        return eventDetailBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getCurrentEvent();
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        viewModel = viewModelProvider.get(EventViewModel.class);
        eventDetailBinding.setLifecycleOwner(getViewLifecycleOwner());

    }

    private void getCurrentEvent() {
        viewModel.getCurrentEvent().observe(getViewLifecycleOwner(), new Observer<Event>() {
            @Override
            public void onChanged(Event event) {
                if (event != null)
                    eventDetailBinding.setEvent(event);
            }
        });
    }

}
