package ru.spliterash.spcore.structure.spLinkedlist;


import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * LinkedList который оставляет callback после добавления, чтобы можно было легко удалить элемент
 */
public class SPLinkedList<T> implements Iterable<T> {
    private Node first;
    private Node last;
    private int size = 0;

    public int size() {
        return size;
    }

    public LinkedListElement<T> add(T element) {
        Node node = new Node();
        node.value = element;
        if (last != null)
            last.next = node;
        node.prev = last;

        last = node;

        if (first == null)
            first = node;

        size++;

        return node;
    }

    public void remove(T toRemove) {
        Iterable<LinkedListElement<T>> iterable = this::elementIterator;

        for (LinkedListElement<T> element : iterable) {
            if (element.get().equals(toRemove)) {
                element.remove();
                break;
            }
        }
    }

    public boolean isEmpty() {
        return first == null;
    }


    @Override
    public ListIterator<T> iterator() {
        return new ListIterator<T>() {
            private int index = 0;
            private Node currentNode;

            @Override
            public boolean hasNext() {
                if (currentNode == null && first != null)
                    return true;
                else
                    return currentNode != null && currentNode.next != null;
            }

            @Override
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                if (currentNode == null) {
                    currentNode = first;
                } else {
                    currentNode = currentNode.next;
                    index++;
                }
                return currentNode.value;
            }

            @Override
            public boolean hasPrevious() {
                return currentNode != null && currentNode.prev != null;
            }

            @Override
            public T previous() {
                if (!hasPrevious())
                    throw new NoSuchElementException();
                currentNode = currentNode.prev;
                index--;

                return currentNode.value;
            }

            @Override
            public int nextIndex() {
                return index + 1;
            }

            @Override
            public int previousIndex() {
                return index - 1;
            }

            @Override
            public void remove() {
                // Мне лень это реализовывать
                throw new UnsupportedOperationException();
            }

            @Override
            public void set(T t) {
                currentNode.value = t;
            }

            @Override
            public void add(T t) {
                // Мне лень это реализовывать
                throw new UnsupportedOperationException();
            }
        };
    }

    public Iterator<LinkedListElement<T>> elementIterator() {
        return new Iterator<LinkedListElement<T>>() {
            private Node currentNode;

            @Override
            public boolean hasNext() {
                if (currentNode == null && first != null)
                    return true;
                else
                    return currentNode != null && currentNode.next != null;
            }

            @Override
            public LinkedListElement<T> next() {
                if (!hasNext())
                    throw new NoSuchElementException();

                if (currentNode == null) {
                    currentNode = first;
                } else {
                    currentNode = currentNode.next;
                }
                return currentNode;
            }
        };
    }

    public Optional<T> first() {
        return Optional.ofNullable(first).map(v -> v.value);
    }

    public Optional<T> last() {
        return Optional.ofNullable(last).map(v -> v.value);
    }

    public interface LinkedListElement<T> {
        // Удалить элемент
        void remove();

        T get();
    }

    private class Node implements LinkedListElement<T> {
        private T value;
        private boolean deleted = false;
        private Node prev;
        private Node next;

        @Override
        public void remove() {
            if (deleted)
                throw new IllegalArgumentException();

            if (prev == null)
                first = next;
            else
                prev.next = this.next;

            if (next == null)
                last = prev;
            else
                next.prev = this.prev;

            size--;

            prev = null;
            next = null;
            deleted = true;
        }

        @Override
        public T get() {
            return value;
        }
    }
}
