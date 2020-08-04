package com.basemosama.studentportal.ui.professor.group;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorGroupBinding;
import com.basemosama.studentportal.interfaces.DrawerListener;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.professor.Group;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.ui.student.group.PostAdapter;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;
import com.skydoves.powerspinner.OnSpinnerItemSelectedListener;
import com.skydoves.powerspinner.PowerSpinnerView;

import java.util.ArrayList;
import java.util.List;

public class ProfessorGroupFragment extends Fragment implements PostAdapter.PostClickListener, SwipeRefreshLayout.OnRefreshListener, DrawerListener {

    private Context context;
    private FragmentProfessorGroupBinding groupBinding;
    private ProfessorGroupViewModel viewModel;
    private PowerSpinnerView groupSpinner;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText makePostEditText;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupBinding = FragmentProfessorGroupBinding.inflate(inflater, container, false);
        setUpUI();
        return groupBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getGroups();
        getPosts();
        getMakePostError();
        getMakingPostResult();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (groupSpinner.isShowing())
            groupSpinner.dismiss();
    }

    private void setUpUI() {
        context = getContext();
        groupSpinner = groupBinding.groupSpinner;
        recyclerView = groupBinding.professorPostsRv;
        swipeRefreshLayout = groupBinding.groupLayout;
        makePostEditText = groupBinding.addProfessorPostEditText;
        adapter = new PostAdapter(context, new ArrayList<Post>(), this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        iMainActivity.setDrawerListener(this);

        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        viewModel = viewModelProvider.get(ProfessorGroupViewModel.class);

        groupBinding.setViewModel(viewModel);
        groupBinding.setLifecycleOwner(getViewLifecycleOwner());
        groupBinding.setUser(iMainActivity.getMyPreferenceManger().getCurrentUser());

    }

    private void getGroups() {
        viewModel.getGroups().observe(getViewLifecycleOwner(), new Observer<Resource<List<Group>>>() {
            @Override
            public void onChanged(Resource<List<Group>> listResource) {
                handleGroupResource(listResource);
            }
        });
    }

    public void getPosts() {
        viewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                handlePostResource(listResource);
            }
        });
    }

    private void getMakingPostResult() {

        viewModel.getMakePostResult().observe(getViewLifecycleOwner(), new Observer<Resource<Post>>() {
            @Override
            public void onChanged(Resource<Post> postResource) {

                if (postResource == null)
                    return;

                Post post = postResource.data;
                switch (postResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        String error = AppUtility.getError(context, post, postResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        if (post != null) {
                            adapter.addPostToAdapter(post);
                            groupBinding.professorPostsErrorTextView.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.add_post_success_message, Toast.LENGTH_SHORT).show();
                            makePostEditText.setText("");
                        } else {
                            Toast.makeText(context, Constants.MAIN_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                viewModel.getMakePostResult().setValue(null);

            }
        });

    }


    private void getMakePostError() {
        viewModel.getMakePostError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                makePostEditText.setError(error);
            }
        });
    }


    private void handleGroupResource(Resource<List<Group>> listResource) {
        if (listResource == null || listResource.data == null)
            return;

        final List<Group> groups = listResource.data;
        List<String> groupNames = new ArrayList<>();
        for (Group group : groups)
            groupNames.add(group.getName());
        Integer groupPosition = viewModel.getGroupPosition().getValue();


        switch (listResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                groupSpinner.setItems(groupNames);
                if (viewModel.getGroupPosition() != null && groupPosition != null) {
                    groupSpinner.selectItemByIndex(groupPosition);
                }
                groupSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, Object o) {
                        long groupId = groups.get(position).getId();
                        if (viewModel.getGroupPosition().getValue() == null || viewModel.getGroupPosition().getValue() != position) {
                            viewModel.setGroupPosition(position);
                        }
                        if (viewModel.getGroupId() == null || viewModel.getGroupId() != groupId) {
                            viewModel.setGroupId(groupId);
                        }
                    }
                });
                String error = AppUtility.getError(context, groups, listResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                groupSpinner.setItems(groupNames);
                if (viewModel.getGroupPosition() != null && groupPosition != null) {
                    groupSpinner.selectItemByIndex(groupPosition);
                }
                groupSpinner.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
                    @Override
                    public void onItemSelected(int position, Object o) {
                        long groupId = groups.get(position).getId();
                        if (viewModel.getGroupPosition().getValue() == null || viewModel.getGroupPosition().getValue() != position) {
                            viewModel.setGroupPosition(position);
                        }
                        if (viewModel.getGroupId() == null || viewModel.getGroupId() != groupId) {
                            viewModel.setGroupId(groupId);
                        }
                    }
                });
                break;
        }
    }

    private void handlePostResource(Resource<List<Post>> listResource) {
        if (listResource == null || listResource.data == null)
            return;
        swipeRefreshLayout.setRefreshing(false);
        groupBinding.professorChooseGroupTextView.setVisibility(View.GONE);

        List<Post> posts = listResource.data;

        switch (listResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                break;

            case ERROR:
                iMainActivity.hideLoading();
                if (posts != null && posts.size() > 0) {
                    adapter.updateAdapter(posts);
                    groupBinding.groupLayout.setVisibility(View.VISIBLE);
                    groupBinding.professorPostsErrorTextView.setVisibility(View.GONE);
                } else {
                    groupBinding.professorPostsErrorTextView.setVisibility(View.VISIBLE);
                }
                String error = AppUtility.getError(context, posts, listResource.message);
                if (!TextUtils.isEmpty(error))
                    Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                break;

            case SUCCESS:
                iMainActivity.hideLoading();
                if (posts != null && posts.size() > 0) {
                    adapter.updateAdapter(posts);
                    groupBinding.groupLayout.setVisibility(View.VISIBLE);
                    groupBinding.professorPostsErrorTextView.setVisibility(View.GONE);
                } else {
                    groupBinding.professorPostsErrorTextView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onPostClickListener(Post post, View view) {
        if (post != null) {
            viewModel.setCurrentPost(post);
            NavController navController = Navigation.findNavController(view);
            navController.navigate(R.id.action_nav_group_to_professor_comment_fragment);
        }
    }

    @Override
    public void onRefresh() {
        viewModel.setShouldRefreshPosts(true);
    }

    @Override
    public void onDrawerStateChanged() {
        if (groupSpinner.isShowing())
            groupSpinner.dismiss();
    }


}
