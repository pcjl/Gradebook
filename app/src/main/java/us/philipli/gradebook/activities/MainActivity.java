package us.philipli.gradebook.activities;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import java.util.ArrayList;

import us.philipli.gradebook.R;
import us.philipli.gradebook.adapters.CourseAdapter;
import us.philipli.gradebook.course.Course;

public class MainActivity extends AppCompatActivity {

    // Set up recycler view for courses
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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

    private void setupCoursesList () {

        mRecyclerView = (RecyclerView) findViewById(R.id.courses_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<Course> myDataset = new ArrayList<>();

        Course temp = new Course();
        temp.setCourseName("Dank Memes 101");

        myDataset.add(temp);

        // specify an adapter (see also next example)
        mAdapter = new CourseAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
