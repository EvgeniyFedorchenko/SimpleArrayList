package com.evgeniyfedorchenko.simplearraylist.implementations;

import com.evgeniyfedorchenko.simplearraylist.interfaces.SimpleList;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class SimpleArrayList<E> implements SimpleList<E> {

    private Object[] innerArray;
    private static final int DEFAULT_CAPACITY = 10;
    private static final float boostRatio = 1.5F;
    private int size;

    public SimpleArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public SimpleArrayList(int initialCapacity) {
        size = 0;
        if (initialCapacity >= 0) {
            this.innerArray = new Object[initialCapacity];
        } else {
            throw new IllegalArgumentException();
        }
    }

    public SimpleArrayList(Collection<? extends E> sourceCollection) {
        size = sourceCollection.size();
        this.innerArray = Arrays.copyOf(sourceCollection.toArray(), sourceCollection.size());
    }

    @Override
    public E add(E item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (size < innerArray.length) {
            for (int i = 0; i < innerArray.length; i++) {
                if (innerArray[i] == null) {
                    innerArray[i] = item;
                    size++;
                    return item;
                }
            }
        }
        int oldSize = size;
        boostSize();
        innerArray[oldSize] = item;
        size++;
        return item;
    }

    private void boostSize() {
        Object[] newInnerArray = new Object[Math.round(size * boostRatio)];
        System.arraycopy(innerArray, 0, newInnerArray, 0, size);
        innerArray = newInnerArray;
    }

    @Override
    public E add(int index, E item) {
        if (item == null) {
            throw new NullPointerException();
        }
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        if (size < innerArray.length) {
            Object[] turnedEls = Arrays.copyOfRange(innerArray, index - 1, size - 1);
            innerArray[index] = item;
            System.arraycopy(turnedEls, 0, innerArray, index + 1, turnedEls.length);
            size++;
            return item;
        }
        boostSize();
        return add(index, item);
    }

    @Override
    public E set(int index, E item) {

        if (item == null) {
            throw new NullPointerException();
        } else if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E oldValue = getItem(index);
        innerArray[index] = item;
        return oldValue;
    }

    @Override
    public E remove(E item) {
        /* Так как оригинальный ArrayList удаляет только первое вхождение элемента,
           то будем тоже удалять только первое вхождение */
        if (item == null) {
            throw new NullPointerException();
        }
        int index = IntStream.range(0, size)
                .filter(i -> innerArray[i].equals(item))
                .findAny().orElseThrow(NoSuchElementException::new);
        return remove(index);
    }

    @Override
    public E remove(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        E item = getItem(index);
        System.arraycopy(innerArray, index + 1, innerArray, index, size - index);
        size--;
        return item;
    }

    @Override
    public boolean contains(E item) {
        return indexOf(item) >= 0;
    }

    @Override
    public int indexOf(E item) {
        if (item == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < size; i++) {
            if (innerArray[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E item) {
        if (item == null) {
            throw new NullPointerException();
        }
        for (int i = size - 1; i > 0; i--) {
            if (innerArray[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return getItem(index);
    }

    @Override
    public boolean equals(Object otherList) {
        if (this == otherList) {
            return true;
        }
        if (otherList == null || getClass() != otherList.getClass()) {
            return false;
        }

        SimpleArrayList<?> otherList1 = (SimpleArrayList<?>) otherList;
        if (size != otherList1.size()) {
            return false;
        }
        for (int i = 0; i < size - 1; i++) {
            if (!innerArray[i].equals(otherList1.get(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        for (Object element : innerArray) {
            if (element == null) {
                break;
            }
            element = null;
        }
        size = 0;
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(innerArray, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    private E getItem(int index) {
        return (E) innerArray[index];
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public E next() {
                index++;
                return getItem(index);
            }
        };
    }
}
