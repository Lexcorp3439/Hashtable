import java.util.*;

public class HashSet<K> extends AbstractSet<K> implements Set<K>{
    private HashTable hashTable = new HashTable<K, Object>();
    private Object SIMPLE = true;
    @Override
    public int size() {
        return hashTable.size();
    }

    @Override
    public boolean isEmpty() {
        return hashTable.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return hashTable.containsKey(o);
    }

    @Override
    public Iterator iterator() {
        return hashTable.getIterator(HashTable.Type.KEYS);
    }

    @Override
    public boolean add(K o) {
        return null != hashTable.put(o, SIMPLE);
    }

    @Override
    public boolean remove(Object o) {
        return hashTable.remove(o) != null;
    }

    @Override
    public void clear() {
        hashTable.clear();
    }
}
