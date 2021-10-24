package de.astahsrm.gremiomat.faculty;

import java.util.List;
import java.util.Optional;

public interface FacultyService {
    public Optional<Faculty> getByAbbr(String abbr);

    public List<Faculty> getAllFaculties();
}
