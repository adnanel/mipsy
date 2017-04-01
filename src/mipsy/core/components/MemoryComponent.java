package mipsy.core.components;

import mipsy.types.MemoryEntry;

import java.util.HashMap;
import java.util.function.Consumer;

/**
 * Created by Adnan on 4/1/2017.
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
        MemoryEntry entry = memory.get(address);
        if ( entry == null ) entry = new MemoryEntry(address, 0);

        return entry.value;
    }
}
