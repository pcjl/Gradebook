package us.philipli.gradebook.course;

public class Assessment {
    private String name;
    private double weight;
    private double numerator;
    private double denominator;

    private double grade;

    // Added after merge
    private String code;

    public Assessment() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public boolean setDenominator(double denominator) {
        if (denominator > 0) {
            this.denominator = denominator;

            return true;
        } else {
            return false;
        }
    }

    public double getGrade() {
        return this.grade;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    private double updateGrade(double numerator, double denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        this.grade = this.numerator / this.denominator;

        return this.grade;
    }

    private double updateGrade(double numerator) {
        this.numerator = numerator;
        this.grade = this.numerator / this.denominator;

        return this.grade;
    }

    // Added after merge.
    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
