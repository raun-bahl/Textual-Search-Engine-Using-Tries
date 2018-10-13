package comp3506.assn2.utils;

/**
 * Represents a Node inside a LinkedList
 * @param <T> T object that the Node stores.
 */
public class Node<T> {

    // element's object
    T element;
    // reference to the next node
    Node link;

    public Node(T element) {
        this.link = null;
        this.element = element;
    }


    public Node(T element, Node n) {
        this.element = element;
        this.link = n;
    }

    public void setLink(Node n) {
        this.link = n;
    }

    public void setElement(T element) {
        this.element = element;
    }

    public Node getLink() {
        return this.link;
    }

    public T getElement() {
        return this.element;
    }
}
