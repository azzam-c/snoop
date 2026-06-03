package com.azzam.snoop_one.service;
// REPRESENTS SERVICE SIDE, ACTUAL LOGGING OF ITEMS
import com.azzam.snoop_one.model.MemoryLog;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.Instant;
import java.util.ArrayList;

public class MemoryLogger {
    private GlobalMemory memory;    //we need to get the info from somewhere in beginSnaps
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
                System.out.println("-SNAPSHOT SUCCESSFUL-"); i++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        printLogs();
    }
    public void beginSnapsSecs(int stampCount, int freq) {
        beginSnapsMillis(stampCount, (freq*1000));
    }
    public void printLogs() {
        for(int i=0; i < logs.size(); i++) {
            System.out.println("--SNAPSHOT #" + (i+1) + " ");
            System.out.println(logs.get(i));
        }
    }
}
