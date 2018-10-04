package comp3506.assn2.utils;

public class Node<T> {

    T element;
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
