package mipsy.core.dataphases;

import mipsy.Utility;
import mipsy.core.MIPSCore;
import mipsy.core.components.MemoryComponent;
import mipsy.instructions.InstructionHalt;
import mipsy.types.Instruction;
import mipsy.types.NoMoreInstructionsException;

import java.util.function.Consumer;

/**
 * Created by adnan on 05.04.2017..
 */
public class MEM extends DataPhase {
    private MemoryComponent memoryComponent = new MemoryComponent("DataMem");

    public MEM(MIPSCore core) {
        super(core, PhaseNames.MEM);
    }

    private int MemToReg;
    private int RegWrite;
    private int exmemOUT2;
    private int exmemOUT4;

    private boolean isHalt;

    private Instruction currentInstruction;

    @Override
    public PhaseResult step(Consumer<String> logger) throws NoMoreInstructionsException {
        logger = Utility.appendToLogger("MEM - ", logger);

        EXMEM exmem = core.EXMEM;
        if ( exmem.OUT3 == null ) {
            logger.accept("No work to do, skipping...");
            return PhaseResult.NO_ACTION;
        }

        logger.accept("START");

        core.IF.PCSrc = exmem.Branch * exmem.OUT1;
        core.IF.Jump = exmem.Jump;

        core.IF.EX_MEM_OUT5 = exmem.OUT5;
        core.IF.EX_MEM_OUT0 = exmem.OUT0;
        if ( exmem.Branch == 1 )
            core.IF.isStalling = false;

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

        isHalt = exmem.currentInstruction instanceof InstructionHalt;

        currentInstruction = exmem.currentInstruction;

        logger.accept("END");


        if ( isHalt ) return PhaseResult.NO_ACTION;
        return new PhaseResult(currentInstruction);
    }

    @Override
    public void writeResults(Consumer<String> logger) {
        logger = Utility.appendToLogger("MEM - ", logger);

        MEMWB memwb = core.MEMWB;

        memwb.OUT0 = memoryComponent.getReadData(logger);
        memwb.OUT1 = exmemOUT2;
        memwb.OUT2 = exmemOUT4;
        memwb.MemToReg = MemToReg;
        memwb.RegWrite = RegWrite;
        memwb.currentInstruction = currentInstruction;
        currentInstruction = null;
    }
}
