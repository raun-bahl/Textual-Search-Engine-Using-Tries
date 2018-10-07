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
						int count = docLine.indexOf(docLine.trim());
						columnNumber = count + 1;



                        Pattern p = Pattern.compile("\\p{Alpha}");
                        Matcher m = p.matcher(docLine);

								if (m.find()) {
									columnNumber = m.start() + 1;

								}

                        docLine = docLine.trim();



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


                                //Remove redundant punctuation
                                String putWord = word
                                        .replaceAll("[^a-zA-Z\\']",
                                                "").toLowerCase();


                                mainDocTrie.storeWords(mainDocContainer,
                                            putWord, pair);

                                columnNumber += putWord.length() + 1;
                            }

                    }

                    lineNumber++;
                }

                System.out.println("Data Structures Loaded!");

//                int count = wordCount("");
//                System.out.println(count);

                phraseOccurrence("this");
//                int count = wordCount("hey");
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

        if (word.isEmpty() || word == null) {
            throw new IllegalArgumentException("Word is null.");
        }
        int result;

           result = mainDocTrie.wordCount(mainDocContainer, word);

        return result;
    }

    @Override
    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {


        List<Pair<Integer,Integer>> positionalList = new ArrayList<>();

        List<Pair<Integer,Integer>> helperList = new ArrayList<>();

        List<Pair<Integer,Integer>> helperList2 = new ArrayList<>();

		/*
		Please keep in mind that you need to tokenize the phrase here as well.
		 */
		String[] phraseToken = phrase.split(" ");
		//This case is only for 1 word
		if (phraseToken.length == 1) {
           positionalList = mainDocTrie.returnList(mainDocContainer,
                   phraseToken[0],
                   positionalList);

            for (Pair<Integer,Integer> pair : positionalList) {
                System.out.println("(" + pair.getLeftValue() + "," + pair
                        .getRightValue() + ")");
            }

            return positionalList;
        } else {

		    positionalList = mainDocTrie.returnList(mainDocContainer,
                    phraseToken[0], positionalList);
		    helperList = mainDocTrie.returnList(mainDocContainer,
                    phraseToken[1], helperList);

		    for (int i =0; i< positionalList.size(); i++) {
		        if (helperList.get(i).getRightValue().equals(positionalList.get(i).getRightValue()+phraseToken[0].length() + 1)) {
                    //helperList2.add(helperList.get(i));
                    helperList2.add(positionalList.get(i));
                }
            }
        }

        for (Pair<Integer,Integer> pair : helperList2) {
		    System.out.println("(" + pair.getLeftValue()+","+pair
                    .getRightValue()+")");
        }
        return helperList2;
    }
}
