package com.basemosama.studentportal.repository;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.basemosama.studentportal.database.CacheDataBase;
import com.basemosama.studentportal.database.dao.StudentDao;
import com.basemosama.studentportal.model.network.ApiResponse;
import com.basemosama.studentportal.model.network.ApiResult;
import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.model.student.Event;
import com.basemosama.studentportal.model.student.Marks;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.model.student.Reply;
import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.model.student.StudentResult;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.network.ServiceGenerator;
import com.basemosama.studentportal.network.client.StudentClient;
import com.basemosama.studentportal.network.util.NetworkBoundResource;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.MyPreferenceManger;

import java.util.List;

public class StudentRepository {
    private static StudentRepository repository;
    private StudentClient client;
    private String apiToken;
    private User user;
    private int gradeId, departmentId, studentId;

    private MediatorLiveData<Resource<Post>> postResultLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Comment>> commentResultLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Reply>> replyResultLiveData = new MediatorLiveData<>();

    private StudentDao studentDao;

    public StudentRepository(Context context) {
        studentDao = CacheDataBase.getInstance(context).studentDao();
        client = ServiceGenerator.createService(StudentClient.class);
        MyPreferenceManger myPreferenceManger = new MyPreferenceManger(context);
        apiToken = myPreferenceManger.getApiKey();
        user = myPreferenceManger.getCurrentUser();
        gradeId = user.getGradeId();
        departmentId = user.getDepartmentId();
        studentId = user.getId();
    }


    public static StudentRepository getInstance(Application application) {
        if (repository == null)
            repository = new StudentRepository(application.getApplicationContext());
        return repository;
    }

    public static void clear() {
        repository = null;
    }

