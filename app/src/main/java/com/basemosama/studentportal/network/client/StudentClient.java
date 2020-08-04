package com.basemosama.studentportal.network.client;

import androidx.lifecycle.LiveData;

import com.basemosama.studentportal.model.login.LoggedInUser;
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

import java.util.List;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface StudentClient {

    @FormUrlEncoded
    @POST("{type}")
    LiveData<ApiResult<ApiResponse<LoggedInUser>>> login(
            @Path("type") String type,
            @Field("email") String username,
            @Field("password") String password);

    @POST("/profile")
    LiveData<ApiResult<ApiResponse<LoggedInUser>>> getUserProfile(@Query("api_token") String apiToken);

    @GET("subjects")
    LiveData<ApiResult<ApiResponse<List<Subject>>>> getSubjects(@Query("grade_id") int gradeId,
                                                                @Query("department_id") int departmentId);

    @GET("assignments")
    LiveData<ApiResult<ApiResponse<List<Assignment>>>> getAssignments(@Query("grade_id") int gradeId);

    @GET("assignments")
    LiveData<ApiResult<ApiResponse<List<Assignment>>>> getSubjectAssignments(@Query("grade_id") int gradeId,
                                                                             @Query("subject_id") int subjectId);


    @GET("sources")
    LiveData<ApiResult<ApiResponse<List<Source>>>> getSubjectSources(@Query("grade_id") int gradeId,
                                                                     @Query("subject_id") int subjectId);


    @GET("marks")
    LiveData<ApiResult<ApiResponse<Marks>>> getMarks(@Query("subject_id") int subjectId,
                                                     @Query("student_id") int studentId);

    @GET("result")
    LiveData<ApiResult<ApiResponse<List<StudentResult>>>> getResults(@Query("id") int id,
                                                                     @Query("student_id") int studentId);

    @GET("tables")
    LiveData<ApiResult<ApiResponse<List<Table>>>> getTable(@Query("grade_id") int gradeId);

    @GET("events")
    LiveData<ApiResult<ApiResponse<List<Event>>>> getEvents(@Query("department_id") int departmentId);


    //Group
    @GET("posts")
    LiveData<ApiResult<ApiResponse<List<Post>>>> getPosts(@Query("api_token") String apiToken);

    @POST("comments")
    LiveData<ApiResult<ApiResponse<List<Comment>>>> getComments(@Query("post_id") long postId);

    @POST("replies")
    LiveData<ApiResult<ApiResponse<List<Reply>>>> getReplies(@Query("comment_id") long commentId);


    @POST("make-post")
    LiveData<ApiResult<ApiResponse<Post>>> makePost(@Query("api_token") String apiToken,
                                                    @Query("body") String body);

    @POST("make-comment")
    LiveData<ApiResult<ApiResponse<Comment>>> makeComment(@Query("api_token") String apiToken,
                                                          @Query("body") String body,
                                                          @Query("post_id") long postId);

    @POST("make-reply")
    LiveData<ApiResult<ApiResponse<Reply>>> makeReply(@Query("api_token") String apiToken,
                                                      @Query("body") String body,
                                                      @Query("comment_id") long commentId);

}
