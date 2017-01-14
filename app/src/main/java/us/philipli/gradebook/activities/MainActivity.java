package us.philipli.gradebook.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import us.philipli.gradebook.R;
import us.philipli.gradebook.adapters.CourseAdapter;
import us.philipli.gradebook.course.Course;
import us.philipli.gradebook.sqlite.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    // Toolbar
    private Toolbar myToolbar;

    // Set up recycler view for courses
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();

        // Set up floating action button
        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent (v.getContext(), AddCourseActivity.class);
                startActivity(intent);
            }
        });

        setupCoursesList();
    }

    private void setupToolbar () {
        // Set up toolbar
        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (myToolbar != null) {

            // Hamburger
            // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            /**
             * http://stackoverflow.com/questions/28071763/toolbar-navigation-hamburger-icon-missing
             */
        }
    }

    private void setupCoursesList () {

        mRecyclerView = (RecyclerView) findViewById(R.id.courses_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // Insert into database
        SQLiteDatabase db;
        db = setUpWriteDatabase();

        ContentValues values = new ContentValues();
        values.put("course_name", "TEST");
        values.put("course_weight", "0.5");
        values.put("include", "1");
        values.put("color", "Green");
        values.put("course_marks", "420");

        long newRowId = db.insert("courses", null, values);

        // Read from database
        db = setUpReadableDatabase();

        List<Course> myDataset = mDatabaseHelper.getAllCourses();

        // specify an adapter (see also next example)
        mAdapter = new CourseAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private SQLiteDatabase setUpReadableDatabase() {

        mDatabaseHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        return db;
    }

    private SQLiteDatabase setUpWriteDatabase() {

        mDatabaseHelper = DatabaseHelper.getInstance(this);
        SQLiteDatabase db = mDatabaseHelper.getReadableDatabase();

        return db;
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
