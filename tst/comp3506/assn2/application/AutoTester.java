package comp3506.assn2.application;

import comp3506.assn2.utils.*;

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

						/*
						The following code is for trailing spaces, trailing
						characters still doesn't work though.
						 */
                        if (!docLine.trim().equals(docLine)) {

                            int count = docLine.indexOf(docLine.trim());
                            columnNumber = count + 1;
                            docLine = docLine.trim();

                        } else {

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
//								Pattern p = Pattern.compile("\\p{Alpha}");
//								Matcher m = p.matcher(word);
//
//								if (m.find()) {
//									columnNumber = m.start() + 1;
//
//								}

                                //Set coordinates
                                pair = new Pair<>(0, 0);
                                pair.setLeftValue(lineNumber);
                                pair.setRightValue(columnNumber);


                                //Remove redundant punctuation
                                String putWord = word
                                        .replaceAll("[^a-zA-Z\\']",
                                                "").toLowerCase();


                                /*Should I be calling this twice? */
                                //mainDocTrie.storeWords(mainDocContainer, putWord);



//                                    mainDocTrie.storeWords(mainDocContainer,
//                                            putWord,pair);

                                    mainDocTrie.storeWords(mainDocContainer,
                                            putWord, pair);



//                                if (mainDocTrie.isWordPresent
//                                        (mainDocContainer,putWord)) {
//                                    innerList.insertAtFront(pair);
//                                    invertedIndex.put(wordObject,innerList);
//                                } else {
//                                    innerList = new MyLinkedList<>();
//                                    innerList.insertAtFront(pair);
//                                    invertedIndex.put(wordObject,innerList);
//                                }


								/*This is the string version of the object that
								 goes into mainDocTrieContainer, and this
								is  here for debugging purposes */
//								String stringWord = mainDocTrie.getString(wordObject);
								//System.out.println(stringWord);


                                columnNumber += putWord.length() + 1;



                                /**
                                 * The Buggy AF Code begins
                                 * Also, your PrintWordStrings is broken. If you
                                 * can't find anything then see if any errors
                                 * are related to that.
                                 */


                                //This requires very careful observation
                                //containerList.insertAtFront(mainDocContainer);

                                //Hmmm
//                                for (TrieContainer t: containerList) {
//                                    mainDocTrie.printWordStrings(t,"");
//                                }

                                /*
                                So as of now, I'm making a new Trie Container
                                 for every word iteration and adding it to a
                                 Container List above. I don't know if this is
                                 a good idea or not, and I should really
                                 think about the implications of my design
                                 choices.

                                 */
//                                    if (mainDocTrie.isWordPresent(mainDocContainer,
//                                            putWord)) {
//                                        //System.out.println("yeet");
//                                        innerList.insertAtFront(pair);
//                                        myMap.put(wordObject, innerList);
//                                        outerList.insertAtFront(myMap);
//                                    } else {
//                                        innerList = new MyLinkedList<>();
//                                        innerList.insertAtFront(pair);
//                                        myMap.put(wordObject, innerList);
//                                        outerList.insertAtFront(myMap);
//                                    }

                            }
                        }
                    }

                    lineNumber++;
                }

                mainDocTrie.returnList(mainDocContainer);
//                int count = wordCount("kill");
//                System.out.println(count);
//				int result = wordCount("hello");
//				System.out.println(result);

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
                "random.txt", "files/shakespeare-index.txt",
                "files/stop-words" +
                        ".txt");
    }


    @Override
    public int wordCount(String word) throws IllegalArgumentException {

        int result = 0;

        for (TrieContainer[] t: invertedIndex.keySet()) {
            if (word.equals(mainDocTrie.getString(t))) {
                Iterator iterator = invertedIndex.values().iterator();
                while (iterator.hasNext()) {

                    result += invertedIndex.values().size();
                    iterator.next();
                }
            }
        }
        return result;

//        for (HashMap<TrieContainer[], MyLinkedList<Pair<Integer, Integer>>> hm : outerList) {
//
//            for (TrieContainer[] t : hm.keySet()) {
//
//
//                //GetStringWord needs to be fixed for this.
//                if (word.equals(mainDocTrie.getStringWord(t))) {
//
//                    for (MyLinkedList<Pair<Integer, Integer>> list : hm.values()) {
//
////						System.out.println(result);
//                        result = list.getSize();
//
//                    }
//                }
//            }
//        }
    }

    @Override
    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {


		/*
		Please keep in mind that you need to tokenize the phrase here as well.
		 */
        for (HashMap<TrieContainer[], MyLinkedList<Pair<Integer, Integer>>> hm : outerList) {

            for (TrieContainer[] t : hm.keySet()) {


                //GetStringWord needs to be fixed for this.
                if (phrase.equals(mainDocTrie.getStringWord(t))) {
                    for (MyLinkedList<Pair<Integer, Integer>> list : hm.values()) {

                        for (Pair<Integer, Integer> pair : list) {
                            System.out.println(pair.getLeftValue() + " " + pair.getRightValue());
                        }

                    }
                }
            }
        }
        return null;
    }
}
