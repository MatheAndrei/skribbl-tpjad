package skribble.demo.service;

import skribble.demo.entity.Word;

import java.util.List;

public interface IWordService {
    Word saveWord(Word word);

    // Read operation
    List<Word> fetchWordList();

    // Update operation
    Word updateWord(Word word,
                                Long wordId);

    // Delete operation
    void deleteWordById(Long wordId);
}
