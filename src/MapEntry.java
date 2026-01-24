import java.util.Objects;

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
        return String.format(
                "\nNew Entry:\n\tkey:\t%s\n\tvalue:\t%s\n\thash:\t%d",
                Objects.toString(key, "null"),
                Objects.toString(value, "null"),
                hash
        );
    }
}