package com.basemosama.studentportal.ui.student.source;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
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
import com.basemosama.studentportal.databinding.FragmentAssignmentsBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class SourceFragment extends Fragment implements SourceAdapter.SourceClickListener {
    private SourceViewModel sourceViewModel;
    private FragmentAssignmentsBinding assignmentsBinding;
    private SourceAdapter adapter;
    private int subjectId;
    private Context context;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        assignmentsBinding = FragmentAssignmentsBinding.inflate(inflater, container, false);
        setUpUI();
        return assignmentsBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getAssignments();
    }

    private void setUpUI() {
        context = getContext();
        Bundle bundle = getArguments();
        if (bundle != null) {
            subjectId = bundle.getInt(Constants.ASSIGNMENT_SUBJECT_ID_KEY, 1);
        }
        adapter = new SourceAdapter(context, new ArrayList<Source>(), this);
        assignmentsBinding.assignmentRv.setAdapter(adapter);


    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        sourceViewModel = viewModelProvider.get(SourceViewModel.class);
    }

    private void getAssignments() {
        sourceViewModel.getSources(subjectId).observe(getViewLifecycleOwner(), new Observer<Resource<List<Source>>>() {
            @Override
            public void onChanged(Resource<List<Source>> listResource) {

                if (listResource == null || listResource.data == null)
                    return;

                List<Source> sources = listResource.data;
                switch (listResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        adapter.updateAdapter(listResource.data);
                        String error = AppUtility.getError(context, sources, listResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        adapter.updateAdapter(listResource.data);
                        break;
                }
            }
        });
    }

    @Override
    public void onSourceClickListener(Source source) {
        String path = source.getPath();
        if (URLUtil.isValidUrl(path)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(path));
            startActivity(intent);
        } else {
            Toast.makeText(context, R.string.invalid_assignment_path_error_message, Toast.LENGTH_SHORT).show();
        }
    }
}
