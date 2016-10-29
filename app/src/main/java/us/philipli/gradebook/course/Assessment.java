package us.philipli.gradebook.course;

public class Assessment {

    private String name;
    private double weight;
    private double grade;

    // Added after merge
    String code;

    public Assessment() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getGrade() {
        return grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    // Added after merge.
    public String getCode() {return this.code;}

    public void setCode(String code) {this.code = code;}

}
