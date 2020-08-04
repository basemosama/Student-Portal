package com.basemosama.studentportal.ui.student.group;

import android.app.Application;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.model.student.Reply;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.StudentRepository;
import com.basemosama.studentportal.utility.CombinedLiveData;

import java.util.List;

public class GroupViewModel extends AndroidViewModel {
    public MutableLiveData<String> makePostBody = new MutableLiveData<>();
    public MutableLiveData<String> makeCommentBody = new MutableLiveData<>();
    public MutableLiveData<String> makeReplyBody = new MutableLiveData<>();
    private StudentRepository repository;

    private MutableLiveData<Post> currentPostMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Comment> currentCommentMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<String> makePostError = new MutableLiveData<>();
    private MutableLiveData<String> makeCommentError = new MutableLiveData<>();
    private MutableLiveData<String> makeReplyError = new MutableLiveData<>();

    private LiveData<Resource<List<Post>>> postsLiveData = new MediatorLiveData<>();
    private LiveData<Resource<List<Comment>>> currentPostComments = new MutableLiveData<>();
    private LiveData<Resource<List<Reply>>> currentCommentReplies = new MutableLiveData<>();

    private MutableLiveData<Resource<Post>> postResultLiveData;
    private MutableLiveData<Resource<Comment>> commentResultLiveData;
    private MutableLiveData<Resource<Reply>> replyResultLiveData;

    private MutableLiveData<Boolean> shouldRefreshPostsMutableLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> shouldRefreshCommentsMutableLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> shouldRefreshRepliesMutableLiveData = new MutableLiveData<>(false);

    private CombinedLiveData<Post, Boolean> commentsCombinedLiveData;
    private CombinedLiveData<Comment, Boolean> repliesCombinedLiveData;

    public GroupViewModel(@NonNull Application application) {
        super(application);
        repository = StudentRepository.getInstance(application);

        commentsCombinedLiveData = new CombinedLiveData<>(currentPostMutableLiveData, shouldRefreshCommentsMutableLiveData);
        repliesCombinedLiveData = new CombinedLiveData<>(currentCommentMutableLiveData, shouldRefreshRepliesMutableLiveData);

        refreshPosts();
        updateCurrentPostComments();
        updateCurrentCommentReplies();

        postResultLiveData = repository.getMakePostMutableLiveData();
        commentResultLiveData = repository.getMakeCommentMutableLiveData();
        replyResultLiveData = repository.getMakeReplyMutableLiveData();
    }


    public void refreshPosts() {
        postsLiveData = Transformations.switchMap(shouldRefreshPostsMutableLiveData, new Function<Boolean, LiveData<Resource<List<Post>>>>() {
            @Override
            public LiveData<Resource<List<Post>>> apply(Boolean input) {
                return repository.getPostsMutableLiveData();
            }
        });
    }

    public void updateCurrentPostComments() {
        currentPostComments = Transformations.switchMap(commentsCombinedLiveData, new Function<Pair<Post, Boolean>, LiveData<Resource<List<Comment>>>>() {
            @Override
            public LiveData<Resource<List<Comment>>> apply(Pair<Post, Boolean> input) {
                Long postId = input.first.getId();
                return postId != null ? repository.getCommentsMutableLiveData(postId) : null;
            }
        });
    }

    public void updateCurrentCommentReplies() {
        currentCommentReplies = Transformations.switchMap(repliesCombinedLiveData, new Function<Pair<Comment, Boolean>, LiveData<Resource<List<Reply>>>>() {
            @Override
            public LiveData<Resource<List<Reply>>> apply(Pair<Comment, Boolean> input) {
                Long commentId = input.first.getId();
                return commentId != null ? repository.getRepliesMutableLiveData(commentId) : null;
            }
        });
    }

    public void makePost(View view) {
        String body = makePostBody.getValue();
        if (TextUtils.isEmpty(body)) {
            makePostError.setValue(view.getResources().getString(R.string.post_empty_text_error));
        } else {
            repository.makePost(body);
        }
    }

    public void makeComment(View view) {
        String body = makeCommentBody.getValue();
        if (TextUtils.isEmpty(body)) {
            makeCommentError.setValue(view.getResources().getString(R.string.comment_empty_text_error));
        } else {
            repository.makeComment(body, getCurrentPost().getId());
        }
    }

    public void makeReply(View view) {
        String body = makeReplyBody.getValue();
        if (TextUtils.isEmpty(body)) {
            makeReplyError.setValue(view.getResources().getString(R.string.reply_empty_text_error));
        } else {
            repository.makeReply(body, getCurrentComment().getId());
        }
    }

    //data
    public LiveData<Resource<List<Post>>> getPosts() {
        return postsLiveData;
    }

    public LiveData<Resource<List<Comment>>> getCurrentPostComments() {
        return currentPostComments;
    }

    public LiveData<Resource<List<Reply>>> getCurrentCommentReplies() {
        return currentCommentReplies;
    }

    //make post and comment and reply result
    public MutableLiveData<Resource<Post>> getMakePostResult() {
        return postResultLiveData;
    }

    public MutableLiveData<Resource<Comment>> getMakeCommentResult() {
        return commentResultLiveData;
    }

    public MutableLiveData<Resource<Reply>> getMakeReplyResult() {
        return replyResultLiveData;
    }

    //make post and comment and reply Fields Error
    public MutableLiveData<String> getMakePostError() {
        return makePostError;
    }

    public MutableLiveData<String> getMakeCommentError() {
        return makeCommentError;
    }

    public MutableLiveData<String> getMakeReplyError() {
        return makeReplyError;
    }

    public Post getCurrentPost() {
        return currentPostMutableLiveData.getValue();
    }

    //setter and getters
    public void setCurrentPost(Post post) {
        currentPostMutableLiveData.setValue(post);
    }

    public Comment getCurrentComment() {
        return currentCommentMutableLiveData.getValue();
    }

    public void setCurrentComment(Comment comment) {
        currentCommentMutableLiveData.setValue(comment);
    }

    public void setShouldRefreshPosts(boolean value) {
        shouldRefreshPostsMutableLiveData.setValue(value);
    }

    public void setShouldRefreshComments(boolean value) {
        shouldRefreshCommentsMutableLiveData.setValue(value);
    }

    public void setShouldRefreshReplies(boolean value) {
        shouldRefreshRepliesMutableLiveData.setValue(value);
    }

}
