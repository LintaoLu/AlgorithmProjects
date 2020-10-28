import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first, last;
    private int size;

    // construct an empty deque
    public Deque() {
        size = 0;
        first = null;
        last = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> node = new Node<>(item);
        if (first == null && last == null) {
            first = node;
            last = node;
        } else {
            node.next = first;
            if (first != null) first.pre = node;
            first = node;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> node = new Node<>(item);
        if (first == null && last == null) {
            first = node;
            last = node;
        } else {
            last.next = node;
            node.pre = last;
            last = node;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (first == null) throw new java.util.NoSuchElementException();
        Item item = first.getItem();
        if (first == last) {
            first = null;
            last = null;
        } else {
            first = first.next;
            first.pre = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (last == null) throw new java.util.NoSuchElementException();
        Item item = last.getItem();
        if (first == last) {
            first = null;
            last = null;
        } else {
            last = last.pre;
            last.next = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<>(first);
    }

    private static class DequeIterator<Item> implements Iterator<Item> {

        private Node<Item> curr;

        DequeIterator(Node<Item> curr) {
            this.curr = curr;
        }

        @Override
        public boolean hasNext() {
            return curr != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new java.util.NoSuchElementException();
            Item item = curr.getItem();
            curr = curr.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class Node<Item> {

        Node<Item> pre, next;
        private final Item item;

        Node(Item item) {
            this.item = item;
        }

        public Item getItem() {
            return item;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        for (int i = 0; i < 100; i++) deque.addFirst(i);
        for (int i = 0; i < 100; i++) deque.removeLast();
    }
}
