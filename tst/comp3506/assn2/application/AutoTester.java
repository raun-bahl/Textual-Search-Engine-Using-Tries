package comp3506.assn2.application;

import comp3506.assn2.utils.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import sun.awt.image.ImageWatched;

import javax.swing.text.InternationalFormatter;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 *
 * @author
 */
public class AutoTester implements Search {

    MyLinkedList<TrieContainer> containerList = new MyLinkedList<>();


    Scanner docReader, indexReader, stopWordsReader;
    File docFile, indexFile, stopWordsFile;
    Trie stopWordsTrie, mainDocTrie, indexTrie;
    TrieContainer stopWordsContainer, mainDocContainer, indexContainer;

    /* Data Structures for the inverted index */
    Pair<Integer, Integer> pair;
    MyLinkedList<Pair<Integer, Integer>> innerList;
    MyLinkedList<HashMap<TrieContainer[], MyLinkedList<Pair<Integer, Integer>>>>
            outerList;

    MyLinkedList<HashMap<TrieContainer[], MyLinkedList<Pair<Integer, Integer>>>>
            indexer;

    HashMap<TrieContainer[], MyLinkedList<Pair<Integer,Integer>>> invertedIndex;


    public int lineNumber = 1, columnNumber = 1;



    /**
     * Create an object that performs search operations on a document.
     * If indexFileName or stopWordsFileName are null or an empty string the document should be loaded
     * and all searches will be across the entire document with no stop words.
     * All files are expected to be in the files sub-directory and
     * file names are to include the relative path to the files (e.g. "files\\shakespeare.txt").
     *
     * @param documentFileName  Name of the file containing the text of the document to be searched.
     * @param indexFileName     Name of the file containing the index of sections in the document.
     * @param stopWordsFileName Name of the file containing the stop words ignored by most searches.
     * @throws FileNotFoundException    if any of the files cannot be loaded.
     *                                  The name of the file(s) that could not be loaded should be passed
     *                                  to the FileNotFoundException's constructor.
     * @throws IllegalArgumentException if documentFileName is null or an empty string.
     */
    public AutoTester(String documentFileName, String indexFileName, String stopWordsFileName)
            throws FileNotFoundException, IllegalArgumentException {

        // TODO Implement constructor to load the data from these files and
        // TODO setup your data structures for the application.


        invertedIndex = new HashMap<>();


        indexer = new MyLinkedList<HashMap<TrieContainer[], MyLinkedList<Pair<Integer, Integer>>>>();


        /* Trie steup */
        stopWordsTrie = new Trie();
        indexTrie = new Trie();
        mainDocTrie = new Trie();

        HashMap<TrieContainer[], MyLinkedList<Pair<Integer, Integer>>> myMap = new HashMap<>();

        /* TrieContainer Setup */
        mainDocContainer = new TrieContainer();
        stopWordsContainer = new TrieContainer();
        indexContainer = new TrieContainer();
        TrieContainer[] wordObject;

        Pair<Integer,Integer> pair1 = new Pair<>(0,0);
        Pair<Integer,Integer> pair2 = new Pair<>(0,0);

        docFile = new File(documentFileName);
        indexFile = new File(indexFileName);
        stopWordsFile = new File(stopWordsFileName);

        try {
            try {

                docReader = new Scanner(docFile);
                indexReader = new Scanner(indexFile);
                stopWordsReader = new Scanner(stopWordsFile);
                System.out.println("Files loaded!");

                /**
                 * Store all stop words in the stopWordsTrie.
                 */
                while (stopWordsReader.hasNext()) {
                    stopWordsTrie.storeWords(stopWordsContainer,
                            stopWordsReader.next(),pair1);
                }


				/*
				Code for saving the index number in the index files, might be
				useful later.
				 */
//				while (indexReader.hasNextLine()) {
//					String line = indexReader.nextLine();
//					String tokens[] = line.split(",");
//					int number = Integer.parseInt(tokens[1]);
//					list.add(number);
//				}
//				indexReader.close();
//				indexReader = null;
//				indexReader = new Scanner(indexFile);


				/*
				Parse the index file and store it in indexTrie.
				 */
                while (indexReader.hasNext()) {

                    String line = indexReader.nextLine()
                            .replaceAll("[0-9]", "")
                            .replaceAll(",", "");
                    String[] words = line.split("\\s+");

                    for (String word : words) {
                        indexTrie.storeWords(indexContainer, word,pair2);
                    }
                }

				/*
				Temporary data structures for the inverted index.
				 */
                innerList = new MyLinkedList<>();


                int index = 0;
				/*
				Main Document File Parsing and Indexing
				 */
                while (docReader.hasNext()) {

                    columnNumber = 1;  //ColumnNumber gets reset every line

                    String docLine = docReader.nextLine();

                    //Replace all empty lines
                    if (docLine.isEmpty()) {
                        docLine = docLine
                                .replaceAll("(?m)^[ \\t]*\\r?\\n",
                                        "");
                    } else {

                        Pattern p = Pattern.compile("\\p{Alpha}");
                        Matcher m = p.matcher(docLine);

                        if (m.find()) {
                            //          System.out.println("Trailing characters: " +  m
                            //                .start());
                            columnNumber = m.start() + 1;
                        }

//                        int count = docLine.indexOf(docLine.trim());
                        int count = docLine.indexOf(docLine.trim());
                        columnNumber = count +1 ;


                       // System.out.println("Trailing spaces:" + count);

                        // Trailing special characters
                        docLine = docLine.trim();

                        int count2 = (docLine.length()-docLine.replaceAll
                                ("\\s+"," ").length());

                   //     System.out.println("For replacing extra spaces: " +
                    //            count2);

//                        int count1 = docLine.length() - docLine.replaceAll
//                                ("[^a-zA-Z\\']",
//                                        "").toLowerCase().length();
//
//                        columnNumber += count1;
//
//                        System.out.println(count1);
//                        int nullSpace = docLine.split(" ").length;
//                        columnNumber += nullSpace;


                        columnNumber+= count2;

                        int count3 = (docLine.length() - docLine.replaceAll
                                ("^\'|\'$"," ")
                                .length());

                        //THIS +1 be causing trouble
                        columnNumber += count3;

                        //removes the trailing and ending apostrophes
                        docLine = docLine.replaceAll("^\'|\'$"," ");


                        docLine = docLine.replaceAll("[^a-zA-Z\\']", " ");

                        int temp = docLine.length()-docLine.replaceAll
                                ("[^a-zA-Z\\']", " ").length();

                        columnNumber += temp;

                        //System.out.println(docLine);

                            //Tokenize words in a line
                            String[] words = docLine.split(" ");


                            for (String word : words) {

								/* Pattern Matchers are important for checking
								if your word has any trailing special characters.
								You need to fix this code. As of now, it works
								fine with trailing spaces but trailing special
								characters do not work. The Code below will
								help you later to fix this.
								 */

                                //Set coordinates
                                pair = new Pair<>(0, 0);
                                pair.setLeftValue(lineNumber);
                                pair.setRightValue(columnNumber);


//                                //Remove redundant punctuation
//                                String putWord = word
//                                        .replaceAll("[^a-zA-Z\\']",
//                                                "").toLowerCase();
//
//                                int temp = word.length()-word.replaceAll
//                                        ("[^a-zA-Z\\']", " ").length();

                                int count4 = word.length()-word.replaceAll
                                        ("^\'|\'$", "").length();
                                columnNumber+= count4;

                                word=word.replaceAll("^\'|\'$", "");


                                mainDocTrie.storeWords(mainDocContainer,
                                            word, pair);

//                                columnNumber += temp;
                                columnNumber += word.length() + 1;
                            }

                    }

                    lineNumber++;
                }

                System.out.println("Data Structures Loaded! \n");

//                int count = wordCount("");
//                System.out.println(count);

//                phraseOccurrence("to be or not to be that is the question");

//                prefixOccurrence("obscure");

//                boolean bool=mainDocTrie.isWordPresent(mainDocContainer,"too");
//                System.out.println(bool);

//                prefixOccurrence("abun");
//                int count = wordCount("sprin");
//                System.out.println(count);
//				int result = wordCount("hello");
//				System.out.println(result);

                String[] stringArrayRequired = {"with","flowers"};
                String[] stringArrayExcluded = {"in"};
                wordsNotOnLine(stringArrayRequired,stringArrayExcluded);

            } catch (FileNotFoundException e) {

                if (!new File(documentFileName).exists()) {
                    throw new FileNotFoundException(documentFileName);
                } else if (!new File(indexFileName).exists()) {
                    throw new FileNotFoundException(indexFileName);
                } else if (!new File(stopWordsFileName).exists()) {
                    throw new FileNotFoundException(stopWordsFileName);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) throws FileNotFoundException {
        AutoTester autoTester = new AutoTester("files/" +
                "shakespeare.txt", "files/shakespeare-index.txt",
                "files/stop-words" +
                        ".txt");
    }


    @Override
    public int wordCount(String word) throws IllegalArgumentException {

        if (word.isEmpty() || word == null) {
            throw new IllegalArgumentException();
        }

        int result;

           result = mainDocTrie.wordCount(mainDocContainer, word);

        return result;
    }

    @Override
    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {

        if (phrase.isEmpty() || phrase == null) {
            throw new IllegalArgumentException();
        }

        //Copy the results into this
        List<Pair<Integer,Integer>> positionalList = new ArrayList<>();

        MyLinkedList<Pair<Integer,Integer>> firstWordList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> positionList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> helperList = new MyLinkedList<>();


		/*
		Please keep in mind that you need to tokenize the phrase here as well.
		 */
		String[] phraseToken = phrase.split(" ");
		//This case is only for 1 word
		if (phraseToken.length == 1) {
           positionList = mainDocTrie.returnList(mainDocContainer,
                   phraseToken[0]);

//            for (Pair<Integer,Integer> pair : positionList) {
//                System.out.println("(" + pair.getLeftValue() + "," + pair
//                        .getRightValue() + ")");
//            }
            int count = 0;
            for (Pair<Integer,Integer> pair: positionList) {
                Pair<Integer,Integer> insertionPair = new Pair<>(pair
                        .getLeftValue(),
                        pair.getRightValue());
                System.out.println(pair.getLeftValue() + " " + pair.getRightValue());
                positionalList.add(insertionPair);
                count++;
            }

            System.out.println(count);

//            while (pairIterator.hasNext()) {
//
//                System.out.println("(" + pairIterator.next().getLeftValue() +
//                        "," + pairIterator.next().getRightValue() + ")");
//
//                Pair<Integer,Integer> pair = new Pair<>(pairIterator.next()
//                        .getLeftValue(),pairIterator.next().getRightValue());
//                positionalList.add(pair);
//            }

            return positionalList;


        } else {

		    int length = 0;
		    int phraseLength = phraseToken.length;

		    firstWordList = mainDocTrie.returnList(mainDocContainer,
                    phraseToken[0]);
		    length = phraseToken[0].length();
		    //System.out.println(length);

		    for (int i = 1; i< phraseToken.length; i++) {

		        helperList = mainDocTrie.returnList(mainDocContainer,
                        phraseToken[i].replaceAll("[^a-zA-Z\\']",""));

		        firstWordList = matchLists(length,firstWordList,helperList);

                length += phraseToken[i].length()+1;

		        if (length == phraseLength) {
		            break;
                }
            }

		}

        int count = 0;
        for (Pair<Integer,Integer> pair : firstWordList) {
		    System.out.println("(" + pair.getLeftValue()+","+pair
                    .getRightValue()+")");
		    positionalList.add(pair);
		    count++;
        }
        System.out.println(count);
        return positionalList;

    }

    @Override
    public List<Pair<Integer, Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {

        if (prefix.isEmpty() || prefix == null) {
            throw new IllegalArgumentException();
        }

        List<Pair<Integer,Integer>> pairMyLinkedList = new ArrayList<>();

        pairMyLinkedList = mainDocTrie.predictWord(mainDocContainer, prefix);


        int count = 0;
        for (Pair<Integer,Integer> pair: pairMyLinkedList) {
            System.out.println(pair.getLeftValue() + " " + pair.getRightValue
                    ());
            count++;
        }

        System.out.println("Count: " + count);
        return null;
    }

    /**
     * @param words Array of words to find on a single line in the document.
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {

        MyLinkedList<Pair<Integer,Integer>> helperList = new MyLinkedList<>();
        List<Pair<Integer,Integer>> mainList = new ArrayList<>();
        MyLinkedList<Integer> lineList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> firstWordLineList = new
                MyLinkedList<>();

        if (words.length<1 || words == null) {
            throw new IllegalArgumentException();
        }
        for (String word: words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }

        firstWordLineList = mainDocTrie.returnList(mainDocContainer,
                words[0]);

        for (int i=1; i < words.length; i++) {

            helperList = mainDocTrie.returnList(mainDocContainer, words[i]);
            firstWordLineList = andMatcher(firstWordLineList, helperList);
        }

        for (Pair<Integer,Integer> pair: firstWordLineList) {

            System.out.println(pair.getLeftValue());
            lineList.insertAtBack(pair.getLeftValue());

        }

        return null;
    }

    @Override
    public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {

        MyLinkedList<Pair<Integer,Integer>> myList = new MyLinkedList<>();
        MyLinkedList<Integer> yeetList = new MyLinkedList<>();

        List<Pair<Integer,Integer>> mainList = new ArrayList<>();

        if (words.length<1 || words == null) {
            throw new IllegalArgumentException();
        }
        for (String word: words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }

        for (int i = 0; i < words.length; i++) {
            myList = mainDocTrie.returnList(mainDocContainer,words[i]);
            for (Pair<Integer,Integer> pair: myList) {
                if (!yeetList.contains(pair.getLeftValue())) {
                    yeetList.insertAtBack(pair.getLeftValue());
                }
            }
        }

        for (Integer integer: yeetList) {
            System.out.println(integer);
        }
        return null;
    }

    @Override
    public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {

        MyLinkedList<Pair<Integer,Integer>> wordList1 = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> wordList2 = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> helperList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> tempList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> wordList1Copy = new
                MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> tempList2 = new MyLinkedList<>();


        if (wordsRequired.length<1 || wordsRequired == null) {
            throw new IllegalArgumentException();
        }
        for (String word: wordsRequired) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }

        if (wordsExcluded.length<1 || wordsExcluded == null) {
            throw new IllegalArgumentException();
        }
        for (String word: wordsExcluded) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }

        wordList1 = mainDocTrie.returnList(mainDocContainer,wordsRequired[0]);

        for (int i = 1; i < wordsRequired.length; i++) {
            wordList2 = mainDocTrie.returnList(mainDocContainer,
                    wordsRequired[i]);
            wordList1 = andMatcher(wordList1,wordList2);
        }


        for (Pair<Integer,Integer> pair: wordList1) {
            wordList1Copy.insertAtBack(pair);
        }

        for (Pair<Integer,Integer> pair: wordList1Copy) {
            tempList2.insertAtBack(pair);
        }
//

        //System.out.println(wordsExcluded[0]);
        //System.out.println(wordsExcluded[1]);

        for (int i = 0; i < wordsExcluded.length; i++) {
            helperList = mainDocTrie.returnList(mainDocContainer,
                    wordsExcluded[i]);
//            wordList1 = notMatcher(wordList1,helperList);
            for (Pair<Integer,Integer> pair: helperList) {
//                System.out.println("called");
                tempList.insertAtBack(pair);
//                System.out.println(pair.getLeftValue() + " " + pair
//                        .getRightValue());
            }
        }

//        System.out.println(wordList1Copy.getSize());
//        System.out.println(tempList.getSize());

//        for (int i = 0; i < wordList1Copy.getSize()-1; i++) {
//            System.out.println("wot");
//            for (int j = 0; j < tempList.getSize()-1; j++) {
//                System.out.println("bamboozle");
//                if (wordList1Copy.get(i).getLeftValue() == tempList.get(j)
//                        .getLeftValue()) {
//                    System.out.println("hey");
//                    tempList2.deleteAtPosition(i);
//                }
//            }
//        }

        for (Pair<Integer,Integer> pair1: wordList1Copy) {
            for (Pair<Integer,Integer> pair2: tempList) {

                if (pair1.getLeftValue() == pair2.getLeftValue()) {
                    wordList1Copy.remove(pair1);
                }
            }
        }


//
//        System.out.println("Temp List below:\n");
        for (Pair<Integer,Integer> pair: wordList1Copy) {
            System.out.println(pair.getLeftValue());
        }



//////
//        for (Pair<Integer,Integer> pair: tempList2) {
//            System.out.println(pair.getLeftValue());
//        }

        return null;
    }





    private MyLinkedList<Pair<Integer,Integer>> matchLists(int length,
                                                           MyLinkedList<Pair<Integer,Integer>>
                                                                   list1,
                                                           MyLinkedList<Pair<Integer,Integer>> list2) {

        MyLinkedList<Pair<Integer,Integer>> list = new MyLinkedList<>();


        for (Pair<Integer,Integer> wordPair2 : list2) {

            for (Pair<Integer,Integer> wordPair1: list1) {

                if (wordPair2.getRightValue().equals(wordPair1.getRightValue
                        ()+length+1) && wordPair2.getLeftValue().equals
                        (wordPair1.getLeftValue())) {

                    Pair<Integer,Integer> firstWordPair = new Pair<>
                            (wordPair1.getLeftValue(),
                                    wordPair1.getRightValue());
                    list.insertAtBack(firstWordPair);

                }
            }
        }
        return list;
    }

    private MyLinkedList<Pair<Integer,Integer>> andMatcher
            (MyLinkedList<Pair<Integer,
            Integer>>
                                                      list1,
                                              MyLinkedList<Pair<Integer,
                                                      Integer>> list2) {

        MyLinkedList<Pair<Integer, Integer>> list = new MyLinkedList<>();

        for (Pair<Integer, Integer> wordPair2 : list2) {

            for (Pair<Integer, Integer> wordPair1 : list1) {

                if (wordPair2.getLeftValue().equals(wordPair1.getLeftValue())) {

                    Pair<Integer, Integer> pair = new Pair<>
                            (wordPair1.getLeftValue(), wordPair2.getRightValue());
                    list.insertAtBack(pair);
                }
            }
        }
        return list;
    }

    private MyLinkedList<Pair<Integer,Integer>> notMatcher(
            MyLinkedList<Pair<Integer,Integer>> list1,
            MyLinkedList<Pair<Integer,Integer>> list2) {

        MyLinkedList<Pair<Integer,Integer>> list = new MyLinkedList<>();
        int remover = 0;

        for (Pair<Integer,Integer> wordPair2 : list2) {

            for (Pair<Integer,Integer> wordPair1: list1) {


                if (!wordPair2.getLeftValue().equals(wordPair1.getLeftValue()
                )) {

                    Pair<Integer,Integer> pair = new Pair<>
                            (wordPair1.getLeftValue(),wordPair1.getRightValue
                                    ());

                    list.insertAtBack(pair);
                }
            }
        }

        return list;
    }

}
