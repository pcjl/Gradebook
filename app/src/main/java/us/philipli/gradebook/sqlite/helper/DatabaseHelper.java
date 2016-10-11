package us.philipli.gradebook.sqlite.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This is an SQL helper class
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "gradeManager";

    // Table Names
    private static final String TABLE_STUDENTS = "students";
    private static final String TABLE_COURSES = "courses";
    private static final String TABLE_ASSESSMENTS = "assessments";

    // Common column names
    private static final String KEY_COURSE_CODE = "course_code"; // Used by both Courses and Assessments

    // Students Table column names
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_GPA = "gpa";
    private static final String KEY_INSTITUTION = "institution";

    // Courses Table column names
    private static final String KEY_COURSE_NAME = "course_name";
    private static final String KEY_COURSE_WEIGHT = "course_weight";
    private static final String KEY_INCLUDE = "include";
    private static final String KEY_COLOR = "color";
    private static final String KEY_COURSE_MARKS = "course_marks";

    // Assessments Table column names
    private static final String KEY_ASSESSMENTS_NAME = "assessments_name";
    private static final String KEY_ASSESSMENTS_WEIGHT = "assessments_weight";
    private static final String KEY_ASSESSMENTS_MARKS = "assessments_marks";

    // Table create statements
    private static final String CREATE_TABLE_STUDENTS = "CREATE TABLE " + TABLE_STUDENTS + " ("
            + KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_USER_NAME + " TEXT, " + KEY_GPA + " NUMERIC, " + KEY_INSTITUTION + " TEXT)";

    private static final String CREATE_TABLE_COURSES = "CREATE TABLE " + TABLE_COURSES + " (" +
            KEY_COURSE_CODE + " TEXT PRIMARY KEY, " + KEY_COURSE_NAME + " TEXT, " +
            KEY_COURSE_WEIGHT + " NUMERIC, " + KEY_INCLUDE + " INTEGER, " + KEY_COLOR + " TEXT, " +
            KEY_COURSE_MARKS + " NUMERIC)";

    private static final String CREATE_TABLE_ASSESSMENTS = "CREATE TABLE " + TABLE_ASSESSMENTS + " ("
            + KEY_ASSESSMENTS_NAME + " TEXT, " + KEY_COURSE_CODE + " TEXT, " + KEY_ASSESSMENTS_WEIGHT +
            " NUMERIC, " + KEY_ASSESSMENTS_MARKS + " NUMERIC, PRIMARY KEY (" + KEY_ASSESSMENTS_NAME +
            " , " + KEY_COURSE_CODE + "))";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_STUDENTS);
        db.execSQL(CREATE_TABLE_COURSES);
        db.execSQL(CREATE_TABLE_ASSESSMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
