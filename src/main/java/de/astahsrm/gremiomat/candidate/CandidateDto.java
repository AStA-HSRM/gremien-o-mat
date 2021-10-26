package de.astahsrm.gremiomat.candidate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.astahsrm.gremiomat.constraints.fieldsexist.CourseAndSemesterBothExist;

@CourseAndSemesterBothExist
public class CandidateDto {

    private int age;

    private int semester;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    private String faculty;

    @Size(max = 50)
    private String course;

    @Size(max = 1000)
    private String bio;

    private boolean ageShowing;

    private boolean courseShowing;

    public CandidateDto() {
        this.age = 0;
        this.semester = 0;
        this.firstname = "";
        this.lastname = "";
        this.faculty = "";
        this.course = "";
        this.bio = "";
        this.ageShowing = false;
        this.courseShowing = false;
    }

    public boolean isAgeShowing() {
        return ageShowing;
    }

    public void setAgeShowing(boolean ageShowing) {
        this.ageShowing = ageShowing;
    }

    public boolean isCourseShowing() {
        return courseShowing;
    }

    public void setCourseShowing(boolean courseShowing) {
        this.courseShowing = courseShowing;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
