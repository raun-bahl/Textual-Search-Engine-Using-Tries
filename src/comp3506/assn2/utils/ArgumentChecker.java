package comp3506.assn2.utils;

/**
 * Helper class which checks the arguments for functions in AutoTester. The constructor is
 * overloaded multiple times to cater to different types of arguments provided in the functions
 * of AutoTester.
 */
public class ArgumentChecker {

    public ArgumentChecker() {
    }

    /**
     * Checks if the given word is null or empty.
     * Run time complexity: O(1)
     * @param word
     */
    public ArgumentChecker(String word) {

        if (word.isEmpty() || word == null) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks if the given array of strings is empty or null, or contains any null or empty values.
     * Run time complexity: O(N), where N is the number of strings inside the string array.
     * @param words
     */
    public ArgumentChecker(String[] words) {

        if (words.length < 1 || words == null) {
            throw new IllegalArgumentException();
        }
        for (String word : words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Checks if the given arrays of strings is empty or null, or contains any null or empty values.
     * Run time complexity: O(M+N) ~ O(N), where N is the size of the larger string array.
     *
     * @param titles array of titles of sections
     * @param words array of words
     */
    public ArgumentChecker(String[] titles, String[] words) {
        for (String word : words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }
        for (String title: titles) {
            if (title.length() < 1 || title == null) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Checks if the given arrays of strings is empty or null, or contains any null or empty values.
     *
     * Run time complexity: O(M+N+K) ~ O(N), where N is the size of the largest string array.
     * @param titles array of titles of sections
     * @param wordsRequired array of required words
     * @param wordsExcluded array of exluded words
     */
    public ArgumentChecker(String[] titles, String[] wordsRequired, String[] wordsExcluded) {
        for (String word : wordsRequired) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }
        for (String word : wordsExcluded) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }
        for (String title: titles) {
            if (title.length() < 1 || title == null) {
                throw new IllegalArgumentException();
            }
        }
    }

}
