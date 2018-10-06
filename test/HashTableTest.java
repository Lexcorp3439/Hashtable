import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;


class HashTableTest {

    @Test
    void isEmpty() {
        HashTable test = new HashTable();

        assertTrue(test.isEmpty());
        setHashtable(test);
        assertFalse(test.isEmpty());
        test.clear();
        assertTrue(test.isEmpty());
    }

    @Test
    void put() {
        HashTable test = new HashTable();

        assertEquals(1, test.put("1", 1));
        assertEquals("dsfsd", test.put(34, "dsfsd"));
        assertEquals(2, test.put("2", 2));
        assertEquals(true, test.put(new HashSet<>(), true));
        assertEquals(4, test.put("4", 4));
        assertEquals(5, test.put("5", 5));

        assertNull(test.put("1", 1));
        assertNull(test.put("4", 4));

        //synchronized put();
        Thread thread1 = new Thread(() -> {
            test.put("3", 3);
            test.put("sdf", true);
        });
        Thread thread2 = new Thread(() -> {
            test.put("3", 3);
            test.put(1, "dsfsd");
        });
        thread1.run();
        thread2.run();
        HashTable testThread = new HashTable();
        setPutTest(testThread);
        assertEquals(testThread.toString(), test.toString());
        thread1.interrupt();
        thread1.interrupt();

        assertEquals(5, test.put("sdgfg", 5));
        assertEquals(5, test.put("sdgfgds", 5));
        assertEquals(5, test.put("sdgfgsdfsd", 5));
        assertEquals(5, test.put("fffffffffggh", 5));
        assertEquals(5, test.put("fffffffffgghsdfa;,f;lv,fd;lf,vfd;,bfd;,da;fd;", 5));
    }

    @Test
    void equals() {
        HashTable test1 = new HashTable();
        setHashtable(test1);
        HashTable test2 = new HashTable();
        setHashtable(test2);
        HashTable test3 = new HashTable();
        test3.put(2, "213");
        HashTable test4 = new HashTable();
        setHashtable(test4);
        test4.put("32", "dsf");

        assertTrue(test1.equals(test2));
        assertFalse(test1.equals(test3));
        assertFalse(test1.equals(test4));
        assertFalse(test1.equals(null));

        //test hashcode
        System.out.println(test1.hashCode());
        System.out.println(test2.hashCode());
        System.out.println(test3.hashCode());
        System.out.println(test4.hashCode());
    }

    @Test
    void size() {
        HashTable test = new HashTable();
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
        HashTable test = new HashTable();
        setHashtable(test);

        assertTrue(test.containsKey("1"));
        assertTrue(test.containsKey("35"));
        test.remove("35");
        assertFalse(test.containsKey("35"));
        assertFalse(test.containsKey("dsfsd"));
    }

    @Test
    void containsValue() {
        HashTable test = new HashTable();
        setHashtable(test);

        assertTrue(test.containsValue(1));
        assertTrue(test.containsValue(35));
        test.remove("35");
        assertFalse(test.containsValue(35));
        assertFalse(test.containsValue("dsfsd"));
    }

    @Test
    void get() {
        HashTable test = new HashTable();
        setHashtable(test);

        assertEquals(1, test.get("1"));
        assertNull(test.get(354));
        test.remove("2");
        assertNull(test.get("2"));
    }


    @Test
    void replace() {
        HashTable test = new HashTable();
        setHashtable(test);
        assertEquals(1, test.replace("1", 3));
        assertNull(test.replace("null", 3));

        HashTable test1 = new HashTable();
        setHashtable(test1);
        assertTrue(test1.replace("1", 1, 3));
        assertFalse(test1.replace("false", 3, 2));

        assertEquals(test, test1);
    }

    @Test
    synchronized void remove() {
        HashTable test = new HashTable();
        setHashtable(test);

        assertEquals(4, test.remove("4"));
        assertNull(test.remove("7"));

    }

