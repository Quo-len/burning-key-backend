package com.example.burningkey.texts.service;

import com.example.burningkey.texts.repository.TextRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import com.example.burningkey.texts.entity.Text;

import java.io.*;
import java.util.*;

@Service
public class TextService {

    @Autowired
    private TextRepository textRepository;

    @PostConstruct
    public void addTexts() {

        textRepository.save(Text.builder()
                .title("Insert title")
                .content("Sukumar Azhikode defined a short story as 'a brief prose narrative with an intense episodic or anecdotal effect'. Flannery O'Connor emphasized the need to consider what is exactly meant by the descriptor short.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Typing skill")
                .content("The quick brown fox jumps over the lazy dog. This sentence is often used for typing practice because it contains every letter in the English alphabet at least once. It's a perfect example of a pangram, a sentence that uses all the letters of the alphabet. Typing accurately and efficiently is a valuable skill in today's digital world. With the increasing reliance on computers and digital communication, the ability to type quickly and without errors is more important than ever. Whether you are a student, a professional, or just someone who enjoys spending time online, improving your typing skills can have a significant impact on your productivity and overall experience. To improve your typing skills, start by focusing on proper hand placement and posture. Make sure your fingers rest on the home row keys and that you are sitting up straight. Practice regularly, and challenge yourself with new and varied texts to type. As you become more comfortable and confident, you will notice your speed and accuracy improving. Remember, consistency is key. The more you practice, the better you will become. Happy typing!")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Insert title")
                .content("In the heart of the bustling city, there was a small, cozy bookstore that seemed to be a world of its own. The walls were lined with shelves that reached up to the ceiling, each one filled with books of every genre imaginable. The scent of old paper and fresh ink mingled in the air, creating an aroma that was both comforting and invigorating. People from all walks of life visited the bookstore. Some came to find specific titles, while others wandered aimlessly, letting their fingers glide over the spines of books until something caught their eye. The owner, an elderly gentleman with a kind smile and a wealth of knowledge, was always ready to recommend a good read or engage in a lively discussion about literature. In one corner of the bookstore, there was a small reading nook with a plush armchair and a lamp that cast a warm glow. It was the perfect spot for losing oneself in a good book. Many patrons spent hours there, completely absorbed in the worlds that unfolded within the pages they held. The bookstore also hosted weekly events, from poetry readings to book signings, which drew crowds of enthusiastic readers and aspiring writers. These gatherings fostered a sense of community and camaraderie among the attendees, who shared a common love for the written word. Despite the rise of digital books and online retailers, the bookstore thrived. Its charm lay not only in the vast selection of books but also in the atmosphere it provided—a haven for book lovers in a fast-paced world. The bookstore was more than just a place to buy books; it was a sanctuary where stories came to life and imaginations were set free. As the sun set and the city lights began to twinkle, the bookstore remained a beacon of warmth and knowledge. It was a reminder that, even in the digital age, the magic of holding a book and turning its pages could never be replaced. The little bookstore continued to be a cherished gem in the heart of the city, a place where every visit promised a new adventure.")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Congratulo")
                .content("Deus vult, ave Maria")
                .language("Latin")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Title 1")
                .content("1 Text Text Text Text Text Text ")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Title 2")
                .content("2 Text Text Text Text Text Text ")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Title 3")
                .content("Text")
                .language("English")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Insert title")
                .content("Sukumar Azhikode defined a short story as 'a brief prose narrative with an intense episodic or anecdotal effect'. Flannery O'Connor emphasized the need to consider what is exactly meant by the descriptor short.")
                .language("English")
                .difficulty("Easy")
                .build());
    }

    public long getTextCount() { return textRepository.count(); }

    public List<Text> getTexts(String language, String difficulty) {
        if (language != null && difficulty != null) {
            return textRepository.findTextsByLanguageAndDifficulty(language, difficulty);
        } else if (language != null) {
            return textRepository.findTextsByLanguage(language);
        } else if (difficulty != null) {
            return textRepository.findTextsByDifficulty(difficulty);
        } else {
            return textRepository.findAll();
        }
    }

    public Optional<Text> getTextById(Long id) {
        return textRepository.findById(id);
    }

    public Text createText(Text text) {
        return textRepository.save(text);
    }

    public Optional<Text> updateText(Long id, Text newText) {
        return textRepository.findById(id).map(existingText -> {
            if (newText.getTitle() != null) existingText.setTitle(newText.getTitle());
            if (newText.getContent() != null) existingText.setContent(newText.getContent());
            if (newText.getLanguage() != null) existingText.setLanguage(newText.getLanguage());
            if (newText.getDifficulty() != null) existingText.setDifficulty(newText.getDifficulty());
            return textRepository.save(existingText);
        });
    }

