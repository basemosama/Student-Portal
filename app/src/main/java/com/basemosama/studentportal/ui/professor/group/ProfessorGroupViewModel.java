package com.basemosama.studentportal.ui.professor.group;

import android.app.Application;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.basemosama.studentportal.R;
import com.basemosama.studentportal.model.professor.Group;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.model.student.Reply;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.repository.ProfessorRepository;
import com.basemosama.studentportal.utility.CombinedLiveData;

import java.util.List;

public class ProfessorGroupViewModel extends AndroidViewModel {
    public MutableLiveData<String> makePostBody = new MutableLiveData<>();
    private ProfessorRepository repository;

    private LiveData<Resource<List<Post>>> postsLiveData;
    private LiveData<Resource<List<Comment>>> commentsLiveData;
    private LiveData<Resource<List<Reply>>> repliesLiveData;

    private MutableLiveData<String> makePostError = new MutableLiveData<>();

    private MutableLiveData<Post> currentPostMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Comment> currentCommentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Long> groupIdMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Integer> groupPositionMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<Boolean> shouldRefreshPostsMutableLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> shouldRefreshCommentsMutableLiveData = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> shouldRefreshRepliesMutableLiveData = new MutableLiveData<>(false);

    private CombinedLiveData<Long, Boolean> postsCombinedLiveData;
    private CombinedLiveData<Post, Boolean> commentsCombinedLiveData;
    private CombinedLiveData<Comment, Boolean> repliesCombinedLiveData;

    private LiveData<Resource<List<Group>>> groupsMutableLiveData;

    public ProfessorGroupViewModel(@NonNull Application application) {
        super(application);
        repository = ProfessorRepository.getInstance(application);
        groupsMutableLiveData = repository.getGroups();
        postsCombinedLiveData = new CombinedLiveData<>(groupIdMutableLiveData, shouldRefreshPostsMutableLiveData);
        commentsCombinedLiveData = new CombinedLiveData<>(currentPostMutableLiveData, shouldRefreshCommentsMutableLiveData);
        repliesCombinedLiveData = new CombinedLiveData<>(currentCommentMutableLiveData, shouldRefreshRepliesMutableLiveData);
        updateGroupPosts();
        updateComments();
        updateReplies();
    }


    private void updateGroupPosts() {
        postsLiveData = Transformations.switchMap(postsCombinedLiveData, new Function<Pair<Long, Boolean>, LiveData<Resource<List<Post>>>>() {
            @Override
            public LiveData<Resource<List<Post>>> apply(Pair<Long, Boolean> input) {
                Long groupId = input.first;
                return groupId != null ? repository.getPosts(groupId) : null;
            }
        });
    }

    private void updateComments() {
        commentsLiveData = Transformations.switchMap(commentsCombinedLiveData, new Function<Pair<Post, Boolean>, LiveData<Resource<List<Comment>>>>() {
            @Override
            public LiveData<Resource<List<Comment>>> apply(Pair<Post, Boolean> input) {
                Long postId = input.first.getId();
                return postId != null ? repository.getComments(postId) : null;
            }
        });
    }

    private void updateReplies() {
        repliesLiveData = Transformations.switchMap(repliesCombinedLiveData, new Function<Pair<Comment, Boolean>, LiveData<Resource<List<Reply>>>>() {
            @Override
            public LiveData<Resource<List<Reply>>> apply(Pair<Comment, Boolean> input) {
                Long commentId = input.first.getId();
                return commentId != null ? repository.getReplies(commentId) : null;
            }
        });
    }

    /**
     * Functions For Posts
     **/
    public LiveData<Resource<List<Post>>> getPosts() {
        return postsLiveData;
    }

    public LiveData<Resource<List<Comment>>> getComments() {
        return commentsLiveData;
    }

    public LiveData<Resource<List<Reply>>> getReplies() {
        return repliesLiveData;
    }

    public void makeProfessorPost(View view) {
        String body = makePostBody.getValue();
        if (TextUtils.isEmpty(body)) {
            makePostError.setValue(view.getResources().getString(R.string.post_empty_text_error));
        } else {
            repository.makePost(getGroupId(), body);
        }
    }

    public MutableLiveData<Resource<Post>> getMakePostResult() {
        return repository.getMakePostMutableLiveData();
    }

    public Post getCurrentPost() {
        return currentPostMutableLiveData.getValue();
    }

    public void setCurrentPost(Post post) {
        currentPostMutableLiveData.setValue(post);
    }

    public Comment getCurrentComment() {
        return currentCommentMutableLiveData.getValue();
    }

    public void setCurrentComment(Comment comment) {
        currentCommentMutableLiveData.setValue(comment);
    }


    public MutableLiveData<String> getMakePostError() {
        return makePostError;
    }


    public LiveData<Resource<List<Group>>> getGroups() {
        return groupsMutableLiveData;
    }

    public Long getGroupId() {
        return groupIdMutableLiveData.getValue();
    }

    public void setGroupId(long groupId) {
        groupIdMutableLiveData.setValue(groupId);
    }

    public MutableLiveData<Integer> getGroupPosition() {
        return groupPositionMutableLiveData;
    }

    public void setGroupPosition(int position) {
        groupPositionMutableLiveData.setValue(position);
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
