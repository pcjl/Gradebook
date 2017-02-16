package us.philipli.gradebook.activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import java.util.List;

import us.philipli.gradebook.R;
import us.philipli.gradebook.course.Course;
import us.philipli.gradebook.sqlite.helper.DatabaseHelper;

public class CourseActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private FloatingActionButton myFab;
    private DatabaseHelper mDatabaseHelper;
    private Course myCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        setupToolbar();
        setupFab();

        final ScrollView scrollView = (ScrollView) findViewById(R.id.course_scrollview);
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int dy = scrollView.getScrollY();
                if (dy > 0) {
                    myFab.hide();
                } else {
                    myFab.show();
                }
            }
        });

        setUpReadableDatabase();
        final List<Course> myDataset = this.mDatabaseHelper.getAllCourses();

        Bundle extras = this.getIntent().getExtras();
        this.myCourse = myDataset.get(extras.getInt("pos"));

        getSupportActionBar().setTitle(this.myCourse.getCourseCode());
    }

    private void setupToolbar() {
        // Set up toolbar
        this.myToolbar = (Toolbar) findViewById(R.id.course_toolbar);
        setSupportActionBar(this.myToolbar);

//        if (myToolbar != null) {

        // Hamburger
        // this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /**
         * http://stackoverflow.com/questions/28071763/toolbar-navigation-hamburger-icon-missing
         */
//        }
    }

    private void setupFab() {
        this.myFab = (FloatingActionButton) findViewById(R.id.course_fab);
    }

    private SQLiteDatabase setUpReadableDatabase() {
        this.mDatabaseHelper = DatabaseHelper.getInstance(this);
        return this.mDatabaseHelper.getReadableDatabase();
    }
}
