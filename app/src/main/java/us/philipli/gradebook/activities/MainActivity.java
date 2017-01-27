package us.philipli.gradebook.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import us.philipli.gradebook.R;
import us.philipli.gradebook.adapters.CourseAdapter;
import us.philipli.gradebook.course.Course;
import us.philipli.gradebook.sqlite.helper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private CourseAdapter mAdapter;
    private DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupToolbar();
        setupFab();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView nameText = (TextView) findViewById(R.id.name_text);
        // TODO: Fix on first launch, not showing the text properly (defaults to the default value)
        nameText.setText(sharedPreferences.getString("name", getString(R.string.default_name_setting)));

        TextView schoolText = (TextView) findViewById(R.id.school_text);
        schoolText.setText(sharedPreferences.getString("school", getString(R.string.default_school_setting)));

        setupCoursesList();
    }

    @Override
    protected void onResume() {
        // TODO: Use SQL cursors rather than onResume
        super.onResume();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        TextView nameText = (TextView) findViewById(R.id.name_text);
        nameText.setText(sharedPreferences.getString("name", getString(R.string.default_name_setting)));

        TextView schoolText = (TextView) findViewById(R.id.school_text);
        schoolText.setText(sharedPreferences.getString("school", getString(R.string.default_school_setting)));

        setupCoursesList();
    }

    private void setupToolbar() {
        // Set up toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

//        if (myToolbar != null) {

        // Hamburger
        // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * http://stackoverflow.com/questions/28071763/toolbar-navigation-hamburger-icon-missing
         */
//        }
    }

    private void setupFab() {
        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.fab);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddCourseActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupCoursesList() {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.courses_recyclerview);

        // Use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };;
        mRecyclerView.setLayoutManager(mLayoutManager);

        setUpReadableDatabase();

        final List<Course> myDataset = this.mDatabaseHelper.getAllCourses();

        // specify an adapter (see also next example)
        this.mAdapter = new CourseAdapter(myDataset);
        mRecyclerView.setAdapter(this.mAdapter);

        RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);

        // Set up swiping items
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            boolean undo = false;
            DatabaseHelper mDatabaseHelper = DatabaseHelper.getInstance(getParent());
            int index;
            Course removed;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // TODO: Make deleting courses work properly, also when you add a course without restarting the app, deleting course will crash the app (something isn't being updated?)
                index = viewHolder.getAdapterPosition();
                mDatabaseHelper.deleteCourse(myDataset.get(index).getCourseCode());
                removed = mAdapter.remove(index);

                Snackbar.make(findViewById(R.id.fab), R.string.course_deleted, Snackbar.LENGTH_LONG).setAction(R.string.undo_delete, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mAdapter.add(removed, index);
                        undo = true;
                    }
                }).show();
                if (!undo) {
                }
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
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