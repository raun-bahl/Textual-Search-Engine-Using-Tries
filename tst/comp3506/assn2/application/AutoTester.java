package comp3506.assn2.application;

import com.sun.tools.corba.se.idl.InterfaceGen;
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

    /* File Readers */
    Scanner docReader, indexReader, stopWordsReader;
    File docFile, indexFile, stopWordsFile;
    /* Trie Data Structure */
    Trie stopWordsTrie, mainDocTrie;
    TrieContainer stopWordsContainer, mainDocContainer;
    /* Helper Classes */
    ArgumentChecker argumentChecker;
    WordMatcher wordMatcher;

    Pair<Integer, Integer> pair;
    /* Main Index List which stores the names of sections and their line numbers */
    MyLinkedList<Pair<String,Integer>> indexList;

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

        /* Integer Array to save and count spaces and special characters. */
        Integer[] charAndSpaceCount = new Integer[5];

        indexList = new MyLinkedList<>();
        wordMatcher = new WordMatcher();

        /* Trie setup */
        stopWordsTrie = new Trie();
        mainDocTrie = new Trie();

        /* TrieContainer Setup */
        mainDocContainer = new TrieContainer();
        stopWordsContainer = new TrieContainer();

        Pair<Integer, Integer> pair1 = new Pair<>(0, 0);

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
                 * Store all stop words in the stopWordsTrie.Since indices are not needed, a dummy
                 * value is used as the pair.
                 */
                while (stopWordsReader.hasNext()) {
                    stopWordsTrie.storeWords(stopWordsContainer, stopWordsReader.next(), pair1);
                }

                /**
                 * Store the index names and their line numbers inside the indexList.
                 */
                while (indexReader.hasNext()) {

                    String indexLine = indexReader.nextLine();
                    String numberString = indexLine.replaceAll("\\D+", "");
                    int indexNumber = Integer.parseInt(numberString);
                    indexLine = indexLine.replaceAll("[0-9]","").
                            replaceAll(",$","");
                    Pair<String, Integer> indexPair = new Pair<>(indexLine,indexNumber);
                    indexList.insertAtBack(indexPair);
                }


				/*
				Main Document File Parsing and Indexing. Words are stored inside a trie with their
				positions.
				 */
                while (docReader.hasNext()) {
                    //ColumnNumber gets reset every line
                    columnNumber = 1;

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
                            columnNumber = m.start() + 1;
                        }

                        charAndSpaceCount[0] = docLine.indexOf(docLine.trim());
                        columnNumber = charAndSpaceCount[0] + 1;
                        docLine = docLine.trim();


                        charAndSpaceCount[1] = (docLine.length() - docLine.replaceAll
                                ("\\s+", " ").length());
                        columnNumber += charAndSpaceCount[1];

                        charAndSpaceCount[2] = (docLine.length() - docLine.replaceAll
                                ("^\'|\'$", " ")
                                .length());
                        columnNumber += charAndSpaceCount[2];

                        //removes the trailing and ending apostrophes
                        docLine = docLine.replaceAll("^\'|\'$", " ");
                        //Remove everything except for middle apostrophes and alphabets
                        docLine = docLine.replaceAll("[^a-zA-Z\\']", " ");

                        charAndSpaceCount[3] = docLine.length() - docLine.replaceAll
                                ("[^a-zA-Z\\']", " ").length();
                        columnNumber += charAndSpaceCount[3];

                        //Tokenize words in a line
                        String[] words = docLine.split(" ");

                        for (String word : words) {

                            //Set location for a word
                            pair = new Pair<>(0, 0);
                            pair.setLeftValue(lineNumber);
                            pair.setRightValue(columnNumber);

                            charAndSpaceCount[4] = word.length() - word.replaceAll
                                    ("^\'|\'$", "").length();
                            columnNumber += charAndSpaceCount[4];
                            word = word.replaceAll("^\'|\'$", "");

                            //Store word and location in the trie
                            mainDocTrie.storeWords(mainDocContainer,
                                    word, pair);

                            columnNumber += word.length() + 1;
                        }
                    }
                    lineNumber++;
                }

                System.out.println("Data Structures Loaded! \n");

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


    /**
     * Counts the occurrences of the given word
     * @param word The word to be counted in the document.
     * @return Integer denoting the occurences.
     * @throws IllegalArgumentException if word is null or an empty String.
     */
    @Override
    public int wordCount(String word) throws IllegalArgumentException {

        argumentChecker = new ArgumentChecker(word);

        int result;
        result = mainDocTrie.wordCount(mainDocContainer, word);
        return result;

    }

    /**
     *
     * @param phrase The phrase to be found in the document.
     * @return List of pairs, where each pair indicates the line and column number of
     *         each occurrence of the phrase.
     *         Returns an empty list if the phrase is not found in the document.
     * @throws IllegalArgumentException if phrase is null or an empty String.
     */
    @Override
    public List<Pair<Integer, Integer>> phraseOccurrence(String phrase)
            throws IllegalArgumentException {

        argumentChecker = new ArgumentChecker(phrase);

        //Copy the results into this
        List<Pair<Integer, Integer>> returnList = new ArrayList<>();
        MyLinkedList<Pair<Integer, Integer>> firstWordList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> positionList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> helperList = new MyLinkedList<>();


        String[] phraseToken = phrase.split(" ");
        //If the phrase is only a single word
        if (phraseToken.length == 1) {
            //List of all positions of the word
            positionList = mainDocTrie.returnList(mainDocContainer,
                    phraseToken[0]);

            for (Pair<Integer, Integer> pair : positionList) {
                Pair<Integer, Integer> insertionPair =
                        new Pair<>(pair.getLeftValue(), pair.getRightValue());
                returnList.add(insertionPair);
            }
            return returnList;

        } else {   //The phrase is multiple words

            int length;
            int phraseLength = phraseToken.length;

            /*
            ContiguousWordMatcher below ensure that all of the words are contiguous by taking 
            length as a parameter, which is essentially the partial length of the word or the length
            of the phrase before the word that is being matched. 
             */
            firstWordList = mainDocTrie.returnList(mainDocContainer,
                    phraseToken[0]);
            length = phraseToken[0].length();

            for (int i = 1; i < phraseToken.length; i++) {

                helperList = mainDocTrie.returnList(mainDocContainer,
                        phraseToken[i].replaceAll("[^a-zA-Z\\']", ""));

                firstWordList = wordMatcher.contiguousWordMatcher(length, firstWordList, helperList);

                length += phraseToken[i].length() + 1;

                if (length == phraseLength) {
                    break;
                }
            }
        }
        returnList = pairListConverter(firstWordList);
        return returnList;
    }

    /**
     *  Finds all occurrences of the prefix in the document.
     * @param prefix The prefix of a word that is to be found in the document.
     * @return  List of pairs, where each pair indicates the line and column number of each
     *          occurrence of the prefix.
     *          Returns an empty list if the prefix is not found in the document.
     * @throws IllegalArgumentException if prefix is null or an empty String.
     */
    @Override
    public List<Pair<Integer, Integer>> prefixOccurrence(String prefix)
            throws IllegalArgumentException {

        List<Pair<Integer,Integer>> prefixList = new ArrayList<>();

        MyLinkedList<Pair<Integer, Integer>> predictionList = new MyLinkedList<>();
        
        /* Calls the Trie's predictWord method, which traverses to the last character of the 
        prefix and then calls the predict method, which traverses through all of it's children 
        nodes.
         */
        predictionList = mainDocTrie.predictWord(mainDocContainer, prefix);

        prefixList = pairListConverter(predictionList);

        return prefixList;
    }


    /** Searches the document for lines that contain all the words in the 'words' parameter.
     *  The words don't need to be contiguous in a line.
     *
     * @param words Array of words to find on a single line in the document.
     * @return Line numbers of each of the words. Empty List if words do not appear in any line
     *         of the document.
     * @throws IllegalArgumentException
     */
    @Override
    public List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {

        argumentChecker = new ArgumentChecker(words);

        MyLinkedList<Pair<Integer, Integer>> helperList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> firstWordList = new MyLinkedList<>();
        List<Integer> returnList = new ArrayList<>();
        
        
        firstWordList = mainDocTrie.returnList(mainDocContainer,
                words[0]);
        for (int i = 1; i < words.length; i++) {
            
            helperList = mainDocTrie.returnList(mainDocContainer, words[i]);
            /* andMatcher spits out a list of pairs of words that are on the same line. 
            Consequently, firstWordList gets smaller and smaller each iteration. */
            firstWordList = wordMatcher.andMatcher(firstWordList, helperList);
        }

        for (Pair<Integer, Integer> pair : firstWordList) {
            returnList.add(pair.getLeftValue());
        }
        
        return returnList;
    }

    /**
     * Searches the document for lines that contain any of the words in the 'words' parameter.
     * Words do not need to be contiguous on a line.
     *
     * @param words Array of words to find on a single line in the document.
     * @return  List of line numbers any of the words appear on.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in the array are null or empty.
     */
    @Override
    public List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {

        argumentChecker = new ArgumentChecker(words);

        MyLinkedList<Pair<Integer, Integer>> wordList = new MyLinkedList<>();
        MyLinkedList<Integer> helperList = new MyLinkedList<>();
        List<Integer> returnList = new ArrayList<>();


        /* Unlike andMatcher, we could iterate over all words as there is no constraint. */
        for (int i = 0; i < words.length; i++) {
            
            wordList = mainDocTrie.returnList(mainDocContainer, words[i]);
            
            for (Pair<Integer, Integer> pair : wordList) {
                
                if (!helperList.contains(pair.getLeftValue())) {
                    
                    helperList.insertAtBack(pair.getLeftValue());
                }
            }
        }

        for (Integer integer : helperList) {
            returnList.add(integer);
        }
        return returnList;
    }

    /**
     * Searches the document for lines that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * @param wordsRequired Array of words to find on a single line in the document.
     * @param wordsExcluded Array of words that must not be on the same line as 'wordsRequired'.
     * @return List of line numbers on which all the wordsRequired appear
     *         and none of the wordsExcluded appear in the document.
     *         Returns an empty list if no lines meet the search criteria.
     * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded are null or an
     *                                  empty array or any of the Strings in either of the arrays
     *                                  are null or empty.
     */
    @Override
    public List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded)
            throws IllegalArgumentException {

        MyLinkedList<Pair<Integer, Integer>> requiredWordList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> finalRequiredWordList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> excludedWordList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> finalExcludedWordList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer, Integer>> requiredWordListCopy = new MyLinkedList<>();

        List<Integer> returnList = new ArrayList<>();


        if (wordsExcluded.length < 1 || wordsExcluded == null) {
            throw new IllegalArgumentException();
        }
        for (String word : wordsExcluded) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }

        requiredWordList = mainDocTrie.returnList(mainDocContainer, wordsRequired[0]);
        for (int i = 1; i < wordsRequired.length; i++) {
            finalRequiredWordList = mainDocTrie.returnList(mainDocContainer,
                    wordsRequired[i]);
            requiredWordList = wordMatcher.andMatcher(requiredWordList, finalRequiredWordList);
        }

        /* Making a copy of the list because of potential changes to the original list in code
        ahead.
         */
        for (Pair<Integer, Integer> pair : requiredWordList) {
            requiredWordListCopy.insertAtBack(pair);
        }


        for (int i = 0; i < wordsExcluded.length; i++) {
            excludedWordList = mainDocTrie.returnList(mainDocContainer,
                    wordsExcluded[i]);

            for (Pair<Integer, Integer> pair : excludedWordList) {
                finalExcludedWordList.insertAtBack(pair);
            }
        }

        /*
        If a pair from finalExcludedWordList is present in the
        requiredWordList's copy then remove it.
         */
        for (Pair<Integer, Integer> pair1 : requiredWordListCopy) {
            for (Pair<Integer, Integer> pair2 : finalExcludedWordList) {

                if (pair1.getLeftValue() == pair2.getLeftValue()) {
                    requiredWordListCopy.remove(pair1);
                }
            }
        }

        for (Pair<Integer, Integer> pair : requiredWordListCopy) {
            returnList.add(pair.getLeftValue());
        }
        return returnList;
    }

    /**
     * Searches the document for sections that contain all the words in the 'words' parameter.
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param words Array of words to find within a defined section in the document.
     * @return  List of triples, where each triple indicates the line and column number and word
     *          found, for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the
     *         document, or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or the Strings in either of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer, Integer, String>> simpleAndSearch(String[] titles, String[] words)
            throws IllegalArgumentException {

        MyLinkedList<Pair<Integer,Integer>> sectionNumberList = new MyLinkedList<>();
        
        MyLinkedList<Triple<Integer,Integer,String>> firstWordList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> helperList = new MyLinkedList<>();
        
        List<Triple<Integer,Integer,String>> returnList = new ArrayList<>();

        /* The following list now contains Pairs of all locations of sections of the titles
        provided.
         */
        sectionNumberList = indexManager(titles,indexList,lineNumber);

        firstWordList = mainDocTrie.returnTripleList(mainDocContainer,words[0]);
        for (int i = 1; i < words.length; i++) {

            helperList = mainDocTrie.returnTripleList(mainDocContainer,words[i]);
            /* extractAndTriples is essentially andMatcher but for Triples */
            firstWordList = wordMatcher.extractAndTriples(firstWordList,helperList,sectionNumberList);

        }

        returnList = tripleListConverter(firstWordList);
        return returnList;
    }

    /**
     * Searches the document for sections that contain any of the words in the 'words' parameter.
     * Implements simple "or" logic when searching for the words.
     * The words do not need to be on the same lines.
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param words Array of words to find within a defined section in the document.
     * @return  List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if words is null or an empty array
     *                                  or any of the Strings in either of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer, Integer, String>> simpleOrSearch(String[] titles, String[] words)
            throws IllegalArgumentException {

        MyLinkedList<Pair<Integer,Integer>> sectionNumberList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer, Integer, String>> tempTripleList = new MyLinkedList<>();
        MyLinkedList<Pair<Integer,Integer>> wordList;
        MyLinkedList<Triple<Integer, Integer, String>> finalTripleList = new MyLinkedList<>();
        List<Triple<Integer,Integer,String>> returnList = new ArrayList<>();

        for (String word : words) {
            if (word.isEmpty() || word == null) {
                throw new IllegalArgumentException();
            }
        }
        for (String title: titles) {
            if (title.length() < 1 || title == null) {
                throw new IllegalArgumentException();
            }
        }

         /* The following list now contains Pairs of all locations of sections of the titles
        provided.
         */
        sectionNumberList = indexManager(titles,indexList,lineNumber);

        for (int j = 0; j < words.length; j++) {

            wordList = mainDocTrie.returnList(mainDocContainer, words[j]);
            tempTripleList = wordMatcher.extractOrTriples(wordList, sectionNumberList, words[j]);

            for (Triple<Integer, Integer, String> triple : tempTripleList) {
                finalTripleList.insertAtBack(triple);
            }
        }

        returnList = tripleListConverter(finalTripleList);
        return returnList;
    }

    /**
     * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
     * and none of the words in the 'wordsExcluded' parameter.
     * Implements simple "not" logic when searching for the words.
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param wordsRequired Array of words to find within a defined section in the document.
     * @param wordsExcluded Array of words that must not be in the same section as 'wordsRequired'.
     * @return List of triples, where each triple indicates the line and column number and word found,
     *         for each occurrence of one of the required words.
     *         Returns an empty list if the words are not found in the indicated sections of the document,
     *         or all the indicated sections are not part of the document.
     * @throws IllegalArgumentException if wordsRequired is null or an empty array
     *                                  or any of the Strings in any of the arrays are null or empty.
     */
    @Override
    public List<Triple<Integer, Integer, String>> simpleNotSearch(String[] titles, String[]
            wordsRequired, String[] wordsExcluded) throws IllegalArgumentException {

        MyLinkedList<Pair<Integer,Integer>> sectionNumberList = new MyLinkedList<>();

        MyLinkedList<Triple<Integer,Integer,String>> wordList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> helperList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> wordListCopy = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> excludedWordList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> finalExcludedWordList = new MyLinkedList<>();

        List<Triple<Integer,Integer,String>> returnList = new ArrayList<>();


        sectionNumberList = indexManager(titles,indexList,lineNumber);

        wordList = mainDocTrie.returnTripleList(mainDocContainer,wordsRequired[0]);
        for (int i = 1; i < wordsRequired.length; i++) {
            helperList = mainDocTrie.returnTripleList(mainDocContainer,wordsRequired[i]);
            wordList = wordMatcher.extractAndTriples(wordList,helperList,sectionNumberList);
        }

        for (Triple<Integer,Integer,String> triple : wordList) {
            wordListCopy.insertAtBack(triple);
        }

        for (int i = 0; i< wordsExcluded.length; i++) {
            excludedWordList = mainDocTrie.returnTripleList(mainDocContainer,wordsExcluded[i]);

            for (Triple<Integer,Integer,String> triple : excludedWordList) {
                finalExcludedWordList.insertAtBack(triple);
            }
        }

        for (Pair<Integer,Integer> section: sectionNumberList) {

            for (Triple<Integer, Integer, String> triple1 : wordListCopy) {

                for (Triple<Integer, Integer, String> triple2 : finalExcludedWordList) {

                    /* If the two words are in the same section, remove them */
                    if (wordMatcher.areInSameSection(section,triple1,triple2)) {
                        wordListCopy.remove(triple1);
                    }
                }
            }
        }

        returnList = tripleListConverter(wordListCopy);
        return returnList;
    }

    /**
     *
     * @param titles Array of titles of the sections to search within,
     *               the entire document is searched if titles is null or an empty array.
     * @param wordsRequired Array of words to find within a defined section in the document.
     * @param orWords Array of words, of which at least one, must be in the same section as 'wordsRequired'.
     * @return
     * @throws IllegalArgumentException
     */
    @Override
    public List<Triple<Integer, Integer, String>> compoundAndOrSearch(String[] titles, String[]
            wordsRequired, String[] orWords) throws IllegalArgumentException {

        List<Triple<Integer, Integer, String>> returnList = new ArrayList<>();
        MyLinkedList<Pair<Integer,Integer>> sectionNumberList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer, Integer, String>> tempAndList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer, Integer, String>> helperList = new MyLinkedList<>();

        MyLinkedList<Pair<Integer,Integer>> tempOrWordList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> anotherOrWordList = new MyLinkedList<>();
        MyLinkedList<Triple<Integer,Integer,String>> finalWordList = new MyLinkedList<>();

        MyLinkedList<Triple<Integer,Integer,String>> compoundList = new MyLinkedList<>();

        MyLinkedList<Triple<Integer, Integer, String>> helperList1 = new MyLinkedList<>();

        sectionNumberList = indexManager(titles,indexList,lineNumber);

        tempAndList = mainDocTrie.returnTripleList(mainDocContainer,wordsRequired[0]);

        for (int i = 1; i < wordsRequired.length; i++) {

            helperList1 = mainDocTrie.returnTripleList(mainDocContainer,wordsRequired[i]);
            tempAndList = wordMatcher.extractAndTriples(tempAndList,helperList,sectionNumberList);

        }

                    for (Triple<Integer,Integer,String> triple : tempAndList) {

                helperList1.insertAtBack(triple);
            }


        for (int i =0; i < orWords.length; i++) {

            tempOrWordList = mainDocTrie.returnList(mainDocContainer, orWords[i]);

            anotherOrWordList =wordMatcher.extractOrTriples(tempOrWordList, sectionNumberList,
                    orWords[i]);

//            for (Triple<Integer,Integer,String> triple : anotherOrWordList) {
//                System.out.println(triple.getLeftValue() + " " + triple.getCentreValue() + " " +
//                        triple.getRightValue());
//                returnList.add(triple);
//            }
            compoundList = wordMatcher.compoundMatcher(sectionNumberList, tempAndList,
                    anotherOrWordList);

            for (Triple<Integer, Integer, String> triple : compoundList) {
                finalWordList.insertAtBack(triple);
            }
            compoundList = new MyLinkedList<>();
        }

        for (Triple<Integer,Integer,String> triple : finalWordList) {
            System.out.println(triple.getLeftValue() + " " + triple.getCentreValue() + " " +
                    triple.getRightValue());
            returnList.add(triple);
        }

        return returnList;
    }

