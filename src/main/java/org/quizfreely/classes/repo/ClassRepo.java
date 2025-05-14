package org.quizfreely.classes.repo;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface ClassRepo extends CrudRepository<ClassEntity, UUID> {
    List<ClassEntity> findByAuthedUserId(String authedUserId);
    ClassEntity findById(UUID id);
}

