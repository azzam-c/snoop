package com.azzam.snoop_one.service;
// REPRESENTS SERVICE SIDE, ACTUAL LOGGING OF ITEMS
import com.azzam.snoop_one.model.MemoryLog;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.Instant;
import java.util.ArrayList;

public class MemoryLogger {
    private GlobalMemory memory;
    private ArrayList<MemoryLog> logs;
    public MemoryLogger(GlobalMemory memory, ArrayList<MemoryLog> logs) {
        this.memory = memory;
        this.logs = logs;
    }
    public void beginSnapsMillis(int stampCount, int freq) {
        int i=1; while(i<=stampCount) {
            try {
                Thread.sleep(freq);
                long used = memory.getTotal()-memory.getAvailable();
                //used, total, timestamp
                logs.add(new MemoryLog(used, memory.getTotal(), Instant.now()));
                System.out.print("-SNAPSHOT SUCCESSFUL-"); i++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void beginSnapsSecs(int stampCount, int freq) {
        beginSnapsMillis(stampCount, (freq*1000));
    }
}
