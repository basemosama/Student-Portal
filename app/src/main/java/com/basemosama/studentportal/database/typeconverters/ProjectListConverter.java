package com.basemosama.studentportal.database.typeconverters;

import androidx.room.TypeConverter;

import com.basemosama.studentportal.model.professor.ProfessorProject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ProjectListConverter {

    @TypeConverter
    public static List<ProfessorProject> JsonToLis(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<ProfessorProject>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String ListToJson(List<ProfessorProject> data) {
        Gson gson = new Gson();
        return data == null ? null : gson.toJson(data);
    }

}
