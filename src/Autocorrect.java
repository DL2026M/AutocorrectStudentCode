import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Autocorrect
 * <p>
 * A command-line tool to suggest similar words when given one not in the dictionary.
 * </p>
 * @author Zach Blick
 * @author David Lutch
 */
public class Autocorrect {

    private static String[] words;
    private static int threshold;
    public Autocorrect(String[] words, int threshold) {
        this.words = words;
        this.threshold = threshold;
    }

    public static void main(String[] args) {
        // Continuously prompts the user
        Autocorrect wholeTest = new Autocorrect(words, threshold);
        while (true) {
            String[] dictionary = loadDictionary("large");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Please enter a word (or type quit to end the program): ");
            String wordEntered = scanner.nextLine();
            if  (wordEntered.equals("quit")) {
                break;
            }
            if (wordEntered.length() >= 4 && wordEntered.length() <= 8) {
                threshold = 3;
            }
            if (wordEntered.length() > 8) {
                threshold = 4;
            }
            if (wordEntered.length() == 3) {
                threshold = 2;
            }
            if (wordEntered.length() <= 2) {
                threshold = 1;
            }

            wholeTest = new Autocorrect(dictionary, threshold);

            boolean validWordTester = wholeTest.isValidWord(wordEntered);
            if (validWordTester) {
                System.out.println(wordEntered + "' is a word!" + '\n');
            }
            if (!validWordTester) {
                String[] words = wholeTest.runTest(wordEntered);
                // Prints the top 3 most likely words that the user meant (or all of them if less than 3 words)
                if (words.length <= 3) {
                    for (int i = 0; i < words.length; i++) {
                        System.out.println(words[i]);
                    }
                }
                else {
                    for  (int i = 0; i < 3; i++) {
                        System.out.println(words[i]);
                    }
                }
                System.out.println("");
            }
        }
    }

    public boolean isValidWord(String typedWord) {
        int index = Arrays.binarySearch(words, typedWord);
        return index >= 0;
    }

    public String[] runTest(String typed) {

        ArrayList<WordDistancePair> sorted = new ArrayList<WordDistancePair>();
        ArrayList<String> returnedWords = new ArrayList<String>();

        for (int i = 0; i < words.length; i++) {
            int distance = editDistance(words[i], typed);
            WordDistancePair pair = new WordDistancePair(words[i], distance);
            sorted.add(pair);
        }
        // Sorts alphabetically
        sorted.sort(Comparator.comparing(WordDistancePair::getWord));
        // Sorts by edit distance
        sorted.sort(Comparator.comparingInt(WordDistancePair::getEditDistance));

        // Adds every word that is less than or equal to the distance away of the typed word to an arraylist
        for (int i = 0; i < sorted.size(); i++) {
            if (threshold >= sorted.get(i).getEditDistance()) {
                // Using an arraylist to avoiding having a huge 1D array that is mostly empty since we don't know how
                // many words fit the criteria
                returnedWords.add(sorted.get(i).getWord());
            }
        }

        // Converting the arraylist into a 1D array
        String[] returnedWordsArray = new String[returnedWords.size()];
        for (int i = 0; i < returnedWords.size(); i++) {
            returnedWordsArray[i] = returnedWords.get(i);
        }

        return returnedWordsArray;
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
        // Tabulation
        for (int i = 1; i <= DICTIONARYWORD_LENGTH; i++) {
            for (int j = 1; j <= TYPEDWORD_LENGTH; j++) {
                // Checks to see if the last letter on each string is the same
                if (dictionaryWord.charAt(i - 1) == typedWord.charAt(j - 1)) {
                    tabulation[i][j] = tabulation[i - 1][j - 1];
                }
                // Checks to see if the minimum solution is using substitution, deletion, or addition
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