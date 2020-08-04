package com.basemosama.studentportal.ui.student.group;

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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentGroupBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class GroupFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, PostAdapter.PostClickListener {

    private Context context;
    private FragmentGroupBinding groupBinding;
    private GroupViewModel viewModel;
    private RecyclerView recyclerView;
    private PostAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText makePostEditText;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        groupBinding = FragmentGroupBinding.inflate(inflater, container, false);
        setUpUI();
        return groupBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getPosts();
        getMakePostError();
        getMakingPostResult();
    }

    private void setUpUI() {
        context = getContext();
        recyclerView = groupBinding.postsRv;
        swipeRefreshLayout = groupBinding.postsSwipeContainer;
        makePostEditText = groupBinding.addPostEditText;
        adapter = new PostAdapter(context, new ArrayList<Post>(), this);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();
        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.student_navigation);
        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));
        viewModel = viewModelProvider.get(GroupViewModel.class);
        groupBinding.setViewModel(viewModel);
        groupBinding.setLifecycleOwner(getViewLifecycleOwner());
        groupBinding.setUser(iMainActivity.getMyPreferenceManger().getCurrentUser());
    }

    public void getPosts() {
        viewModel.getPosts().observe(getViewLifecycleOwner(), new Observer<Resource<List<Post>>>() {
            @Override
            public void onChanged(Resource<List<Post>> listResource) {
                if (listResource == null || listResource.data == null)
                    return;
                swipeRefreshLayout.setRefreshing(false);
                List<Post> posts = listResource.data;
                switch (listResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        if (posts != null && posts.size() > 0) {
                            adapter.updateAdapter(posts);
                            groupBinding.postsErrorTextView.setVisibility(View.GONE);
                        } else {
                            groupBinding.postsErrorTextView.setVisibility(View.VISIBLE);
                        }
                        String error = AppUtility.getError(context, posts, listResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        iMainActivity.hideLoading();
                        if (posts != null && posts.size() > 0) {
                            adapter.updateAdapter(posts);
                            groupBinding.postsErrorTextView.setVisibility(View.GONE);
                        } else {
                            groupBinding.postsErrorTextView.setVisibility(View.VISIBLE);
                        }
                        break;
                }
            }
        });
    }

    private void getMakingPostResult() {
        viewModel.getMakePostResult().observe(getViewLifecycleOwner(), new Observer<Resource<Post>>() {
            @Override
            public void onChanged(Resource<Post> postResource) {
                if (postResource == null)
                    return;
                switch (postResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        String error = AppUtility.getError(context, postResource.data, postResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();

                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        Post post = postResource.data;
                        if (post != null) {
                            adapter.addPostToAdapter(post);
                            groupBinding.postsErrorTextView.setVisibility(View.GONE);
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

    @Override
    public void onRefresh() {
        viewModel.setShouldRefreshPosts(true);
    }

    @Override
    public void onPostClickListener(Post post, View view) {
        if (post != null) {
            viewModel.setCurrentPost(post);
            iMainActivity.getNavController().navigate(R.id.action_nav_group_to_comment_fragment);
        }
    }
}
