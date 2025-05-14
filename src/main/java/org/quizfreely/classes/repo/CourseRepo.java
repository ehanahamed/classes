package org.quizfreely.classes.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepo extends CrudRepository<Course, long> {
    Course findById(long id);
}

