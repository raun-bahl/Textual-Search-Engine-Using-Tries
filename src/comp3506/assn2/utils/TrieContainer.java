package comp3506.assn2.utils;

/**
 * Class which represents a node inside a Trie.
 */
public class TrieContainer {

    // assigns true if the character is the end of the word
    public boolean isEnd;
    char ch;
    // Children nodes
    public TrieContainer[] series = new TrieContainer[27];

    // list which saves the indices of a given word
    MyLinkedList<Pair<Integer,Integer>> linkedList;

    public TrieContainer() {

        isEnd = false;

        for (int i = 0; i < 27; i++) {
            series[i] = null;
        }

        linkedList = new MyLinkedList<>();
    }

    /**
     * Helper method which converts a given TrieContainer object to its respective character
     * @param t TrieContainer object
     * @return  character that the TrieContainer object holds
     */
    public char convertToChar(TrieContainer t) {

            for (int i = 0; i < series.length; i++) {
                TrieContainer k = t.series[i];
                    if (k != null) {
                        ch = ((char) (97 + i));
                }
            }

        return ch;
    }
}
