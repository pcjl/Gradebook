package us.philipli.gradebook.course;

import java.util.ArrayList;

public class Course {

    private String courseCode; // Course code; The primary key.
    private String courseName;
    private double weight;
    private ArrayList<Assessment> assessments;

    // Added after merge.
    String color;
    double grade; // User's grade of this course.
    int include; // This is an indicator that shows whether user wants to include this course in GPA.

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

    // Added after merge.
    public String getColor() {return this.color;}

    public void setColor(String color) {this.color = color;}

    public double getGrade() {return this.grade;}

    public void setGrade(double grade) {this.grade = grade;}

    public int getInclude() {return this.include;}

    public void setInclude(int include) {this.include = include;}
}
