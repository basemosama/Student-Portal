package com.basemosama.studentportal.ui.student.marks;

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
import com.basemosama.studentportal.databinding.FragmentMarksBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Marks;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

public class MarksFragment extends Fragment {

    private int subjectId = 1;
    private MarksViewModel marksViewModel;
    private FragmentMarksBinding marksBinding;
    private IMainActivity iMainActivity;
    private Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        marksBinding = FragmentMarksBinding.inflate(inflater, container, false);
        setUpUI();
        return marksBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getMarks();
    }

    private void setUpUI() {
        context = getContext();

        Bundle bundle = getArguments();
        if (bundle != null) {
            subjectId = bundle.getInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, 1);
        }
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        marksViewModel = viewModelProvider.get(MarksViewModel.class);
    }

    private void getMarks() {
        marksViewModel.getMarks(subjectId).observe(getViewLifecycleOwner(), new Observer<Resource<Marks>>() {
            @Override
            public void onChanged(Resource<Marks> marksResource) {
                if (marksResource == null || marksResource.data == null)
                    return;

                Marks marks = marksResource.data;
                switch (marksResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        marksBinding.setMarks(marks);
                        String error = AppUtility.getError(context, marks, marksResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        marksBinding.setMarks(marks);
                        break;
                }
            }
        });
    }

}


