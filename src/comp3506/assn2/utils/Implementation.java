package comp3506.assn2.utils;

public class Implementation {
    public class Entry<K, V> {
        K key;
        V value;

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public void setValue(V value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            int i = 7;
            int j = 13;
            if (key != null) {
                int hashCode = i * j + key.hashCode();
                return hashCode;
            }
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass().getName() != o.getClass().getName()) {
                return false;
            }

            Entry e = (Entry) o;
            if (this.key == e.key) {
                return true;
            }
            return false;
        }

    }
}

