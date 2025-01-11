package domain;
import java.util.Objects;

public class Word extends Entity{
    private String word;


    public Word() {
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
