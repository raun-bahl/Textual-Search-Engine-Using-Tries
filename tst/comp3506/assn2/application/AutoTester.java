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
	Pair<Integer,Integer> pair;
	MyLinkedList<Pair<Integer,Integer>> innerList;
	MyLinkedList<HashMap<TrieContainer[],MyLinkedList<Pair<Integer,Integer>>>> outerList;
	List<TrieContainer> wordList = new ArrayList<>();

	MyLinkedList<HashMap<TrieContainer[],MyLinkedList<Pair<Integer,Integer>>>> indexer;

	public int lineNumber=1, columnNumber;

	MyMap<List<TrieContainer>, MyLinkedList<List<Pair<Integer,Integer>>>> invertedIndex;

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

		
		indexer = new MyLinkedList<HashMap<TrieContainer[], MyLinkedList<Pair<Integer,Integer>>>>();


		/* Trie steup */
		stopWordsTrie = new Trie();
		indexTrie = new Trie();
		mainDocTrie = new Trie();

		HashMap<TrieContainer[],MyLinkedList<Pair<Integer,Integer>>> myMap = new HashMap<>();

		/* TrieContainer Setup */
		mainDocContainer = new TrieContainer();
		stopWordsContainer = new TrieContainer();
		indexContainer = new TrieContainer();
		TrieContainer[] wordObject;

		docFile  = new File(documentFileName);
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
					stopWordsTrie.storeWords(stopWordsContainer, stopWordsReader.next());
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
						indexTrie.storeWords(indexContainer, word);
					}
				}

				/*
				Temporary data structures for the inverted index.
				 */
				innerList = new MyLinkedList<>();
				outerList = new MyLinkedList<>();

				int index = 0;
				/*
				Main Document File Parsing and Indexing
				 */
				while (docReader.hasNext()) {
					columnNumber = 1;
					String docLine = docReader.nextLine();
					//Replace all empty lines
					if (docLine.isEmpty()) {
						docLine = docLine
								.replaceAll("(?m)^[ \\t]*\\r?\\n",
										"");
					} else {
						//Regex Lookaround Split below
						//(?<=' ')
						if (!docLine.trim().equals(docLine)) {

							int count = docLine.indexOf(docLine.trim());
							columnNumber = count+1;
							docLine=docLine.trim();
						}

							String[] words = docLine.split(" ");
//							System.out.println("Here we go, boy.");
							for (String word : words) {

                                wordObject = new TrieContainer[word.length()];


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


                                pair = new Pair<>(0, 0);
                                pair.setLeftValue(lineNumber);
                                pair.setRightValue(columnNumber);

                                //System.out.println(pair.getLeftValue() + " " + pair.getRightValue());


                                String putWord = word
                                        .replaceAll("[^a-zA-Z\\']",
                                                "").toLowerCase();

                                //System.out.println(putWord);
                                /*Should I be calling this twice? */
                                //mainDocTrie.storeWords(mainDocContainer, putWord);
                                wordObject = mainDocTrie.storeWords(mainDocContainer, putWord);


                                //TODO: Figure out the Putting Logic Tomorrow
                                //TODO: Debug getStringWord


                                //code is fine till here
                                String stringWord = mainDocTrie.getStringWord(wordObject);
                                //this might be faulty
                                mainDocTrie.printWordStrings(mainDocContainer, "");
								//System.out.println(stringWord);
                                columnNumber += putWord.length() + 1;


                                    if (mainDocTrie.isWordPresent(mainDocContainer, putWord)) {
                                        innerList.insertAtFront(pair);
                                        myMap.put(wordObject, innerList);
                                        outerList.insertAtFront(myMap);
                                    } else {
										innerList = new MyLinkedList<>();
										innerList.insertAtFront(pair);
										myMap.put(wordObject, innerList);
										outerList.insertAtFront(myMap);
									}


							}

					}

					//System.out.println("We're out");






//						//Removing punctuation, numbers etc.
//						//store individual words in the line in an array
//						String[] words = l.split(" ");

//						for (String word: words) {
//
//							pair = new Pair<>(0,0);
//							pair.setLeftValue(lineNumber);
//							pair.setRightValue(columnNumber);
//							mainDocTrie.storeWords(mainDocContainer,word);
//							columnNumber += word.length()+1;
//

//
//							System.out.println(pair.getLeftValue() + " " + pair.getRightValue());
//							innerList.add(pair);
//							outerList.add(innerList);
//
//							/* Might be useful? */
//							//wordObject=mainDocTrie.storeWords(mainDocContainer, word);
//							//wordList.add(wordObject);
//						}

//						while (index< l.length()) {
//							if (Character.isLetter(l.charAt(index))) {
//								sb.append(l.charAt(index));
//								columnNumber++;
//							} else if (l.charAt(index) == ' ') {
//								System.out.println(sb);
//								columnNumber++;
////								wordObject=mainDocTrie.storeWords(mainDocContainer,sb.toString());
//							}
//							//columnNumber++;
//							index++;
//						}
			lineNumber++;
				}
				//mainDocTrie.printWordStrings(mainDocContainer,"");
				//System.out.println(columnNumber);
				//mainDocTrie.getWord(mainDocContainer);

				//invertedIndex.display();

				//invertedIndex.display();
				int result = wordCount("my");
				System.out.println(result);


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
				"random.txt","files/shakespeare-index.txt",
				"files/stop-words" +
				".txt");
	}


	@Override
	public int wordCount(String word) throws IllegalArgumentException {

		int result = 0;

		for (HashMap<TrieContainer[], MyLinkedList<Pair<Integer,Integer>>> hm: outerList) {

			for (TrieContainer[] t: hm.keySet()) {


				//GetStringWord needs to be fixed for this.
				if (word.equals(mainDocTrie.getStringWord(t))) {

					for (MyLinkedList<Pair<Integer,Integer>> list : hm.values()) {

//						System.out.println(result);
						result = list.getSize();

					}
				}
			}
		}

//		if (mainDocTrie.isWordPresent(mainDocContainer,word)) {
//			for (iterator: wordList) {
//
//			}


		/* Faulty code below */
//		if (indexContainer.isEnd) {
//			result++;
//		}
//
//		for (int i = 0; i< 26; i++) {
//			if (indexContainer.series[i] != null && indexContainer.series[i].equals(word)) {
////				result += wordCount(word,indexContainer.series[i]);
//			}
//		}
		return result;
	}


}
