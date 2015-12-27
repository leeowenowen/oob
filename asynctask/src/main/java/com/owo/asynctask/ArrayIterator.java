package com.owo.asynctask;

import java.util.Iterator;

public class ArrayIterator<E>
        implements Iterator<E> {
    private final E[] mArray;
    private int mCurIndex;

    public ArrayIterator(E[] array) {
        mArray = array;
    }

    @Override
    public boolean hasNext() {
        return mArray == null ? false : (mCurIndex < mArray.length);
    }

    @Override
    public E next() {
        return mArray[mCurIndex++];
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Not support remove operation!");
    }
}
