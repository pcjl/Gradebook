package us.philipli.gradebook.activities;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.audiofx.BassBoost;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddCourseActivity.class);
                startActivity(intent);
            }
        });

        setupCoursesList();
    }

    @Override
    protected void onResume() {
        // TODO: Use SQL cursors or something
        super.onResume();
        setupCoursesList();
    }

    private void setupToolbar() {
        // Set up toolbar
        this.myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(this.myToolbar);

//        if (myToolbar != null) {

        // Hamburger
        // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * http://stackoverflow.com/questions/28071763/toolbar-navigation-hamburger-icon-missing
         */
//        }
    }

    private void setupCoursesList() {
        this.mRecyclerView = (RecyclerView) findViewById(R.id.courses_recyclerview);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        this.mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        this.mLayoutManager = new LinearLayoutManager(this);
        this.mRecyclerView.setLayoutManager(mLayoutManager);

        setUpReadableDatabase();

        List<Course> myDataset = this.mDatabaseHelper.getAllCourses();

        // specify an adapter (see also next example)
        this.mAdapter = new CourseAdapter(myDataset);
        this.mRecyclerView.setAdapter(this.mAdapter);
    }

    private SQLiteDatabase setUpReadableDatabase() {
        this.mDatabaseHelper = DatabaseHelper.getInstance(this);
        return this.mDatabaseHelper.getReadableDatabase();
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
