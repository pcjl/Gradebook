package us.philipli.gradebook.sqlite.helper;

/**
 * An contract class that defines Assessment table.
 */

public class Assessments {
    String name; // Primary key
    String code; // Primary key; Course code
    double weight;
    double grade;

    // constructors
    public Assessments() {}

    public Assessments(String name, String code, double weight, double grade) {
        this.name = name;
        this.code = code;
        this.weight = weight;
        this.grade = grade;
    }

    // setters
    public void setName(String name) {this.name = name;}

    public void setCode(String code) {this.code = code;}

    public void setWeight(double weight) {this.weight = weight;}

    public void setGrade(double grade) {this.grade = grade;}

    // getters
    public String getName() {return this.name;}

    public String getCode() {return this.code;}

    public double getWeight() {return this.weight;}

    public double getGrade() {return this.grade;}

}
