package com.evgeniyfedorchenko.simplearraylist.implementations;

import com.evgeniyfedorchenko.simplearraylist.interfaces.StringList;

import java.util.Arrays;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

public class StringArrayList implements StringList {

    private String[] innerArray;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;

    public StringArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public StringArrayList(int initialCapacity) {
        size = 0;
        this.innerArray = new String[initialCapacity];
    }

    public StringArrayList(Collection<String> sourceCollection) {
        size = sourceCollection.size();
        this.innerArray = sourceCollection.toArray(new String[0]);
    }

    @Override
    public String add(String item) {
        String[] local = innerArray;

        if (size < local.length) {
            for (int i = 0; i < local.length; i++) {
                if (local[i] == null) {
                    local[i] = item;
                    size++;
                    return item;
                }
            }
        }
        int oldSize = size;
        boostSize();
        local[oldSize - 1] = item;
        return item;
    }

    private void boostSize() {
        String[] newInnerArray = new String[size + size / 2];
        System.arraycopy(innerArray, 0, newInnerArray, 0, size);
        innerArray = newInnerArray;
    }

    @Override
    public String add(int index, String item) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        String[] local = innerArray;
        if (size < local.length) {
            String[] turnedEls = Arrays.copyOfRange(local, index - 1, size - 1);
            local[index] = item;
            System.arraycopy(turnedEls, 0, local, index + 1, turnedEls.length);
            size++;
            return item;
        }
        boostSize();
        return add(index, item);
    }

    @Override
    public String set(int index, String item) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        String oldElement = innerArray[index];
        innerArray[index] = item;
        return oldElement;

    }

    @Override
    public String remove(String item) {
        /* Так как оригинальный ArrayList удаляет только первое вхождение элемента,
           то будем тоже удалять только первое вхождение */

        int index = IntStream.range(0, size)
                .filter(i -> innerArray[i].equals(item))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
        return remove(index);
    }

    @Override
    public String remove(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        String item = innerArray[index];
        System.arraycopy(innerArray, index + 1, innerArray, index, size - index);
        size--;
        return item;
    }

    @Override
    public boolean contains(String item) {
        return indexOf(item) >= 0;
    }

    @Override
    public int indexOf(String item) {
        for (int i = 0; i < size - 1; i++) {
            if (innerArray[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(String item) {
        for (int i = size - 1; i > 0; i--) {
            if (innerArray[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public String get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }
        return innerArray[index];
    }

    @Override
    public boolean equals(StringList otherList) {
        if (this == otherList) {
            return true;
        }
        if (otherList == null || getClass() != otherList.getClass() || size != otherList.size()) {
            return false;
        }
        for (int i = 0; i < size - 1; i++) {
            if (!innerArray[i].equals(otherList.get(i))) {
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
        for (String element : innerArray) {
            if (element == null) {
                break;
            }
            element = null;
        }
        size = 0;
    }

    @Override
    public String[] toArray() {
        return Arrays.copyOf(innerArray, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }
}
