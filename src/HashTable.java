import org.jetbrains.annotations.NotNull;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;

public class HashTable<K, V> implements Map<K, V> {
    private final int DEFAULT_CAPACITY = 8;
    private final int MAXIMUM_CAPACITY = 16;
    private final float LOAD_FACTOR = 0.75f;
    private float loadFactor;
    private int capacity;
    private int size = 0;
    private int lastSize;

    private volatile Node[] hashTable;
    private volatile boolean[] deleted;

    @SuppressWarnings("WeakerAccess")
    public HashTable() {
        loadFactor = LOAD_FACTOR;
        this.capacity = DEFAULT_CAPACITY;
        //noinspection unchecked
        hashTable = new HashTable.Node[capacity];
        entrySet = new EntrySet();
        deleted = new boolean[capacity];
    }

    public HashTable(boolean link) {
        loadFactor = LOAD_FACTOR;
        this.capacity = DEFAULT_CAPACITY;
        //noinspection unchecked
        hashTable = new HashTable.Node[capacity];
        setDeleted();
    }

    public HashTable(int capacity) {
        loadFactor = LOAD_FACTOR;
        if (capacity >= DEFAULT_CAPACITY && capacity
                <= MAXIMUM_CAPACITY) {
            this.capacity = capacity;
        } else {
            this.capacity = DEFAULT_CAPACITY;
        }
        //noinspection unchecked
        hashTable = new HashTable.Node[capacity];
        setDeleted();
    }

    public HashTable(float loadFactor, int capacity) {
        if (loadFactor <= LOAD_FACTOR) {
            this.loadFactor = loadFactor;
        } else {
            this.loadFactor = LOAD_FACTOR;
        }

        if (capacity >= DEFAULT_CAPACITY && capacity
                <= MAXIMUM_CAPACITY) {
            this.capacity = capacity;
        } else {
            this.capacity = DEFAULT_CAPACITY;
        }
        //noinspection unchecked
        hashTable = new HashTable.Node[capacity];
        setDeleted();
    }

    @Override
    public synchronized int size() {
        return size;
    }

    @Override
    public synchronized boolean isEmpty() {
        return size == 0;
    }

