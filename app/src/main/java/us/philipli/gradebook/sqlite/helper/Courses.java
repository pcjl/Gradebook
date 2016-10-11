package us.philipli.gradebook.sqlite.helper;

/**
 * A contract class that defines Courses table
 */

public class Courses {
    String code; // Course ode; Primary key
    String name; // Course name
    long weight;
    int include; // This is an indicator that shows whether this course's mark shold be included into GPA; 1 for true 0 for false
    String color;
    long marks;

    // constructors
    public Courses(){}

    public Courses(String code, String name, long weight, int include, String color, long marks) {
        this.code = code;
        this.name = name;
        this.weight = weight;
        this.include = include;
        this.color = color;
        this.marks = marks;
    }

    // setters
    public void setCode(String code) {this.code = code;}

    public void setName(String name) {this.name = name;}

    public void setWeight(long weight) {this.weight = weight;}

    public void setInclude(int include) {this.include = include;}

    public void setColor(String color) {this.color = color;}

    public void setMarks(long marks) {this.marks = marks;}

    // getters
    public String getCode() {return this.code;}

    public String getName() {return this.name;}

    public long getWeight() {return this.weight;}

    public int getInclude() {return this.include;}

    public String getColor() {return this.color;}

    public long getMarks() {return this.marks;}
}
