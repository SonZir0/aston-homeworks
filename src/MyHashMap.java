import java.util.Arrays;
import java.util.Objects;

public class MyHashMap<K, V> {
    static final int DEFAULT_STARTING_CAPACITY = 16;
    static final double LOAD_FACTOR = 0.75;

    private MapEntry<K, V>[] data;
    private int size = 0;
    private int currentCapacity;

    public MyHashMap(int startingCapacity) {
        if (startingCapacity < 0)
            throw new IllegalArgumentException("Вместимость коллекции не может быть отрицательной!");
        
        data = (MapEntry<K, V>[]) new MapEntry[startingCapacity];
        this.currentCapacity = startingCapacity;
    }

    public MyHashMap() {
        this(DEFAULT_STARTING_CAPACITY);
    }

    // Возвращаем старое значение V, если ключ существует, и мы обновили значение. В противном случае - null.
    public V put(K key, V value) {
        int hash = calcHash(key);
        if ((size + 1) > (currentCapacity * LOAD_FACTOR))
            resize();

        V temp = putNodeLogic(key, value, hash);
        if (temp != null) return temp;
        size++;
        return null;
    }

    // Возвращаем старое значение V, если ключ существует, и мы обновили значение. В противном случае - null.
    private V putNodeLogic(K key, V value, int hash) {
        int index = hash % currentCapacity;
        MapEntry<K, V> prevEntry = null;
        MapEntry<K, V> currentEntry = data[index];

        while (currentEntry != null) {
            if (hash == currentEntry.hash && Objects.equals(key, currentEntry.key)) {
                V temp = currentEntry.value;
                currentEntry.value = value;
                return temp;
            }
            prevEntry = currentEntry;
            currentEntry = currentEntry.getNextEntry();
        }

        MapEntry<K, V> newEntry = new MapEntry<K, V>(key, value, hash);
        // если бакет пустой/в цикл не заходили
        if (prevEntry == null) data[index] = newEntry;
            // ноды уже есть, новый ключ добавляем
        else prevEntry.nextEntry = newEntry;
        return null;
    }

    public V get(K key) {
        int hash = calcHash(key);
        int index = hash % currentCapacity;

        MapEntry<K, V> currentEntry = data[index];
        while (currentEntry != null) {
            if (hash == currentEntry.hash && Objects.equals(key, currentEntry.key))
                return currentEntry.value;
            currentEntry = currentEntry.getNextEntry();
        }
        return null;
    }

    public V remove(K key) {
        int hash = calcHash(key);
        int index = hash % currentCapacity;

        MapEntry<K, V> prevEntry = null;
        MapEntry<K, V> currentEntry = data[index];
        while (currentEntry != null) {
            if (hash == currentEntry.hash && Objects.equals(key, currentEntry.key)) {
                if (prevEntry == null) data[index] = currentEntry.getNextEntry();
                else prevEntry.nextEntry = currentEntry.getNextEntry();
                size--;
                return currentEntry.value;
            }
            prevEntry = currentEntry;
            currentEntry = currentEntry.getNextEntry();
        }
        return null;
    }

    private int calcHash(K key) {
        int objHash;
        return (key == null) ? 0 : (objHash = key.hashCode()) ^ (objHash >>> 16);
    }

    private void resize() {
        if (currentCapacity == 0) currentCapacity = 1;
        else currentCapacity = currentCapacity << 1;

        MapEntry<K, V>[] entryArr = getEntryArray();
        data = (MapEntry<K, V>[]) new MapEntry[currentCapacity];
        redistributeEntries(entryArr);
    }

    public MapEntry<K, V>[] getEntryArray() {
        MapEntry<K, V>[] entryArr = (MapEntry<K, V>[]) new MapEntry[this.size];
        for (int bucketIndex = 0, entryArrSize = 0;
             bucketIndex < currentCapacity && entryArrSize < this.size;
             bucketIndex++) {
            MapEntry<K, V> currentEntry = data[bucketIndex];
            while (currentEntry != null) {
                entryArr[entryArrSize] = currentEntry;
                entryArrSize++;
                currentEntry = currentEntry.getNextEntry();
            }
        }
        return entryArr;
    }

    private void redistributeEntries(MapEntry<K, V>[] entryArr) {
        Arrays.stream(entryArr).forEach(entry ->
                putNodeLogic(entry.key, entry.value, entry.hash));
    }

    public int getSize() {
        return size;
    }

    public int getCapacity() {
        return currentCapacity;
    }
}