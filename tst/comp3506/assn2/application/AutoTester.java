package comp3506.assn2.application;

import comp3506.assn2.utils.Trie;
import comp3506.assn2.utils.TrieContainer;

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author 
 */
public class AutoTester implements Search {

//	BufferedReader docReader, indexReader, stopWordsReader;
	Scanner docReader, indexReader, stopWordsReader;
	File docFile, indexFile, stopWordsFile;
	Trie stopWordsTrie, mainDocTrie, indexTrie;
	TrieContainer stopWordsContainer, mainDocContainer, indexContainer;

	List<Integer> list;

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

		char[] ch = new char[100000];
		int index = 0;

		list = new ArrayList<Integer>();

		// TODO Implement constructor to load the data from these files and
		// TODO setup your data structures for the application.
		stopWordsTrie = new Trie();
		stopWordsContainer = new TrieContainer();
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
				 * Good for debugging
				 */
				while (stopWordsReader.hasNext()) {
					stopWordsTrie.storeWords(stopWordsContainer, stopWordsReader.next());
				}
//				stopWordsTrie.printWordStrings(stopWordsContainer,"");


//				while (indexReader.hasNext()) {
//					indexReader.useDelimiter(",");
//					if (indexReader.hasNextInt()) {
//						list.add(indexReader.nextInt());
//					}
//				}
//

				while (indexReader.hasNextLine()) {
					String line = indexReader.nextLine();
					String tokens[] = line.split(",");
					int number = Integer.parseInt(tokens[1]);
					list.add(number);
				}

				for (Integer i : list) {
					System.out.println(i);
				}

				indexReader.close();
				indexReader = null;

				indexReader = new Scanner(indexFile);


				System.out.println("\n\n");
				while (indexReader.hasNext()) {
					//indexReader.useDelimiter(",");
					String line = indexReader.nextLine().replaceAll("[0-9]","").replaceAll(",","");
					System.out.println(line);
				}




//				while (indexReader.hasNext()) {
//					for (int i = 0; i < indexReader.next().length(); i++) {
//						char c = indexReader.next().charAt(i);
//						if (Character.isLetter(c)) {
//							ch[index++] = c;
//						} else {
//							ch[index] = '\0';
//							index = 0;
//							String b = new String(ch);
//							indexTrie.storeWords(indexContainer, b);
//						}
//					}
//				}

//				while (indexReader.hasNext()) {
//					char c = indexReader.next().charAt(0);
//					if (Character.isLetter(c)) {
//						ch[index++] = c;
//					} else {
//						ch[index] = '\0';
//						index = 0;
//						indexTrie.storeWords(indexContainer, indexReader.next());
//					}
//				}

				//indexTrie.printWordStrings(indexContainer, "");

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
		AutoTester autoTester = new AutoTester("files/shakespeare.txt","files/shakespeare-index.txt","files/stop-words.txt");
	}

	@Override
	public int wordCount(String word) throws IllegalArgumentException {
		return 0;
	}
}