    /*** Get Assignments from API**/
    public LiveData<Resource<List<Assignment>>> getAssignmentsLiveData() {
        return new NetworkBoundResource<List<Assignment>, List<Assignment>>() {
            @Override
            protected void saveCallResult(@NonNull List<Assignment> items) {
                studentDao.insertAssignments(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Assignment> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Assignment>> loadFromDb() {
                return studentDao.getAssignments();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Assignment>>>> createCall() {
                return client.getAssignments(gradeId);
            }
        }.getAsLiveData();
    }

    // Function To get certain subject assignments
    public LiveData<Resource<List<Assignment>>> getSubjectAssignments(final int subjectId) {
        return new NetworkBoundResource<List<Assignment>, List<Assignment>>() {
            @Override
            protected void saveCallResult(@NonNull List<Assignment> items) {
                studentDao.insertAssignments(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Assignment> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Assignment>> loadFromDb() {
                return studentDao.getSubjectAssignments(subjectId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Assignment>>>> createCall() {
                return client.getSubjectAssignments(gradeId, subjectId);
            }
        }.getAsLiveData();
    }


    /*** Get Sources for a certain Subject from API**/
    public LiveData<Resource<List<Source>>> getSubjectSources(final int subjectId) {
        return new NetworkBoundResource<List<Source>, List<Source>>() {
            @Override
            protected void saveCallResult(@NonNull List<Source> items) {
                studentDao.insertSources(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Source> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Source>> loadFromDb() {
                return studentDao.getSubjectSources(subjectId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Source>>>> createCall() {
                return client.getSubjectSources(gradeId, subjectId);
            }
        }.getAsLiveData();
    }


    /*** Get Year Work Marks from API**/
    public LiveData<Resource<Marks>> getMarks(final int subjectId) {
        return new NetworkBoundResource<Marks, Marks>() {
            @Override
            protected void saveCallResult(@NonNull Marks marks) {
                studentDao.insertSubjectMarks(marks);
            }

            @Override
            protected boolean shouldFetch(@Nullable Marks data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Marks> loadFromDb() {
                return studentDao.getSubjectMarks(subjectId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<Marks>>> createCall() {
                return client.getMarks(subjectId, studentId);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<Post>>> getPostsMutableLiveData() {
        return new NetworkBoundResource<List<Post>, List<Post>>() {
            @Override
            protected void saveCallResult(@NonNull List<Post> items) {
                for (Post post : items) {
                    long noOfComments = post.getComments() == null ? 0 : post.getComments().size();
                    post.setNoOfComments(noOfComments);
                }
                studentDao.insertPosts(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Post> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Post>> loadFromDb() {
                return studentDao.getPosts();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Post>>>> createCall() {
                return client.getPosts(apiToken);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<Comment>>> getCommentsMutableLiveData(final long postId) {
        return new NetworkBoundResource<List<Comment>, List<Comment>>() {
            @Override
            protected void saveCallResult(@NonNull List<Comment> items) {
                for (Comment comment : items) {
                    List<Reply> replies = comment.getReplies();
                    if (comment.getReplies() != null && replies.size() > 0) {
                        long noOfReplies = replies.size();
                        Reply reply = replies.get(0);
                        comment.setNoOfReplies(noOfReplies);
                        comment.setLatestReply(reply);
                    }
                }
                studentDao.insertComments(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Comment> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Comment>> loadFromDb() {
                return studentDao.getComments(postId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Comment>>>> createCall() {
                return client.getComments(postId);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<Reply>>> getRepliesMutableLiveData(final long commentId) {
        return new NetworkBoundResource<List<Reply>, List<Reply>>() {
            @Override
            protected void saveCallResult(@NonNull List<Reply> items) {
                studentDao.insertReplies(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Reply> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Reply>> loadFromDb() {
                return studentDao.getReplies(commentId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Reply>>>> createCall() {
                return client.getReplies(commentId);
            }
        }.getAsLiveData();
    }


    public void makePost(String body) {
        postResultLiveData.setValue(Resource.loading((Post) null));
        final LiveData<ApiResult<ApiResponse<Post>>> apiResultLiveData = client.makePost(apiToken, body);
        postResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<Post>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<Post>> apiResult) {
                postResultLiveData.removeSource(apiResultLiveData);
                if (apiResult instanceof ApiResult.Success) {
                    Post post = (Post) ((ApiResult.Success) apiResult).getData();
                    postResultLiveData.setValue(Resource.success((post)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    postResultLiveData.setValue(Resource.error(error, (Post) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    postResultLiveData.setValue(Resource.error(error, (Post) null));
                }
            }
        });
    }


    public void makeComment(String body, long postId) {
        commentResultLiveData.setValue(Resource.loading((Comment) null));
        final LiveData<ApiResult<ApiResponse<Comment>>> apiResultLiveData = client.makeComment(apiToken, body, postId);
        commentResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<Comment>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<Comment>> apiResult) {
                commentResultLiveData.removeSource(apiResultLiveData);
                if (apiResult instanceof ApiResult.Success) {
                    Comment comment = (Comment) ((ApiResult.Success) apiResult).getData();
                    commentResultLiveData.setValue(Resource.success((comment)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    commentResultLiveData.setValue(Resource.error(error, (Comment) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    commentResultLiveData.setValue(Resource.error(error, (Comment) null));
                }
            }
        });
    }

    public void makeReply(String body, long commentId) {
        replyResultLiveData.setValue(Resource.loading((Reply) null));
        final LiveData<ApiResult<ApiResponse<Reply>>> apiResultLiveData = client.makeReply(apiToken, body, commentId);
        replyResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<Reply>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<Reply>> apiResult) {
                replyResultLiveData.removeSource(apiResultLiveData);
                if (apiResult instanceof ApiResult.Success) {
                    Reply reply = (Reply) ((ApiResult.Success) apiResult).getData();
                    replyResultLiveData.setValue(Resource.success((reply)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    replyResultLiveData.setValue(Resource.error(error, (Reply) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    replyResultLiveData.setValue(Resource.error(error, (Reply) null));
                }
            }
        });
    }

    public MutableLiveData<Resource<Post>> getMakePostMutableLiveData() {
        return postResultLiveData;
    }

    public MutableLiveData<Resource<Comment>> getMakeCommentMutableLiveData() {
        return commentResultLiveData;
    }

    public MutableLiveData<Resource<Reply>> getMakeReplyMutableLiveData() {
        return replyResultLiveData;
    }


    public LiveData<Resource<List<Event>>> getEventMutableLiveData() {
        return new NetworkBoundResource<List<Event>, List<Event>>() {
            @Override
            protected void saveCallResult(@NonNull List<Event> items) {
                studentDao.insertEvents(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Event> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Event>> loadFromDb() {
                return studentDao.getEvents();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Event>>>> createCall() {
                return client.getEvents(departmentId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Subject>>> getSubjectMutableLiveData() {
        return new NetworkBoundResource<List<Subject>, List<Subject>>() {

            @Override
            protected void saveCallResult(@NonNull List<Subject> subjects) {
                studentDao.insertSubjects(subjects);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Subject> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Subject>> loadFromDb() {
                return studentDao.getSubjects();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Subject>>>> createCall() {
                return client.getSubjects(gradeId, departmentId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<Table>> getTableMutableLiveData() {
        return new NetworkBoundResource<Table, List<Table>>() {
            @Override
            protected void saveCallResult(@NonNull List<Table> items) {
                studentDao.insertTables(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable Table data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Table> loadFromDb() {
                return studentDao.getTable();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Table>>>> createCall() {
                return client.getTable(gradeId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<StudentResult>> getResultMutableLiveData() {
        return new NetworkBoundResource<StudentResult, List<StudentResult>>() {

            @Override
            protected void saveCallResult(@NonNull List<StudentResult> items) {
                studentDao.insertStudentResults(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable StudentResult data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<StudentResult> loadFromDb() {
                return studentDao.getStudentResult();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<StudentResult>>>> createCall() {
                return client.getResults(departmentId, studentId);
            }
        }.getAsLiveData();
    }
}
