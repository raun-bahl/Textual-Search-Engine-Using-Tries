package comp3506.assn2.utils;

/**
 * Helper class which checks the arguments for functions in AutoTester. The constructor is
 * overloaded multiple times to cater to different types of arguments provided in the functions
 * of AutoTester.
 */
public class ArgumentChecker {

    public ArgumentChecker(String word) {

        if (word.isEmpty() || word == null) {
            throw new IllegalArgumentException();
        }
    }

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
