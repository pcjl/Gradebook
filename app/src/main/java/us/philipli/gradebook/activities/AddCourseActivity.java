package us.philipli.gradebook.activities;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;

import us.philipli.gradebook.R;
import us.philipli.gradebook.course.Assessment;
import us.philipli.gradebook.course.Course;
import us.philipli.gradebook.sqlite.helper.DatabaseHelper;

public class AddCourseActivity extends AppCompatActivity {
    final int MAX_ASSESSMENTS = 10; // Total number of text views to add
    final ArrayList<Assessment> ASSESSMENTS = new ArrayList<>(MAX_ASSESSMENTS);
    final ArrayList<TextView> ASSESSMENT_VIEWS = new ArrayList<>(MAX_ASSESSMENTS); // Create an empty array
    private int assessmentCount = 0;

    private String courseCode;
    private String courseName;
    private double courseWeight = -1.0;
    private int include = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        setupToolBar();
        changeTransparency();

        final TextView firstAssessment = (TextView) findViewById(R.id.first_assessment);
        ASSESSMENT_VIEWS.add(firstAssessment);

        EditText courseCodeField = (EditText) findViewById(R.id.code_field);
        courseCodeField.setText(this.courseCode);
        courseCodeField.addTextChangedListener(new GenericTextWatcher(this, courseCodeField));

        EditText courseNameField = (EditText) findViewById(R.id.name_field);
        courseNameField.setText(this.courseName);
        courseNameField.addTextChangedListener(new GenericTextWatcher(this, courseNameField));

        EditText courseWeightField = (EditText) findViewById(R.id.weight_field);
        courseWeightField.addTextChangedListener(new GenericTextWatcher(this, courseWeightField));