    public boolean deleteText(Long id) {
        if (!textRepository.existsById(id)) {
            return false;
        }
        textRepository.deleteById(id);
        return true;
    }

    public Optional<Text> getRandomText(String language, String difficulty) {
        List<Text> texts = getTexts(language, difficulty);
        if (texts.isEmpty()) {
            return Optional.empty(); // or throw an exception, depending on your requirements
        }
        Random random = new Random();
        int randomIndex = random.nextInt(texts.size());
        return Optional.ofNullable(texts.get(randomIndex));
    }

    public Optional<Text> getRandomWords(String fileName, int numWords, int numSignsPercent, int numUpperCasePercent, boolean doubleEveryWord) {
        Text randomWords = new Text();
        randomWords.setTitle(String.format("%s;%d;%d;%d;%b;", fileName, numWords, numSignsPercent, numUpperCasePercent, doubleEveryWord));
        randomWords.setContent(generateText(fileName, numWords, numSignsPercent, numUpperCasePercent, doubleEveryWord));
        return Optional.of(randomWords);
    }

    public static List<String> getWordSets() {
        ClassPathResource resource = new ClassPathResource("wordSets");
        File directory;
        try {
            directory = resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        File[] files = directory.listFiles();
        List<String> fileNames = new ArrayList<>();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    fileNames.add(file.getName());
                }
            }
        }
        return fileNames;
    }

    // refactor all down below
    public String generateText(String fileName, int numWords, int numSignsPercent, int numUpperCasePercent, boolean doubleEveryWord) {
        Resource resource = new ClassPathResource("wordSets/" + fileName);
        File file;
        try {
            file = resource.getFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String absolutePath = file.getAbsolutePath();

        List<String> randomWords = new ArrayList<>(numWords);
        int numOfLines = getNumberOfLines(absolutePath);
        Random rand = new Random();

        HashMap<Integer, Integer> randIndexes = new HashMap<>(numWords);
        int maxRandIndex = Integer.MIN_VALUE;
        for (int i = 0; i < numWords; i++) {
            int randomIndex = rand.nextInt(0, numOfLines);
            if (randomIndex > maxRandIndex) {
                maxRandIndex = randomIndex;
            }
            randIndexes.put(randomIndex, randIndexes.getOrDefault(randomIndex, 0) + 1);
        }

        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            for (int i = 0; i <= maxRandIndex; i++) {
                line = br.readLine();
                if (randIndexes.containsKey(i)) {
                    for (int j = 0; j < randIndexes.get(i); j++) {
                        randomWords.add(line);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Collections.shuffle(randomWords);

        int numUpperCaseWords = (int) (numWords * (numUpperCasePercent / 100.0));
        int numSigns = (int) (numWords * (numSignsPercent / 100.0));

        Set<Integer> modifiedIndices = new HashSet<>();
        for (int i = 0; i < numUpperCaseWords; i++) {
            int randomIndex;
            do {
                randomIndex = rand.nextInt(numWords);
            } while (modifiedIndices.contains(randomIndex));
            modifiedIndices.add(randomIndex);

            String word = randomWords.get(randomIndex);
            randomWords.set(randomIndex, word.substring(0, 1).toUpperCase() + word.substring(1));
        }

        modifiedIndices = new HashSet<>();
        for (int i = 0; i < numSigns; i++) {
            int randomIndex;
            do {
                randomIndex = rand.nextInt(numWords);
            } while (modifiedIndices.contains(randomIndex));
            modifiedIndices.add(randomIndex);

            String word = randomWords.get(randomIndex);

            List<String> signs = Arrays.asList("?", "!", ".", ",", ":", ";", "\"", "'");

            int randomSignIdx = rand.nextInt(signs.size());
            if (signs.get(randomSignIdx).equals("\"") || signs.get(randomSignIdx).equals("'")) {
                String randomBothSidesSign = signs.get(randomSignIdx);
                randomWords.set(randomIndex, randomBothSidesSign + word + randomBothSidesSign);
            } else {
                String randomEndSign = signs.get(randomSignIdx);
                randomWords.set(randomIndex, word + randomEndSign);
            }
        }

        StringBuilder text = new StringBuilder();

        if (doubleEveryWord) {
            for (String word : randomWords) {
                text.append(word).append(" ").append(word).append(" ");
            }
        } else {
            for (String word : randomWords) {
                text.append(word).append(" ");
            }
        }

        return text.toString().trim();
    }

    // Method to count the number of lines in the file
    public static int getNumberOfLines(String filePath) {
        int lines = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) {
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

}
