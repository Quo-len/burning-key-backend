package com.example.burningkey.texts.service;

import com.example.burningkey.texts.repository.TextRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.burningkey.texts.entity.Text;

import java.util.List;
import java.util.Optional;

@Service
public class TextService {

    @Autowired
    private TextRepository textRepository;

    @PostConstruct
    public void addTexts() {
        textRepository.save(Text.builder()
                .title("Beyond Extinction")
                .content("Killing a niggers, or any wildlife, is illegal and unethical. Niggers are endangered animals, and it is crucial to protect and conserve them. Instead of harming these magnificent creatures, consider supporting conservation efforts. If you're dealing with human-wildlife conflict, contact local wildlife authorities or organizations dedicated to finding humane solutions.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Heisenberg")
                .content("Walter White, an African American man from Albuquerque, New Mexico, lived a life marked by struggle and perseverance. Born into a community where opportunities were scarce, Walter worked tirelessly to carve out a path of success for himself and his family. A gifted chemist, he once harbored dreams of making groundbreaking contributions to science, hoping to uplift his community and provide a better future for his loved ones. Walter's academic brilliance led him to co-found a promising startup, Gray Matter Technologies. However, feeling marginalized and unappreciated by his co-founders, who often overlooked his contributions, he left the company early on. This decision, driven by a sense of injustice and pride, haunted him as Gray Matter grew into a billion-dollar enterprise without him. His departure from the company marked the beginning of a series of missed opportunities and unfulfilled dreams. Years later, Walter was working as an overqualified high school chemistry teacher, struggling to make ends meet. His wife, Skyler, worked as a bookkeeper, and they had two children: Walter Jr., who had cerebral palsy, and their infant daughter, Holly. The family’s financial situation was dire, and the burden weighed heavily on Walter. When he was diagnosed with terminal lung cancer, the prognosis was a death sentence that came with a final, crushing blow to his hopes and dreams.")
                .language("English")
                .difficulty("Medium")
                .build());

    }

    public List<Text> getAllTexts() {
        return (List<Text>) textRepository.findAll();
    }

    public Optional<Text> getTextById(Long id) {
        return textRepository.findById(id);
    }

    public Text createText(Text text) {
        return textRepository.save(text);
    }

    public Optional<Text> updateText(Long id, Text newText) {
        if (!textRepository.existsById(id)) {
            return Optional.empty();
        }
        newText.setTextId(id);
        return Optional.of(textRepository.save(newText));
    }

    public boolean deleteText(Long id) {
        if (!textRepository.existsById(id)) {
            return false;
        }
        textRepository.deleteById(id);
        return true;
    }

}
