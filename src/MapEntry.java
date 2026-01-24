public class MapEntry <K, V> {
    final K key;
    V value;
    final int hash;
    MapEntry<K, V> nextEntry;

    public MapEntry(K key, V value, int hash) {
        this.key = key;
        this.value = value;
        this.hash = hash;
        this.nextEntry = null;
    }

    public MapEntry<K, V> getNextEntry() {
        return nextEntry;
    }

    @Override
    public String toString() {
        return "\nNew Entry:\n" +
                "\tkey:\t" + key +
                "\tvalue:\t" + value +
                "\thash:\t" + hash;
    }
}