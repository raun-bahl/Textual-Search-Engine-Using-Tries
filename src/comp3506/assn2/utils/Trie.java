package comp3506.assn2.utils;

/**
 * Main Trie Class. Saves words inside itself.
 */
public class Trie {

    /**
     * Constructor.
     */
    public Trie() {
    }

    /**
     * Function that stores words into a trie.
     *
     * @param start The starting TrieContainer/TrieNode
     * @param word The word to be saved
     * @param location The Pair location or the index of the word
     */
    public void storeWords(TrieContainer start, String word, Pair<Integer,
            Integer> location){

        start.linkedList = new MyLinkedList<>();

            char tempChar, character;
            int aint = (int) 'a';

            for (int j = 0; j < word.length(); j++) {
                tempChar = word.charAt(j);

                if (tempChar == '\'') {

                    int apostropheChar = 26;

                    //If series is filled, then proceed forward to traverse through the series of
                    // the filled Trie Object. If not, then create a new TrieContainer and place the
                    // character at the correct position. Finally, check if the character is the
                    // end of the word, and mark isEnd accordingly.
                    if (start.series[apostropheChar] != null) {
                        if (word.length() - 1 == j) {
                            start.series[apostropheChar].isEnd = true;
                        }
                        start = start.series[apostropheChar];
                    } else {
                        TrieContainer trie = new TrieContainer();
                        trie.isEnd = (word.length() - 1 == j ? true : false);
                        start.series[apostropheChar] = trie;
                        start = start.series[apostropheChar];
                    }

                } else {
                    character = Character.toLowerCase(tempChar);

                    if (start.series[character - aint] != null) {
                        if (word.length() - 1 == j) {
                            start.series[character - aint].isEnd = true;
                        }
                        start = start.series[character - aint];
                    } else {
                        TrieContainer trie = new TrieContainer();
                        trie.isEnd = (word.length() - 1 == j ? true : false);
                        start.series[character - aint] = trie;
                        start = start.series[character - aint];
                    }
                }
            }
        //Store the location of the word
        start.linkedList.insertAtBack(location);
    }


