package mipsy.core.components;

import mipsy.types.MemoryEntry;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created on 4/1/2017.
 */
public class MemoryComponent {
    private HashMap<Integer, MemoryEntry> memory;

    private int address;
    private int writeData;

    private int memWrite;
    private int memRead;

    private String name;

    public MemoryComponent(String name) {
        this.name = name;
    }

    public void setMemory(HashMap<Integer, MemoryEntry> memory) {
        this.memory = memory;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public void setWriteData(int writeData) {
        this.writeData = writeData;
    }

    public void setMemWrite(int memWrite) {
        this.memWrite = memWrite;
    }

    public void setMemRead(int memRead) {
        this.memRead = memRead;
    }

    public int getReadData(Consumer<String> logger) {
        if ( memRead == 0 ) {
            logger.accept(String.format("%s: Not reading memory because MemRead = 0", name));
            return 0;
        }
        MemoryEntry entry = memory.get(address);
        if ( entry == null ) entry = new MemoryEntry(address, 0);

        return entry.value;
    }


    public void execute(Consumer<String> logger) {
        if ( memWrite == 0 ) {
            logger.accept(String.format("%s: Not writing to memory because MemWrite = 0", name));
            return;
        }

        logger.accept(String.format("%s: Writing %s to address %s", name, "0x" + Integer.toHexString(writeData), "0x" + Integer.toHexString(address)));

        MemoryEntry entry = memory.computeIfAbsent(address, a -> new MemoryEntry(a, 0));

        entry.value = writeData;
    }
}
