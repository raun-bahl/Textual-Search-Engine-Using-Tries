package comp3506.assn2.utils;

import java.util.Map;

public class MyMap<K,V>{

    private Entry<K, V>[] entryTable;
    private int initialCapacity = 16;

    public MyMap() {
        entryTable = new Entry[initialCapacity];
    }

    public MyMap(int capacity) {
        initialCapacity = capacity;
        entryTable = new Entry[initialCapacity];
    }

    public void put(K key, V value) {
        if (key == null) {
            throw new RuntimeException("null key is not allowed");
        }
        // hash value of the key
        int hashValue = hashValue(key);
        // create the entry
        Entry<K, V> entry = new Entry<K, V>(key, value, null);

        // if no entry found at the hash value index of the entry table then put
        // the value
        if (entryTable[hashValue] == null) {
            entryTable[hashValue] = entry;
        } else {// if found then put the value in a linked list
            Entry<K, V> previous = null;
            Entry<K, V> current = entryTable[hashValue];
            while (current != null) {
                if (current.k.equals(key)) {
                    if (previous == null) {
                        entry.next = current.next;
                        entryTable[hashValue] = entry;
                    } else {
                        entry.next = current.next;
                        previous.next = entry;
                    }
                }
                previous = current;
                current = current.next;
            }
            previous.next = entry;
        }

    }

    public V get(K key) {
        if (key == null) {
            return null;
        }
        // hash value of the key
        int hashValue = hashValue(key);
        if (entryTable[hashValue] == null) {
            return null;
        } else {
            Entry<K, V> temp = entryTable[hashValue];
            while (temp != null) {
                if (temp.k.equals(key)) {
                    return temp.v;
                }
                temp = temp.next;
            }
        }
        return null;
    }

    public boolean remove(K key) {
        if (key == null) {
            return false;
        }
        // hash value of the key
        int hashValue = hashValue(key);
        if (entryTable[hashValue] == null) {
            return false;
        } else {
            Entry<K, V> previous = null;
            Entry<K, V> current = entryTable[hashValue];
            while (current != null) {
                if (current.k.equals(key)) {
                    if (previous == null) {
                        entryTable[hashValue] = entryTable[hashValue].next;
                        return true;
                    } else {
                        previous.next = current.next;
                        return true;
                    }
                }
                previous = current;
                current = current.next;
            }
            return false;
        }
    }

    public boolean containsKey(K key) {
        int hashValue = hashValue(key);
        if (entryTable[hashValue] == null) {
            return false;
        } else {
            Entry<K, V> current = entryTable[hashValue];
            while (current != null) {
                if (current.k.equals(key)) {
                    return true;
                }
                current = current.next;
            }
        }
        return false;
    }

    public int size() {
        int count = 0;
        for (int i = 0; i < entryTable.length; i++) {
            if (entryTable[i] != null) {
                int nodeCount = 0;
                for (Entry<K, V> e = entryTable[i]; e.next != null; e = e.next) {
                    nodeCount++;
                }
                count += nodeCount;
                count++;// consider also vertical count
            }
        }
        return count;
    }

    public void display() {

        for(int i=0; i<initialCapacity; i++) {
            if (entryTable[i] != null) {
                Entry<K,V> entry = entryTable[i];
                while (entry != null) {
                    System.out.println("{" + entry.k + "=" + entry.v + "} ");
                    entry = entry.next;
                }
            }
        }
    }

    private int hashValue(K key) {
        return Math.abs(key.hashCode()) % initialCapacity;
    }

    private static class Entry<K, V> {
        private K k;
        private V v;
        private Entry<K, V> next;

        public Entry(K k, V v, Entry<K, V> next) {
            this.k = k;
            this.v = v;
            this.next = next;
        }

    }

}
