package comp3506.assn2.application;

import comp3506.assn2.utils.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

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

                phraseOccurrence("make itself");

//                prefixOccurrence("attent");
//                int count = wordCount("hey");
//                System.out.println(count);
//				int result = wordCount("hello");
//				System.out.println(result);

//                String[] stringArray = {"this","they","dude"};
//                wordsOnLine(stringArray);

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

        MyLinkedList<Pair<Integer,Integer>> firstWordList = new
                MyLinkedList<>();


        MyLinkedList<Pair<Integer,Integer>> positionList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> helperList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> finalList = new MyLinkedList<>();

        MyLinkedList<Pair<Integer,Integer>> lolList = new MyLinkedList<>();


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

            Iterator<Pair<Integer, Integer>> pairIterator = positionList
                    .iterator();

            while (pairIterator.hasNext()) {

                System.out.println("(" + pairIterator.next().getLeftValue() +
                        "," + pairIterator.next().getRightValue() + ")");

                Pair<Integer,Integer> pair = new Pair<>(pairIterator.next()
                        .getLeftValue(),pairIterator.next().getRightValue());
                positionalList.add(pair);
            }

            return positionalList;


        } else {

		    int length = 0;
		    int phraseLength = phraseToken.length;

		    firstWordList = mainDocTrie.returnList(mainDocContainer,
                    phraseToken[0]);
		    length = phraseToken[0].length();
		    System.out.println(length);

		    for (int i = 1; i< phraseToken.length; i++) {

		        helperList = mainDocTrie.returnList(mainDocContainer,
                        phraseToken[i]);

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

        MyLinkedList<Pair<Integer,Integer>> myList = new MyLinkedList<>();
        List<Pair<Integer,Integer>> mainList = new ArrayList<>();
        MyLinkedList<Integer> lineList = new MyLinkedList<>();
        List<Integer> yeetList = new ArrayList<>();

        if (words.length<1 || words == null) {
            throw new IllegalArgumentException();
        }
        for (String word: words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }

//        for (String word: words) {
//            myList = mainDocTrie.returnList(mainDocContainer, word);
//            for (Pair<Integer,Integer> pair : myList) {
//                mainList.add(pair);
//            }
//        }
//
////        for(int i=0; i< mainList.size(); i++) {
////            System.out.println(mainList.get(i).getLeftValue() + " " +
////                    mainList.get(i).getRightValue());
////        }
//
//        for (int i = 0; i < mainList.size(); i++) {
//            for (int j = i+1; j < mainList.size(); j++) {
//
//                if (mainList.get(i).getLeftValue().equals(mainList.get(j)
//                        .getLeftValue())) {
//
//                    yeetList.add(mainList.get(j).getLeftValue());
//                    //lineList.insertAtBack(mainList.get(j).getLeftValue());
//                }
//                break;
//            }
//        }
//
//
//
//        for (Integer integer : yeetList) {
//            System.out.println(integer);
//        }
//        for (Pair<Integer,Integer> pair: mainList) {
//            System.out.println(pair.getLeftValue() + " " + pair.getRightValue());
//        }


        return null;
    }

    @Override
    public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {

        if (words.length<1 || words == null) {
            throw new IllegalArgumentException();
        }
        for (String word: words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }
        return null;
    }

    @Override
    public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {

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

        return null;
    }

    private MyLinkedList<Pair<Integer,Integer>> matchLists(int length,
                                                           MyLinkedList<Pair<Integer,Integer>>
                                                                   list1,
                                                           MyLinkedList<Pair<Integer,Integer>> list2) {

        MyLinkedList<Pair<Integer,Integer>> list = new MyLinkedList<>();

        outerLoop:
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
}