/****************************** HELPER FUNCTIONS **************************************************/

    /**
     * Index Manager which spits out a List of Indices of all the given titles. This method
     * stores the section number of each section inside a Pair, where the leftValue is the
     * starting line number and the rightValue is the ending line number.
     *
     * @param titles titles from the index for which sections
     * @param indexList the actual List of all indices
     * @param lineNumber the very last line number of the file, for when the last section is needed.
     * @return List of Pairs of Section Numbers.
     */
    private MyLinkedList<Pair<Integer,Integer>> indexManager(String[] titles,
                                                             MyLinkedList<Pair<String, Integer>> indexList,
                                                             Integer lineNumber) {

        MyLinkedList<Pair<Integer,Integer>> sectionNumberList = new MyLinkedList<>();


        for (int i = 0; i < titles.length; i++) {

            for (int j = 0; j< indexList.getSize()-1; j++) {

                if (j == indexList.getSize() -2) {
                    //Check if the last section is being asked for
                    if (titles[i].equals(indexList.get(j+1).getLeftValue())) {

                        Pair<Integer, Integer> pair = new Pair<>(indexList.get(j).getRightValue(),
                                lineNumber);
                        sectionNumberList.insertAtBack(pair);

                    }
                } else if (titles[i].equals(indexList.get(j).getLeftValue())) {

                        Pair<Integer, Integer> pair = new Pair<>(indexList.get(j).getRightValue(),
                                indexList.get(j + 1).getRightValue());

                    sectionNumberList.insertAtBack(pair);

                    }
                }
            }
        return sectionNumberList;
        }

    /**
     * Helper Function to convert the LinkedList of Pairs to a java.util.List of Pairs.
     * @param list List of Pairs that is to be converted
     * @return ArrayList of the same List of Pairs
     */
    private List<Pair<Integer,Integer>> pairListConverter(MyLinkedList<Pair<Integer,Integer>> list) {
        List<Pair<Integer,Integer>> returnList = new ArrayList<>();
        for(Pair<Integer,Integer> pair : list) {
            returnList.add(pair);
        }
        return returnList;
    }

    /**
     * Helper Function to convert the LinkedList of Triples to a java.util.List of Triples.
     * @param list List of Triples that is to be converted
     * @return ArrayList of the same List of Triples.
     */
    private List<Triple<Integer,Integer,String>> tripleListConverter(MyLinkedList<Triple<Integer,
            Integer,String>> list) {

        List<Triple<Integer,Integer,String>> returnList = new ArrayList<>();

        for(Triple<Integer,Integer,String> triple : list) {
            returnList.add(triple);
        }
        return returnList;
    }

    /**
     * Helper Function to convert the LinkedList of Integers to a java.util.List of Integers.
     * @param list List of Integers that is to be converted
     * @return ArrayList of the same List of Integers.
     */
    private List<Integer> listConverter(MyLinkedList<Integer> list) {

        List<Integer> returnList = new ArrayList<>();

        for (Integer integer : list) {
            returnList.add(integer);
        }
        return returnList;
    }

}
