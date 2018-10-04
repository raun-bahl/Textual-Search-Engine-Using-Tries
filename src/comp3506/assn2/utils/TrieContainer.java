package comp3506.assn2.utils;

public class TrieContainer {

    public boolean isEnd;
    char ch;
    public TrieContainer[] series = new TrieContainer[27];

    public TrieContainer() {
        isEnd = false;

        for (int i = 0; i < 27; i++) {
            series[i] = null;
        }
    }

    public char convertToChar(TrieContainer t) {
        for (int i =0; i< t.series.length; i++) {
            TrieContainer n = t.series[i];
            if (n !=  null) {
                System.out.println(97+i);
                ch = ((char)(97+i));
            } else {
            }
        }
        return ch;
    }
}
