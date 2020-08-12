package core.basesyntax;

/**
 * <p>Реалізувати свою HashMap, а саме методи `put(K key, V value)`, `getValue()` та `getSize()`.
 * Дотриматися основних вимог щодо реалізації мапи (initial capacity, load factor, resize...)
 * За бажанням можна реалізувати інші методи інтрефейсу Map.</p>
 */
public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR = 0.75;

    private Entry<K, V>[] elementData;
    private int size;

    public MyHashMap() {
        elementData = new Entry[DEFAULT_CAPACITY];
    }

    @Override
    public void put(K key, V value) {
        if (size > elementData.length * LOAD_FACTOR) {
            resizeAndTransfer();
        }

        if (key == null) {
            putForNullKey(value);
            return;
        }

        int index = getIndexFor(key.hashCode());
        if (elementData[index] == null) {
            addEntry(key, value, index);
        } else {
            replaceOrAddEntry(key, value, index);
        }
    }

    private void resizeAndTransfer() {

        Entry<K, V>[] oldElementData = elementData;
        elementData = new Entry[elementData.length * 2];
        size = 0;
        for (int i = 0; i < oldElementData.length; i++) {
            Entry<K, V> entry = oldElementData[i];
            while (entry != null) {
                put(entry.key, entry.value);
                entry = entry.next;
            }
        }
    }

    @Override
    public V getValue(K key) {
        int index = key == null ? 0 : getIndexFor(key.hashCode());
        Entry<K, V> entry = elementData[index];
        if (entry == null) {
            return null;
        }
        while (entry != null) {
            if (theSameKey(key, entry.key)) {
                return entry.value;
            }
            entry = entry.next;
        }
        return null;
    }

    private boolean theSameKey(K key, K key1) {
        return key == key1 || (key != null && key.equals(key1));
    }

    @Override
    public int getSize() {
        return size;
    }

    private void putForNullKey(V value) {
        if (null == elementData[0]) {
            addEntry(null, value, 0);
            return;
        }
        replaceOrAddEntry(null, value, 0);
    }

    private void replaceOrAddEntry(K key, V value, int index) {
        boolean replaced = false;
        Entry<K, V> entry = elementData[index];

        while (!replaced) {
            if (theSameKey(key, entry.key)) {
                entry.value = value;
                replaced = true;
            }

            if (entry.next != null) {
                entry = entry.next;
            } else {
                break;
            }
        }

        if (!replaced) {
            entry.next = new Entry<>(key, value);
            size++;
        }
    }

    private void addEntry(K key, V value, int index) {
        Entry<K, V> entry = new Entry<>(key, value);
        elementData[index] = entry;
        size++;
    }

    private int getIndexFor(int hashCode) {
        return Math.abs(hashCode) % elementData.length;
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }
}
