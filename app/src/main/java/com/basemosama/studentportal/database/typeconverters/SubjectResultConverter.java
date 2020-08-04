package com.basemosama.studentportal.database.typeconverters;

import androidx.room.TypeConverter;

import com.basemosama.studentportal.model.student.SubjectResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class SubjectResultConverter {

    @TypeConverter
    public static List<SubjectResult> JsonToLis(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<SubjectResult>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String ListToJson(List<SubjectResult> data) {
        Gson gson = new Gson();
        return data == null ? null : gson.toJson(data);
    }

}
