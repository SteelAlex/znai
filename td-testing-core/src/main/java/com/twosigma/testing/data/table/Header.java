package com.twosigma.testing.data.table;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * @author mykola
 */
public class Header {
    private Map<String, Integer> indexByName;
    private List<String> namesByIndex;

    private List<String> keyNames;
    private List<Integer> keyIdx;

    public Header(Stream<String> names) {
        this.indexByName = new HashMap<>();
        this.namesByIndex = new ArrayList<>();
        this.keyNames = new ArrayList<>();
        this.keyIdx = new ArrayList<>();

        names.forEach(this::add);
    }

    public Record createRecord(Stream<Object> values) {
        return new Record(this, values);
    }

    public boolean has(String name) {
        return indexByName.containsKey(name);
    }

    public Stream<String> getNames() {
        return namesByIndex.stream();
    }

    public boolean hasKeyColumns() {
        return ! keyNames.isEmpty();
    }

    public Stream<String> getKeyNames() {
        return keyNames.stream();
    }

    public Stream<Integer> getKeyIdx() {
        return keyIdx.stream();
    }

    public IntStream getColumnIdxStream() {
        return IntStream.range(0, namesByIndex.size());
    }

    private void add(String nameWithMeta) {
        boolean startsWithAsterisk = nameWithMeta.startsWith("*");

        String name = startsWithAsterisk ? nameWithMeta.substring(1) : nameWithMeta;
        int newIdx = namesByIndex.size();

        if (startsWithAsterisk) {
            keyNames.add(name);
            keyIdx.add(newIdx);
        }

        Integer previousIndex = indexByName.put(name, newIdx);
        if (previousIndex != null) {
            throw new IllegalStateException("getHeader name '" + name + "' was already present. current getHeader: " + name);
        }

        namesByIndex.add(name);
    }

    public String columnNameByIdx(int idx) {
        validateIdx(idx);
        return namesByIndex.get(idx);
    }


    public int size() {
        return namesByIndex.size();
    }

    @Override
    public String toString() {
        return namesByIndex.toString();
    }

    /**
     * column index by column name
     * @param columnName column name to get index for
     * @return column index
     * @throws IllegalArgumentException if column is not defined
     */
    public int columnIdxByName(String columnName) {
        final Integer idx = indexByName.get(columnName);
        if (idx == null) {
            throw new IllegalArgumentException("column '" + columnName + "' is not present");
        }

        return idx;
    }

    void validateIdx(int idx) {
        if (idx < 0 || idx >= namesByIndex.size()) {
            throw new IllegalArgumentException("column idx " + idx + " is out of boundaries. header size is " +
                namesByIndex.size() + ", header is " + namesByIndex);
        }
    }
}
