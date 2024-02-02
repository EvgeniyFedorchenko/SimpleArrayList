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

    private void grow() {
        Object[] newInnerArray = new Object[Math.round(size * boostRatio)];
        System.arraycopy(innerArray, 0, newInnerArray, 0, size);
        innerArray = newInnerArray;
    }

    @Override
    public E add(E item) {
        checkNullItem(item);
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
        grow();
        innerArray[oldSize] = item;
        size++;
        return item;
    }

    @Override
    public E add(int index, E item) {
        checkNullItem(item);
        checkInvalidIndex(index);
        if (size < innerArray.length) {
            Object[] turnedEls = Arrays.copyOfRange(innerArray, index - 1, size - 1);
            innerArray[index] = item;
            System.arraycopy(turnedEls, 0, innerArray, index + 1, turnedEls.length);
            size++;
            return item;
        }
        grow();
        return add(index, item);
    }

    private void checkInvalidIndex(int index) {
        if (index >= size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void checkNullItem(E item) {
        if (item == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public E set(int index, E item) {

        checkNullItem(item);
        checkInvalidIndex(index);
        E oldValue = getItem(index);
        innerArray[index] = item;
        return oldValue;
    }

    @Override
    public E remove(E item) {
        /* Так как оригинальный ArrayList удаляет только первое вхождение элемента,
           то будем тоже удалять только первое вхождение */
        checkNullItem(item);
        int index = IntStream.range(0, size)
                .filter(i -> innerArray[i].equals(item))
                .findAny()
                .orElseThrow(NoSuchElementException::new);
        return remove(index);
    }

    @Override
    public E remove(int index) {
        checkInvalidIndex(index);
        E item = getItem(index);
        System.arraycopy(innerArray, index + 1, innerArray, index, size - index);
        size--;
        return item;
    }

    @Override
    public int indexOf(E item) {
        checkNullItem(item);
        for (int i = 0; i < size; i++) {
            if (innerArray[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int lastIndexOf(E item) {
        checkNullItem(item);
        for (int i = size - 1; i > 0; i--) {
            if (innerArray[i].equals(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public E get(int index) {
        checkInvalidIndex(index);
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
    public boolean contains(E item) {

        Object[] objects = innerArray;
        boolean sorted = true;
        for (int i = 0; i < size - 1; i++) {
            if (compare(innerArray[i + 1], objects[i])) {
                sorted = false;
                break;
            }
        }
        if (!sorted) {
            quickSort();
        }
        return binarySearch(item) >= 0;
    }

    private int binarySearch(E item) {
        int min = 0;
        int max = size - 1;

        while (min <= max) {
            int mid = (min + max) / 2;
            Object midElement = innerArray[mid];

            if (item.equals(midElement)) {
                return mid;
            } else if (compare(item, midElement)) {
                max = mid - 1;
            } else {
                min = mid + 1;
            }
        }
        return -1;
    }

    @Override
    public void quickSort() {
        quickSort(innerArray, 0, size - 1);
    }

    private void quickSort(Object[] arr, int low, int high) {
        if (low < high) {

            Object pivot = arr[high];
            int i = (low - 1);

            for (int j = low; j < high; j++) {
                if(compare(arr[j], pivot)) {
                    i++;
                    Object temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
            Object temp = arr[i + 1];
            arr[i + 1] = arr[high];
            arr[high] = temp;
            int pi = i + 1;

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private boolean compare(Object firstObj, Object secondObj) {

        String firstString = firstObj.toString();
        String secondString = secondObj.toString();

        return firstString.matches("\\d+")
                ? 0 > Long.parseLong(firstString) - Long.parseLong(secondString)
                : 0 > firstString.compareTo(secondString);
    }

    @Override
    public Object[] toArray() {
        return Arrays.copyOf(innerArray, size);
    }

    @Override
    public String toString() {
        return Arrays.toString(toArray());
    }

    @SuppressWarnings("unchecked")
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
