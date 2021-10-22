package de.astahsrm.gremiomat.candidate;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import de.astahsrm.gremiomat.constraints.semesterandcourse.SemesterAndCourse;

public class CandidateDto {

    @Min(0)
    @Max(120)
    private int age;

    @Min(1)
    @Max(20)
    @SemesterAndCourse
    private int semester;

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    @Size(max = 50)
    @SemesterAndCourse
    private String course;

    @Size(max = 1000)
    private String bio;

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
