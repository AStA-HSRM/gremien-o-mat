package de.astahsrm.gremiomat.constraints.semesterandcourse;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.astahsrm.gremiomat.candidate.CandidateDto;

public class SemesterAndCourseConstraintValidator implements ConstraintValidator<SemesterAndCourse, CandidateDto> {
 
    @Override
    public boolean isValid(CandidateDto form, ConstraintValidatorContext context) {
        return (!form.getCourse().isBlank() && form.getSemester() != 0) || (form.getCourse().isBlank() && form.getSemester() == 0);
    }
 
}
