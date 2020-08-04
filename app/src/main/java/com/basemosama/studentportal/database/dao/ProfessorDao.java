package com.basemosama.studentportal.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.basemosama.studentportal.model.professor.Department;
import com.basemosama.studentportal.model.professor.Group;
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

@Dao
public interface ProfessorDao {
    @Query("SELECT * FROM events")
    LiveData<List<Event>> getEvents();

    @Insert
    void insertEvents(List<Event> events);

    @Query("SELECT * FROM professor_grades")
    LiveData<List<ProfessorGrade>> getGrades();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertGrades(List<ProfessorGrade> grades);

    @Query("SELECT * FROM professor_departments")
    LiveData<List<Department>> getDepartments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDepartments(List<Department> departments);

    @Query("SELECT * FROM subject WHERE departmentId = :departmentId AND gradeId =:gradeId")
    LiveData<List<Subject>> getSubjectsForSpecificGrade(int departmentId, int gradeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertSubjects(List<Subject> subjects);

    @Query("SELECT * FROM assignments ORDER BY updated_at DESC")
    LiveData<List<Assignment>> getAssignments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAssignment(Assignment assignment);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSource(Source source);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertAssignments(List<Assignment> assignments);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertSources(List<Source> sources);

    @Query("SELECT * FROM sources ORDER BY updated_at DESC")
    LiveData<List<Source>> getSources();

    @Query("SELECT * FROM grade_table WHERE grade_id = :gradeId LIMIT 1")
    LiveData<Table> getTable(int gradeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertTables(List<Table> tables);

    @Query("SELECT * FROM students WHERE departmentId =:departmentId AND gradeId = :gradeId ")
    LiveData<List<User>> getStudents(int departmentId, int gradeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long[] insertStudents(List<User> students);

    @Query("SELECT * FROM professor_profile LIMIT 1")
    LiveData<ProfessorProfileData> getProfileData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertProfileData(ProfessorProfileData data);

    @Query("SELECT * FROM student_marks WHERE subjectId = :id AND studentId =:studentId ")
    LiveData<Marks> getStudentMarks(int id, int studentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertStudentMarks(Marks marks);

    @Query("SELECT * FROM groups ORDER BY name ASC")
    LiveData<List<Group>> getGroups();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertGroups(List<Group> groups);

    @Query("SELECT * FROM student_posts WHERE groupId =:groupId ORDER BY updatedAt DESC ")
    LiveData<List<Post>> getPosts(long groupId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertPosts(List<Post> posts);

    @Query("SELECT * FROM student_comments WHERE postId = :postId ORDER BY updatedAt DESC")
    LiveData<List<Comment>> getComments(long postId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertComments(List<Comment> comments);

    @Query("SELECT * FROM student_replies WHERE commentId = :commentId ")
    LiveData<List<Reply>> getReplies(long commentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long[] insertReplies(List<Reply> replies);
}
