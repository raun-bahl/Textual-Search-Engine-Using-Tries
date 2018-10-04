package comp3506.assn2.utils;

import java.io.*;

public class Trie {

    BufferedReader docReader, indexReader, stopWordsReader;
    static TrieContainer start;
    static int wordCount1 = 0;
    static int wordCount2 = 0;
    static int temp = 0;


    TrieContainer[] myWord;
    char ch;

    public Trie() {
        //myWord = new TrieContainer[30];
    }
    public TrieContainer[] storeWords(TrieContainer start, String word){

        myWord = new TrieContainer[word.length()];
        char tempChar, character;
        int aint = (int)'a';

        for (int j = 0; j < word.length(); j++) {
            tempChar = word.charAt(j);
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
        }
        return myWord;
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
            if(t!=null){
                printWordStrings(t, toPrint + (char)(97+i));
            }
        }
    }

    public String getStringWord(TrieContainer[] word) {
        if (word == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String returnString;

        for (int i = 0; i<word.length; i++) {
            ch = word[i].convertToChar(word[i]);
            sb.append(ch);
           //System.out.println(ch);
        }
        returnString = sb.toString();
        return returnString;
    }

    public static void main(String[] args) {
        //do nothing
    }

}
