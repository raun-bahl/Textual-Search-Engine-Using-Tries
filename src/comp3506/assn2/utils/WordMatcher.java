package comp3506.assn2.utils;

/**
 * Word Matcher Class.
 * Helps matches words in various contexts using their location lists.
 * Supports AND,OR,NOT and CONTIGUOUS word matching.
 */
public class WordMatcher {

    /* Constructor */
    public WordMatcher() {
    }

    /**
     * Helper Function which ensures that two given words are contiguous.
     *
     * Run time complexity: O(M*N), where M is the size of the first list, and N is the size of
     * the second list. When the size of the 2 lists is equal, then O(N^2) (Worst-case).
     *
     * @param length length of a part of the Phrase that has been already processed by
     *               contiguousWordMatcher.
     * @param formerWordList List of Pairs of the former word in the phrase.
     * @param secondWordList List of Pairs of the latter word in the phrase.
     * @return a smaller sublist of words which indeed are contiguous.
     */
    public MyLinkedList<Pair<Integer, Integer>> contiguousWordMatcher(int length, MyLinkedList<Pair<Integer,Integer>>
            formerWordList, MyLinkedList<Pair<Integer, Integer>> secondWordList) {

        MyLinkedList<Pair<Integer, Integer>> list = new MyLinkedList<>();

        for (Pair<Integer, Integer> wordPair2 : secondWordList) {

            for (Pair<Integer, Integer> wordPair1 : formerWordList) {

                if (wordPair2.getRightValue().equals(wordPair1.getRightValue
                        () + length + 1) && wordPair2.getLeftValue().equals
                        (wordPair1.getLeftValue())) {

                    Pair<Integer, Integer> firstWordPair = new Pair<>
                            (wordPair1.getLeftValue(),
                                    wordPair1.getRightValue());
                    list.insertAtBack(firstWordPair);

                }
            }
        }
        return list;
    }


    /**
     * Implements AND logic on two given lists. Ensures that the words are on the same line.
     *
     *  Run time complexity: O(M*N), where M is the size of the first list, and N is the size of
     *  the second list. When the size of the 2 lists is equal, then O(N^2) (Worst-case).
     *
     * @param formerWordList List of Pairs of the first word.
     * @param secondWordList List of Pairs of the second word.
     * @return A smaller sublist of pairs which have the same Left Value, ie are on the same line.
     */
    public MyLinkedList<Pair<Integer, Integer>> andMatcher(MyLinkedList<Pair<Integer, Integer>>
                                                                   formerWordList,
                                                           MyLinkedList<Pair<Integer, Integer>> secondWordList) {

        MyLinkedList<Pair<Integer, Integer>> list = new MyLinkedList<>();

        for (Pair<Integer, Integer> wordPair2 : secondWordList) {

            for (Pair<Integer, Integer> wordPair1 : formerWordList) {

                if (wordPair2.getLeftValue().equals(wordPair1.getLeftValue())) {

                    Pair<Integer, Integer> pair = new Pair<>
                            (wordPair1.getLeftValue(), wordPair2.getRightValue());
                    list.insertAtBack(pair);
                }
            }
        }
        return list;
    }

    /**
     * Helper function that returns a List of Triples for a list of words within a specified
     * section. Implements simple OR logic while finding words.
     *
     *  Run time complexity: O(M*N), where M is the size of the first list, and N is the size of
     * the second list. When the size of the 2 lists is equal, then O(N^2) (Worst-case).
     *
     * @param wordList List of Pairs of Words that are to be found within a section.
     * @param sectionList List of Pairs of Section Numbers.
     * @param word the word for which the occurrences are to be found.
     * @return List of Triples of words within the list of sections.
     */
    public MyLinkedList<Triple<Integer,Integer,String>> extractOrTriples(MyLinkedList<Pair<Integer,
            Integer>> wordList, MyLinkedList<Pair<Integer,Integer>> sectionList, String word) {

        MyLinkedList<Triple<Integer,Integer,String>> tripleList = new MyLinkedList<>();

            for (Pair<Integer, Integer> pair1 : sectionList) {

                for (Pair<Integer, Integer> pair : wordList) {

                    if (pair.getLeftValue() > pair1.getLeftValue() && pair.getLeftValue() <
                            pair1.getRightValue()) {

                        Triple triple = new Triple<>(pair.getLeftValue(), pair.getRightValue(), word);
                        tripleList.insertAtBack(triple);

                    }
                }
            }
        return tripleList;
    }

