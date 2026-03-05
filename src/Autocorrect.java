import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author David Lutch
 */
public class Autocorrect {

    /**
     * Constucts an instance of the Autocorrect class.
     * @param words The dictionary of acceptable words.
     * @param threshold The maximum number of edits a suggestion can have.
     */
    private String[] words;
    private int threshold;
    public Autocorrect(String[] words, int threshold) {
      this.words = words;
      this.threshold = threshold;
    }


    /**
     * Runs a test from the tester file, AutocorrectTester.
     * @param typed The (potentially) misspelled word, provided by the user.
     * @return An array of all dictionary words with an edit distance less than or equal
     * to threshold, sorted by edit distnace, then sorted alphabetically.
     */
    public String[] runTest(String typed) {


        ArrayList<WordDistancePair> sorted = new ArrayList<WordDistancePair>();


        // Two 1D Arrays where one contains is the word in the dictionary and the other one contains the edit distance
        // of that word in the dictionary to the typed word
        for (int i = 0; i < words.length; i++) {
            int distance = editDistance(words[i], typed);
            WordDistancePair pair = new WordDistancePair(words[i], distance);
            sorted.add(pair);
        }
        // Sorts alphabetically
        sorted.sort(Comparator.comparing(WordDistancePair::getWord));
        // Sorts by edit distance
        sorted.sort(Comparator.comparingInt(WordDistancePair::getEditDistance));

        for (int i = 0; i < sorted.size(); i++) {
            sorted.get()
        }

        return new String[0];
    }
    public int editDistance(String dictionaryWord, String typedWord) {


        final int DICTIONARYWORD_LENGTH = dictionaryWord.length();
        final int TYPEDWORD_LENGTH = typedWord.length();
        int[][] tabulation = new int[DICTIONARYWORD_LENGTH + 1][TYPEDWORD_LENGTH + 1];
        int minEditDistance = 0;

        // Base cases
        for (int h = 0; h <= DICTIONARYWORD_LENGTH; h++) {
            tabulation[h][0] = h;
        }
        for (int h = 0; h <= TYPEDWORD_LENGTH; h++) {
            tabulation[0][h] = h;
        }

        for (int i = 1; i <= DICTIONARYWORD_LENGTH; i++) {
            for (int j = 1; j <= TYPEDWORD_LENGTH; j++) {
                // Checks to see if the last letter on each string is the same
                if (dictionaryWord.charAt(i - 1) == typedWord.charAt(j - 1)) {
                    tabulation[i][j] = tabulation[i - 1][j - 1];
                }
                else {
                    tabulation[i][j] = Math.min(tabulation[i - 1][j], Math.min(tabulation[i][j - 1], tabulation[i - 1][j - 1])) + 1;
                }
            }
        }
        return tabulation[DICTIONARYWORD_LENGTH][TYPEDWORD_LENGTH];
    }


    /**
     * Loads a dictionary of words from the provided textfiles in the dictionaries directory.
     * @param dictionary The name of the textfile, [dictionary].txt, in the dictionaries directory.
     * @return An array of Strings containing all words in alphabetical order.
     */
    private static String[] loadDictionary(String dictionary)  {
        try {
            String line;
            BufferedReader dictReader = new BufferedReader(new FileReader("dictionaries/" + dictionary + ".txt"));
            line = dictReader.readLine();

            // Update instance variables with test data
            int n = Integer.parseInt(line);
            String[] words = new String[n];

            for (int i = 0; i < n; i++) {
                line = dictReader.readLine();
                words[i] = line;
            }
            return words;
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}