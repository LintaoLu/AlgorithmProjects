import edu.princeton.cs.algs4.StdRandom;
import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private final DynamicArray<Item> container;

    // construct an empty randomized queue
    public RandomizedQueue() {
        container = new DynamicArray<>();
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return container.size() == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return container.size();
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        container.add(item);
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniform(container.size());
        Item item = container.get(random);
        swap(random, container.size() - 1);
        container.remove();
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new java.util.NoSuchElementException();
        int random = StdRandom.uniform(container.size());
        return container.get(random);
    }

    // swap two elements
    private void swap(int i, int j) {
        Item temp = container.get(i);
        container.set(i, container.get(j));
        container.set(j, temp);
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return container.iterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<>();
        for (int i = 1; i <= 100; i++) queue.enqueue(i);
        for (int i = 1; i <= 100; i++) System.out.println(queue.dequeue());
    }

    private static class DynamicArray<Item> implements Iterable<Item> {

        private int index;
        private Item[] buffer;

        public DynamicArray() {
            index = -1;
            buffer = (Item[]) new Object[1000];
        }

        public Item get(int pos) {
            if (index < 0 || pos > index) {
                throw new IllegalArgumentException();
            }
            return buffer[pos];
        }

        public void add(Item item) {
            if (size() >= buffer.length / 2) expandBuffer();
            buffer[++index] = item;
        }

        private void expandBuffer() {
            int capacity = buffer.length;
            Item[] temp = (Item[]) new Object[capacity * 2];
            System.arraycopy(buffer, 0, temp, 0, capacity);
            buffer = temp;
        }

        public int size() {
            return index + 1;
        }

        public void set(int pos, Item item) {
            if (index < 0 || pos > index) {
                throw new IllegalArgumentException();
            }
            buffer[pos] = item;
        }

        public void remove() {
            if (index < 0) {
                throw new IllegalArgumentException();
            }
            index--;
        }

        @Override
        public Iterator<Item> iterator() {
            return new DynamicArrayIterator<>(this);
        }

        private static class DynamicArrayIterator<Item> implements Iterator<Item> {

            private final DynamicArray<Item> dynamicArray;
            private int curr;
            private final int[] indices;

            DynamicArrayIterator(DynamicArray<Item> dynamicArray) {
                this.dynamicArray = dynamicArray;
                indices = StdRandom.permutation(dynamicArray.size());
            }

            @Override
            public boolean hasNext() {
                return curr < indices.length;
            }

            @Override
            public Item next() {
                if (!hasNext()) throw new java.util.NoSuchElementException();
                return dynamicArray.get(indices[curr++]);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        }
    }
}