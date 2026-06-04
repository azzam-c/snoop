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
        return String.format("USED RAM: %.2f GiB%nTOTAL RAM: %.2f GiB", toGiB(getUsed()), toGiB(getTotal()));
    }
    public static double toGiB(long mem) {
        return mem / 1024.0 / 1024.0 / 1024.0;
    }
}