    /**
     * Helper function that returns a List of Triples for a list of words within a specified
     * section. Implements simple AND logic while finding for words.
     *
     *  Run time complexity: O(M*(N+K)) ~ O(M*N), where M is the size of the section list, N and
     *  K are the sizes of the two given lists, and N is the larger out of the two. Worst case
     *  would still be O(N^2) though, when the section list has a list size equal to the list of
     *  occurrences of a word (unlikely, but still a possibility).
     *
     * @param formerWordList List of Triples of Word1.
     * @param secondWordList List of Triples of Word2.
     * @param sectionList List of Pairs of Section Numbers.
     * @return List of Triples of words within the list of sections.
     */
    public MyLinkedList<Triple<Integer, Integer, String >> extractAndTriples
            (MyLinkedList<Triple<Integer,Integer, String>> formerWordList, MyLinkedList<Triple<Integer,
                    Integer, String>> secondWordList,
             MyLinkedList<Pair<Integer,Integer>> sectionList) {

        MyLinkedList<Triple<Integer,Integer,String>> tripleList = new MyLinkedList<>();

        Triple<Integer,Integer,String> triple1 = new Triple<>(0,0,"");
        Triple<Integer,Integer,String> triple2 = new Triple<>(0,0,"");


        for (int k = 0; k < sectionList.getSize(); k++) {


            for (int i = 0, j = 0; i < formerWordList.getSize()-1 && j < secondWordList.getSize()-1;
                 i++,j++) {


                if ((formerWordList.get(i).getLeftValue() > sectionList.get(k).getLeftValue() && formerWordList.get(i)
                        .getLeftValue() < sectionList.get(k).getRightValue())) {

                    triple1 = new Triple<>(formerWordList.get(i).getLeftValue
                            (), formerWordList.get(i).getCentreValue(), formerWordList.get(i).getRightValue());

                }
                if(secondWordList.get(j)
                        .getLeftValue()
                        > sectionList.get(k).getLeftValue() && secondWordList.get(j).getLeftValue() <
                        sectionList.get(k).getRightValue()) {

                    triple2 = new Triple<>(secondWordList.get(j).getLeftValue
                            (), secondWordList.get(j).getCentreValue(), secondWordList.get(j).getRightValue());
                }
            }

            if ((triple1.getLeftValue() > sectionList.get(k).getLeftValue() &&
                    triple1.getCentreValue() < sectionList.get(k).getRightValue()) &&
                    (triple2.getLeftValue()> sectionList.get(k).getLeftValue() &&
                            triple2.getCentreValue() < sectionList.get(k).getRightValue())) {
                tripleList.insertAtBack(triple1);
                tripleList.insertAtBack(triple2);
            }


        }
        return tripleList;
    }

    /**
     * checks if the 2 given lists of word occurrences are within a given section.
     *
     * Run time complexity: O(1), happens in constant time since it's just a comparison operation.
     *
     * @param sectionList LowerLimit and UpperLimit lineNumbers of the specified section
     * @param firstWordList occurrence of word 1.
     * @param secondWordList occurrence of word 2.
     * @return true if the triples are in the same section, false if they're not.
     */
    public boolean areInSameSection(Pair<Integer,Integer> sectionList,
                                     Triple<Integer,Integer,String> firstWordList,
                                     Triple<Integer,Integer,String> secondWordList) {

        if ((firstWordList.getLeftValue() > sectionList.getLeftValue() &&
                firstWordList.getLeftValue() < sectionList.getRightValue())
                                      &&
                (secondWordList.getLeftValue() > sectionList.getLeftValue() &&
                secondWordList.getLeftValue() < sectionList.getRightValue())) {
            return true;
        }
        return false;
    }
}
