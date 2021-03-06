package pl.sii;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static java.lang.Long.parseLong;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static pl.sii.PopularWords.logger;

public class PopularWordsTest {
    private static final PopularWords testee = new PopularWords();
    private static final String REGEX = " ";

    private final String FILE_PATH = (getClass().getResource("/all.num")).getPath();

    @Test
    public void shouldReturnOneThousandMostPopularWords() throws IOException {
        //given
        Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff = getWordsFrequencyListCreatedByAdamKilgarriff();

        //when
        Map<String, Long> result = testee.findOneThousandMostPopularWords();

        //then
        assertFalse(result.isEmpty());
        assertEquals(1000, result.size());
        compareWordListsFrequency(wordsFrequencyListCreatedByAdamKilgarriff, result);
    }

    private void compareWordListsFrequency(Map<String, Long> wordsFrequencyListCreatedByAdamKilgarriff, Map<String, Long> result) {
        long totalFrequencyByKilgarriff = wordsFrequencyListCreatedByAdamKilgarriff.values().stream().reduce(0L, Long::sum);
        long totalFrequencyInAResult = result.values().stream().reduce(0L, Long::sum);
        System.out.println("totalFrequencyByKilgarriff = " + totalFrequencyByKilgarriff);
        System.out.println("totalFrequencyInAResult = " + totalFrequencyInAResult);

        result.forEach((key, value) -> {
            BigDecimal valueUsagePercentage = calculatePercentage(value, totalFrequencyInAResult);
            try {
                BigDecimal kilgarriffUsagePercentage = calculatePercentage(wordsFrequencyListCreatedByAdamKilgarriff.get(key), totalFrequencyByKilgarriff);
                BigDecimal diff = kilgarriffUsagePercentage.subtract(valueUsagePercentage);
                System.out.println(key + "," + valueUsagePercentage + "%," + kilgarriffUsagePercentage + "%," + (new BigDecimal(0.5).compareTo(diff.abs()) > 0) + " " + diff);
            } catch (NullPointerException ex) {
                BigDecimal kilgarriffUsagePercentage = calculatePercentage(0, totalFrequencyByKilgarriff);
                BigDecimal diff = kilgarriffUsagePercentage.subtract(valueUsagePercentage);
                System.out.println(key + "," + valueUsagePercentage + "%," + kilgarriffUsagePercentage + "%," + (new BigDecimal(0.5).compareTo(diff.abs()) > 0) + " " + diff);
            }

        });
    }

    private BigDecimal calculatePercentage(double obtained, double total) {
        return BigDecimal.valueOf(obtained * 100 / total).setScale(4, RoundingMode.HALF_UP);
    }

    private Map<String, Long> getWordsFrequencyListCreatedByAdamKilgarriff() {
        Map<String, Long> wordsFrequency = new HashMap<>();

        try (Scanner fileContent = new Scanner(new File(FILE_PATH))) {
            fileContent.nextLine();
            while (fileContent.hasNextLine()) {
                String[] parts = fileContent.nextLine().split(REGEX);
                if (wordsFrequency.get(parts[1]) != null) {
                    wordsFrequency.put(parts[1], wordsFrequency.get(parts[1]) + parseLong(parts[0]));
                } else {
                    wordsFrequency.put(parts[1], parseLong(parts[0]));
                }
            }
        } catch (FileNotFoundException e) {
            logger.error("The file has not been found");
        }

        return wordsFrequency;
    }

}
