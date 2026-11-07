package org.tolis;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class HashMap<K, V> implements Dictionary<K, V> {

    private static final int DEFAULT_CAPACITY = 4;

    private Entry<K, V>[] hashTable;
    private int currentSize;
    private UniversalHashFunction hashFunction;

    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    public HashMap(int capacity) {
        this.hashTable = new Entry[capacity];
        this.currentSize = 0;
        this.hashFunction = new UniversalHashFunction(capacity);

        for (int i = 0; i < hashTable.length; i++) {
            hashTable[i] = emptyEntry();
        }
    }

    @Override
    public void put(K key, V value) {
        this.rehash();

        insert(key, value);
    }

    @Override
    public V remove(K key) {
        int index = hashFunction.hash(key.hashCode());
        int loopCounter = 0;

        while (loopCounter < hashTable.length) {
            if (isEmpty(index)) {
                return null;
            }

            if (hashTable[index].getKey().equals(key)) {
                V value = hashTable[index].getValue();

                hashTable[index] = deletedEntry();

                currentSize--;

                rehash();

                return value;
            }

            loopCounter++;
            index = (index + 1) % hashTable.length;
        }

        return null;
    }

    @Override
    public V get(K key) {
        int index = hashFunction.hash(key.hashCode());
        int loopCounter = 0;

        while (loopCounter < hashTable.length) {
            if (key.equals(hashTable[index].getKey())) {
                return hashTable[index].getValue();
            }

            loopCounter++;
            index = (index + 1) % hashTable.length;
        }

        return null;
    }

    @Override
    public boolean contains(K key) {
        int index = hashFunction.hash(key.hashCode());
        int loopCounter = 0;

        while (loopCounter < hashTable.length) {
            if (isEmpty(index)) {
                return false;
            }

            if (hashTable[index].getKey().equals(key)) {
                return true;
            }

            loopCounter++;
            index = (index + 1) % hashTable.length;
        }

        return false;
    }

    @Override
    public int size() {
        return this.currentSize;
    }

    @Override
    public void clear() {
        currentSize = 0;

        for (int i = 0; i < hashTable.length; i++) {
            hashTable[i] = emptyEntry();
        }
    }

    @Override
    public Iterator<Entry<K, V>> iterator() {
        return new HashIterator();
    }


    private static class EntryImpl<K, V> implements Dictionary.Entry<K, V> {
        private K key;
        private V value;

        public EntryImpl(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }
    }

    private class HashIterator implements Iterator<Entry<K, V>> {
        private int index;
        private int keysLeft;

        public HashIterator() {
            index = 0;
            keysLeft = currentSize;
        }

        @Override
        public boolean hasNext() {
            return keysLeft != 0;   // true if there are left keys to return, otherwise false
        }

        @Override
        public Entry<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            while (hashTable[index].getKey().equals("⊥") || hashTable[index].getKey().equals("⊙")) { // current slot is empty continue searching since there are keys left to return
                index++;
            }

            index++;
            keysLeft--;

            Entry<K, V> entry = hashTable[index - 1];

            return entry;
        }
    }

    private void insert(K key, V value) {
        int index = hashFunction.hash(key.hashCode());

        while (true) {
            if (isEmpty(index) || isDeleted(index)) {
                hashTable[index] = new EntryImpl<>(key, value);
                currentSize++;
                return;
            }

            if (key.equals(hashTable[index].getKey())) {
                hashTable[index] = new EntryImpl<>(key, value);
                return;
            }

            index = (index + 1) % hashTable.length;
        }
    }

    private boolean isEmpty(int index) {
        return hashTable[index].getKey().equals("⊥");
    }

    private boolean isDeleted(int index) {
        return hashTable[index].getKey().equals("⊙");
    }

    private void rehash() {
        double limit = (double) currentSize / hashTable.length;

        int newCapacity;

        if (limit >= 0.75) {
            newCapacity = hashTable.length * 2;
        } else if (limit < 0.25) {
            newCapacity = hashTable.length / 2;
        } else {
            return;
        }

        HashMap<K, V> newHashMap = new HashMap<>(newCapacity);

        for (Entry<K, V> entry : this) {
            newHashMap.insert(entry.getKey(), entry.getValue());
        }

        this.hashTable = newHashMap.hashTable;
        this.hashFunction = newHashMap.hashFunction;
        this.currentSize = newHashMap.currentSize;
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V> emptyEntry() {
        return (Entry<K, V>) new EntryImpl<>("⊥", null);
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V> deletedEntry() {
        return (Entry<K, V>) new EntryImpl<>("⊙", null);
    }
}
