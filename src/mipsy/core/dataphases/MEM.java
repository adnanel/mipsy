package mipsy.core.dataphases;

import mipsy.core.MIPSCore;
import mipsy.core.components.MemoryComponent;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class MEM extends DataPhase {
    private MemoryComponent memoryComponent = new MemoryComponent("DataMem");

    public MEM(MIPSCore core) {
        super(core);
    }

    private int MemToReg;
    private int RegWrite;
    private int exmemOUT2;
    private int exmemOUT4;

    private boolean isHalt;

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        EXMEM exmem = core.EXMEM;
        if ( exmem.OUT3 == null ) return;

        core.IF.PCSrc = exmem.Branch * exmem.OUT1;
        core.IF.EX_MEM_OUT0 = exmem.OUT0;

        memoryComponent.setMemory(core.memory);
        memoryComponent.setMemWrite(exmem.MemWrite);
        memoryComponent.setMemRead(exmem.MemRead);
        memoryComponent.setWriteData(exmem.OUT3.value);
        memoryComponent.setAddress(exmem.OUT2);

        memoryComponent.execute(logger);

        MemToReg = exmem.MemToReg;
        RegWrite = exmem.RegWrite;
        exmemOUT2 = exmem.OUT2;
        exmemOUT4 = exmem.OUT4;

        isHalt = exmem.isHalt;
    }

    @Override
    public void writeResults(Consumer<String> logger) {
        MEMWB memwb = core.MEMWB;

        memwb.OUT0 = memoryComponent.getReadData(logger);
        memwb.OUT1 = exmemOUT2;
        memwb.OUT2 = exmemOUT4;
        memwb.MemToReg = MemToReg;
        memwb.RegWrite = RegWrite;
        memwb.isHalt = isHalt;
    }
}
