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
import com.basemosama.studentportal.database.dao.ProfessorDao;
import com.basemosama.studentportal.database.executor.AppExecutor;
import com.basemosama.studentportal.model.network.ApiResponse;
import com.basemosama.studentportal.model.network.ApiResult;
import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.Group;
import com.basemosama.studentportal.model.professor.Prediction;
import com.basemosama.studentportal.model.professor.ProfessorGrade;
import com.basemosama.studentportal.model.professor.ProfessorProfileData;
import com.basemosama.studentportal.model.student.Assignment;
import com.basemosama.studentportal.model.student.Comment;
import com.basemosama.studentportal.model.student.Event;
import com.basemosama.studentportal.model.student.Marks;
import com.basemosama.studentportal.model.student.Post;
import com.basemosama.studentportal.model.student.Reply;
import com.basemosama.studentportal.model.student.Source;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.model.student.User;
import com.basemosama.studentportal.network.ServiceGenerator;
import com.basemosama.studentportal.network.client.ProfessorClient;
import com.basemosama.studentportal.network.util.NetworkBoundResource;
import com.basemosama.studentportal.network.util.Resource;
import com.basemosama.studentportal.utility.Constants;
import com.basemosama.studentportal.utility.MyPreferenceManger;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfessorRepository {
    private static ProfessorRepository repository;
    private ProfessorClient client;
    private ProfessorClient predictionClient;
    private String apiToken;
    private User professor;
    private int departmentId;
    private MediatorLiveData<Resource<Assignment>> addAssignmentResultLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Source>> addSourceResultLiveData = new MediatorLiveData<>();

    private MediatorLiveData<Resource<Marks>> addMarksResultLiveData = new MediatorLiveData<>();
    private MutableLiveData<Resource<Prediction>> predictionMutableLiveData = new MutableLiveData<>();

    private MediatorLiveData<Resource<Post>> postResultLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Comment>> commentResultLiveData = new MediatorLiveData<>();
    private MediatorLiveData<Resource<Reply>> replyResultLiveData = new MediatorLiveData<>();
    private ProfessorDao professorDao;

    public ProfessorRepository(Context context) {
        professorDao = CacheDataBase.getInstance(context).professorDao();
        client = ServiceGenerator.createService(ProfessorClient.class);
        predictionClient = ServiceGenerator.createPredictionService(ProfessorClient.class);
        MyPreferenceManger myPreferenceManger = new MyPreferenceManger(context);
        apiToken = myPreferenceManger.getApiKey();
        professor = myPreferenceManger.getCurrentUser();
        departmentId = professor.getDepartmentId();
    }

    public static ProfessorRepository getInstance(Application application) {
        if (repository == null)
            repository = new ProfessorRepository(application.getApplicationContext());
        return repository;
    }

    public static void clear() {
        repository = null;
    }

    public LiveData<Resource<List<ProfessorGrade>>> getGrades() {
        return new NetworkBoundResource<List<ProfessorGrade>, List<ProfessorGrade>>() {
            @Override
            protected void saveCallResult(@NonNull List<ProfessorGrade> items) {
                for (ProfessorGrade grade : items) {
                    grade.setTimeStamp(System.currentTimeMillis() / 1000);
                }
                professorDao.insertGrades(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<ProfessorGrade> data) {
                long currentTime = System.currentTimeMillis() / 1000;
                if (data == null || data.size() == 0) {
                    return true;
                } else {
                    for (ProfessorGrade grade : data) {
                        long lastRefresh = grade.getTimeStamp();
                        return (currentTime - lastRefresh) >= Constants.DATABASE_UPDATE_INTERVAL;
                    }
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<List<ProfessorGrade>> loadFromDb() {
                return professorDao.getGrades();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<ProfessorGrade>>>> createCall() {
                return client.getGrades();
            }
        }.getAsLiveData();
    }

    /**
     * Get All Departments without using any Params
     */
    public LiveData<Resource<List<Department>>> getDepartments() {
        return new NetworkBoundResource<List<Department>, List<Department>>() {
            @Override
            protected void saveCallResult(@NonNull List<Department> items) {
                professorDao.insertDepartments(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Department> data) {

                long currentTime = System.currentTimeMillis() / 1000;
                if (data == null || data.size() == 0) {
                    return true;
                } else {
                    for (Department department : data) {
                        long lastRefresh = department.getTimeStamp();
                        return (currentTime - lastRefresh) >= Constants.DATABASE_UPDATE_INTERVAL;
                    }
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<List<Department>> loadFromDb() {
                return professorDao.getDepartments();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Department>>>> createCall() {
                return client.getDepartments();
            }
        }.getAsLiveData();
    }

    /**
     * Get Subjects For Specific Grade and Department
     */
    public LiveData<Resource<List<Subject>>> getSubjectsForSpecificGrade(final int departmentId, final int gradeId) {
        return new NetworkBoundResource<List<Subject>, List<Subject>>() {
            @Override
            protected void saveCallResult(@NonNull List<Subject> items) {
                professorDao.insertSubjects(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Subject> data) {
                long currentTime = System.currentTimeMillis() / 1000;
                if (data == null || data.size() == 0) {
                    return true;
                } else {
                    for (Subject subject : data) {
                        long lastRefresh = subject.getTimeStamp();
                        if ((currentTime - lastRefresh) >= Constants.DATABASE_UPDATE_INTERVAL)
                            return true;
                    }
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<List<Subject>> loadFromDb() {
                return professorDao.getSubjectsForSpecificGrade(departmentId, gradeId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Subject>>>> createCall() {
                return client.getSubjectsForSpecificGrade(gradeId, departmentId);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<Assignment>>> getAssignments() {
        return new NetworkBoundResource<List<Assignment>, List<Assignment>>() {
            @Override
            protected void saveCallResult(@NonNull List<Assignment> items) {
                professorDao.insertAssignments(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Assignment> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Assignment>> loadFromDb() {
                return professorDao.getAssignments();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Assignment>>>> createCall() {
                return client.getAssignments(apiToken);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Source>>> getSources() {
        return new NetworkBoundResource<List<Source>, List<Source>>() {
            @Override
            protected void saveCallResult(@NonNull List<Source> items) {
                professorDao.insertSources(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Source> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Source>> loadFromDb() {
                return professorDao.getSources();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Source>>>> createCall() {
                return client.getSources(apiToken);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<Table>> getTable(final int gradeId) {
        return new NetworkBoundResource<Table, List<Table>>() {
            @Override
            protected void saveCallResult(@NonNull List<Table> items) {
                professorDao.insertTables(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable Table data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Table> loadFromDb() {
                return professorDao.getTable(gradeId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Table>>>> createCall() {
                return client.getTable(gradeId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<User>>> getStudents(final int departmentId, final int gradeId) {
        return new NetworkBoundResource<List<User>, List<User>>() {
            @Override
            protected void saveCallResult(@NonNull List<User> items) {
                for (User user : items)
                    user.setTimeStamp(System.currentTimeMillis() / 1000);
                professorDao.insertStudents(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<User> users) {
                long currentTime = System.currentTimeMillis() / 1000;
                if (users == null || users.size() == 0) {
                    return true;
                } else {
                    for (User user : users) {
                        long lastRefresh = user.getTimeStamp();
                        return (currentTime - lastRefresh) >= Constants.DATABASE_UPDATE_INTERVAL;
                    }
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<List<User>> loadFromDb() {
                return professorDao.getStudents(departmentId, gradeId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<User>>>> createCall() {
                return client.getStudents(apiToken, departmentId, gradeId);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<ProfessorProfileData>> getProfileData() {
        return new NetworkBoundResource<ProfessorProfileData, ProfessorProfileData>() {
            @Override
            protected void saveCallResult(@NonNull ProfessorProfileData data) {
                data.setTimeStamp(System.currentTimeMillis() / 1000);
                professorDao.insertProfileData(data);
            }

            @Override
            protected boolean shouldFetch(@Nullable ProfessorProfileData data) {
                long currentTime = System.currentTimeMillis() / 1000;
                return data == null || (currentTime - data.getTimeStamp()) >= Constants.DATABASE_UPDATE_INTERVAL;
            }

            @NonNull
            @Override
            protected LiveData<ProfessorProfileData> loadFromDb() {
                return professorDao.getProfileData();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<ProfessorProfileData>>> createCall() {
                return client.getProfessorProfileData(apiToken);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<Marks>> getMarks(final int subjectId, final int studentId) {
        return new NetworkBoundResource<Marks, Marks>() {

            @Override
            protected void saveCallResult(@NonNull Marks marks) {
                professorDao.insertStudentMarks(marks);
            }

            @Override
            protected boolean shouldFetch(@Nullable Marks data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Marks> loadFromDb() {
                return professorDao.getStudentMarks(subjectId, studentId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<Marks>>> createCall() {
                return client.getMarks(subjectId, studentId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Event>>> getEventMutableLiveData() {
        return new NetworkBoundResource<List<Event>, List<Event>>() {

            @Override
            protected void saveCallResult(@NonNull List<Event> items) {
                professorDao.insertEvents(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Event> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Event>> loadFromDb() {
                return professorDao.getEvents();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Event>>>> createCall() {
                return client.getEvents(departmentId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Group>>> getGroups() {
        return new NetworkBoundResource<List<Group>, List<Group>>() {
            @Override
            protected void saveCallResult(@NonNull List<Group> items) {
                for (Group group : items)
                    group.setTimeStamp(System.currentTimeMillis() / 1000);
                professorDao.insertGroups(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Group> data) {
                long currentTime = System.currentTimeMillis() / 1000;
                if (data == null || data.size() == 0) {
                    return true;
                } else {
                    for (Group grade : data) {
                        long lastRefresh = grade.getTimeStamp();
                        return (currentTime - lastRefresh) >= Constants.DATABASE_UPDATE_INTERVAL;
                    }
                    return false;
                }
            }

            @NonNull
            @Override
            protected LiveData<List<Group>> loadFromDb() {
                return professorDao.getGroups();
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Group>>>> createCall() {
                return client.getGroups(apiToken);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Post>>> getPosts(final long groupId) {
        return new NetworkBoundResource<List<Post>, List<Post>>() {
            @Override
            protected void saveCallResult(@NonNull List<Post> items) {
                for (Post post : items) {
                    long noOfComments = post.getComments() == null ? 0 : post.getComments().size();
                    post.setNoOfComments(noOfComments);
                }
                professorDao.insertPosts(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Post> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Post>> loadFromDb() {
                return professorDao.getPosts(groupId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Post>>>> createCall() {
                return client.getPosts(apiToken, groupId);
            }
        }.getAsLiveData();
    }

    public LiveData<Resource<List<Comment>>> getComments(final long postId) {
        return new NetworkBoundResource<List<Comment>, List<Comment>>() {
            @Override
            protected void saveCallResult(@NonNull List<Comment> items) {
                for (Comment comment : items) {
                    if (comment.getReplies() != null && comment.getReplies().size() > 0) {
                        long noOfReplies = comment.getReplies().size();
                        Reply reply = comment.getReplies().get(0);
                        comment.setNoOfReplies(noOfReplies);
                        comment.setLatestReply(reply);
                    }
                }
                professorDao.insertComments(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Comment> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Comment>> loadFromDb() {
                return professorDao.getComments(postId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Comment>>>> createCall() {
                return client.getComments(postId);
            }
        }.getAsLiveData();
    }


    public LiveData<Resource<List<Reply>>> getReplies(final long commentId) {
        return new NetworkBoundResource<List<Reply>, List<Reply>>() {
            @Override
            protected void saveCallResult(@NonNull List<Reply> items) {
                professorDao.insertReplies(items);
            }

            @Override
            protected boolean shouldFetch(@Nullable List<Reply> data) {
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Reply>> loadFromDb() {
                return professorDao.getReplies(commentId);
            }

            @NonNull
            @Override
            protected LiveData<ApiResult<ApiResponse<List<Reply>>>> createCall() {
                return client.getReplies(commentId);
            }
        }.getAsLiveData();
    }


    public void makePost(long groupId, String body) {
        postResultLiveData.setValue(Resource.loading((Post) null));
        final LiveData<ApiResult<ApiResponse<Post>>> apiResultLiveData = client.makePost(apiToken, groupId, body);
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


    /**
     * POST Method used to add new Assignment with these @params
     *
     * @param title        : Source Title.
     * @param path         : Source url to open.
     * @param departmentId : id of the department which have the subject.
     * @param gradeId      : id of the grade which have the subject.
     * @param subjectId    : id of the subject to add a new Assignment to.
     */
    public void addAssignment(String title, String path, int departmentId, int gradeId, int subjectId, final Subject subject) {
        addAssignmentResultLiveData.setValue(Resource.loading((Assignment) null));
        final LiveData<ApiResult<ApiResponse<Assignment>>> apiResultLiveData = client.addAssignment(apiToken, title, path, departmentId, gradeId, subjectId);
        addAssignmentResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<Assignment>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<Assignment>> apiResult) {
                addAssignmentResultLiveData.removeSource(apiResultLiveData);
                if (apiResult instanceof ApiResult.Success) {
                    final Assignment assignment = (Assignment) ((ApiResult.Success) apiResult).getData();
                    assignment.setProfessor(professor);
                    assignment.setSubject(subject);
                    AppExecutor.getExecutor().getDiskIo().execute(new Runnable() {
                        @Override
                        public void run() {
                            professorDao.insertAssignment(assignment);
                        }
                    });
                    addAssignmentResultLiveData.setValue(Resource.success((assignment)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    addAssignmentResultLiveData.setValue(Resource.error(error, (Assignment) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    addAssignmentResultLiveData.setValue(Resource.error(error, (Assignment) null));
                }
            }
        });
    }

    /**
     * POST Method used to add new source with these @params
     *
     * @param title        : Source Title.
     * @param path         : Source url to open.
     * @param departmentId : id of the department which have the subject.
     * @param gradeId      : id of the grade which have the subject.
     * @param subjectId    : id of the subject to add a new Assignment to.
     */
    public void addSource(String title, String path, int departmentId, int gradeId, int subjectId, final Subject subject) {
        addSourceResultLiveData.setValue(Resource.loading((Source) null));
        final LiveData<ApiResult<ApiResponse<Source>>> apiResultLiveData = client.addSource(apiToken, title, path, departmentId, gradeId, subjectId);
        addSourceResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<Source>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<Source>> apiResult) {
                addSourceResultLiveData.removeSource(apiResultLiveData);
                if (apiResult instanceof ApiResult.Success) {
                    final Source source = (Source) ((ApiResult.Success) apiResult).getData();
                    source.setProfessor(professor);
                    source.setSubject(subject);
                    AppExecutor.getExecutor().getDiskIo().execute(new Runnable() {
                        @Override
                        public void run() {
                            professorDao.insertSource(source);
                        }
                    });
                    addSourceResultLiveData.setValue(Resource.success((source)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    addSourceResultLiveData.setValue(Resource.error(error, (Source) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    addSourceResultLiveData.setValue(Resource.error(error, (Source) null));
                }
            }
        });
    }


    /**
     * POST Method used to add new marks for a specific student with these @params
     *
     * @param studentId  : student  Id to add marks to.
     * @param attendance : student attendance mark.
     * @param work       : student work mark.
     * @param midterm    : student midterm mark.
     * @param semester   : student semester mark.
     * @param subjectId  : id of the subject to add marks to.
     * @return result of the method if the marks has been added or not
     */
    public void addMarks(int studentId, int subjectId, double attendance, double work, double midterm, double semester) {
        addMarksResultLiveData.setValue(Resource.loading((Marks) null));
        final LiveData<ApiResult<ApiResponse<Marks>>> apiResultLiveData = client.addMarks(apiToken, studentId, subjectId, attendance, work, midterm, semester);
        addMarksResultLiveData.addSource(apiResultLiveData, new Observer<ApiResult<ApiResponse<Marks>>>() {
            @Override
            public void onChanged(ApiResult<ApiResponse<Marks>> apiResult) {
                addMarksResultLiveData.removeSource(apiResultLiveData);
                if (apiResult instanceof ApiResult.Success) {
                    Marks marks = (Marks) ((ApiResult.Success) apiResult).getData();
                    addMarksResultLiveData.setValue(Resource.success((marks)));
                } else if (apiResult instanceof ApiResult.Empty) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    addMarksResultLiveData.setValue(Resource.error(error, (Marks) null));
                } else if (apiResult instanceof ApiResult.Error) {
                    String error = ((ApiResult.Error) apiResult).getError();
                    addMarksResultLiveData.setValue(Resource.error(error, (Marks) null));
                }
            }
        });
    }

    public void predictResult(int attendance, int work, int midterm) {
        predictionMutableLiveData.setValue(Resource.loading((Prediction) null));
        predictionClient.getPrediction(attendance, work, midterm).enqueue(new Callback<Prediction>() {
            @Override
            public void onResponse(Call<Prediction> call, Response<Prediction> response) {
                Prediction prediction = response.body();
                if (response.isSuccessful() && prediction != null) {
                    predictionMutableLiveData.setValue(Resource.success((prediction)));
                } else {
                    predictionMutableLiveData.setValue(Resource.error(Constants.MAIN_ERROR_MESSAGE, (Prediction) null));
                }
            }

            @Override
            public void onFailure(Call<Prediction> call, Throwable t) {
                predictionMutableLiveData.setValue(Resource.error(Constants.MAIN_ERROR_MESSAGE, (Prediction) null));
            }
        });
    }

    public MutableLiveData<Resource<Assignment>> getAddAssignmentResultLiveData() {
        return addAssignmentResultLiveData;
    }

    public MutableLiveData<Resource<Source>> getAddSourceResultLiveData() {
        return addSourceResultLiveData;
    }

    public MutableLiveData<Resource<Marks>> getAddMarksResultLiveData() {
        return addMarksResultLiveData;
    }

    public MutableLiveData<Resource<Prediction>> getPredictionMutableLiveData() {
        return predictionMutableLiveData;
    }
}
