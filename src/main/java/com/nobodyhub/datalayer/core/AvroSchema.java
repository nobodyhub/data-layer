package com.nobodyhub.datalayer.core;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * @author Ryan
 */
public class AvroSchema {
    private List<AvroRecord> records = Lists.newArrayList();

    public void addRecord(AvroRecord record) {
        this.records.add(record);
    }
}
