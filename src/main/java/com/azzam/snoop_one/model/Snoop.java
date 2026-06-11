package com.azzam.snoop_one.model;

import java.time.Instant;
import java.util.ArrayList;

public class Snoop {        //a snoop is the object that holds a specific snapshot run. MemoryLoggers make Snoops.
    private  int id;
    private String name;
    private Instant createdAt;
    private int count;
    private int freq;
    private ArrayList<MemoryLog> logs;

    public Snoop(String name, int id, ArrayList<MemoryLog> logs, int freq, int count) {
        this.count = count;
        this.freq = freq;
        this.createdAt = Instant.now();
        this.logs = logs;
        this.id = id;
        this.name = name;
    }
    public void addLog(MemoryLog mem) {
        logs.add(mem);
    }
}
