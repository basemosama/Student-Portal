package com.basemosama.studentportal.utility;

public class Constants {
    public static final String PREDICTION_BASE_URL = "https://ml-prediction-grade-api.herokuapp.com/";
    //public static final String BASE_URL ="http://10.0.2.2:3000";
    public static final String BASE_URL = "https://shrouded-inlet-67316.herokuapp.com/api/v1/";

    public static final String STUDENT_PATH = "login-student";
    public static final String PROFESSOR_PATH = "login-professor";
    public static final String PROFESSOR_TEXT = "professor";
    public static final String INTENT_LOGGED_IN_USER_KEY = "logged_in_user";
    public static final String ASSIGNMENT_TYPE_KEY = "assignment_type";
    public static final String ASSIGNMENT_GRADE_ID_KEY = "assignment_grade_id";
    public static final String ASSIGNMENT_SUBJECT_ID_KEY = "assignment_subject_id";
    public static final String SUBJECT_NAME_KEY = "subject_name";
    public static final String STUDENT_ID_KEY = "student_id";
    public static final String SAVED_LOGGED_IN_USER_KEY = "saved_user";
    public static final String SHARED_PREFS_KEY = "com.basemosama.studentportal.prefs";
    public static final String EVENT_TITLE_BUNDLE_KEY = "event_title";
    public static final String PROFILE_INFO_BUNDLE_KEY = "profile_info_bundle";
    public static final String MAIN_ERROR_MESSAGE = "Sorry, Something Went Wrong";
    public static final String STUDENT_MARKS_BUNDLE_KEY = "student_marks_bundle_key";
    public static final String STUDENT_COMMENTS_BUNDLE_KEY = "comments";


    public static final String SAVED_LOGGED_IN_USER_API_TOKEN_KEY = "saved_logged_in_api_token";
    public static final String SAVED_LOGGED_IN_USER_TYPE_KEY = "saved_logged_in_type";
    public static final String INTENT_LOGGED_IN_USER_TYPE_KEY = "intent_saved_logged_in_type";

    public static final String LIKED_TAG = "Liked";

    public static final String OPENED_ARROW_TAG = "opened_arrow";
    public static final String CLOSED_ARROW_TAG = "closed_arrow";
    public static final String STUDENT_TAG = "student_tag";
    public static final String PROFESSOR_TAG = "professor_tag";


    public static final long DATABASE_UPDATE_INTERVAL = 60 * 60 * 24 * 30;
}

