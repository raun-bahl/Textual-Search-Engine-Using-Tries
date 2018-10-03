package comp3506.assn2.application;

import comp3506.assn2.utils.MyMap;
import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Trie;
import comp3506.assn2.utils.TrieContainer;

import java.io.*;
import java.util.*;

/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author 
 */
public class AutoTester implements Search {

	Scanner docReader, indexReader, stopWordsReader;
	File docFile, indexFile, stopWordsFile;
	Trie stopWordsTrie, mainDocTrie, indexTrie;
	TrieContainer stopWordsContainer, mainDocContainer, indexContainer;

	/* Data Structures for the inverted index */
	Pair<Integer,Integer> pair;
	List<Pair<Integer,Integer>> innerList;
	List<List<Pair<Integer,Integer>>> outerList;
	List<TrieContainer> wordList = new ArrayList<>();

	public int lineNumber=1, columnNumber=1;

	MyMap<List<TrieContainer>, List<List<Pair<Integer,Integer>>>> invertedIndex;

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

		invertedIndex = new MyMap<List<TrieContainer>,List<List<Pair<Integer,Integer>>>>();

		/* Trie steup */
		stopWordsTrie = new Trie();
		indexTrie = new Trie();
		mainDocTrie = new Trie();

		/* TrieContainer Setup */
		stopWordsContainer = new TrieContainer();
		indexContainer = new TrieContainer();
		mainDocContainer = new TrieContainer();
		TrieContainer temppp = new TrieContainer();

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
							.replaceAll("[0-9]","")
							.replaceAll(",","");
					String[] words = line.split("\\s+");

					for (String word: words) {
						indexTrie.storeWords(indexContainer,word);
					}
				}

				/*
				Temporary data structures for the inverted index.
				 */
				innerList = new ArrayList<>();
				outerList = new ArrayList<>();

				int index = 0;
				/*
				Main Document File Parsing and Indexing
				 */
				while (docReader.hasNext()) {
					columnNumber = 1;
					String docLine = docReader.nextLine();
					//Replace all empty lines
					if (docLine.isEmpty()) {
						docLine = docLine.
								replaceAll("(?m)^[ \\t]*\\r?\\n",
										"");
					} else {
						//Removing punctuation, numbers etc.
						String l = docLine
								.replaceAll("[^a-zA-Z\\']",
										" ").toLowerCase()
								.replaceAll("( )+", " ")
								.replaceAll("\\d","");
						//store individual words in the line in an array
						String[] words = l.split(" ");

						for (String word: words) {

							pair = new Pair<>(0,0);
							pair.setLeftValue(lineNumber);
							pair.setRightValue(columnNumber);
							mainDocTrie.storeWords(mainDocContainer,word);
							columnNumber += word.length()+1;

							if (mainDocTrie.isWordPresent(mainDocContainer,word)) {
								innerList.add(pair);
								outerList.add(innerList);
							} else {
								innerList = new ArrayList<>();
								innerList.add(pair);
								outerList.add(innerList);
							}

							System.out.println(pair.getLeftValue() + " " + pair.getRightValue());
							innerList.add(pair);
							outerList.add(innerList);

							/* Might be useful? */
							//temppp=mainDocTrie.storeWords(mainDocContainer, word);
							//wordList.add(temppp);
						}

//						while (index< l.length()) {
//							if (Character.isLetter(l.charAt(index))) {
//								sb.append(l.charAt(index));
//								columnNumber++;
//							} else if (l.charAt(index) == ' ') {
//								System.out.println(sb);
//								columnNumber++;
////								temppp=mainDocTrie.storeWords(mainDocContainer,sb.toString());
//							}
//							//columnNumber++;
//							index++;
//						}
					}
					lineNumber++;
					}
				//System.out.println(columnNumber);


				invertedIndex.put(wordList,outerList);
				//invertedIndex.display();


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
				"shakespeare.txt","files/shakespeare-index.txt",
				"files/stop-words" +
				".txt");
	}

	@Override
	public int wordCount(String word) throws IllegalArgumentException {

		int result = 0;

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