    public synchronized int contains(Object key) {
        int n = -1;
        int hash1 = hashCode1(key);
        int hash2 = hashCode2(key);

        while (n != capacity - 1) {
            n++;
            int index = (hash1 + n * hash2) % (capacity - 1);
            Map.Entry<K, V> node = hashTable[index];
            if (node != null && deleted[index]
                    && node.getKey().equals(key)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return contains(key) >= 0;
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        if (value == null) {
            throw new NullPointerException();
        }
        for (int i = 0; i < capacity; i++) {
            if (deleted[i]) {
                if (value.equals(hashTable[i].getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public synchronized V get(Object key) {
        int index = contains(key);
        if (index < 0) {
            return null;
        }
        //noinspection unchecked
        return hashTable[index].value;
    }

    @Override
    public synchronized V put(K key, V value) {
        if (containsKey(key)) {
            return null;
        }
        size++;
        int index = getIndex(key);
        Node newNode = new Node(key.hashCode(), key, value, size - 1);
        hashTable[index] = newNode;
        deleted[index] = true;

        return value;
    }

    @Override
    public synchronized V remove(Object key) {
        int index = contains(key);

        if (index < 0) {
            return null;
        }
        size--;

        deleted[index] = false;
        return hashTable[index].getValue();
    }

    @Override
    public synchronized void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (Map.Entry<? extends K, ? extends V> elem : m.entrySet()) {
            put(elem.getKey(), elem.getValue());
        }
    }

    @Override
    public synchronized void clear() {
        for (int i = 0; i < capacity; i++) {
            hashTable[i] = null;
        }
        size = 0;
    }

    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) { //сделать упорядоченный
        Objects.requireNonNull(action);
        Iterator<Entry<K, V>> iterator = entrySet.iterator();

        for (; iterator.hasNext(); ) {
            Map.Entry<K, V> node = iterator.next();
            action.accept(node.getKey(), node.getValue());
        }
    }

    private int getIndex(K key) {
        int n = -1;
        float k = (float) size / capacity;

        if (k > loadFactor) {
            rehash();
        }
        while (true) {
            n++;
            int index = (hashCode1(key) + n * hashCode2(key)) % (capacity - 1);
            if (hashTable[index] == null) {
                return index;
            }
        }
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        int index = contains(key);
        if (index < 0 || !hashTable[index].getValue().equals(oldValue)) {
            return false;
        }
        hashTable[index].setValue(newValue);
        return true;
    }

    @Override
    public V replace(K key, V value) {
        int index = contains(key);
        if (index < 0) {
            return null;
        }
        V old = hashTable[index].getValue();
        hashTable[index].setValue(value);
        return old;
    }

    private void rehash() {
        int old = capacity;
        capacity *= 2;
        //noinspection unchecked
        HashTable<K, V>.Node[] between = Arrays.copyOf(hashTable, capacity);
        loadFactor += (1 - loadFactor) / 2;
        //noinspection unchecked
        hashTable = new HashTable.Node[capacity];
        setDeleted();
        for (int i = 0; i < old; i++) {
            if (between[i] != null) {
                int index = getIndex(between[i].getKey());
                hashTable[index] = between[i];
                deleted[index] = true;

            }
        }
    }

    @Override
    public String toString() {
        return Arrays.toString(hashTable);
    }

    @Override
    public synchronized boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof HashTable)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<K, V>> iterator = ((HashTable<K, V>) obj).entrySet.iterator();
        for (; iterator.hasNext(); ) {
            Map.Entry<K, V> elem = iterator.next();
            K key = elem.getKey();
            V value = elem.getValue();
            if (!containsKey(key) || get(key) != value) {
                return false;
            }
        }
        return true;
    }

    private int hashCode1(Object key) {
        return Math.abs(key.hashCode()) % (capacity - 1);
    }

    private int hashCode2(Object key) {
        return 1 + Math.abs(key.hashCode()) % (capacity - 2);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        Iterator<Map.Entry<K, V>> iterator = entrySet.iterator();
        for (; iterator.hasNext(); ) {
            hash += iterator.next().hashCode();
        }
        return hash;
    }


    private final int KEYS = 2;
    private final int VALUES = 3;

    private volatile Set<Map.Entry<K, V>> entrySet = new EntrySet();
    private volatile Set<K> keySet = new KeySet();
    private volatile Collection<V> values = new ValueCollection();

    public  <T> java.util.Iterator<T> getIterator(int type) {
        if (size == 0) {
            return Collections.emptyIterator();
        } else {
            return new MyIterator<>(type);  //посмотреть и проверить
        }
    }

    @NotNull
    @Override
    public Collection<V> values() {
        return values;
    }

    private class ValueCollection extends AbstractCollection<V> {
        @NotNull
        public Iterator<V> iterator() {
            return getIterator(VALUES);
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return containsValue(o);
        }

        public void clear() {
            HashTable.this.clear();
        }
    }

    @NotNull
    @Override
    public Set<K> keySet() {
        return keySet;
    }

    private class KeySet extends AbstractSet<K> {
        @NotNull
        public java.util.Iterator<K> iterator() {
            return getIterator(KEYS);
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return containsKey(o);
        }

        public boolean remove(Object o) {
            return HashTable.this.remove(o) != null;
        }

        public void clear() {
            HashTable.this.clear();
        }
    }

    ////////////////////////////////////////////////////////////////////////
    @NotNull
    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    class EntrySet extends AbstractSet<Map.Entry<K, V>> {
        @NotNull
        public Iterator<Map.Entry<K, V>> iterator() {
            return getIterator(0);
        }

        public boolean add(Map.Entry<K, V> o) {
            return super.add(o);
        }

        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Object key = entry.getKey();
            return HashTable.this.containsKey(key);
        }

        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) o;
            Object key = entry.getKey();
            return HashTable.this.remove(key) != null;
        }

        public void clear() {
            HashTable.this.clear();
        }

        @Override
        public int size() {
            return size;
        }
    }

    ///////////////////////////////////////////////////////////////////////
    class MyIterator<T> implements java.util.Iterator<T> {
        Node[] table = HashTable.this.hashTable;
        boolean[] del = HashTable.this.deleted;
        int type;
        int index = -1;
        int count = 0;
        int size1 = HashTable.this.size;

        MyIterator(int type) {
            this.type = type;
        }

        @Override
        public boolean hasNext() {
            return count < size1;
        }

        //одправить
        @Override
        public T next() {
            index++;
            while (index < capacity) {
                if (del[index]) {
                    Map.Entry node = table[index];
                    count++;
                    //noinspection unchecked
                    return type == KEYS ? (T) node.getKey() : (type == VALUES ? (T) node.getValue() : (T) node);
                } else {
                    index++;
                }
            }
            return null;
        }
    }

    class Node implements Map.Entry<K, V> {
        private int hash;
        private K key;
        private V value;
        private int index;

        int getHash() {
            return hash;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V newValue) {
            if (value == null)
                throw new NullPointerException();

            V old = value;
            value = newValue;
            return old;
        }

        public int getIndex() {
            return index;
        }


        Node(int hash, K key, V value, int index) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.index = index;
        }

        public final String toString() {
            return key + "=>" + value;
        }

        @Override
        public int hashCode() {
            return key.hashCode() ^ value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof HashTable.Node) {
                //noinspection unchecked
                Node node = (Node) obj;
                return Objects.equals(key, node.key) && Objects.equals(value, node.value);
            }
            return false;
        }
    }

    private void setDeleted() {
        deleted = new boolean[capacity];
        for (int i = 0; i < capacity; i++) {
            deleted[i] = false;
        }
    }

}
