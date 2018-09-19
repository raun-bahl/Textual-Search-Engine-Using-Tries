package comp3506.assn2.application;

import comp3506.assn2.utils.Trie;
import comp3506.assn2.utils.TrieContainer;

import java.io.*;

/**
 * Hook class used by automated testing tool.
 * The testing tool will instantiate an object of this class to test the functionality of your assignment.
 * You must implement the constructor stub below and override the methods from the Search interface
 * so that they call the necessary code in your application.
 * 
 * @author 
 */
public class AutoTester implements Search {

	BufferedReader docReader, indexReader, stopWordsReader;
	Trie stopWordsTrie;
	TrieContainer stopWordsContainer;

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
		stopWordsTrie = new Trie(stopWordsFileName);
		stopWordsContainer = new TrieContainer();
		try {
			try {
				docReader = new BufferedReader(new FileReader(documentFileName));
				indexReader = new BufferedReader(new FileReader(indexFileName));
				stopWordsReader = new BufferedReader(new FileReader(stopWordsFileName));
				System.out.println("Files loaded!");
				String word;
				while ((word = stopWordsReader.readLine()) != null) {
					stopWordsTrie.storeWords(stopWordsContainer,word);
				}
				stopWordsTrie.printWordStrings(stopWordsContainer,"");


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
