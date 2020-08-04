package com.basemosama.studentportal.network.client;

import androidx.lifecycle.LiveData;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ProfessorClient {

    @GET("assigns")
    LiveData<ApiResult<ApiResponse<List<Assignment>>>> getAssignments(@Query("api_token") String apiToken);

    @GET("source-prof")
    LiveData<ApiResult<ApiResponse<List<Source>>>> getSources(@Query("api_token") String apiToken);

    @GET("grades")
    LiveData<ApiResult<ApiResponse<List<ProfessorGrade>>>> getGrades();

    @GET("departments")
    LiveData<ApiResult<ApiResponse<List<Department>>>> getDepartments();

    @GET("subjects")
    LiveData<ApiResult<ApiResponse<List<Subject>>>> getSubjectsForSpecificGrade(@Query("grade_id") int gradeId,
                                                                                @Query("department_id") int departmentId);

    @POST("profile-professor")
    LiveData<ApiResult<ApiResponse<ProfessorProfileData>>> getProfessorProfileData(@Query("api_token") String apiToken);

    @GET("tables")
    LiveData<ApiResult<ApiResponse<List<Table>>>> getTable(@Query("grade_id") int gradeId);


    @GET("student-grade")
    LiveData<ApiResult<ApiResponse<List<User>>>> getStudents(@Query("api_token") String apiToken,
                                                             @Query("department_id") int departmentId,
                                                             @Query("grade_id") int gradeId);

    @GET("marks")
    LiveData<ApiResult<ApiResponse<Marks>>> getMarks(@Query("subject_id") int subjectId,
                                                     @Query("student_id") int studentId);

    @GET("events")
    LiveData<ApiResult<ApiResponse<List<Event>>>> getEvents(@Query("department_id") int departmentId);


    // Add Items using POST METHOD
    @POST("add-assignment")
    LiveData<ApiResult<ApiResponse<Assignment>>> addAssignment(@Query("api_token") String apiToken, @Query("title") String title, @Query("path") String path,
                                                               @Query("department_id") int departmentId, @Query("grade_id") int gradeId, @Query("subject_id") int subjectId);

    @POST("add-source")
    LiveData<ApiResult<ApiResponse<Source>>> addSource(@Query("api_token") String apiToken, @Query("title") String title, @Query("path") String path,
                                                       @Query("department_id") int departmentId, @Query("grade_id") int gradeId, @Query("subject_id") int subjectId);

    @POST("add-mark")
    LiveData<ApiResult<ApiResponse<Marks>>> addMarks(@Query("api_token") String apiToken, @Query("student_id") int studentId, @Query("subject_id") int subjectId,
                                                     @Query("attendance") double attendance, @Query("work") double work, @Query("midterm") double midterm,
                                                     @Query("semester") double semester);

    @FormUrlEncoded
    @POST("predict")
    Call<Prediction> getPrediction(@Field("attendance") int attendance, @Field("work") int work, @Field("midterm") int midterm);

    //Group
    @GET("groups")
    LiveData<ApiResult<ApiResponse<List<Group>>>> getGroups(@Query("api_token") String apiToken);


    @GET("group")
    LiveData<ApiResult<ApiResponse<List<Post>>>> getPosts(@Query("api_token") String apiToken, @Query("group_id") long groupId);

    @POST("comments")
    LiveData<ApiResult<ApiResponse<List<Comment>>>> getComments(@Query("post_id") long postId);

    @POST("replies")
    LiveData<ApiResult<ApiResponse<List<Reply>>>> getReplies(@Query("comment_id") long commentId);


    @POST("group/post")
    LiveData<ApiResult<ApiResponse<Post>>> makePost(@Query("api_token") String apiToken, @Query("group_id") long groupId, @Query("body") String body);

    @POST("make-comment")
    LiveData<ApiResult<ApiResponse<Comment>>> makeComment(@Query("api_token") String apiToken, @Query("body") String body, @Query("post_id") long postId);

    @POST("make-reply")
    LiveData<ApiResult<ApiResponse<Reply>>> makeReply(@Query("api_token") String apiToken,
                                                      @Query("body") String body,
                                                      @Query("comment_id") long commentId);

}
