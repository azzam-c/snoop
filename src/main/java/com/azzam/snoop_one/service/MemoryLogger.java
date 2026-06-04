package com.azzam.snoop_one.service;
// REPRESENTS SERVICE SIDE, ACTUAL LOGGING OF ITEMS
import com.azzam.snoop_one.model.LoggerConfig;
import com.azzam.snoop_one.model.MemoryLog;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Logger;

public class MemoryLogger {
    private GlobalMemory memory;    //we need to get the info from somewhere in beginSnaps
    private ArrayList<MemoryLog> logs;
    public MemoryLogger(GlobalMemory memory, ArrayList<MemoryLog> logs) {
        this.memory = memory;
        this.logs = logs;
    }
    private void beginSnapsMillis(int stampCount, int freq) {
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
    public void beginSnapsSecs(int snapCount, int freq) {
        beginSnapsMillis(snapCount, (freq*1000));
    }
    public void beginSnapsSecs(LoggerConfig config) {
        beginSnapsMillis(config.getSnapCount(), (config.getFreq() * 1000));
    }
    public void printLogs() {
        if (logs.isEmpty()) {
            System.out.println("No memory logs recorded.");
            return;
        }
        int labelWidth = 16;
        String[] headers = new String[logs.size()];
        String[] timestamps = new String[logs.size()];
        String[] usedValues = new String[logs.size()];
        String[] totalValues = new String[logs.size()];
        int[] colWidths = new int[logs.size()];
        for (int i = 0; i < logs.size(); i++) {
            MemoryLog log = logs.get(i);

            headers[i] = "SNAPSHOT #" + (i + 1);
            timestamps[i] = log.getTimestamp().toString();
            usedValues[i] = String.format("%.2f", MemoryLog.toGiB(log.getUsed()));
            totalValues[i] = String.format("%.2f", MemoryLog.toGiB(log.getTotal()));

            colWidths[i] = Math.max(headers[i].length(), timestamps[i].length());
            colWidths[i] = Math.max(colWidths[i], usedValues[i].length());
            colWidths[i] = Math.max(colWidths[i], totalValues[i].length());
        }
        printSeparator(labelWidth, colWidths);
        printRow("", headers, labelWidth, colWidths);
        printSeparator(labelWidth, colWidths);
        printRow("TIMESTAMP", timestamps, labelWidth, colWidths);
        printRow("USED RAM (GiB)", usedValues, labelWidth, colWidths);
        printRow("TOTAL RAM (GiB)", totalValues, labelWidth, colWidths);
        printSeparator(labelWidth, colWidths);
    }
    private void printRow(String label, String[] values, int labelWidth, int[] colWidths) {
        System.out.printf("| %-" + labelWidth + "s ", label);

        for (int i = 0; i < values.length; i++) {
            System.out.printf("| %-" + colWidths[i] + "s ", values[i]);
        }

        System.out.println("|");
    }
    private void printSeparator(int labelWidth, int[] colWidths) {
        System.out.print("+");
        System.out.print("-".repeat(labelWidth + 2));

        for (int width : colWidths) {
            System.out.print("+");
            System.out.print("-".repeat(width + 2));
        }
        System.out.println("+");
    }
}
