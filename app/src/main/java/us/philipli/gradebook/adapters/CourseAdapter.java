package us.philipli.gradebook.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import us.philipli.gradebook.R;
import us.philipli.gradebook.activities.CourseActivity;
import us.philipli.gradebook.course.Course;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.CourseHolder> {
    private List<Course> mDataset;

    // Provide a suitable constructor (depends on the kind of dataset)
    public CourseAdapter(List<Course> mDataset) {
        this.mDataset = mDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CourseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_course, parent, false);
        return new CourseHolder(v);
    }

    @Override
    public void onBindViewHolder(CourseHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textview_course_name.setText(this.mDataset.get(position).getCourseName());
        holder.textview_course_code.setText(this.mDataset.get(position).getCourseCode());
        holder.textview_course_weight.setText("Weight: " + String.format("%.2f", this.mDataset.get(position).getWeight()));
        holder.textview_course_mark.setText(String.format("%.1f%%", this.mDataset.get(position).getGrade()));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.mDataset.size();
    }

    public Course remove(int index) {
        Course removed = this.mDataset.remove(index);
        notifyItemRemoved(index);
        notifyItemRangeChanged(index, this.mDataset.size());
        notifyDataSetChanged();;
        return removed;
    }

    public void add(Course course, int index) {
        this.mDataset.add(index, course);
        notifyItemInserted(index);
        notifyItemRangeChanged(index, this.mDataset.size());
        notifyDataSetChanged();;
    }

    public void clear() {
        this.mDataset.clear();
    }

    class CourseHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout relativeLayout;
        TextView textview_course_name;
        TextView textview_course_code;
        TextView textview_course_weight;
        TextView textview_course_mark;

        CourseHolder(View itemView) {
            super(itemView);
            this.relativeLayout = (RelativeLayout) itemView.findViewById(R.id.course_item);
            this.relativeLayout.setOnClickListener(this);

            this.textview_course_name = (TextView) itemView.findViewById(R.id.textview_course_name);
            this.textview_course_code = (TextView) itemView.findViewById(R.id.textview_course_code);
            this.textview_course_weight = (TextView) itemView.findViewById(R.id.textview_course_weight);
            this.textview_course_mark = (TextView) itemView.findViewById(R.id.textview_course_mark);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), CourseActivity.class);
            intent.putExtra("pos", this.getLayoutPosition());
            view.getContext().startActivity(intent);
        }
    }
}