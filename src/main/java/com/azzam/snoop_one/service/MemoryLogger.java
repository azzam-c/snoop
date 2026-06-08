package com.azzam.snoop_one.service;

// REPRESENTS SERVICE SIDE, ACTUAL LOGGING OF ITEMS

import com.azzam.snoop_one.model.LoggerConfig;
import com.azzam.snoop_one.model.MemoryLog;
import oshi.hardware.GlobalMemory;

import java.time.Instant;
import java.util.ArrayList;

public class MemoryLogger {
    private GlobalMemory memory;
    private ArrayList<MemoryLog> logs;

    public MemoryLogger(GlobalMemory memory, ArrayList<MemoryLog> logs) {
        this.memory = memory;
        this.logs = logs;
    }

    public void takeSnapshot() {
        long used = memory.getTotal() - memory.getAvailable();
        logs.add(new MemoryLog(used, memory.getTotal(), Instant.now()));
    }

    private void beginSnapsMillis(int stampCount, int freq) {
        int i = 1;

        while (i <= stampCount) {
            try {
                Thread.sleep(freq);
                takeSnapshot();
                System.out.println("-SNAPSHOT SUCCESSFUL-");
                i++;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        printLogs();
    }

    public void beginSnapsSecs(int snapCount, int freq) {
        beginSnapsMillis(snapCount, freq * 1000);
    }

    public void beginSnapsSecs(LoggerConfig config) {
        beginSnapsMillis(config.getSnapCount(), config.getFreq() * 1000);
    }

    public String getFormattedLogs() {
        if (logs.isEmpty()) {
            return "No memory logs recorded.";
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

        StringBuilder sb = new StringBuilder();

        appendSeparator(sb, labelWidth, colWidths);
        appendRow(sb, "", headers, labelWidth, colWidths);
        appendSeparator(sb, labelWidth, colWidths);
        appendRow(sb, "TIMESTAMP", timestamps, labelWidth, colWidths);
        appendRow(sb, "USED RAM (GiB)", usedValues, labelWidth, colWidths);
        appendRow(sb, "TOTAL RAM (GiB)", totalValues, labelWidth, colWidths);
        appendSeparator(sb, labelWidth, colWidths);

        return sb.toString();
    }

    public void printLogs() {
        System.out.print(getFormattedLogs());
    }

    private void appendRow(StringBuilder sb, String label, String[] values, int labelWidth, int[] colWidths) {
        sb.append(String.format("| %-" + labelWidth + "s ", label));

        for (int i = 0; i < values.length; i++) {
            sb.append(String.format("| %-" + colWidths[i] + "s ", values[i]));
        }

        sb.append("|\n");
    }

    private void appendSeparator(StringBuilder sb, int labelWidth, int[] colWidths) {
        sb.append("+");
        sb.append("-".repeat(labelWidth + 2));

        for (int width : colWidths) {
            sb.append("+");
            sb.append("-".repeat(width + 2));
        }

        sb.append("+\n");
    }
}