package us.philipli.gradebook.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import us.philipli.gradebook.R;
import us.philipli.gradebook.course.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {

    private ArrayList<Course> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseAdapter(ArrayList<Course> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_course, parent, false);
        CourseHolder vh = new CourseHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textview_course_name.setText(mDataset.get(position).getCourseName());
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class CourseHolder extends RecyclerView.ViewHolder {

        public TextView textview_course_name;

        public CourseHolder(View itemView) {
            super(itemView);

            textview_course_name = (TextView) itemView.findViewById(R.id.textview_course_name);

        }
    }
}