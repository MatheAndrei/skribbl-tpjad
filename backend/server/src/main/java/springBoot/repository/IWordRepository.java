package springBoot.repository;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import domain.Word;

@Repository
public interface IWordRepository extends CrudRepository<Word, Long> {
    
}
