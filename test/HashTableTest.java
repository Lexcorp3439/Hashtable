import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;


@SuppressWarnings("unchecked")
class HashTableTest {

    @Test
    void isEmpty() {
        HashTable<String, Integer> test = new HashTable();

        setHashtable(test);
        assertFalse(test.isEmpty());
        test.clear();
        assertTrue(test.isEmpty());
    }

    @Test
    void put() {
        HashTable<String, Integer> test = new HashTable();

        assertEquals(Integer.valueOf(1), test.put("1", 1));
        assertEquals(Integer.valueOf(2), test.put("2", 2));
        assertEquals(Integer.valueOf(4), test.put("4", 4));
        assertEquals(Integer.valueOf(5), test.put("5", 5));

        assertNull(test.put("1", 1));
        assertNull(test.put("4", 4));

        assertEquals(Integer.valueOf(5), test.put("sdgfg", 5));
        assertEquals(Integer.valueOf(5), test.put("sdgfgds", 5));
        assertEquals(Integer.valueOf(5), test.put("sdgfgsdfsd", 5));
        assertEquals(Integer.valueOf(5), test.put("fffffffffggh", 5));
        assertEquals(Integer.valueOf(5), test.put("fffffffffgghsdfa;,f;lv,fd;lf,vfd;,bfd;,da;fd;", 5));
    }

    @Test
    void equals() {
        HashTable<String, Integer> test1 = new HashTable();
        setHashtable(test1);
        Map<String, Integer> map1 = new Hashtable();
        setHashtable(map1);

        HashTable<String, Integer> test2 = new HashTable();
        setHashtable(test2);
        Map<String, Integer> map2 = new Hashtable();
        setHashtable(map2);

        HashTable<String, Integer> test3 = new HashTable();
        test3.put("213", 2);
        Map<String, Integer> map3 = new Hashtable();
        map3.put("213", 2);

        HashTable<String, Integer> test4 = new HashTable();
        setHashtable(test4);
        test4.put("32", 56);

        assertEquals(test1, test2);
        assertNotEquals(test1, test3);
        assertNotEquals(test1, test4);
        assertNotEquals(null, test1);

        assertEquals(map1.hashCode(), test1.hashCode());
        assertEquals(map2.hashCode(), test2.hashCode());
        assertEquals(map3.hashCode(), test3.hashCode());
    }

