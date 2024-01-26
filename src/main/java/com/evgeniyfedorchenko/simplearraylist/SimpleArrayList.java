package com.evgeniyfedorchenko.simplearraylist;

import com.evgeniyfedorchenko.simplearraylist.interfaces.StringList;

import java.util.Arrays;
import java.util.Collection;

public class SimpleArrayList implements StringList {

    private String[] innerArray;
    private static final int DEFAULT_SIZE = 10;
    private int size;

    public SimpleArrayList() {
        this(DEFAULT_SIZE);
    }

    public SimpleArrayList(int initialSize) {
        size = 0;
        this.innerArray = new String[initialSize];
    }

    public SimpleArrayList(Collection<String> sourceCollection) {
        size = sourceCollection.size();
        this.innerArray = sourceCollection.toArray(new String[0]);
    }

    @Override
    public String add(String item) {
        String[] local = innerArray;

        if (size - 1 < innerArray.length) {
            for (int i = 0; i < local.length; i++) {
                if (local[i] == null) {
                    local[i] = item;
                    size += 1;
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
        size += size / 2;
    }

    @Override
    public String add(int index, String item) {
        if (index >= innerArray.length || index < 0) {
            throw new RuntimeException();
        }
        String[] local = innerArray;
        if (local[size - 1] == null) {
            String[] turnedEls = Arrays.copyOfRange(local, index, size - 1);
            local[index] = item;
            System.arraycopy(turnedEls, 0, local, index + 1, turnedEls.length);
            return item;
        }
        boostSize();
        return add(index, item);
    }

    @Override
    public String set(int index, String item) {
        return null;
    }

    @Override
    public String remove(String item) {
        return null;
    }

    @Override
    public String remove(int index) {
        return null;
    }

    @Override
    public boolean contains(String item) {
        return false;
    }

    @Override
    public int indexOf(String item) {
        return 0;
    }

    @Override
    public int lastIndexOf(String item) {
        return 0;
    }

    @Override
    public String get(int index) {
        return null;
    }

    @Override
    public boolean equals(StringList otherList) {
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void clear() {

    }

    @Override
    public String[] toArray() {
        return new String[0];
    }

    @Override
    public String toString() {
        return Arrays.toString(innerArray);
    }
}
