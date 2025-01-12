package domain;
import java.util.Objects;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
// @NoArgsConstructor
// @AllArgsConstructor
@Builder
public class Word extends BaseEntity<Long>{
    private String word;


    public Word() {
    }

    public Word(Long id, String word) {
        this.id = id;
        this.word = word;
    }

    public Word(String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public String toString() {
        return "{" +
            " word='" + getWord() + "'" +
            "}";
    }
    
}
