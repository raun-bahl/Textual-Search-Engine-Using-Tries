package comp3506.assn2.utils;

/**
 * Word Matcher Class.
 * Helps matches words in different contexts using their location lists.
 * Supports AND,OR,NOT,COMPOUND and CONTIGUOUS word matching.
 */
public class WordMatcher {

    public WordMatcher() {
    }

    public MyLinkedList<Pair<Integer, Integer>> contiguousWordMatcher(int length,
                                                                       MyLinkedList<Pair<Integer,Integer>>
                                                                               list1,
                                                                       MyLinkedList<Pair<Integer, Integer>>
                                                                               list2) {

        MyLinkedList<Pair<Integer, Integer>> list = new MyLinkedList<>();

        for (Pair<Integer, Integer> wordPair2 : list2) {

            for (Pair<Integer, Integer> wordPair1 : list1) {

                if (wordPair2.getRightValue().equals(wordPair1.getRightValue
                        () + length + 1) && wordPair2.getLeftValue().equals
                        (wordPair1.getLeftValue())) {

                    Pair<Integer, Integer> firstWordPair = new Pair<>
                            (wordPair1.getLeftValue(),
                                    wordPair1.getRightValue());
                    list.insertAtBack(firstWordPair);

                }
            }
        }
        return list;
    }

    public MyLinkedList<Pair<Integer, Integer>> andMatcher(MyLinkedList<Pair<Integer, Integer>> list1,
             MyLinkedList<Pair<Integer, Integer>> list2) {

        MyLinkedList<Pair<Integer, Integer>> list = new MyLinkedList<>();

        for (Pair<Integer, Integer> wordPair2 : list2) {

            for (Pair<Integer, Integer> wordPair1 : list1) {

                if (wordPair2.getLeftValue().equals(wordPair1.getLeftValue())) {

                    Pair<Integer, Integer> pair = new Pair<>
                            (wordPair1.getLeftValue(), wordPair2.getRightValue());
                    list.insertAtBack(pair);
                }
            }
        }
        return list;
    }

    public MyLinkedList<Triple<Integer,Integer,String>> extractOrTriples(MyLinkedList<Pair<Integer,
            Integer>>
                                                                                  wordList,
                                                                          MyLinkedList<Pair<Integer,Integer>>
                                                                                  sectionList,
                                                                          String word) {

        MyLinkedList<Triple<Integer,Integer,String>> tripleList = new MyLinkedList<>();

        for (Pair<Integer, Integer> pair1 : sectionList) {

            for (Pair<Integer, Integer> pair : wordList) {

                if (pair.getLeftValue() > pair1.getLeftValue() && pair.getLeftValue() <
                        pair1.getRightValue()) {

                    Triple triple = new Triple<>(pair.getLeftValue(), pair.getRightValue(), word);
                    tripleList.insertAtBack(triple);

                }
            }
        }
        return tripleList;
    }

    public MyLinkedList<Triple<Integer, Integer, String >> extractAndTriples
            (MyLinkedList<Triple<Integer,Integer, String>> list1, MyLinkedList<Triple<Integer,
                    Integer, String>> list2,
             MyLinkedList<Pair<Integer,Integer>> sectionList) {

        MyLinkedList<Triple<Integer,Integer,String>> tripleList = new MyLinkedList<>();

        Triple<Integer,Integer,String> triple1 = new Triple<>(0,0,"");
        Triple<Integer,Integer,String> triple2 = new Triple<>(0,0,"");




        for (int k = 0; k < sectionList.getSize()-1; k++) {

            for (int i = 0, j = 0; i < list1.getSize()-1 && j < list2.getSize()-1; i++,j++) {


                if ((list1.get(i).getLeftValue() > sectionList.get(k).getLeftValue() && list1.get(i)
                        .getLeftValue() < sectionList.get(k).getRightValue())) {

                    triple1 = new Triple<>(list1.get(i).getLeftValue
                            (), list1.get(i).getCentreValue(), list1.get(i).getRightValue());

                }
                if(list2.get(j)
                        .getLeftValue()
                        > sectionList.get(k).getLeftValue() && list2.get(j).getLeftValue() <
                        sectionList.get(k).getRightValue()) {

                    triple2 = new Triple<>(list2.get(j).getLeftValue
                            (), list2.get(j).getCentreValue(), list2.get(j).getRightValue());
                }
            }

            if ((triple1.getLeftValue() > sectionList.get(k).getLeftValue() &&
                    triple1.getCentreValue() < sectionList.get(k).getRightValue()) &&
                    (triple2.getLeftValue()> sectionList.get(k).getLeftValue() &&
                            triple2.getCentreValue() < sectionList.get(k).getRightValue())) {
                tripleList.insertAtBack(triple1);
                tripleList.insertAtBack(triple2);
            }


        }
        return tripleList;
    }

    public MyLinkedList<Triple<Integer, Integer, String >> compoundMatcher
            (MyLinkedList<Pair<Integer,
                    Integer>> sectionList, MyLinkedList<Triple<Integer,Integer,String>> list1,
             MyLinkedList<Triple<Integer,
                     Integer, String>> list2 ) {

        MyLinkedList<Triple<Integer,Integer,String>> tripleList = new MyLinkedList<>();
        Triple<Integer,Integer,String> triple2 = new Triple<>(0,0,"");
        Triple<Integer,Integer,String> triple1 = new Triple<>(0,0,"");

        for (int k = 0; k < sectionList.getSize(); k++) {

            for (int i = 0, j = 0; i < list1.getSize()-1 && j < list2.getSize()-1; i++, j++) {

                System.out.println("List1:" + list1.get(i).getLeftValue());
                System.out.println("List2:" + list2.get(j).getLeftValue());

                if ((list1.get(i).getLeftValue() > sectionList.get(k).getLeftValue() && list1.get(i)
                        .getLeftValue() < sectionList.get(k).getRightValue())) {

                    triple1 = new Triple<>(list1.get(i).getLeftValue
                            (), list1.get(i).getCentreValue(), list1.get(i).getRightValue());

                }
                if(list2.get(j)
                        .getLeftValue()
                        > sectionList.get(k).getLeftValue() && list2.get(j).getLeftValue() <
                        sectionList.get(k).getRightValue()) {


                    System.out.println("List2:" + list2.get(j).getLeftValue());

                    triple2 = new Triple<>(list2.get(j).getLeftValue
                            (), list2.get(j).getCentreValue(), list2.get(j).getRightValue());
                }
            }

            if ((triple1.getLeftValue() > sectionList.get(k).getLeftValue() &&
                    triple1.getCentreValue() < sectionList.get(k).getRightValue()) &&
                    (triple2.getLeftValue()> sectionList.get(k).getLeftValue() &&
                            triple2.getCentreValue() < sectionList.get(k).getRightValue())) {
                tripleList.insertAtBack(triple1);
                tripleList.insertAtBack(triple2);
            }
        }
        return tripleList;
    }

    public boolean areInSameSection(Pair<Integer,Integer> sectionList,
                                     Triple<Integer,Integer,String> list1,
                                     Triple<Integer,Integer,String> list2) {

        if (list1.getLeftValue() > sectionList.getLeftValue() && list1.getLeftValue() <
                sectionList.getRightValue() && list2.getLeftValue() > sectionList.getLeftValue() &&
                list2.getLeftValue() < sectionList.getRightValue()) {
            return true;
        }
        return false;
    }
}
