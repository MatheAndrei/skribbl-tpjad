package springBoot.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import domain.Word;
import springBoot.repository.IWordRepository;

import java.util.*;

@Service
public class WordService implements IWordService {
    @Autowired
    private IWordRepository wordRepository;
    @Override
    public Word saveWord(Word word) {
        return wordRepository.save(word);
    }

    @Override
    public List<Word> fetchWordList() {
        return (List<Word>) wordRepository.findAll();
    }

    @Override
    public Word updateWord(Word word, Long wordId) {
        Word wordDb = wordRepository.findById(wordId).get();
        if(wordDb != null) {
            wordDb.setWord(word.getWord());
        }
        return wordRepository.save(word);
    }

    @Override
    public void deleteWordById(Long wordId) {
        wordRepository.deleteById(wordId);
    }

    public Optional<Word> fetchWord(int id) {
        return wordRepository.findById((long) id);
    }

    public List<Word> fetchRandomWordList(int numberOfWords) {
        long totalWords = getTotalNumberOfWords();
        if (numberOfWords > totalWords) {
            throw new IllegalArgumentException("Requested number of words exceeds the database size.");
        }

        Set<Integer> selectedIds = new HashSet<>();
        List<Optional<Word>> optionalRandomWords = new ArrayList<>();
        Random random = new Random();

        while (optionalRandomWords.size() < numberOfWords) {
            int randomIndex = random.nextInt((int) totalWords);
            if (!selectedIds.contains(randomIndex)) {
                selectedIds.add(randomIndex);
                optionalRandomWords.add(fetchWord(randomIndex)); // Fetching an Optional<Word>
            }
        }

        return optionalRandomWords.stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }


    public long getTotalNumberOfWords() {
        return wordRepository.count();
    }
}
