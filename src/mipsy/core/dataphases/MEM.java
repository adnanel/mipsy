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

    @Override
    public void step(Consumer<String> logger) throws NoMoreInstructionsException {
        EXMEM exmem = core.EXMEM;
        MEMWB memwb = core.MEMWB;

        core.IF.PCSrc = exmem.Branch * exmem.OUT1;
        core.IF.EX_MEM_OUT0 = exmem.OUT0;

        memoryComponent.setMemory(core.memory);
        memoryComponent.setMemWrite(exmem.MemWrite);
        memoryComponent.setMemRead(exmem.MemRead);
        memoryComponent.setWriteData(exmem.OUT3.value);
        memoryComponent.setAddress(exmem.OUT2);

        memoryComponent.execute(logger);

        memwb.OUT0 = memoryComponent.getReadData(logger);
        memwb.OUT1 = exmem.OUT2;
        memwb.OUT2 = exmem.OUT4;
        memwb.MemToReg = exmem.MemToReg;
        memwb.RegWrite = exmem.RegWrite;
    }
}
