package com.example.burningkey.texts.api.controller;

import com.example.burningkey.texts.api.dto.TextDto;
import com.example.burningkey.texts.entity.Text;
import com.example.burningkey.texts.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/texts")
@CrossOrigin // cross domain tomcat's port 8080 and react's 3000
public class TextController {

    @Autowired
    private TextService textService;

    // Get all texts
    @GetMapping
    public ResponseEntity<List<TextDto>> getTexts(@RequestParam(required = false) String language, @RequestParam(required = false) String difficulty) {
        List<TextDto> textDtos = textService.getTexts(language, difficulty).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(textDtos);
    }

    // Get text by id
    @GetMapping("/{id}")
    public ResponseEntity<TextDto> getTextById(@PathVariable Long id) {
        Optional<Text> optionalText = textService.getTextById(id);
        return optionalText.map(text -> ResponseEntity.ok(convertToDto(text)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get random text by given parameters
    @GetMapping("/text")
    public ResponseEntity<TextDto> getRandomText(@RequestParam(required = false) String language, @RequestParam(required = false) String difficulty) {
        Optional<Text> optionalText = textService.getRandomText(language, difficulty);
        return optionalText.map(text -> ResponseEntity.ok(convertToDto(text)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/word-sets")
    public ResponseEntity<List<String>> getWordSets() {
        return ResponseEntity.ok(TextService.getWordSets());
    }

    @GetMapping("/generate-words")
    public ResponseEntity<TextDto> getRandomWords(
            @RequestParam() String wordSetName,
            @RequestParam(required = false) Integer numWords,
            @RequestParam(required = false) Integer numSignsPercent,
            @RequestParam(required = false) Integer numUpperCasePercent,
            @RequestParam(required = false) Boolean doubleEveryWord) {
        int numWordsToUse = (numWords != null) ? numWords : 10;
        int numSignsPercentToUse = (numSignsPercent != null) ? numSignsPercent : 0;
        int numUpperCasePercentToUse = (numUpperCasePercent != null) ? numUpperCasePercent : 0;
        boolean  doubleEveryWordToUse = (doubleEveryWord != null) ? doubleEveryWord : false;
        Optional<Text> optionalWords = textService.getRandomWords(
                wordSetName,
                numWordsToUse,
                numSignsPercentToUse,
                numUpperCasePercentToUse,
                doubleEveryWordToUse);
        return optionalWords.map(text -> ResponseEntity.ok(convertToDto(text)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new text
    @PostMapping
    public ResponseEntity<TextDto> createText(@RequestBody TextDto textDto) {
        Text newText = convertToEntity(textDto);
        newText = textService.createText(newText);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToDto(newText));
    }

    // Update an existing text
    @PutMapping("/{id}")
    public ResponseEntity<TextDto> updateText(@PathVariable Long id, @RequestBody TextDto newTextDto) {
        Text newText = convertToEntity(newTextDto);
        Optional<Text> updatedText = textService.updateText(id, newText);
        return updatedText.map(text -> ResponseEntity.ok(convertToDto(text)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a text
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteText(@PathVariable Long id) {
        boolean deleted = textService.deleteText(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    // Convert Entity to DTO
    private TextDto convertToDto(Text text) {
        TextDto textDto = new TextDto();
        textDto.setTextId(text.getId());
        textDto.setTitle(text.getTitle());
        textDto.setContent(text.getContent());
        textDto.setLanguage(text.getLanguage());
        textDto.setDifficulty(text.getDifficulty());
        return textDto;
    }

    // Convert DTO to Entity
    private Text convertToEntity(TextDto textDto) {
        Text text = new Text();
        text.setId(textDto.getTextId());
        text.setTitle(textDto.getTitle());
        text.setContent(textDto.getContent());
        text.setLanguage(textDto.getLanguage());
        text.setDifficulty(textDto.getDifficulty());
        return text;
    }

}
