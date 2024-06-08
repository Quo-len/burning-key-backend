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
                .title("Legenda")
                .content("Sukumar Azhikode defined a short story as 'a brief prose narrative with an intense episodic or anecdotal effect'. Flannery O'Connor emphasized the need to consider what is exactly meant by the descriptor short.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Typing skill")
                .content("The quick brown fox jumps over the lazy dog. This sentence is often used for typing practice because it contains every letter in the English alphabet at least once.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Silent Reflections")
                .content("In the quiet of dawn, I find peace in solitude. Each morning brings a chance to start anew, leaving behind the shadows of yesterday.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Whispered Dreams")
                .content("As night falls, dreams whisper secrets of the heart. In the dark, I discover hopes and fears, waiting to be embraced.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Echoes of Memory")
                .content("Memories linger like echoes in an empty room. They remind me of moments gone by, yet they never truly fade away.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Dancing Shadows")
                .content("Under the moonlight, shadows dance in the night. They tell stories of forgotten times, weaving a tapestry of light and dark.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Morning Light")
                .content("With the first light of day, hope awakens. The world feels fresh and full of possibilities, a blank canvas ready to be painted.")
                .language("English")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Dreams and Reality")
                .content("In the quiet hours of early morning, when dreams still linger in the corners of my mind, I find myself caught between worlds. Each dream feels like a distant memory, a fleeting glimpse of a reality that exists just beyond my reach.")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Waves of Time")
                .content("Time flows like a river, with each moment a wave that crashes upon the shores of my consciousness. The past and present merge, creating ripples that echo through my thoughts, reminding me of all that has been and all that is yet to come.")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Inner Dialogues")
                .content("Within the silence of my own mind, conversations unfold between different facets of my identity. These inner dialogues reveal hidden truths and unspoken fears, allowing me to explore the depths of who I am and who I wish to be.")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Shadows of Memory")
                .content("Memories cast long shadows that stretch across the landscape of my mind. Each one holds a piece of my history, a fragment of a story that defines who I am. As I reflect on these moments, I find meaning in the patterns they create.")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Twilight Reflections")
                .content("As the sun sets and the sky is painted with shades of orange and pink, I am reminded of the transient beauty of life. This twilight hour, where day meets night, offers a moment of reflection and a chance to appreciate the delicate balance of existence.")
                .language("English")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Transcendent Ephemera")
                .content("Within the interstices of diurnal cognition and nocturnal reverie, I encounter phantasms of ideation that illuminate the crepuscular recesses of the psyche. These ephemera, though transient, inscribe indelible marks upon the palimpsest of consciousness, evoking a sublime interplay between lucidity and obfuscation.")
                .language("English")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Chronotopic Disjunction")
                .content("Navigating the chronotopic continuum, I confront the paradox of temporal simultaneity and fragmentation. Each temporal fragment, a tessera in the mosaic of existence, reflects the multifaceted nature of reality, wherein the confluence of past, present, and future generates a complex, interwoven tapestry of experiential phenomena.")
                .language("English")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Ontic Duality")
                .content("In the dialectical oscillation between being and becoming, I am ensnared by the paradoxical duality of ontic existence. This perpetual fluctuation between the immutable and the ephemeral constitutes the essence of my ontological inquiry, compelling a relentless interrogation of the self as both a static and dynamic entity.")
                .language("English")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Phenomenological Dichotomy")
                .content("The phenomenological dichotomy of presence and absence manifests as a dynamic tension within the perceptual field. Each encounter with the noumenal world is mediated by the interplay of the seen and the unseen, engendering a profound epistemological rift that challenges the coherence of objective reality.")
                .language("English")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Noological Resonance")
                .content("In the pursuit of noological transcendence, I grapple with the ineffable resonance of the metaphysical. This pursuit transcends mere epistemic inquiry, seeking instead a deeper, ontological synthesis that harmonizes the corporeal and the incorporeal, revealing the hidden symmetries that underlie the fabric of existence.")
                .language("English")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Прекрасна пора")
                .content("Літо настало, і природа оживає: пташки співають, квіти розцвітають, а сонце радує своїм теплом.")
                .language("Ukrainian")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Прекрасна пора")
                .content("Літо настало, і природа оживає: пташки співають, квіти розцвітають, а сонце радує своїм теплом.")
                .language("Ukrainian")
                .difficulty("Easy")
                .build());


        textRepository.save(Text.builder()
                .title("Парк Паркович")
                .content("Вчора я зустрів старого друга у парку, його звати Патрік Паркінсон. Ми пригадали спільні пригоди та поділилися новинами.")
                .language("Ukrainian")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Успішність")
                .content("Щоб досягти успіху, потрібно бути наполегливим і цілеспрямованим у своїх діях.")
                .language("Ukrainian")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Не гроші")
                .content("Здоров'я - це найбільше багатство. Тому важливо дбати про нього, регулярно вправляючись та правильно харчуючись.")
                .language("Ukrainian")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Добро")
                .content("Кожен день - це нова можливість створити щось чудове і зробити світ кращим.")
                .language("Ukrainian")
                .difficulty("Easy")
                .build());

        textRepository.save(Text.builder()
                .title("Ранок")
                .content("Сьогоднішній день почався з важкої пробудження, та швидкого сніданку. Попри це, я вирішив зібратися зі своїми друзями на прогулянку в парку. Там, звичайно, був Патрік Паркінсон. Погода була прекрасною, і ми насолоджувалися спілкуванням та свіжим повітрям.")
                .language("Ukrainian")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Мистецтво")
                .content("У вівторок я відвідав музей сучасного мистецтва разом із групою студентів разом з моїм другом Патріком Паркінсоном з мого університету. Експозиція вражала різноманіттям та оригінальністю творів.")
                .language("Ukrainian")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Книга")
                .content("Завдяки новій книзі, яка потрапила до моєї бібліотеки, я провів весь вечір у захоплюючому світі пригод та фантастичних історій Патріка Паркінсона.")
                .language("Ukrainian")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Кіно")
                .content("Я вирішив розслабитися після довгого робочого дня, організувавши собі домашній кіносеанс. Обравши мій улюблений фільм \"Пригоди Парка Паркінсона\", я погрузився у світ кіно та емоцій.")
                .language("Ukrainian")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Гори")
                .content("На вихідних я вирушив у мандрівку до гір. Підкорюючи вершини та насолоджуючись красою природи, я відчував себе вільним і щасливим.")
                .language("Ukrainian")
                .difficulty("Medium")
                .build());

        textRepository.save(Text.builder()
                .title("Тиждень")
                .content("Після завершення важкого робочого тижня я вирішив приділити собі трохи часу на саморозвиток. Запланувавши вечір, наповнений читанням фахової літератури та виконанням вправ з медитації, я відчув, що це допомагає мені зосередитися і знайти внутрішню гармонію.")
                .language("Ukrainian")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Ціль")
                .content("Останнім часом я задумувався над тим, які цілі я хочу досягти в своєму житті і який шлях обрати для їх досягнення. Свідомо відводячи час для самоаналізу та планування, я зрозумів, що ключ до успіху полягає в усвідомленні власних цінностей та поступовому крокуванні в напрямку своїх мрій.")
                .language("Ukrainian")
                .difficulty("Hard")
                .build());

        textRepository.save(Text.builder()
                .title("Медитація")
                .content("Медитуючи про майбутнє, я розумію, що важливо знайти баланс між професійним ростом та особистим щастям. Стежачи за своїми мріями, я відчуваю, що кожен крок, який я роблю, приводить мене ближче до своєї ідеальної версії себе.")
                .language("Ukrainian")
                .difficulty("Hard")
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
