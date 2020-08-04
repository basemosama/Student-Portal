package com.basemosama.studentportal.database;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.basemosama.studentportal.database.dao.ProfessorDao;
import com.basemosama.studentportal.database.dao.StudentDao;
import com.basemosama.studentportal.database.typeconverters.DateConverter;
import com.basemosama.studentportal.database.typeconverters.GradesListConverter;
import com.basemosama.studentportal.database.typeconverters.SubjectListConverter;
import com.basemosama.studentportal.database.typeconverters.SubjectResultConverter;
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
import com.basemosama.studentportal.model.student.StudentResult;
import com.basemosama.studentportal.model.student.Subject;
import com.basemosama.studentportal.model.student.Table;
import com.basemosama.studentportal.model.student.User;

@TypeConverters({DateConverter.class, SubjectResultConverter.class, GradesListConverter.class, SubjectListConverter.class,})
@Database(entities = {Event.class, Marks.class, Table.class, Subject.class, StudentResult.class,
        Assignment.class, Source.class, Post.class, Comment.class, Reply.class, ProfessorGrade.class, Department.class,
        User.class, ProfessorProfileData.class, Group.class}, version = 1, exportSchema = false)
public abstract class CacheDataBase extends RoomDatabase {
    private static final String LOG_TAG = CacheDataBase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "sas_cache_database";
    private static CacheDataBase sInstance;

    public static CacheDataBase getInstance(Context context) {
        synchronized (LOCK) {
            if (sInstance == null) {
                Log.i(LOG_TAG, "CreatingDatabase");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        CacheDataBase.class, DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract StudentDao studentDao();

    public abstract ProfessorDao professorDao();

}
