package comp3506.assn2.utils;

public class TrieContainer {

    public boolean isEnd;
    public TrieContainer[] series = new TrieContainer[26];

    public TrieContainer() {
        isEnd = false;

        for (int i =0; i< 26; i++) {
            series[i] = null;
        }
    }
}
