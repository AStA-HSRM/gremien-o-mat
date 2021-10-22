package de.astahsrm.gremiomat.constraints.semesterandcourse;

import javax.validation.Payload;

public @interface SemesterAndCourse {
    String message() default "Both Semester and course must either be both or neither entered.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
