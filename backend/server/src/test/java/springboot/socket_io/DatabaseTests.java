package springboot.socket_io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import domain.Word;
import springBoot.repository.IWordRepository;
import springBoot.service.WordService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = DatabaseTests.class)
public class DatabaseTests {

    @Mock
    private IWordRepository wordRepository;

    @InjectMocks
    private WordService wordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveWord() {
        Word word = new Word();
        word.setId((long) 1);
        word.setWord("TestWord");

        when(wordRepository.save(word)).thenReturn(word);

        Word savedWord = wordService.saveWord(word);

        assertNotNull(savedWord);
        assertEquals("TestWord", savedWord.getWord());
        verify(wordRepository, times(1)).save(word);
    }

    @Test
    void testFetchWordList() {
        List<Word> words = Arrays.asList(
                new Word((long)1, "Word1"),
                new Word((long)2, "Word2"),
                new Word((long)3, "Word3")
        );

        when(wordRepository.findAll()).thenReturn(words);

        List<Word> fetchedWords = wordService.fetchWordList();

        assertEquals(3, fetchedWords.size());
        verify(wordRepository, times(1)).findAll();
    }

    @Test
    void testUpdateWord() {
        Word existingWord = new Word((long)1, "OldWord");
        Word updatedWord = new Word((long)1, "NewWord");

        when(wordRepository.findById(1L)).thenReturn(Optional.of(existingWord));
        when(wordRepository.save(existingWord)).thenReturn(existingWord);

        Word result = wordService.updateWord(updatedWord, 1L);

        assertNotNull(result);
        assertEquals("NewWord", result.getWord());
        verify(wordRepository, times(1)).findById(1L);
        verify(wordRepository, times(1)).save(existingWord);
    }

    @Test
    void testDeleteWordById() {
        long wordId = 1L;

        doNothing().when(wordRepository).deleteById(wordId);

        wordService.deleteWordById(wordId);

        verify(wordRepository, times(1)).deleteById(wordId);
    }

    @Test
    void testFetchWord() {
        Word word = new Word((long)1, "TestWord");

        when(wordRepository.findById(1L)).thenReturn(Optional.of(word));

        Optional<Word> fetchedWord = wordService.fetchWord(1);

        assertTrue(fetchedWord.isPresent());
        assertEquals("TestWord", fetchedWord.get().getWord());
        verify(wordRepository, times(1)).findById(1L);
    }

    @Test
    void testFetchRandomWordList() {
        List<Word> words = Arrays.asList(
                new Word((long)1, "Word1"),
                new Word((long)2, "Word2"),
                new Word((long)3, "Word3")
        );

        when(wordRepository.count()).thenReturn(3L);
        when(wordRepository.findById(anyLong())).thenAnswer(invocation -> {
            long id = invocation.getArgument(0);
            return Optional.of(words.get((int) id));
        });

        List<Word> randomWords = wordService.fetchRandomWordList(2);

        assertEquals(2, randomWords.size());
        verify(wordRepository, times(2)).findById(anyLong());
    }

    @Test
    void testFetchRandomWordListExceedsDatabaseSize() {
        when(wordRepository.count()).thenReturn(2L);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            wordService.fetchRandomWordList(3);
        });

        assertEquals("Requested number of words exceeds the database size.", exception.getMessage());
    }

    @Test
    void testGetTotalNumberOfWords() {
        when(wordRepository.count()).thenReturn(5L);

        long totalWords = wordService.getTotalNumberOfWords();

        assertEquals(5L, totalWords);
        verify(wordRepository, times(1)).count();
    }
}
