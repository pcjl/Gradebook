package us.philipli.gradebook.sqlite.helper;

import us.philipli.gradebook.course.Assessment;

/**
 * An contract class that defines Assessment table.
 */

public class Assessments {
    String name; // Primary key
    String code; // Primary key; Course code
    long weight;
    long marks;

    // constructors
    public Assessments() {}

    public Assessments(String name, String code, long weight, long marks) {
        this.name = name;
        this.code = code;
        this.weight = weight;
        this.marks = marks;
    }

    // setters
    public void setName(String name) {this.name = name;}

    public void setCode(String code) {this.code = code;}

    public void setWeight(long weight) {this.weight = weight;}

    public void setMarks(long marks) {this.marks = marks;}

    // getters
    public String getName() {return this.name;}

    public String getCode() {return this.code;}

    public long getWeight() {return this.weight;}

    public long getMarks() {return this.marks;}

}