    @Test
    void size() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);
        assertEquals(6, test.size());
        test.remove("2");
        assertEquals(5, test.size());
        test.clear();
        assertEquals(0, test.size());
        assertEquals(0, new HashTable<>().size());
    }

    @Test
    void containsKey() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        assertTrue(test.containsKey("1"));
        assertTrue(test.containsKey("35"));
        test.remove("35");
        assertFalse(test.containsKey("35"));
        assertFalse(test.containsKey("dsfsd"));
    }

    @Test
    void containsValue() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        assertTrue(test.containsValue(1));
        assertTrue(test.containsValue(35));
        test.remove("35");
        assertFalse(test.containsValue(35));
    }

    @Test
    void get() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        assertEquals(Integer.valueOf(1), test.get("1"));
        assertNull(test.get("344"));
        test.remove("2");
        assertNull(test.get("2"));
    }


    @Test
    void replace() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);
        assertEquals(Integer.valueOf(1), test.replace("1", 3));
        assertNull(test.replace("null", 3));

        HashTable<String, Integer> test1 = new HashTable();
        setHashtable(test1);
        assertTrue(test1.replace("1", 1, 3));
        assertFalse(test1.replace("false", 3, 2));

        assertEquals(test, test1);
    }

    @Test
    synchronized void remove() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        assertEquals(Integer.valueOf(4), test.remove("4"));
        assertNull(test.remove("7"));

    }

    @Test
    void forEach() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        List<Integer> listValue = new ArrayList();
        List<Integer> listValue1 = new ArrayList();
        listValue.add(1);
        listValue.add(1);
        listValue.add(3);
        listValue.add(4);
        listValue.add(5);
        listValue.add(35);

        List<String> listKey = new ArrayList();
        List<String> listKey1 = new ArrayList();
        listKey.add("1");
        listKey.add("2");
        listKey.add("3");
        listKey.add("4");
        listKey.add("5");
        listKey.add("35");


        test.forEach((key, value) -> {
            listKey1.add(key);
            listValue1.add(value);
        });

        assertEquals(listValue, listValue1);
        assertEquals(listKey, listKey1);
    }

    @Test
    void putAll() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);
        Map<String, Integer> map = new LinkedHashMap(8, 0.75f, true);
        map.put("4", 4);
        map.put("35", 35);
        map.put("5", 5);

        HashTable<String, Integer> test1 = new HashTable<>();
        test1.put("1", 1);
        test1.put("2", 1);
        test1.put("3", 3);
        test1.putAll(map);

        assertEquals(test, test1);
    }

    @Test
    void clear() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);
        test.clear();

        assertTrue(test.isEmpty());
    }

    @Test
    void keySet() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        Map<String, Integer> map1 = new Hashtable();
        setHashtable(map1);

        assertEquals(map1.keySet(), test.keySet());
        test.clear();
        assertEquals(new HashSet(), test.keySet());
    }

    @Test
    void values() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);
        ArrayList<Integer> testSet = new ArrayList();
        setValues(testSet);

        test.values().clear();
        assertTrue(test.values().isEmpty());
    }

    @Test
    void entrySet() {
        HashTable<String, Integer> test = new HashTable();
        setHashtable(test);

        Map<String, Integer> map = new Hashtable();
        setHashtable(map);

        assertEquals(map.entrySet(), test.entrySet());
        test.clear();
        assertTrue(test.keySet().isEmpty());
    }

    @Test
    void special(){
        HashTable<String, Integer> test = new HashTable(value -> {
            String s = (String) value;
            int h = 0;
            int length = s.length() >> 1;
            for (int i = 0; i < length; i++) {
                h = 33 * h + s.charAt(i);
            }
            return h;
        });
        assertEquals(Integer.valueOf(1), test.put("sdgfg", 1));
        assertEquals(Integer.valueOf(2), test.put("sdgfgds", 2));
        assertNull(test.put("sdgfgds", 4));
        assertEquals(Integer.valueOf(3), test.put("sdgfgsdfsd", 3));
        assertEquals(Integer.valueOf(4), test.put("fffffffffggh", 4));
        assertEquals(Integer.valueOf(5), test.put("fffffffffgghsdfa;,f;lv,fd;lf,vfd;,bfd;,da;fd;", 5));

        assertTrue(test.containsValue(1));
        assertTrue(test.containsValue(5));
        assertEquals(Integer.valueOf(2), test.remove("sdgfgds"));
        assertFalse(test.containsValue(35));

        assertEquals(Integer.valueOf(1), test.put("s", 1));
        assertEquals(Integer.valueOf(2), test.put("sdg", 2));
        assertEquals(Integer.valueOf(3), test.put("sdgfgsdf", 3));
        assertEquals(Integer.valueOf(4), test.put("ffffffggh", 4));
        assertEquals(Integer.valueOf(5), test.put("ffffgghsdfa;,f;lv,fd;lf,vfd;,bfd;,da;fd;", 5));

        setHashtable(test);
        assertTrue(test.containsValue(1));
    }


    ////Special test`s function
    private void setHashtable(Map<String, Integer> test) {
        test.put("1", 1);
        test.put("2", 1);
        test.put("3", 3);
        test.put("4", 4);
        test.put("35", 35);
        test.put("5", 5);
    }

    private void setValues(Collection<Integer> testSet) {
        testSet.add(1);
        testSet.add(1);
        testSet.add(3);
        testSet.add(4);
        testSet.add(5);
        testSet.add(35);
    }
}