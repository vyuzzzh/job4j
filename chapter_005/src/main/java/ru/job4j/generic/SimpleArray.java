package ru.job4j.generic;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class SimpleArray<T> implements Iterable<T> {
    private T[] array;
    private int currentIndex;

    SimpleArray(Class t, int size) {
        this.array = (T[]) Array.newInstance(t, size);
        currentIndex = 0;
    }

    void add(T model) {
        this.array[currentIndex++] = model;
    }

    void set(int index, T model) {
        if (this.array[index] == null) {
            add(model);
        } else {
            this.array[index] = model;
            currentIndex++;
        }
    }

    void remove(int index) {
        System.arraycopy(this.array, index + 1, this.array, index, currentIndex - index - 1);
        this.array[currentIndex - 1] = null;
        currentIndex--;
    }

    T get(int index) {
        return this.array[index];
    }

    T[] getArray() {
        return Arrays.copyOf(this.array, this.currentIndex);
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int iteratorCount = 0;

            @Override
            public boolean hasNext() {
                return iteratorCount < currentIndex;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return array[iteratorCount++];
            }
        };
    }
}
