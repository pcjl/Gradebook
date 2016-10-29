package us.philipli.gradebook.sqlite.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import us.philipli.gradebook.course.Assessment;
import us.philipli.gradebook.course.Course;

/**
 * This is an SQL helper class
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper sInstance;

    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 2;
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

    public static synchronized DatabaseHelper getsInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private DatabaseHelper(Context context) {
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
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ASSESSMENTS);

        // create new tables
        onCreate(db);
    }


    // CRUD Operations
    /*
    * Create a new student and return its id.
    * Return: user id
     */
    public long createStudent(Students student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, student.getName());
        values.put(KEY_GPA, student.getGpa());
        values.put(KEY_INSTITUTION, student.getInstitution());

        // insert row
        long User_id = db.insert(TABLE_STUDENTS, null, values);

        return User_id;
    }

    /*
    * Get single student
     */
    public Students getStudent(long user_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + TABLE_STUDENTS + " WHERE " + KEY_USER_ID +
                " = " + user_id;

        Log.e(LOG, select);

        Cursor c = db.rawQuery(select, null);

        if (c != null) {
            c.moveToFirst();
        }

        Students student = new Students();
        student.setId(c.getInt(c.getColumnIndex(KEY_USER_ID)));
        student.setName(c.getString(c.getColumnIndex(KEY_USER_NAME)));
        student.setGpa(c.getLong(c.getColumnIndex(KEY_GPA)));
        student.setInstitution(c.getString(c.getColumnIndex(KEY_INSTITUTION)));

        return student;
    }

    /*
    * Updating a student
     */
    public int updateStudent(Students student) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_NAME, student.getName());
        values.put(KEY_GPA, student.getGpa());
        values.put(KEY_INSTITUTION, student.getInstitution());

        // updating row
        return db.update(TABLE_STUDENTS, values, KEY_USER_ID + " =?",
                new String[] {String.valueOf(student.getId())});
    }

    /*
    * Creating a course with a list of assessments
     */
    public void createCourse(Course course, Assessment[]assessments) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_COURSE_CODE, course.getCourseCode());
        values.put(KEY_COURSE_NAME, course.getCourseName());
        values.put(KEY_COURSE_WEIGHT, course.getWeight());
        values.put(KEY_INCLUDE, course.getInclude());
        values.put(KEY_COLOR, course.getColor());
        values.put(KEY_COURSE_MARKS, course.getGrade());

        // insert row
        db.insert(TABLE_COURSES, null, values);

        // assigning assessments to course
        for (Assessment assessment : assessments) {
            createAssessment(assessment);
        }
    }

    // Fetching a course

    public Course getCourse(String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + TABLE_COURSES + " WHERE " + KEY_COURSE_CODE +
                " = " + code;

        Cursor c = db.rawQuery(select, null);

        if (c != null)
            c.moveToFirst();

        Course course = new Course();
        course.setCourseCode(c.getString(c.getColumnIndex(KEY_COURSE_CODE)));
        course.setCourseName(c.getString(c.getColumnIndex(KEY_COURSE_NAME)));
        course.setWeight(c.getLong(c.getColumnIndex(KEY_COURSE_WEIGHT)));
        course.setInclude(c.getInt(c.getColumnIndex(KEY_INCLUDE)));
        course.setColor(c.getString(c.getColumnIndex(KEY_COLOR)));
        course.setGrade(c.getLong(c.getColumnIndex(KEY_COURSE_MARKS)));

        return course;
    }

    // Fetching all courses

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<Course>();
        String select = "SELECT * FROM " + TABLE_COURSES;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(select, null);

        if (c.moveToFirst()) {
            do {
                Course course = new Course();
                course.setCourseCode(c.getString(c.getColumnIndex(KEY_COURSE_CODE)));
                course.setCourseName(c.getString(c.getColumnIndex(KEY_COURSE_NAME)));
                course.setWeight(c.getLong(c.getColumnIndex(KEY_COURSE_WEIGHT)));
                course.setInclude(c.getInt(c.getColumnIndex(KEY_INCLUDE)));
                course.setColor(c.getString(c.getColumnIndex(KEY_COLOR)));
                course.setGrade(c.getLong(c.getColumnIndex(KEY_COURSE_MARKS)));

                courses.add(course);
            } while (c.moveToNext());
        }
        return courses;
    }

    // Deleting a course

    public void deleteCourse(String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_COURSES, KEY_COURSE_CODE + " =?",
                new String[] {String.valueOf(code)});
    }

    // Creating an assessment

    public void createAssessment(Assessment assessment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ASSESSMENTS_NAME, assessment.getName());
        values.put(KEY_COURSE_CODE, assessment.getCode());
        values.put(KEY_ASSESSMENTS_WEIGHT, assessment.getWeight());
        values.put(KEY_ASSESSMENTS_MARKS, assessment.getGrade());

        db.insert(TABLE_ASSESSMENTS, null, values);
    }

    // Fetching an assessment

    public Assessment getAssessment(String name, String code) {
        SQLiteDatabase db = this.getReadableDatabase();

        String select = "SELECT * FROM " + TABLE_ASSESSMENTS + " WHERE " + KEY_ASSESSMENTS_NAME +
                " = " + name + " AND " + KEY_COURSE_CODE + " = " + code;

        Cursor c = db.rawQuery(select, null);

        if (c != null)
            c.moveToFirst();

        Assessment assessment = new Assessment();
        assessment.setName(c.getString(c.getColumnIndex(KEY_ASSESSMENTS_NAME)));
        assessment.setCode(c.getString(c.getColumnIndex(KEY_COURSE_CODE)));
        assessment.setWeight(c.getLong(c.getColumnIndex(KEY_ASSESSMENTS_WEIGHT)));
        assessment.setGrade(c.getLong(c.getColumnIndex(KEY_ASSESSMENTS_MARKS)));

        return assessment;
    }

    // Fetching all assessments under a course

    public List<Assessment> getAllAssessmets(String code) {
        List<Assessment> assessments = new ArrayList<Assessment>();
        String select = "SELECT * FROM " + TABLE_ASSESSMENTS + " WHERE " +
                KEY_COURSE_CODE + " = " + code;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(select, null);

        if (c.moveToFirst()) {
            do {
                Assessment assessment = new Assessment();
                assessment.setName(c.getString(c.getColumnIndex(KEY_ASSESSMENTS_NAME)));
                assessment.setCode(c.getString(c.getColumnIndex(KEY_COURSE_CODE)));
                assessment.setWeight(c.getLong(c.getColumnIndex(KEY_ASSESSMENTS_WEIGHT)));
                assessment.setGrade(c.getLong(c.getColumnIndex(KEY_ASSESSMENTS_MARKS)));

                assessments.add(assessment);
            } while (c.moveToNext());
        }
        return assessments;
    }

    // Deleting an assessment

    public void deleteAssessment(String name, String code) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_ASSESSMENTS, KEY_ASSESSMENTS_NAME + " =? and " + KEY_COURSE_CODE +
        " =?", new String[] {name, code});
    }

    // Updating an assessment

    public int updateAssessment(Assessment assessment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ASSESSMENTS_NAME, assessment.getName());
        values.put(KEY_COURSE_CODE, assessment.getCode());
        values.put(KEY_ASSESSMENTS_WEIGHT, assessment.getWeight());
        values.put(KEY_ASSESSMENTS_MARKS, assessment.getGrade());

        return db.update(TABLE_ASSESSMENTS, values,  KEY_ASSESSMENTS_NAME + " =? and " + KEY_COURSE_CODE +
                " =?", new String[] {assessment.getName(), assessment.getCode()});
    }
}
