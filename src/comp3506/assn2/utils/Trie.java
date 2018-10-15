package comp3506.assn2.utils;

/**
 * Main Trie Class. Uses TrieContainers to save words inside itself. The Trie has the ability to
 * store words, perform word counts of a specific word, prefix searches, store positions of words
 * and retrieve those positions as well.
 *
 * References:
 * To implement this class, some implementation help was taken from the provided link :
 * http://javabypatel.blogspot.com/2015/07/trie-datastructure-explanation-and-applications.html
 *
 * Space Complexity: The space complexity of this Trie would be O(N*K), where N is the number of
 * Containers/Nodes inside the trie, and K is the amount of child pointers of each Node. This is
 * because of the fact that even null points occupy space. In the worst case, the trie will store
 * every character in each word.
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
     * Run-time complexity: O(M), where M is the length of the word that is being stored.  At the
     * end of the function, the location of the word is stored in the linked list, which can be
     * done in constant time.
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
     *  Returns a list of all locations the word is at.
     *  Run-time complexity: O(M), where the M is the size of the word that is to be found. This
     * operation is basically traversing throughout the trie to add the positions of the
     * character to a linked list, and hence happens in linear time (Because addition to a
     * linked list takes place in constant time).
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
     * Counts the occurrences of the given word.
     *
     * Run-time complexity: O(M), where the M is the size of the word that is to be found. This
     * operation is basically traversing throughout the trie to match the characters of a given
     * word, and hence happens in linear time.
     *
     * @param start The starting TrieContainer/TrieNode
     * @param word  word for which the occurrences are to be found
     * @return  Integer representing the occurrences of the given word
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
     * Checks if the given word is present in the given Trie or not.
     *
     * Run-time complexity: O(M), where M is the length of the word that is to be found in the
     * trie. Essentially, all the trie containers are traversed through to find each character in
     * the given word, which happens in linear time.
     *
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
     * word is a prefix for.
     *
     * Run time complexity: O(M+N) ~ O(N), where M is the length of the prefix and N is the
     * number of nodes hanging off the prefix. The worst case occurs when an empty string is
     * searched as the prefix, because then all of the nodes will be traversed through.
     *
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
     * Run-time Complexity: O(N), where N is the number of nodes hanging off the prefix. All of
     * the TrieContainers/Nodes are traversed over from the last node of the prefix, and since
     * all of them execute in linear time they all add up to an final run time complexity of O(N).
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

    /**
     * Returns a list of all locations the word is at in the file, along with the word itself.
     *
     * Run time Complexity: O(M), where the M is the size of the word that is to be found. This
     * operation is basically traversing throughout the trie to add the positions of the
     * character to a linked list, and hence happens in linear time (Because addition to a
     * linked list takes place in constant time).
     *
     * @param start the starting node of a Trie/ main container of a trie that's being searched
     * @param word word for whose the locations are being searched
     * @return
     */
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
