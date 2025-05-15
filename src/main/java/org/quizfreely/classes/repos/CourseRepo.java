package org.quizfreely.classes.repos;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepo extends CrudRepository<Course, long> {
    Course findById(long id);
}

