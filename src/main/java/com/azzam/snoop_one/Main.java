package com.azzam.snoop_one;
import com.azzam.snoop_one.service.MemoryLogger;
import com.azzam.snoop_one.model.MemoryLog;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import java.util.Scanner;
import com.azzam.snoop_one.model.LoggerConfig;

import java.time.Instant;
import java.util.ArrayList;

public class Main {
        public static void main(String[] args) {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            GlobalMemory memory = hal.getMemory();
            ArrayList<MemoryLog> list = new ArrayList<>();
            MemoryLogger loglist = new MemoryLogger(memory,list);
            loglist.beginSnapsSecs(prep());
        }
        public static LoggerConfig prep() {
            Scanner s = new Scanner(System.in);
            System.out.print("This program is meant to take basic snapshots of your memory" +
                    "usage over some time.\n");
            System.out.println("Please enter how many snapshots you want: ");
            int snapCount = s.nextInt();
            System.out.println("Please enter your interval in seconds: ");
            int freq = s.nextInt();
            LoggerConfig config = new LoggerConfig(freq, snapCount);
            return config;
        }
}
