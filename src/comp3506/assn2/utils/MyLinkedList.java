package comp3506.assn2.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Personal implementation of a LinkedList which can be iterated over.
 * @param <T> Object that the linked list will hold.
 */
public class MyLinkedList<T> implements Iterable<T> {

    Node start;
    Node end;
    int size = 0;

    public MyLinkedList() {
        start = null;
        end = null;
        size = 0;
    }

    /**
     * Iterator for the LinkedList.
     * @param <T>
     */
    private class ListIterator<T> implements java.util.Iterator<T>{

        //Reference to the current node
        private Node current;

        /**
         * Default constructor
         * Run-time complexity: O(1)
         */
        private ListIterator() {
            current = start;
        }

        /**
         * Gets the next element.
         * Run-time complexity: O(1)
         *
         * @return <T>, the element
         *
         *
         */
        public T next() {
            if (current == null) {
                throw new NoSuchElementException("No such element exists.");
            } else {
                T temp = (T) current.getElement();
                current = current.getLink();
                return temp;
            }
        }

        /**
         * Checks if there is a next element in the data structure that
         * it's iterating over.
         * Run-time complexity: O(1)
         *
         * @return  true if a reference exists
         *          false if there is no reference to the next node
         */
        public boolean hasNext() {
            return current != null;
        }
    }

    /**
     * Iterator object
     * Run-time complexity: O(1)
     *
     * @return the iterating object
     */
   //Does making this not an override cause any problemos?
    @Override
    public java.util.Iterator<T> iterator() {
        return new ListIterator();

    }

    /**
     * Returns the size of the LinkedList
     * @return size of the linked list
     */
    public int getSize() {
        return this.size;
    }

    /**
     *  Checks whether the linked list is empty or not
     * @return  true if it's empty, false otherwise.
     */

    public boolean isEmpty() {
        return start == null;
    }

    /**
     * Insert element at the start of the LinkedList.
     * @param element object to be inserted
     */
    public void insertAtFront(T element) {
        Node nodePointer = new Node(element,null);
        size++;

        if (start == null) {
            start = nodePointer;
            end = start;
        } else {
            nodePointer.setLink(start);
            start = nodePointer;
        }
    }

    public void insertAtBack(T element) {

        Node nodePointer = new Node(element, null);
        size++;
        if (start == null) {
            start = nodePointer;
            end = start;
        } else {
            end.setLink(nodePointer);
            end = nodePointer;
        }
    }

    /**
     * Checks if the LinkedList contains the element or not.
     * @param element object that is to be checked
     * @return true if the object is in the linked list, false otherwise
     */
    public boolean contains(T element) {
        if (start == null) {
            return false;
        }

        Node<T> node = start;

        while (node != null) {
            if (node.getElement().equals(element)) {
                return true;
            }
            node = node.getLink();
        }
        return false;
    }

    /**
     * Gets the element object at a specified index.
     * @param index the index of the element to be found
     * @return the element object itself
     */
    public T get(int index) {

        if (index<0) {
            return null;
        }

        Node<T> temp = start;
        for (int i =0; i< index; i++) {
            temp = temp.link;
        }

        return temp.getElement();
    }

    /**
     * Removes the provided element from the LinkedList
     * @param element object to be removed.
     */
    public void remove(T element) {
        Node<T> currentNode = start;
        Node<T> previousNode = null;

        while (currentNode != null) {
            if (element.equals(currentNode.getElement())) {
                if (previousNode == null) {
                    start = currentNode.link;
                } else {
                    previousNode.link = currentNode.link;
                }
                size--;
            } else {
                previousNode = currentNode;
            }
            currentNode = currentNode.link;
        }
    }
}

