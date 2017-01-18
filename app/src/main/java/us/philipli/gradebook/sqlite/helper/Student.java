package us.philipli.gradebook.sqlite.helper;

/**
 * A contract class that defines Student table.
 */

public class Student {
    private int id; // Primary key
    private String name;
    private long gpa;
    private String institution;

    // Constructors
    public Student() {
    }

    public Student(int id, String name, long gpa, String institution) {
        this.id = id;
        this.name = name;
        this.gpa = gpa;
        this.institution = institution;
    }

    // setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGpa(long gpa) {
        this.gpa = gpa;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    // getters
    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public long getGpa() {
        return this.gpa;
    }

    public String getInstitution() {
        return this.institution;
    }
}
