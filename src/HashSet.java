import org.jetbrains.annotations.NotNull;

import java.util.*;

public class HashSet extends AbstractSet implements Set{
    private HashTable hashTable = new HashTable();
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

    @NotNull
    @Override
    public Iterator iterator() {
        return hashTable.getIterator(2);
    }

    @Override
    public boolean add(Object o) {
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
