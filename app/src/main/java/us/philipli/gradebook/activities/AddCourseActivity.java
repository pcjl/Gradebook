package us.philipli.gradebook.activities;

import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import us.philipli.gradebook.R;

public class AddCourseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        setupToolBar();
        changeTransparency();
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
        EditText assessmentField = (EditText) findViewById(R.id.assessment_field);

        return false;
    }

    @Override
    public boolean onSupportNavigateUp() {

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

        return false;
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
