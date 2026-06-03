package com.azzam.snoop_one;
import com.azzam.snoop_one.service.MemoryLogger;
import com.azzam.snoop_one.model.MemoryLog;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;

import java.time.Instant;
import java.util.ArrayList;

public class Main {
        public static void main(String[] args) {
            SystemInfo si = new SystemInfo();
            HardwareAbstractionLayer hal = si.getHardware();
            GlobalMemory memory = hal.getMemory();
            ArrayList<MemoryLog> list = new ArrayList<>();
            MemoryLogger loglist = new MemoryLogger(memory,list);
            loglist.beginSnapsSecs(5,3);
        }
}
