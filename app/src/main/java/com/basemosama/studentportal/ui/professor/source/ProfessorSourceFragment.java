package com.basemosama.studentportal.ui.professor.source;

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
import androidx.recyclerview.widget.RecyclerView;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorSourcesBinding;
import com.basemosama.studentportal.interfaces.FabClickListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.ui.student.source.SourceAdapter;
import com.basemosama.studentportal.utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class ProfessorSourceFragment extends Fragment implements SourceAdapter.SourceClickListener, FabClickListener {
    private FragmentProfessorSourcesBinding professorSourcesBinding;
    private ProfessorSourceViewModel sourceViewModel;
    private Context context;
    private RecyclerView sourceRV;
    private SourceAdapter adapter;
    private IMainActivity iMainActivity;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        professorSourcesBinding = FragmentProfessorSourcesBinding.inflate(inflater, container, false);
        setupUI();
        return professorSourcesBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpFAB();
        setUpViewModel();
        getAssignments();
    }

    private void setupUI() {
        context = getContext();
        sourceRV = professorSourcesBinding.professorSourceRv;
        adapter = new SourceAdapter(context, new ArrayList<Source>(), this);
        sourceRV.setAdapter(adapter);
    }

    private void setUpFAB() {
        iMainActivity = (IMainActivity) getActivity();
        iMainActivity.setFabClickListener(this);
        iMainActivity.showAddFab();
    }

    private void setUpViewModel() {
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        sourceViewModel = viewModelProvider.get(ProfessorSourceViewModel.class);

        sourceViewModel = new ViewModelProvider(this)
                .get(ProfessorSourceViewModel.class);

    }

    private void getAssignments() {
        sourceViewModel.getSources().observe(getViewLifecycleOwner(), new Observer<Resource<List<Source>>>() {
            @Override
            public void onChanged(Resource<List<Source>> listResource) {
                handleSourceResource(listResource);
            }
        });
    }

    private void handleSourceResource(Resource<List<Source>> listResource) {
        if (listResource == null || listResource.data == null)
            return;

        switch (listResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                adapter.updateAdapter(listResource.data);
                String error = AppUtility.getError(context, listResource.data, listResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                adapter.updateAdapter(listResource.data);
                break;
        }
    }

    @Override
    public void onFabClickListener(NavController navController) {
        navController.navigate(R.id.action_professor_source_fragment_to_add_source_fragment);
    }

    @Override
    public void onSourceClickListener(Source source) {
        String path = source.getPath();
        if (URLUtil.isValidUrl(path)) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(source.getPath()));
            startActivity(intent);
        } else {
            Toast.makeText(context, R.string.invalid_source_path_error_message, Toast.LENGTH_SHORT).show();
        }
    }
}
