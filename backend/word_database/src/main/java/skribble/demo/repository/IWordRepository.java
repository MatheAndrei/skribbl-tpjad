package skribble.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import skribble.demo.entity.Word;

// Annotation
@Repository
public interface IWordRepository extends CrudRepository<Word, Long> {
}
