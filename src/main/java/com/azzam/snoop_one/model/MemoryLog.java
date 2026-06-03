package com.azzam.snoop_one.model;
import oshi.hardware.GlobalMemory;

import java.time.Instant;
public class MemoryLog {
    private long used;
    private long total;
    Instant timestamp;
    public MemoryLog(long used, long total, Instant timestamp) {
        this.used = used;
        this.total = total;
        this.timestamp = timestamp;
    }
    public long getUsed() {
        return this.used;
    }
    public Instant getTimestamp() {
        return this.timestamp;
    }
    public long getTotal() {
        return this.total;
    }
    public String toString() {
        return ("USED RAM: " + getUsed() + " bytes " + "\nTOTAL RAM: " + getTotal());
    }
}
