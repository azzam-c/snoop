package com.azzam.snoop_one.model;

public class LoggerConfig {
    private int freq;
    private int snapCount;
    public int getFreq() {
        return this.freq;
    }
    public int getSnapCount() {
        return this.snapCount;
    }
    public LoggerConfig(int snapCount, int freq) {
        this.freq = freq;
        this.snapCount = snapCount;
    }
}
