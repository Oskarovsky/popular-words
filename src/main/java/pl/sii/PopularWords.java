package pl.sii;

import org.apache.commons.lang3.NotImplementedException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class PopularWords {

    private final String FILE_PATH = (getClass().getResource("/3esl.txt")).getPath();
    private static final String REGEX = "([,.\\s]+)";

    public static void main(String[] args) {
        PopularWords popularWords = new PopularWords();
        Map<String, Long> result = popularWords.findOneThousandMostPopularWords();
        result.entrySet().forEach(System.out::println);
    }

    private List<String> extractWordsFrom(String filePath) throws IOException
    {
        List<String> words = new ArrayList<>(Collections.emptyList());
        FileReader file = new FileReader(filePath);
        BufferedReader br = new BufferedReader(file);

        String line = br.readLine();

        while(line != null)
        {
            words.addAll(Arrays.asList(line.split(REGEX)));
            line = br.readLine();
        }

        file.close();
        return words;
    }

    public Map<String, Long> findOneThousandMostPopularWords() {
        throw new NotImplementedException("TODO implementation");
    }
}
