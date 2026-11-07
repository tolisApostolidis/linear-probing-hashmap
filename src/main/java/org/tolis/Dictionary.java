package org.tolis;

import java.util.Iterator;

public interface Dictionary<K, V> extends Iterable<Dictionary.Entry<K, V>> {

    /**
     * Represents a key-value pair in the dictionary
     *
     * @param <K> the key type
     * @param <V> the value type
     */
    interface Entry<K, V> {
        /**
         * Returns the key
         *
         * @return the key
         */
        K getKey();

        /**
         * Returns the value
         *
         * @return the value
         */
        V getValue();
    }

    /**
     * Associates the specified value with the specified key
     *
     * @param key the key
     * @param value the value
     */
    void put (K key, V value);

    /**
     * Removes the entry associated with the specified key
     *
     * @param key the key
     * @return the previously associated value, or null if the key was not found
     */
    V remove(K key);

    /**
     * Returns the value associated with the specified key
     *
     * @param key the key
     * @return the associated value, or null if the key was not found
     */
    V get(K key);

    /**
     * Checks whether the dictionary contains the specified key
     *
     * @param key the key
     * @return true if the key exists, otherwise false
     */
    boolean contains(K key);

    /**
     * Checks whether the dictionary is empty
     *
     * @return true if the dictionary is empty, otherwise false
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns the number of entries in the dictionary.
     *
     * @return the number of entries
     */
    int size();

    /**
     * Removes all entries from the dictionary
     */
    void clear();

    /**
     * Returns an iterator over the entries in the dictionary
     *
     * @return an iterator over the entries
     */
    Iterator<Entry<K, V>> iterator();
}
