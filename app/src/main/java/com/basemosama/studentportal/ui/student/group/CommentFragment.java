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
import com.basemosama.studentportal.databinding.FragmentCommentBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class CommentFragment extends Fragment implements CommentAdapter.CommentClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private FragmentCommentBinding commentBinding;
    private GroupViewModel viewModel;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText makeCommentEditText;
    private IMainActivity iMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        commentBinding = FragmentCommentBinding.inflate(inflater, container, false);
        setUpUI();
        return commentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getComments();
        getMakeCommentError();
        getMakingCommentResult();
    }

    private void setUpUI() {
        context = getContext();
        recyclerView = commentBinding.commentsRv;
        makeCommentEditText = commentBinding.addCommentEditText;
        swipeRefreshLayout = commentBinding.commentSwipeContainer;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CommentAdapter(context, new ArrayList<Comment>(), this);
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
        commentBinding.setViewModel(viewModel);
        commentBinding.setLifecycleOwner(getViewLifecycleOwner());
        commentBinding.setUser(iMainActivity.getMyPreferenceManger().getCurrentUser());
    }

    private void getComments() {
        viewModel.getCurrentPostComments().observe(getViewLifecycleOwner(), new Observer<Resource<List<Comment>>>() {
            @Override
            public void onChanged(Resource<List<Comment>> listResource) {
                if (listResource == null || listResource.data == null)
                    return;
                swipeRefreshLayout.setRefreshing(false);
                List<Comment> comments = listResource.data;
                switch (listResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        if (comments != null && comments.size() > 0) {
                            adapter.updateAdapter(comments);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            commentBinding.commentsErrorTextView.setVisibility(View.GONE);
                        } else {
                            commentBinding.commentsErrorTextView.setVisibility(View.VISIBLE);
                        }
                        String error = AppUtility.getError(context, comments, listResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        if (comments != null && comments.size() > 0) {
                            adapter.updateAdapter(comments);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                            commentBinding.commentsErrorTextView.setVisibility(View.GONE);
                        } else {
                            commentBinding.commentsErrorTextView.setVisibility(View.VISIBLE);
                        }
                        break;
                }

            }

        });
    }

    private void getMakingCommentResult() {
        viewModel.getMakeCommentResult().observe(getViewLifecycleOwner(), new Observer<Resource<Comment>>() {
            @Override
            public void onChanged(Resource<Comment> commentResource) {
                if (commentResource == null)
                    return;
                switch (commentResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;
                    case ERROR:
                        iMainActivity.hideLoading();
                        String error = AppUtility.getError(context, commentResource.data, commentResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;
                    case SUCCESS:
                        iMainActivity.hideLoading();
                        Comment comment = commentResource.data;
                        if (comment != null) {
                            adapter.addCommentToAdapter(comment);
                            commentBinding.commentsErrorTextView.setVisibility(View.GONE);
                            Toast.makeText(context, R.string.add_comment_success_message, Toast.LENGTH_SHORT).show();
                            makeCommentEditText.setText("");
                        } else {
                            Toast.makeText(context, Constants.MAIN_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                viewModel.getMakeCommentResult().setValue(null);
            }
        });
    }

    private void getMakeCommentError() {
        viewModel.getMakeCommentError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                makeCommentEditText.setError(error);
            }
        });
    }

    @Override
    public void onCommentClickListener(Comment comment) {
        if (comment != null) {
            viewModel.setCurrentComment(comment);
            iMainActivity.getNavController().navigate(R.id.action_comment_fragment_to_reply_fragment);
        }
    }

    @Override
    public void onRefresh() {
        viewModel.setShouldRefreshComments(true);
    }
}
