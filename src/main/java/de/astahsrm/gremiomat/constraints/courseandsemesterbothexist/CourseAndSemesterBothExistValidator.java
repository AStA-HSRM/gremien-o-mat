package de.astahsrm.gremiomat.constraints.courseandsemesterbothexist;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.BeanWrapperImpl;

import de.astahsrm.gremiomat.candidate.CandidateDto;

public class CourseAndSemesterBothExistValidator
    implements ConstraintValidator<CourseAndSemesterBothExist, CandidateDto> {

  @Override
  public void initialize(CourseAndSemesterBothExist constraintAnnotation) {
    // Initializes Validator
  }

  @Override
  public boolean isValid(CandidateDto dto, ConstraintValidatorContext context) {
    String course = (String) new BeanWrapperImpl(dto).getPropertyValue("course");
    int semester = (int) new BeanWrapperImpl(dto).getPropertyValue("semester");
    if (course != null) {
      return (!course.isBlank() && semester != 0) || (course.isBlank() && semester == 0);
    } else {
      return semester == 0;
    }
  }
}