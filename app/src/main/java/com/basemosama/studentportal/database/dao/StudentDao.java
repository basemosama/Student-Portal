package com.basemosama.studentportal.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

@Dao
public interface StudentDao {

    @Query("SELECT * FROM events")
    LiveData<List<Event>> getEvents();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertEvents(List<Event> events);

    @Query("SELECT * FROM grade_table limit 1")
    LiveData<Table> getTable();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertTables(List<Table> tables);

    @Query("SELECT * FROM student_marks WHERE subjectId = :id ")
    LiveData<Marks> getSubjectMarks(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSubjectMarks(Marks marks);

    @Query("SELECT * FROM subject")
    LiveData<List<Subject>> getSubjects();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertSubjects(List<Subject> subjects);

    @Query("SELECT * FROM student_result limit 1")
    LiveData<StudentResult> getStudentResult();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertStudentResults(List<StudentResult> results);

    @Query("SELECT * FROM assignments ORDER BY updated_at DESC")
    LiveData<List<Assignment>> getAssignments();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAssignments(List<Assignment> assignments);

    @Query("SELECT * FROM assignments WHERE subjectId =:subjectId ORDER BY updated_at DESC")
    LiveData<List<Assignment>> getSubjectAssignments(int subjectId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertSources(List<Source> sources);

    @Query("SELECT * FROM sources WHERE subjectId =:subjectId ORDER BY updated_at DESC")
    LiveData<List<Source>> getSubjectSources(int subjectId);

    @Query("SELECT * FROM student_posts ORDER BY updatedAt DESC ")
    LiveData<List<Post>> getPosts();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPost(Post post);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPosts(List<Post> posts);

    @Query("SELECT * FROM student_comments WHERE postId = :postId ")
    LiveData<List<Comment>> getComments(long postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertComment(Comment comment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertComments(List<Comment> comments);

    @Query("SELECT * FROM student_replies WHERE commentId = :commentId ")
    LiveData<List<Reply>> getReplies(long commentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertReply(Reply reply);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertReplies(List<Reply> replies);

}
