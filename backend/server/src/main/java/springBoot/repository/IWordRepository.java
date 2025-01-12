package springBoot.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import domain.Word;

// Annotation
@Repository
@EntityScan()
public interface IWordRepository extends CrudRepository<Word, Long> {
    
}
