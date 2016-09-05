package us.philipli.gradebook.activities;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import org.w3c.dom.Text;

import java.util.Arrays;

import us.philipli.gradebook.R;
import us.philipli.gradebook.course.Assessment;

public class AddCourseActivity extends AppCompatActivity {

    final int MAX_ASSESSMENTS = 10; // total number of text views to add
    final Assessment[] ASSESSMENTS = new Assessment[MAX_ASSESSMENTS];
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

    // TODO: Watching for text changes should really be done with TextWatcher
    // http://stackoverflow.com/questions/5702771/how-to-use-single-textwatcher-for-multiple-edittexts
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

    private void showDiscardDialog() {
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
    }

    private void showEditDeleteDialog(View v) {

        final View row = v;

        new MaterialDialog.Builder(this)
                .items(R.array.long_press_items)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                        switch (which) {
                            case 0:
                                dialog.dismiss();
                                showAddAssessmentDialog(row);
                        }
                    }
                })
                .show();
    }

    /**
     * Shows the add assessment dialog, user can enter assessment name and assessment weight
     *
     * @return returns true if user saves assessment
     */
    public void showAddAssessmentDialog(View v) {

        final LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.assessment_list);
        final TextView row = (TextView) v;
        final int assessmentIndex = mLinearLayout.indexOfChild(v);

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.add_assessment_title)
                .customView(R.layout.dialog_add_assessment, false)
                .positiveText(R.string.action_save)
                .negativeText(R.string.add_assessment_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        // TODO: Add course weight check for invalid input

                        EditText assessmentNameField = (EditText) dialog.getCustomView().findViewById(R.id.assessment_name_field);
                        EditText assessmentWeightField = (EditText) dialog.getCustomView().findViewById(R.id.assessment_weight_field);

                        ASSESSMENTS[assessmentIndex] = new Assessment();
                        ASSESSMENTS[assessmentIndex].setName(assessmentNameField.getText().toString());

                        dialog.dismiss();

                        row.setText(ASSESSMENTS[assessmentIndex].getName());
                        row.setTextColor(Color.parseColor("#000000"));

                        addAssessmentRow();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .build();

        final View positiveAction = dialog.getActionButton(DialogAction.POSITIVE);
        positiveAction.setEnabled(false);

        final EditText assessmentNameField = (EditText) dialog.getCustomView().findViewById(R.id.assessment_name_field);

        // Edit a selection
        if (ASSESSMENTS[assessmentIndex] != null) {
            dialog.setTitle(R.string.edit_assessment_title);
            assessmentNameField.setText(ASSESSMENTS[assessmentIndex].getName());
        }

        assessmentNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                positiveAction.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dialog.show();
    }

    public void addAssessmentRow() {

        if (assessmentCount + 1 < MAX_ASSESSMENTS) {

            LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.assessment_list);

            View.OnClickListener addListener = new View.OnClickListener() {
                public void onClick(View v) {
                    showAddAssessmentDialog(v);
                }
            };

            final TextView assessmentRow = new TextView(this);

            // View params
            assessmentRow.setText(R.string.button_add_another_assessment);
            assessmentRow.setClickable(true);
            assessmentRow.setOnClickListener(addListener);
            assessmentRow.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

            int assessmentHeight = (int) getResources().getDimension(R.dimen.item_assessment_height);
            assessmentRow.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    assessmentHeight));

            // Add view to layout
            mLinearLayout.addView(assessmentRow);
            ASSESSMENT_VIEWS[assessmentCount] = assessmentRow;

            // Set previous to un-clickable, only enable long click

            // First one needs to be specially set
            TextView startingAssessment = (TextView) findViewById(R.id.first_assessment);
            startingAssessment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // do nothing
                }
            });
            startingAssessment.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showEditDeleteDialog(v);
                    return false;
                }
            });

            // Set the rest
            if (assessmentCount >= 1) {
                ASSESSMENT_VIEWS[assessmentCount - 1].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // do nothing
                    }
                });
                ASSESSMENT_VIEWS[assessmentCount - 1].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showEditDeleteDialog(v);
                        return false;
                    }
                });
            }

            assessmentCount++;

            // Scroll down automatically if the new assessment added is off screen
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
            showDiscardDialog();
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
                showDiscardDialog();
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
