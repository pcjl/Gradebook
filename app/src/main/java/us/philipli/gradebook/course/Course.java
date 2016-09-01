package us.philipli.gradebook.course;

import java.util.ArrayList;

public class Course {

    private String courseCode;
    private String courseName;
    private double weight;
    private ArrayList<Assessment> assessments;

    public Course () {
        this.assessments = new ArrayList<Assessment>();
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void addAssessment(String name, double weight) {

        Assessment newAssessment = new Assessment();
        newAssessment.setName(name);
        newAssessment.setWeight(weight);

        this.assessments.add(newAssessment);
    }
}
