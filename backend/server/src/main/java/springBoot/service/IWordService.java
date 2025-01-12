package springBoot.service;

import java.util.List;

import domain.Word;

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