        Switch includeSwitch = (Switch) findViewById(R.id.gpa_include_switch);
        includeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                include = (isChecked) ? 1 : 0;
            }
        });
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
        // Check if any of the input has changed
        return !(this.assessmentCount == 0 && (this.courseCode == null || this.courseCode.equals(""))
                && (this.courseName == null || this.courseName.equals("")) && this.courseWeight == -1.0);
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
        final LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.assessment_list);
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
                                break;
                            case 1:
                                dialog.dismiss();

                                int rowIndex = mLinearLayout.indexOfChild(row);
                                ViewGroup parent = ((ViewGroup) row.getParent());

                                if (rowIndex == 0 && ASSESSMENTS.size() == 1) {
                                    ASSESSMENT_VIEWS.get(rowIndex).setText(R.string.button_add_assessment);
                                    ASSESSMENT_VIEWS.get(rowIndex).setTextColor(Color.parseColor("#808080"));
                                    parent.removeView(parent.getChildAt(rowIndex + 1));

                                    // Set again click behavior
                                    ASSESSMENT_VIEWS.get(rowIndex).setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            showAddAssessmentDialog(v);
                                        }
                                    });

                                    ASSESSMENT_VIEWS.get(rowIndex).setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            // do nothing
                                            return false;
                                        }
                                    });
                                } else if (rowIndex == 0) {
                                    ASSESSMENTS.set(rowIndex, ASSESSMENTS.get(rowIndex));
                                    ASSESSMENT_VIEWS.get(rowIndex).setText(ASSESSMENTS.get(1).getName());
                                    parent.removeView(parent.getChildAt(1));
                                    ASSESSMENT_VIEWS.remove(1);
                                } else {
                                    ASSESSMENT_VIEWS.remove(rowIndex);
                                    parent.removeView(row);
                                }

                                ASSESSMENTS.remove(rowIndex);
                                break;
                        }
                    }
                })
                .show();
    }

    /**
     * Shows the add assessment dialog, user can enter assessment name and assessment weight
     */
    public void showAddAssessmentDialog(View v) {
        final LinearLayout mLinearLayout = (LinearLayout) findViewById(R.id.assessment_list);
        final TextView row = (TextView) v;
        final int assessmentIndex = mLinearLayout.indexOfChild(v);

        final boolean edit = !ASSESSMENTS.isEmpty() && (assessmentIndex < ASSESSMENTS.size());

        MaterialDialog dialog = new MaterialDialog.Builder(this)
                .title(R.string.add_assessment_title)
                .customView(R.layout.dialog_add_assessment, false)
                .positiveText(R.string.action_save)
                .negativeText(R.string.add_assessment_cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        EditText assessmentNameField = (EditText) dialog.getCustomView().findViewById(R.id.assessment_name_field);
                        EditText assessmentWeightField = (EditText) dialog.getCustomView().findViewById(R.id.assessment_weight_field);

                        Assessment newAssessment = new Assessment();
                        newAssessment.setName(assessmentNameField.getText().toString());
                        try {
                            newAssessment.setWeight(Double.parseDouble(assessmentWeightField.getText().toString()));
                        } catch (Exception e) {
                            // TODO: Catch exception
                        }

                        dialog.dismiss();

                        row.setText(newAssessment.getName());
                        row.setTextColor(Color.parseColor("#000000"));

                        if (!edit) {
                            ASSESSMENTS.add(newAssessment);
                            addAssessmentRow();
                        } else {
                            int rowIndex = mLinearLayout.indexOfChild(row);
                            ASSESSMENTS.set(rowIndex, newAssessment);
                        }
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
        final EditText assessmentWeightField = (EditText) dialog.getCustomView().findViewById(R.id.assessment_weight_field);

        // Edit a selection
        if (edit) {
            dialog.setTitle(R.string.edit_assessment_title);
            assessmentNameField.setText(ASSESSMENTS.get(assessmentIndex).getName());
            assessmentWeightField.setText(String.valueOf(ASSESSMENTS.get(assessmentIndex).getWeight()));
            positiveAction.setEnabled(true);
        }

        TextWatcher assessmentFieldsWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Check null?
                String name = assessmentNameField.getText().toString();
                String weight = assessmentWeightField.getText().toString();
                if (!name.equals("") && !weight.equals("")) {
                    positiveAction.setEnabled(true);
                } else {
                    positiveAction.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        assessmentNameField.addTextChangedListener(assessmentFieldsWatcher);
        assessmentWeightField.addTextChangedListener(assessmentFieldsWatcher);

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
            ASSESSMENT_VIEWS.add(assessmentRow);

            // Set previous to un-clickable, only enable long click

            // First one needs to be specially set
            for (int i = 0; i < ASSESSMENT_VIEWS.size() - 1; i++) {
                ASSESSMENT_VIEWS.get(i).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // do nothing
                    }
                });
                ASSESSMENT_VIEWS.get(i).setOnLongClickListener(new View.OnLongClickListener() {
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
                // Save
                if (this.courseCode == null || this.courseCode.equals("") || this.courseName == null || this.courseName.equals("") || this.courseWeight < 0) {
                    new MaterialDialog.Builder(this)
                            .content(R.string.course_create_warning)
                            .positiveText(R.string.ok)
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    Toast.makeText(AddCourseActivity.this, R.string.course_saved,
                            Toast.LENGTH_LONG).show();
                    DatabaseHelper mDatabaseHelper = DatabaseHelper.getInstance(this);

                    Course newCourse = new Course();
                    newCourse.setCourseCode(this.courseCode);
                    newCourse.setCourseName(this.courseName);
                    newCourse.setWeight(this.courseWeight);
                    newCourse.setInclude((this.include));
                    newCourse.setColor("green");
                    newCourse.setGrade(100.0d);

                    mDatabaseHelper.createCourse(newCourse, ASSESSMENTS);

                    finish();
                    return true;
                }
        }

        return super.onOptionsItemSelected(item);
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseWeight(double courseWeight) {
        this.courseWeight = courseWeight;
    }
}

class GenericTextWatcher implements TextWatcher {
    private AddCourseActivity activity;
    private View view;

    GenericTextWatcher(AddCourseActivity activity, View view) {
        this.activity = activity;
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        switch (this.view.getId()) {
            case R.id.code_field:
                this.activity.setCourseCode(text);
                break;
            case R.id.name_field:
                this.activity.setCourseName(text);
                break;
            case R.id.weight_field:
                try {
                    this.activity.setCourseWeight(Double.parseDouble(text));
                } catch (Exception e) {
                    this.activity.setCourseWeight(-1.0);
                }
                break;
        }
    }
}