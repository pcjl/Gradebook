package us.philipli.gradebook.activities;

import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import pl.coreorb.selectiondialogs.views.SelectedItemView;
import us.philipli.gradebook.ColorDialogFragment;
import us.philipli.gradebook.R;

public class AddCourseActivity extends AppCompatActivity {

    final int MAX_ASSESSMENTS = 10; // total number of textviews to add
    final TextView[] ASSESSMENT_VIEWS = new TextView[MAX_ASSESSMENTS]; // create an empty array;
    private int assessmentCount = 0;

    private String courseCode;
    private String courseName;
    private Double courseWeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        setupToolBar();
        changeTransparency();

        final TextView firstAssessment = (TextView) findViewById(R.id.first_assessment);
        ASSESSMENT_VIEWS[0] = firstAssessment;
    }

    private void changeTransparency() {

        ImageView checkIcon = (ImageView) findViewById(R.id.check_icon);
        ImageView classIcon = (ImageView) findViewById(R.id.class_icon);
        ImageView weightIcon = (ImageView) findViewById(R.id.weight_icon);
        ImageView assessmentIcon = (ImageView) findViewById(R.id.assessment_icon);

        checkIcon.setImageAlpha(140);
        classIcon.setImageAlpha(140);
        weightIcon.setImageAlpha(140);
        assessmentIcon.setImageAlpha(140);
    }

    private void setupToolBar() {

        // Set up toolbar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        if (myToolbar != null) {
            // Hide title
            this.getSupportActionBar().setDisplayShowTitleEnabled(false);

            // Add close button
            this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
            this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private boolean getValues() {

        EditText courseCodeField = (EditText) findViewById(R.id.code_field);
        EditText courseNameField = (EditText) findViewById(R.id.name_field);
        EditText courseWeightField = (EditText) findViewById(R.id.weight_field);

        courseCode = courseCodeField.getText().toString();
        courseName = courseNameField.getText().toString();

        try {
            courseWeight = Double.parseDouble(courseWeightField.getText().toString());
        } catch (Exception ex) {
            courseWeight = 0.0;
        }

        return !(assessmentCount == 0 && courseCode.equals("")
                && courseName.equals("") && courseWeight == 0.0);
    }

    public void addCourse(View v) {

        if (assessmentCount + 1 < MAX_ASSESSMENTS) {
            LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.assessment_list);

            View.OnClickListener addListener = new View.OnClickListener() {
                public void onClick(View v) {
                    addCourse(v);
                }
            };

            final TextView assessmentRow = new TextView(this);

            // View params
            assessmentRow.setText(R.string.button_add_another_assessment);
            assessmentRow.setClickable(true);
            assessmentRow.setOnClickListener(addListener);
            assessmentRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            int assessmentHeight = (int) getResources().getDimension(R.dimen.item_assessment_height);
//            assessmentRow.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            assessmentRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    assessmentHeight));

            // Add view to layout
            mLinearLayout.addView(assessmentRow);
            ASSESSMENT_VIEWS[assessmentCount] = assessmentRow;

            // Set previous to unclickable
            TextView startingAssessment = (TextView) findViewById(R.id.first_assessment);
            startingAssessment.setClickable(false);

            if (assessmentCount >= 1) {
                ASSESSMENT_VIEWS[assessmentCount - 1].setClickable(false);
            }

            assessmentCount++;

            final NestedScrollView mNestedScrollView = (NestedScrollView) findViewById(R.id.assessment_scroll);
            mNestedScrollView.post(new Runnable() {
                @Override
                public void run() {
                    mNestedScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                }
            });

        }
    }

    @Override
    public boolean onSupportNavigateUp() {

        if (getValues()) {
            new MaterialDialog.Builder(this)
                    .content(R.string.course_discard_dialog)
                    .positiveText(R.string.keep_editing)
                    .negativeText(R.string.discard)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            finish(); // Destroy this activity
                        }
                    })
                    .show();
        } else {
            finish();
        }
        return false;
    }

    // Handle back action
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (getValues()) {
                new MaterialDialog.Builder(this)
                        .content(R.string.course_discard_dialog)
                        .positiveText(R.string.keep_editing)
                        .negativeText(R.string.discard)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                finish(); // Destroy this activity
                            }
                        })
                        .show();
            } else {
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Add save button
        final MenuItem menuItem = menu.add(Menu.NONE, 0, Menu.NONE, R.string.action_save);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_IF_ROOM);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case 0:
                // save
                Toast.makeText(AddCourseActivity.this, "Data saved",
                        Toast.LENGTH_LONG).show();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
