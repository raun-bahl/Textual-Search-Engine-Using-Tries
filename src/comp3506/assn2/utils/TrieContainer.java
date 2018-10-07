package comp3506.assn2.utils;

import java.util.HashMap;

public class TrieContainer {

    public boolean isEnd;
    char ch;
    public TrieContainer[] series = new TrieContainer[27];

    MyLinkedList<Pair<Integer,Integer>> linkedList;

    public TrieContainer() {
        isEnd = false;

        //linkedList = new MyLinkedList<>();

        for (int i = 0; i < 27; i++) {
            series[i] = null;
        }

        linkedList = new MyLinkedList<>();
    }


    public char convertToChar(TrieContainer t) {

            // we are iterating over the whole array nevertheless, this might be causing the problem.
            for (int i = 0; i < series.length; i++) {
                TrieContainer k = t.series[i];
                    if (k != null) {
                        //System.out.println(97+i);
// It was printing it out because all of those trie containers were //
// filled up.

                        // It might be doing it because ch 97+i gets the last TrieContainer in, alphabetically arranged, so
                        // accessing any character before the character changes to the last one.
                        ch = ((char) (97 + i));
//                        if (k.isEnd) {
//                            break;
//                        }
                }
            }
        return ch;
    }
}


//    public java.util.Map<Integer,String> map;
//
//    public void random() {
//        map = new HashMap<>();
//        map.keySet()
//    }