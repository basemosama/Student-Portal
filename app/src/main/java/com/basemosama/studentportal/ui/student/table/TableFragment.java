package com.basemosama.studentportal.ui.student.table;

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
import com.basemosama.studentportal.databinding.FragmentTableBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;

public class TableFragment extends Fragment {
    private FragmentTableBinding tableBinding;
    private TableViewModel tableViewModel;
    private Context context;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tableBinding = FragmentTableBinding.inflate(inflater, container, false);
        setUpUI();
        return tableBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getTable();
    }

    private void setUpUI() {
        context = getContext();
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        tableViewModel = viewModelProvider.get(TableViewModel.class);
    }

    private void getTable() {
        tableViewModel.getTable().observe(getViewLifecycleOwner(), new Observer<Resource<Table>>() {
            @Override
            public void onChanged(Resource<Table> tableResource) {
                if (tableResource == null || tableResource.data == null)
                    return;

                Table table = tableResource.data;
                switch (tableResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        tableBinding.setTable(table);
                        String error = AppUtility.getError(context, table, tableResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        tableBinding.setTable(table);
                        break;
                }
            }
        });
    }
}
