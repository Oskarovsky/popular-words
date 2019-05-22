package pl.sii;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;


public class PopularWords {

    final static Logger logger = Logger.getLogger(PopularWords.class);

    private final String FILE_PATH = (getClass().getResource("/3esl.txt")).getPath();
    private static final String REGEX = "([,.\\s]+)";


    public static void main(String[] args) throws IOException
    {
        PopularWords popularWords = new PopularWords();
        Map<String, Long> result = popularWords.findOneThousandMostPopularWords();
        result.entrySet().forEach(System.out::println);
    }


    private List<String> extractWordsFrom(String filePath) throws IOException
    {
        List<String> words = new ArrayList<>(Collections.emptyList());

        try (BufferedReader fileContent = new BufferedReader(new FileReader(FILE_PATH))) {
            String line = fileContent.readLine();
            while (line != null) {
                words.addAll(Arrays.asList(line.split(REGEX)));
                line = fileContent.readLine();
            }
        } catch (FileNotFoundException ex) {
            logger.error("The file has not been found");
        }
        return words;
    }

    Map<String, Long> findOneThousandMostPopularWords() throws IOException {
        return extractWordsFrom(FILE_PATH)
                .stream()
                .collect(Collectors.toConcurrentMap(word -> word, word -> 1L, Long::sum))
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(1000)
                .collect(toMap(Map.Entry::getKey,
                        Map.Entry::getValue,
                        (v1, v2) -> { throw new IllegalStateException(); },
                        LinkedHashMap::new));
    }
}


