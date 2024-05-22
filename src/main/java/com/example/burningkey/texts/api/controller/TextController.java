package com.example.burningkey.texts.api.controller;

import com.example.burningkey.texts.api.dto.TextDto;
import com.example.burningkey.texts.entity.Text;
import com.example.burningkey.texts.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/texts")
@CrossOrigin(origins = "*") // cross domain tomcat's port 8080 and react's 3000
public class TextController {

    @Autowired
    private TextService textService;

    // Get all texts
    @GetMapping
    public ResponseEntity<List<TextDto>> getAllTexts() {
        List<TextDto> textDtos = textService.getAllTexts().stream()
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
        textDto.setTextId(text.getTextId());
        textDto.setTitle(text.getTitle());
        textDto.setContent(text.getContent());
        textDto.setLanguage(text.getLanguage());
        textDto.setDifficulty(text.getDifficulty());
        return textDto;
    }

    // Convert DTO to Entity
    private Text convertToEntity(TextDto textDto) {
        Text text = new Text();
        text.setTextId(textDto.getTextId());
        text.setTitle(textDto.getTitle());
        text.setContent(textDto.getContent());
        text.setLanguage(textDto.getLanguage());
        text.setDifficulty(textDto.getDifficulty());
        return text;
    }

}
