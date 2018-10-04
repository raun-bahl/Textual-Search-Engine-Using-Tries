package comp3506.assn2.utils;

public class MyLinkedList<T> {

    Node start;
    Node end;
    int size = 0;

    public MyLinkedList() {
        start = null;
        end = null;
        size = 0;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isEmpty() {
        return start == null;
    }

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

    public void insertAtPosition(T element, int position) {
        Node nodePointer = new Node(element, null);
        Node pointer = start;
        position -= 1;

        for (int i=1; i<size; i++) {
            if (i == position) {
                Node temp = pointer.getLink();
                pointer.setLink(nodePointer);
                nodePointer.setLink(temp);
                break;
            }
            pointer = pointer.getLink();
        }
        size++;
    }

    public void deleteAtPosition(int pos) {
        if (pos == 1)
        {
            start = start.getLink();
            size--;
            return ;
        }
        if (pos == size)
        {
            Node s = start;
            Node t = start;
            while (s != end)
            {
                t = s;
                s = s.getLink();
            }
            end = t;
            end.setLink(null);
            size --;
            return;
        }
        Node ptr = start;
        pos = pos - 1 ;
        for (int i = 1; i < size - 1; i++)
        {
            if (i == pos)
            {
                Node tmp = ptr.getLink();
                tmp = tmp.getLink();
                ptr.setLink(tmp);
                break;
            }
            ptr = ptr.getLink();
        }
        size-- ;
    }

    public void display() {

        if (size == 0) {
            System.out.println("Empty LinkedList\n");
        }
        if (start.getLink() == null) {
            System.out.println(start.getElement());
        }
        Node pointer = start;
        System.out.println(start.getElement() + "->");
        pointer = start.getLink();
        while (pointer.getLink() != null) {
            System.out.println(pointer.getElement() + "->");
            pointer = pointer.getLink();
        }
        System.out.println(pointer.getElement()+ "->");
    }
}
