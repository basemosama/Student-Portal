package com.basemosama.studentportal.ui.student.group;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
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
import com.basemosama.studentportal.databinding.FragmentReplyBinding;
import com.basemosama.studentportal.interfaces.IMainActivity;
import com.basemosama.studentportal.model.student.Reply;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.AppUtility;
import com.basemosama.studentportal.utility.Constants;

import java.util.ArrayList;
import java.util.List;

public class ReplyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Context context;
    private FragmentReplyBinding replyBinding;
    private GroupViewModel viewModel;
    private RecyclerView recyclerView;
    private ReplyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private EditText makeReplyEditText;
    private IMainActivity iMainActivity;
    private TextView likeTv;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        replyBinding = FragmentReplyBinding.inflate(inflater, container, false);
        setUpUI();
        return replyBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewModel();
        getReplies();
        getMakeReplyError();
        getMakingReplyResult();
        handleLikeButtonClickForComment();
    }

    private void setUpUI() {
        context = getContext();
        recyclerView = replyBinding.commentsRv;
        makeReplyEditText = replyBinding.addReplyEditText;
        swipeRefreshLayout = replyBinding.replySwipeContainer;
        likeTv = replyBinding.replyCommentLikeTv;
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        adapter = new ReplyAdapter(context, new ArrayList<Reply>());
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
        replyBinding.setViewModel(viewModel);
        replyBinding.setLifecycleOwner(getViewLifecycleOwner());
        replyBinding.setNoOfLikes(viewModel.getCurrentComment().getNoOfLikes());
    }

    private void getReplies() {
        viewModel.getCurrentCommentReplies().observe(getViewLifecycleOwner(), new Observer<Resource<List<Reply>>>() {
            @Override
            public void onChanged(Resource<List<Reply>> listResource) {
                if (listResource == null || listResource.data == null)
                    return;
                swipeRefreshLayout.setRefreshing(false);
                List<Reply> replies = listResource.data;
                switch (listResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;
                    case ERROR:
                        iMainActivity.hideLoading();
                        if (replies != null && replies.size() > 0) {
                            adapter.updateAdapter(replies);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        }
                        String error = AppUtility.getError(context, replies, listResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        if (replies != null && replies.size() > 0) {
                            adapter.updateAdapter(replies);
                            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        }
                        break;
                }

            }
        });

    }

    private void getMakingReplyResult() {
        viewModel.getMakeReplyResult().observe(getViewLifecycleOwner(), new Observer<Resource<Reply>>() {
            @Override
            public void onChanged(Resource<Reply> replyResource) {
                if (replyResource == null)
                    return;

                Reply reply = replyResource.data;
                switch (replyResource.status) {
                    case LOADING:
                        iMainActivity.showLoading();
                        break;

                    case ERROR:
                        iMainActivity.hideLoading();
                        String error = AppUtility.getError(context, reply, replyResource.message);
                        if (!TextUtils.isEmpty(error))
                            Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                        break;

                    case SUCCESS:
                        iMainActivity.hideLoading();
                        if (reply != null) {
                            adapter.addReplyToAdapter(reply);
                            Toast.makeText(context, R.string.add_reply_success_message, Toast.LENGTH_SHORT).show();
                            makeReplyEditText.setText("");
                        } else {
                            Toast.makeText(context, Constants.MAIN_ERROR_MESSAGE, Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
                viewModel.getMakeReplyResult().setValue(null);

            }
        });

    }

    private void getMakeReplyError() {
        viewModel.getMakeReplyError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String error) {
                makeReplyEditText.setError(error);
            }
        });
    }

    @Override
    public void onRefresh() {
        viewModel.setShouldRefreshReplies(true);
    }


    private void handleLikeButtonClickForComment() {
        likeTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long noOfLikes = viewModel.getCurrentComment().getNoOfLikes();
                String tag = String.valueOf(likeTv.getTag());
                if (!TextUtils.equals(tag, Constants.LIKED_TAG)) {
                    likeTv.setTextColor(context.getResources().getColor(R.color.colorBlue));
                    likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like, 0, 0, 0);
                    likeTv.setTag(Constants.LIKED_TAG);
                    noOfLikes = noOfLikes + 1;
                } else {
                    likeTv.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    likeTv.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_border, 0, 0, 0);
                    likeTv.setTag("");
                    noOfLikes = noOfLikes - 1;
                }
                replyBinding.setNoOfLikes(noOfLikes);
            }
        });
    }


}