    @Test
    void forEach() {
        HashTable test = new HashTable();
        setHashtable(test);

        List<Integer> listValue = new ArrayList();
        List listValue1 = new ArrayList();
        listValue.add(1);
        listValue.add(1);
        listValue.add(3);
        listValue.add(4);
        listValue.add(5);
        listValue.add(35);

        List<String> listKey = new ArrayList();
        List listKey1 = new ArrayList();
        listKey.add("1");
        listKey.add("2");
        listKey.add("3");
        listKey.add("4");
        listKey.add("5");
        listKey.add("35");


        test.forEach((key, value) -> {
            listValue1.add(value);
        });

        for (Object obj : test.entrySet()) {
            Map.Entry node = (Map.Entry) obj;
            listKey1.add(node.getKey());
        }
        assertEquals(listValue, listValue1);
        assertEquals(listKey, listKey1);
    }

    @Test
    void putAll() {
        HashTable test = new HashTable();
        setHashtable(test);
        Map map = new LinkedHashMap(8, 0.75f, true);
        map.put("4", 4);
        map.put("35", 35);
        map.put("5", 5);

        HashTable test1 = new HashTable<>();
        test1.put("1", 1);
        test1.put("2", 1);
        test1.put("3", 3);
        test1.putAll(map);

        assertEquals(test.toString(), test1.toString());
    }

    @Test
    void clear() {
        HashTable test = new HashTable();
        setHashtable(test);
        test.clear();

        assertTrue(test.isEmpty());
    }

    @Test
    void keySet() {
        HashTable test = new HashTable();
        setHashtable(test);

        StringBuilder str = new StringBuilder();
        for (Object obj : test.keySet()) {
            str.append(obj);
            str.append(" ");
        }
        System.out.println(str.toString());
        System.out.println(test.keySet());

        assertEquals("[1, 2, 3, 4, 5, 35]", test.keySet().toString());
        test.clear();
        assertEquals(new HashSet<>(), test.keySet());
    }

    @Test
    void values() {
        HashTable test = new HashTable();
        setHashtable(test);
        Collection testSet = new ArrayList();
        setValues(testSet);

        //Проверка оболочки values() и keySet()
        StringBuilder str = new StringBuilder();
        for (Object obj : test.values()) {
            str.append(obj);
            str.append(" ");
        }
        System.out.println(str.toString());
        test.put("key", "value");
        System.out.println(test.values());
        test.keySet().remove("key");

        assertEquals(testSet.toString(), test.values().toString());
        test.values().clear();
        assertEquals(new ArrayList<>().toString(), test.values().toString());
    }

    @Test
    void entrySet() {
        HashTable test = new HashTable();
        setHashtable(test);

        StringBuilder str = new StringBuilder();
        for (Object obj : test.entrySet()) {
            str.append(obj);
            str.append(" ");
        }
        System.out.println(str.toString());
        System.out.println(test.entrySet());

        assertEquals("[1=>1, 2=>1, 3=>3, 4=>4, 5=>5, 35=>35]", test.entrySet().toString());
        test.clear();
        assertEquals("[]", test.keySet().toString());
    }


    ////Special test`s function

    private void setHashtable(HashTable test) {
        test.put("1", 1);
        test.put("2", 1);
        test.put("3", 3);
        test.put("4", 4);
        test.put("35", 35);
        test.put("5", 5);
    }

    private void setPutTest(HashTable test) {
        test.put("1", 1);
        test.put(34, "dsfsd");
        test.put("2", 2);
        test.put(new HashSet<>(), true);
        test.put("4", 4);
        test.put("5", 5);
        test.put("3", 3);
        test.put("sdf", true);
        test.put(1, "dsfsd");
    }

    private void setSet(Set testSet) {
        testSet.add("1");
        testSet.add("2");
        testSet.add("3");
        testSet.add("4");
        testSet.add("35");
        testSet.add("5");
    }

    private void setValues(Collection testSet) {
        testSet.add(1);
        testSet.add(1);
        testSet.add(3);
        testSet.add(4);
        testSet.add(5);
        testSet.add(35);
    }
}