    /**
     * Returns a list of all locations the word is at.
     *
     * @param start the starting TrieContainer/TrieNode.
     * @param word the word whose index is to be found
     * @return MyLinkedList of all locations the word is at
     */
    public MyLinkedList<Pair<Integer,Integer>> returnList(TrieContainer start,
                                                  String word) {

        MyLinkedList<Pair<Integer,Integer>> returnList = new MyLinkedList<>();

        for (int i=0; i< word.length(); i++) {

            char ch = word.charAt(i);

            if (ch == '\'') {

                int apostropheChar = 26;
                if (start.series[apostropheChar] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[apostropheChar];
                    } else {
                        //The word is found
                        if (start.series[apostropheChar].isEnd) {
                            for (Pair<Integer, Integer> pair : start.series[apostropheChar]
                                    .linkedList) {
                                returnList.insertAtBack(pair);
                            }
                        }
                    }
                }
            } else {

                if (start.series[ch - 97] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[ch - 97];
                    } else {
                        //The word is found
                        if (start.series[ch - 97].isEnd) {
                                for (Pair<Integer, Integer> pair : start.series[ch - 97]
                                        .linkedList) {
                                    returnList.insertAtBack(pair);
                            }
                        }
                    }
                }
            }
        }
        return returnList;
    }

    /**
     * Counts the occurences of the given word.
     *
     * @param start The starting TrieContainer/TrieNode
     * @param word  word for which the occurences are to be found
     * @return  Integer representing the occurences of the given word
     */
    public int wordCount(TrieContainer start,String word) {
        int count = 0;

        outerLoop:
        for (int i=0; i< word.length(); i++) {
            char ch = word.charAt(i);

            TrieContainer t = start.series[i];

            if (ch == '\'') {
                int apostropheChar = 26;
                if (start.series[apostropheChar] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[apostropheChar];
                    } else {
                        if (start.series[apostropheChar].isEnd) {
                            // word is found, return the size of its Linked List
                            count = start.series[apostropheChar].linkedList
                                    .size;
                        }
                    }
                }
            } else {
                if (start.series[ch - 97] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[ch - 97];
                    } else {
                        if (start.series[ch - 97].isEnd) {
                            // word is found, return the size of its Linked List
                            count = start.series[ch - 97].linkedList.size;
                        }
                        if (word.length()-1 == i) {
                            break outerLoop;
                        }
                    }
                }
            }
        }
        return count;
    }

    /**
     * Checks if the given word is present in the given Trie or not
     * @param start  The starting TrieContainer/TrieNode
     * @param word  The word that is to be found
     * @return  true if the word is present, false otherwise
     */
    public boolean isWordPresent(TrieContainer start, String word){

        boolean isFound = true;
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            if (character == '\'') {
                continue;
            } else {

                if (start.series[character - 97] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[character - 97];
                    } else {
                        if (!start.series[character - 97].isEnd) {
                            isFound = false;
                        }
                    }
                } else {
                    isFound = false;
                    break;
                }
            }
        }
        return isFound;
    }

    /**
     * Prefix searcher function. For a given word, searches for the word and words that the given
     * word is a prefix for
     * @param start  Starting TrieContainer/TrieNode
     * @param word  The word whose prefix is to be searched for
     * @return  MyLinkedList of all the indices of the word and its related prefix words
     */
    public MyLinkedList<Pair<Integer,Integer>> predictWord(TrieContainer start,
                                                           String word) {

        MyLinkedList<Pair<Integer,Integer>> list = new MyLinkedList<>();

        for (int i = 0; i< word.length(); i++) {

            int index = word.charAt(i) - 'a';

            if (start.series[index] != null) {
                // iterate to the last Node of the given prefix.
                start = start.series[index];
            }
        }

        // call helper function to find all children nodes
        predict(word,start,list);

        return list;
    }

    /**
     * Helper recursive function which traverses through all the nodes of the prefix and finds
     * their indices.
     *
     * @param word the word whose children nodes are to be found
     * @param container The starting TrieNode/TrieContainer
     * @param list List to insert the indices of the children nodes in
     */
    public void predict(String word, TrieContainer container,
                        MyLinkedList<Pair<Integer,Integer>> list) {


        if (container.isEnd) {
            //The child node is found
            for (Pair<Integer,Integer> pair: container.linkedList) {

                if (!list.contains(pair)) {
                    list.insertAtBack(pair);
                }
            }
        }

        TrieContainer[] trieContainers = container.series;

        // iterate over all the children nodes of the parent node
        for (TrieContainer trieContainer : trieContainers) {
            if (trieContainer != null) {

                char childChar = trieContainer.convertToChar(trieContainer);
                predict(word + Character.toString(childChar), trieContainer, list);

            }
        }
    }

    public MyLinkedList<Triple<Integer,Integer,String>> returnTripleList(TrieContainer start,
                                                          String word) {

        MyLinkedList<Triple<Integer,Integer,String>> returnList = new MyLinkedList<>();

        for (int i=0; i< word.length(); i++) {

            char ch = word.charAt(i);

            if (ch == '\'') {

                int apostropheChar = 26;
                if (start.series[apostropheChar] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[apostropheChar];
                    } else {
                        //The word is found
                        if (start.series[apostropheChar].isEnd) {
                            for (Pair<Integer, Integer> pair : start.series[apostropheChar]
                                    .linkedList) {
                                Triple<Integer,Integer,String> triple = new Triple<>(pair
                                        .getLeftValue(),pair.getRightValue(),word);
                                returnList.insertAtBack(triple);

                            }
                        }
                    }
                }
            } else {

                if (start.series[ch - 97] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[ch - 97];
                    } else {
                        //The word is found
                        if (start.series[ch - 97].isEnd) {
                            for (Pair<Integer, Integer> pair : start.series[ch - 97]
                                    .linkedList) {
                                Triple<Integer,Integer,String> triple = new Triple<>(pair
                                        .getLeftValue(),pair.getRightValue(),word);
                                returnList.insertAtBack(triple);
                            }
                        }
                    }
                }
            }
        }
        return returnList;
    }

}
