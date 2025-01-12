package springBoot.controller;
// jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import domain.Word;
import springBoot.service.WordService;

import java.util.List;

@RestController
public class WordController {
    @Autowired private WordService wordService;

    @PostMapping("/words")
    public Word saveWord(
            @RequestBody Word word)
    {
        return wordService.saveWord(word);
    }

    // Read operation
    @GetMapping("/words")
    public List<Word> fetchWordsList()
    {
        return wordService.fetchWordList();
    }

    @GetMapping("/words/{count}")
    public List<Word> fetchRandomWordList(@PathVariable("count") int count)
    {
        return wordService.fetchRandomWordList(count);
    }

    // Update operation
    @PutMapping("/words/{id}")
    public Word
    updateWord(@RequestBody Word word,
                     @PathVariable("id") Long wordId)
    {
        return wordService.updateWord(
                word, wordId);
    }

    // Delete operation
    @DeleteMapping("/words/{id}")
    public String deleteWordById(@PathVariable("id")
                                       Long wordId)
    {
        wordService.deleteWordById(
                wordId);
        return "Deleted Successfully";
    }
}