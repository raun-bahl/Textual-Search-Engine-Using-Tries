package comp3506.assn2.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Trie {

    BufferedReader docReader, indexReader, stopWordsReader;
    static TrieContainer start;
    static int wordCount1 = 0;
    static int wordCount2 = 0;
    static int temp = 0;


    TrieContainer[] myWord;
    char ch;

    public Trie() {

    }
    public void storeWords(TrieContainer start, String word, Pair<Integer,
            Integer> location){

        start.linkedList = new MyLinkedList<>();
        myWord = new TrieContainer[word.length()];

            char tempChar, character;
            int aint = (int) 'a';

            for (int j = 0; j < word.length(); j++) {
                tempChar = word.charAt(j);
                //Since mainDocContainer has been filled up with the previous word, the following statement causes
                // myWord[0] to fill up with the first alphabet of
                myWord[j] = start;
                if (tempChar == '\'') {
                    //tempChar = 'x';
                    int apostropheChar = 26;
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
                        temp++;
                    }
                } else {
                    character = Character.toLowerCase(tempChar);
                    //In series, check the position of character,
                    //if it is already filled then check the series of filled Trie object.
                    //if it is not filled then create new TrieContainer object and place it at correct position, and check
                    //if it is end of the word, then mark isEnd = true or else false;
                    if (start.series[character - aint] != null) {
                        if (word.length() - 1 == j) { //if word is found till last character, then mark the end as true.
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
                start.linkedList.insertAtBack(location);
            }

    }


    public void addToList(TrieContainer start, Pair<Integer,
            Integer>
            location) {

        if (start == null) {
            return;
        }
        for (int i = 0; i< start.series.length; i++) {
            TrieContainer t = start.series[i];
            if (t != null) {
                    t.linkedList.insertAtFront(location);

            } else {
                continue;

            }
        }
    }

    public MyLinkedList<Pair<Integer,Integer>> returnList(TrieContainer start,
                                                  String word) {

        MyLinkedList<Pair<Integer,Integer>> returnList = new MyLinkedList<>();
        int count = 0;
        for (int i=0; i< word.length(); i++) {
            char ch = word.charAt(i);
            TrieContainer t = start.series[i];

//                int apostropheChar = 26;
//                if (start.series[apostropheChar] != null) {
//                    if (word.length() - 1 != i) {
//                        start = start.series[apostropheChar];
//                    } else {
//                        if (start.series[apostropheChar].isEnd) {
//                            for (Pair<Integer, Integer> pair : start.series[apostropheChar]
//                                    .linkedList) {
//                                pairList.add(pair);
//                                break;
//                            }
//                        }
//                    }
//                }
                if (start.series[ch - 97] != null) {
                    if (word.length() - 1 != i) {
                        start = start.series[ch - 97];
                    } else {
                        if (start.series[ch - 97].isEnd) {
                            for (Pair<Integer, Integer> pair : start.series[ch - 97]
                                    .linkedList) {
                                returnList.insertAtBack(pair);
                            }
                        }
                    }
                }

        }
        return returnList;
    }

    public int wordCount(TrieContainer start,String word) {
        int count = 0;

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
                            count = start.series[ch - 97].linkedList.size;
                        }
                    }
                }
            }
        }
        return count;
    }

    public boolean isWordPresent(TrieContainer start, String word){
        boolean isFound = true;
        for (int i = 0; i < word.length(); i++) {
            char character = word.charAt(i);
            if (character == '\'') {
                continue;
            } else {
                //if at character position TrieContainer object is present then character is found and
                //start looking for next character is word.
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
    public  void printWordStrings(TrieContainer start, String toPrint) {
        if(start==null){
            return;
        }
        if(start.isEnd){
            System.out.println(toPrint);
        }
        for (int i = 0; i < start.series.length; i++) {
            TrieContainer t = start.series[i];
            if(t != null) {
                printWordStrings(t, toPrint + (char)(97+i));
            }
        }
    }

    public String getStringWord(TrieContainer word) {
        if (word == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String returnString;
        //System.out.println(word.length);
        for (int i = 0; i<word.series.length; i++) {

            TrieContainer n = word.series[i];

            if (n != null) {
                ch = (char) (97 + i);
            }
            sb.append(ch);
            }
        returnString = sb.toString();
        return returnString;
    }

    public String getString(TrieContainer[] word) {
        if (word == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String returnString;

        for (int i=0; i< word.length; i++) {
            TrieContainer t = word[i];
            for (int j = 0; j < t.series.length; i++) {
                ch = t.convertToChar(t);
                sb.append(ch);
                System.out.println(ch);
            }
        }
        returnString = sb.toString();
        return returnString;
    }

    public List<Pair<Integer,Integer>> predictWord(TrieContainer start, String word) {


        MyLinkedList<Pair<Integer,Integer>> pairMyLinkedList = new
                MyLinkedList<>();

        List<Pair<Integer,Integer>> listz = new ArrayList<>();


//        System.out.println("we got here");
        for (int i = 0; i< word.length(); i++) {


            int index = word.charAt(i) - 'a';

            if (start.series[index] != null) {
                //if (word.length()-1 != i) {
                    start = start.series[index];
                //}
            }

        }


        predict(word,start,listz);

        return listz;
    }

    public void predict(String word, TrieContainer container,
                        List<Pair<Integer,Integer>> list) {

//        MyLinkedList<Pair<Integer,Integer>> pairMyLinkedList = new
//                MyLinkedList<>();

        if (container.isEnd) {

            for (Pair<Integer,Integer> pair: container.linkedList) {

                if (!list.contains(pair)) {
                    list.add(pair);
                }
            }
            //return pairMyLinkedList;
            //words.insertAtBack(word);
        }

            TrieContainer[] trieContainers = container.series;

//        System.out.println("we also got here");
            for (TrieContainer trieContainer : trieContainers) {
                if (trieContainer != null) {

                    char childChar = trieContainer.convertToChar(trieContainer);
                        predict(word + Character.toString
                                        (childChar),
                                trieContainer, list);
                }
            }
    }


    public static void main(String[] args) {
    }

}
