package com.basemosama.studentportal.database.typeconverters;

import androidx.room.TypeConverter;

import com.basemosama.studentportal.model.professor.OfficeHour;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class OfficeHourListConverter {

    @TypeConverter
    public static List<OfficeHour> JsonToLis(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<OfficeHour>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String ListToJson(List<OfficeHour> data) {
        Gson gson = new Gson();
        return data == null ? null : gson.toJson(data);
    }

}
