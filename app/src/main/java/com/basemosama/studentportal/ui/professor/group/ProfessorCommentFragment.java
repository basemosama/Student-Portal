package com.basemosama.studentportal.ui.professor.group;

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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.databinding.FragmentProfessorCommentBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.ui.student.group.CommentAdapter;
import com.basemosama.studentportal.utility.AppUtility;

import java.util.ArrayList;
import java.util.List;

public class ProfessorCommentFragment extends Fragment implements CommentAdapter.CommentClickListener, SwipeRefreshLayout.OnRefreshListener {

    private Context context;
    private FragmentProfessorCommentBinding commentBinding;
    private ProfessorGroupViewModel viewModel;
    private RecyclerView recyclerView;
    private CommentAdapter adapter;
    private IMainActivity iMainActivity;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        commentBinding = FragmentProfessorCommentBinding.inflate(inflater, container, false);
        setUpUI();
        return commentBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getComments();
    }

    private void setUpUI() {
        context = getContext();
        recyclerView = commentBinding.professorCommentsRv;
        swipeRefreshLayout = commentBinding.commentSwipeContainer;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new CommentAdapter(context, new ArrayList<Comment>(), this);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void setUpViewModel() {
        iMainActivity = (IMainActivity) getActivity();

        NavController navController = iMainActivity.getNavController();
        NavBackStackEntry backStackEntry = navController.getBackStackEntry(R.id.professor_navigation);

        ViewModelProvider viewModelProvider = new ViewModelProvider(
                backStackEntry.getViewModelStore(),
                new SavedStateViewModelFactory(
                        requireActivity().getApplication(), requireParentFragment()));

        viewModel = viewModelProvider.get(ProfessorGroupViewModel.class);

        commentBinding.setUser(iMainActivity.getMyPreferenceManger().getCurrentUser());

    }


    private void getComments() {
        viewModel.getComments().observe(getViewLifecycleOwner(), new Observer<Resource<List<Comment>>>() {
            @Override
            public void onChanged(Resource<List<Comment>> listResource) {
                handleCommentResource(listResource);
            }
        });
    }

    private void handleCommentResource(Resource<List<Comment>> listResource) {
        if (listResource == null || listResource.data == null)
            return;

        swipeRefreshLayout.setRefreshing(false);
        List<Comment> comments = listResource.data;

        switch (listResource.status) {
            case LOADING:
                iMainActivity.showLoading();
                commentBinding.professorCommentsErrorTextView.setVisibility(View.GONE);
                break;

            case ERROR:
                iMainActivity.hideLoading();
                if (comments != null && comments.size() > 0) {
                    adapter.updateAdapter(comments);
                    recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                    commentBinding.professorCommentsErrorTextView.setVisibility(View.GONE);
                } else {
                    commentBinding.professorCommentsErrorTextView.setVisibility(View.VISIBLE);
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
                    commentBinding.professorCommentsErrorTextView.setVisibility(View.GONE);
                } else {
                    commentBinding.professorCommentsErrorTextView.setVisibility(View.VISIBLE);
                }
                break;
        }
    }


    @Override
    public void onCommentClickListener(Comment comment) {
        if (comment != null) {
            viewModel.setCurrentComment(comment);
            NavController navController = Navigation.findNavController(getView());
            navController.navigate(R.id.action_professor_comment_fragment_to_professorReplyFragment);
        }
    }

    @Override
    public void onRefresh() {
        viewModel.setShouldRefreshComments(true);
    }
}
